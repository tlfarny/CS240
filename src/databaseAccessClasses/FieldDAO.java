package databaseAccessClasses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modelClasses.FieldModels;
import database.Database;
import database.DatabaseException;

public class FieldDAO{
	
	Database db;
	
	public FieldDAO(Database db){
		this.db = db;
	}
	
	public void create(FieldModels field) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		try{
			String query = "insert into fieldModels (parentProjectID, title, xCoord, width, helphtml, knowndata) values (?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
//			stmt.setInt(1, field.getFieldID());
			stmt.setInt(1, field.getParentProjectID());
			stmt.setString(2, field.getTitle());
			stmt.setInt(3, field.getxCoord());
			stmt.setInt(4, field.getWidth());
			stmt.setString(5, field.getHelpHTML());
			stmt.setString(6, field.getKnownData());
			if(stmt.executeUpdate()==1){
				Statement keyStmt = db.getConnection().createStatement();                           
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");                         
				keyRS.next();                                                                       
				int id = keyRS.getInt(1);                                                           
				field.setFieldID(id);
			}
			else{
				throw new DatabaseException("Could not add field: 1");
			}
		}
		catch (SQLException e){
			throw new DatabaseException("Could not add field: 2");
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	public ArrayList<FieldModels> getFieldsFromProject(int projectID) throws DatabaseException{
		System.out.println("get fields from project");
		ArrayList<FieldModels> result = new ArrayList<FieldModels>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select * from fieldModels where parentProjectID = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			while(rs.next()){
				int fieldID = rs.getInt("fieldID");
				int parentProjectID = rs.getInt("parentProjectID");
				String title = rs.getString("title");
				int xCoord = rs.getInt("xCoord");
				int width = rs.getInt("width");
				String helpHTML = rs.getString("helphtml");
				String knownData = rs.getString("knowndata");
				FieldModels temp = new FieldModels(parentProjectID, title, xCoord, width, helpHTML, knownData);
				temp.setFieldID(fieldID);
				result.add(temp);
			}
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			throw new DatabaseException("Could not get fields");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	public FieldModels read(int fieldIDIn) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from fieldModels where fieldID = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, fieldIDIn);
			rs = stmt.executeQuery();
			int fieldID = rs.getInt("fieldID");
			int parentProjectID = rs.getInt("parentProjectID");
			String title = rs.getString("title");
			int xCoord = rs.getInt("xCoord");
			int width = rs.getInt("width");
			String helphtml = rs.getString("helphtml");
			String knowndata = rs.getString("knowndata");
			FieldModels result = new FieldModels(parentProjectID, title, xCoord, width, helphtml, knowndata);
			result.setFieldID(fieldID);
			return result;
		} catch (Exception e) {
			 throw new DatabaseException();
		}
	}
	
	public ArrayList<FieldModels> getFieldsFromNull() throws DatabaseException{
		ArrayList<FieldModels> result = new ArrayList<FieldModels>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from fieldModels";
			stmt = db.getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				int fieldID = rs.getInt("fieldID");
				int parentProjectID = rs.getInt("parentProjectID");
				String title = rs.getString("title");
				int xCoord = rs.getInt("xCoord");
				int width = rs.getInt("width");
				String helpHTML = rs.getString("helphtml");
				String knownData = rs.getString("knowndata");
				FieldModels temp = new FieldModels(parentProjectID, title, xCoord, width, helpHTML, knownData);
				temp.setFieldID(fieldID);
				result.add(temp);
			}
			return result;
		} 
		catch (SQLException e) {
			 throw new DatabaseException("Could not get all fields from null");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
}