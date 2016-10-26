package dataImporter;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import modelClasses.Batch;
import modelClasses.Cell;
import modelClasses.FieldModels;
import modelClasses.Project;
import modelClasses.Record;
import modelClasses.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import database.Database;
import database.DatabaseException;

public class XMLParser {
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<Project> projects = new ArrayList<Project>();
	private ArrayList<Integer> fieldIDs = new ArrayList<Integer>();
	private Database db = null;
	
	public XMLParser(String fileName)throws Exception{
		
		Database.initialize();
		db = new Database();
		try {
			dropAndCreateTables(db);
		} catch (SQLException e) {
			db.endTransaction(false);
		}
		File xmlFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		Element root = doc.getDocumentElement();
		Parser(root);
	}

	public void Parser(Element element){
		ArrayList<Element> rootElements = getChildElements(element);
		ArrayList<Element> userElements = getChildElements(rootElements.get(0));
		for(Element userElement : userElements){
			makeUser(userElement);
		}
		ArrayList<Element> projectElements = getChildElements(rootElements.get(1));
		for(Element projectElement : projectElements){
			makeProjects(projectElement);
		}
	}
	
	public void makeUser(Element userElement){
		String userName = getValue((Element) userElement.getElementsByTagName("username").item(0));
		String password = getValue((Element) userElement.getElementsByTagName("password").item(0));
		String firstName = getValue((Element) userElement.getElementsByTagName("firstname").item(0));
		String lastName= getValue((Element) userElement.getElementsByTagName("lastname").item(0));
		String emailAddress= getValue((Element) userElement.getElementsByTagName("email").item(0));
		int totalNumberIndexed = Integer.parseInt(getValue((Element) userElement.getElementsByTagName("indexedrecords").item(0)));
		User user = new User(userName, password, firstName, lastName, emailAddress, totalNumberIndexed);
		try {
			db.startTransaction();
			db.getUserDAO().create(user);
			db.endTransaction(true);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}
	}
	
	public void makeProjects(Element projectElement){
		fieldIDs.clear();
		String title = getValue((Element)projectElement.getElementsByTagName("title").item(0));
		int recordsPerImage = Integer.parseInt(getValue((Element)projectElement.getElementsByTagName("recordsperimage").item(0)));
		int firstYCoordinate = Integer.parseInt(getValue((Element)projectElement.getElementsByTagName("firstycoord").item(0)));
		int recordHeight = Integer.parseInt(getValue((Element)projectElement.getElementsByTagName("recordheight").item(0)));
		Project project = new Project(title, recordsPerImage, firstYCoordinate, recordHeight);
		int projectID = -1;
		try {
			db.startTransaction();
			db.getProjectDAO().create(project);
			db.endTransaction(true);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}
		projectID = project.getID();
		makeFields(projectID, projectElement);
		makeImages(projectID, projectElement);
	}
	
	public void makeFields(int projectID, Element projectElement){
		Element fieldsElement = (Element)projectElement.getElementsByTagName("fields").item(0);
		NodeList fieldElements = fieldsElement.getElementsByTagName("field");
		for(int i = 0; i < fieldElements.getLength(); i++) {
			Element thisfields = (Element) fieldElements.item(i);
			String title = getValue((Element)thisfields.getElementsByTagName("title").item(0));
			int xCoord = Integer.parseInt(getValue((Element)thisfields.getElementsByTagName("xcoord").item(0)));
			int width = Integer.parseInt(getValue((Element)thisfields.getElementsByTagName("width").item(0)));
			String helpHTML = getValue((Element)thisfields.getElementsByTagName("helphtml").item(0));
			String KnownData = null;
			Element knownData= (Element) thisfields.getElementsByTagName("knowndata").item(0);
			if(knownData != null){
				KnownData = getValue(knownData);
			}
			FieldModels field = new FieldModels(projectID, title, xCoord, width, helpHTML, KnownData);
			try {
				db.startTransaction();
				db.getFieldDAO().create(field);
				db.endTransaction(true);
			} catch (DatabaseException e) {
				db.endTransaction(false);
				e.printStackTrace();
			}
			fieldIDs.add(field.getFieldID());
		}
	}
	
	public void makeImages(int projectID, Element projectElement){
		Element imagesElement = (Element)projectElement.getElementsByTagName("images").item(0);
		NodeList imageElements = imagesElement.getElementsByTagName("image");
		for(int i = 0; i < imageElements.getLength(); i++){
			Element thisImage = (Element) imageElements.item(i);
			String filePath = getValue((Element)thisImage.getElementsByTagName("file").item(0));
			Batch batch = new Batch(projectID, filePath);
			try {
				db.startTransaction();
				db.getBatchDAO().create(batch);
				db.endTransaction(true);
			} catch (DatabaseException e) {
				db.endTransaction(false);
				e.printStackTrace();
			}
			int batchID = batch.getBatchID();
			ArrayList<Element> imageChildren = getChildElements(imageElements.item(i));
			if(imageChildren.size() > 1){
				makeRecords(batchID, thisImage);
			}
		}
	}
	
	public void makeRecords(int batchID, Element thisImage){
		Element recordsElement = (Element)thisImage.getElementsByTagName("records").item(0);
		NodeList recordElements = recordsElement.getElementsByTagName("record");
		for (int i = 0; i < recordElements.getLength(); i++) {
			Element thisrecord = (Element) recordElements.item(i);
			Record record = new Record(batchID, i+1);
			try {
				db.startTransaction();
				db.getRecordDAO().create(record);
				db.endTransaction(true);
			} catch (DatabaseException e) {
				db.endTransaction(false);
				 e.printStackTrace();
			}
			int recordID = record.getRecordID();
			makeCells(recordID, thisrecord);
		}
	}
	
	public void makeCells(int recordID, Element thisrecord){
		Element cellsElement = (Element)thisrecord.getElementsByTagName("values").item(0);
		NodeList cellElements = cellsElement.getElementsByTagName("value");
		String[] valueArray = new String[cellElements.getLength()];
		for (int i = 0; i < cellElements.getLength(); i++) {
			valueArray[i] = getValue((Element) cellElements.item(i));
		}
		try {
			db.startTransaction();
			for (int i = 0; i < valueArray.length; i++) {
				db.getCellDAO().create(new Cell(valueArray[i].toUpperCase(), fieldIDs.get(i), recordID));
			}
			db.endTransaction(true);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}
	
	public static ArrayList<Element> getChildElements(Node node) { 
		ArrayList<Element> result = new ArrayList<Element>();
		NodeList children = node.getChildNodes(); 
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i); 
			if(child.getNodeType() == Node.ELEMENT_NODE){
				result.add((Element)child);
			}
		}
		return result;
	}
	
	public static String getValue(Element element) {
		String result = "";
		Node child = element.getFirstChild();
		result = child.getNodeValue();
		return result;
	}
	
	public void dropAndCreateTables(Database db)throws SQLException, DatabaseException{
		String deleteuser = "DROP TABLE IF EXISTS user;";
		String deleteProject = "DROP TABLE IF EXISTS project;";
		String deleteFieldModels = "DROP TABLE IF EXISTS fieldModels;";
		String deleteBatch = "DROP TABLE IF EXISTS Batch;";
		String deleteRecord = "DROP TABLE IF EXISTS Record;";
		String deleteCell = "DROP TABLE IF EXISTS Cell;";
		db.startTransaction();
		PreparedStatement psUser = db.getConnection().prepareStatement(deleteuser);
		PreparedStatement psProject = db.getConnection().prepareStatement(deleteProject);
		PreparedStatement psFields = db.getConnection().prepareStatement(deleteFieldModels);
		PreparedStatement psBatch = db.getConnection().prepareStatement(deleteBatch);
		PreparedStatement psRecord = db.getConnection().prepareStatement(deleteRecord);
		PreparedStatement psCell = db.getConnection().prepareStatement(deleteCell);
		psUser.execute();
		psProject.execute();
		psFields.execute();
		psBatch.execute();
		psRecord.execute();
		psCell.execute();
		StringBuilder compile = new StringBuilder();
		compile.append("CREATE TABLE user (");
		compile.append("userID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , ");
		compile.append("username TEXT NOT NULL  UNIQUE , ");
		compile.append("password TEXT NOT NULL , ");
		compile.append("firstname TEXT NOT NULL , ");
		compile.append("lastname TEXT NOT NULL , ");
		compile.append("email TEXT NOT NULL , ");
		compile.append("indexedrecords INTEGER NOT NULL , ");
		compile.append("batchnumber INTEGER NOT NULL  DEFAULT -1);");
		PreparedStatement user = db.getConnection().prepareStatement(compile.toString());
		compile = new StringBuilder();
		compile.append("CREATE TABLE project (");
		compile.append("id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , ");
		compile.append("title TEXT NOT NULL , ");
		compile.append("recordsperimage INTEGER NOT NULL , ");
		compile.append("firstycoord INTEGER NOT NULL , ");
		compile.append("recordheight INTEGER NOT NULL );");
		PreparedStatement project = db.getConnection().prepareStatement(compile.toString());
		compile = new StringBuilder();
		compile.append("CREATE TABLE fieldModels (");
		compile.append("fieldID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , ");
		compile.append("parentProjectID INTEGER NOT NULL , ");
		compile.append("title TEXT NOT NULL , ");
		compile.append("xCoord INTEGER NOT NULL , ");
		compile.append("width INTEGER NOT NULL , ");
		compile.append("helphtml TEXT, ");
		compile.append("knowndata TEXT);");
		PreparedStatement fieldModels = db.getConnection().prepareStatement(compile.toString());
		compile = new StringBuilder();
		compile.append("CREATE TABLE Record (");
		compile.append("recordID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , ");
		compile.append("parentBatchID INTEGER NOT NULL , ");
		compile.append("rowNumber INTEGER NOT NULL );");
		PreparedStatement record = db.getConnection().prepareStatement(compile.toString());
		compile = new StringBuilder();
		compile.append("CREATE TABLE Cell (");
		compile.append("cellID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , ");
		compile.append("value TEXT NOT NULL , ");
		compile.append("fieldID INTEGER NOT NULL , ");
		compile.append("recordID INTEGER NOT NULL );");
		PreparedStatement cell = db.getConnection().prepareStatement(compile.toString());
		compile = new StringBuilder();
		compile.append("CREATE TABLE Batch (");
		compile.append("batchID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , ");
		compile.append("parentProjectID INTEGER NOT NULL , ");
		compile.append("filePath TEXT NOT NULL , ");
		compile.append("isAvailable BOOL NOT NULL  DEFAULT true);");
		PreparedStatement batch = db.getConnection().prepareStatement(compile.toString());
		user.executeUpdate();
		project.executeUpdate();
		fieldModels.executeUpdate();
		record.executeUpdate();
		cell.executeUpdate();
		batch.executeUpdate();
		db.endTransaction(true);
	}
	
}
