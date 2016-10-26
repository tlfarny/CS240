package serverFacade;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import communicationsClasses.DownloadBatchParam;
import communicationsClasses.DownloadBatchResult;
import communicationsClasses.GetFieldsResult;
import communicationsClasses.GetProjectsResult;
import communicationsClasses.GetSampleImageParam;
import communicationsClasses.GetSampleImageResult;
import communicationsClasses.SearchParams;
import communicationsClasses.SearchResult;
import communicationsClasses.SubmitBatchParam;
import communicationsClasses.SubmitBatchResult;
import communicationsClasses.ValidateUser_Result;
import modelClasses.Batch;
import modelClasses.Cell;
import modelClasses.FieldModels;
import modelClasses.Project;
import modelClasses.Record;
import modelClasses.SearchTuple;
import modelClasses.User;

import database.Database;
import database.DatabaseException;

public class ServerFacade {
	
	public static void initialize() throws ServerException{
		try {
			Database.initialize();
		} catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public ValidateUser_Result validateUser(String usernameIn, String passwordIn)throws Exception{
		Database db  = new Database();
		try {
			db.startTransaction();
			User user = db.getUserDAO().read(usernameIn, passwordIn);
			db.endTransaction(true);
			if (user == null) {
				return null;
			}
			return new ValidateUser_Result(user);
		} catch (Exception e) {
			db.endTransaction(false);
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		}
	}
	
	public GetProjectsResult getProjects() throws ServerException{
		System.out.println("get projects server facade");
		Database db = new Database();
		try {
			db.startTransaction();
			ArrayList<Project> projects = db.getProjectDAO().readAllProjects();
			db.endTransaction(true);
			return new GetProjectsResult(projects);
		} catch (DatabaseException e) {
			 db.endTransaction(false);
			 throw new ServerException(e.getMessage(), e);
		}
	}
	
	public GetSampleImageResult getSampleImage(GetSampleImageParam param) throws ServerException{
		System.out.println("server facade sample image");
		Database db = new Database();
		try {
			db.startTransaction();
			Batch batch = db.getBatchDAO().read(param.getProjectID());
			db.endTransaction(true);
			return new GetSampleImageResult(batch.getFilePath());
		} catch (DatabaseException e) {
			 db.endTransaction(false);
			 throw new ServerException(e.getMessage(), e);
		}
	}
	
	public DownloadBatchResult downloadBatch(DownloadBatchParam params) throws ServerException{
		System.out.println("ServerFacade");
		Database db = new Database();
		try {
			db.startTransaction();
			Batch batch = db.getBatchDAO().readAvailable(params.getProjectID());
			Project project = db.getProjectDAO().read(params.getProjectID());
			ArrayList<FieldModels> fields = db.getFieldDAO().getFieldsFromProject(params.getProjectID());
			User user = db.getUserDAO().read(params.getUsername(), params.getPassword());
			db.getUserDAO().update(user, batch.getBatchID(), 0);
			System.out.println(batch.getBatchID());
			db.endTransaction(true);
			return new DownloadBatchResult(batch, project, project.getRecordsPerImage(), fields, user);
		} catch (Exception e) {
			db.endTransaction(false);
			e.printStackTrace();
			 throw new ServerException(e.getMessage(), e);
		}
	}
	
	public SubmitBatchResult submitBatch(SubmitBatchParam params) throws ServerException{
		System.out.println("-----------------------------submit batch start--------------------------------");
		Database db = new Database();
		try {
			db.startTransaction();
			User user = db.getUserDAO().read(params.getUsername(), params.getPassword());
			Batch batch = db.getBatchDAO().readFrom(params.getBatchID());
			Project project  =db.getProjectDAO().read(batch.getParentProjectID());
			if(project.getRecordsPerImage() != params.getValues().size()){
				System.out.println("-----------------------------This was hit--------------------------------");
				throw new Exception();
			}
			ArrayList<ArrayList<String>> values = params.getValues();
			ArrayList<FieldModels> fields = db.getFieldDAO().getFieldsFromProject(batch.getParentProjectID());
			for (int i = 0; i < project.getRecordsPerImage(); i++) {
				Record record = new Record(batch.getBatchID(), i+1);
				db.getRecordDAO().create(record);
				for (int j = 0; j < fields.size(); j++) {
					String temp = values.get(i).get(j);
					Cell cell = new Cell(temp.toUpperCase(), fields.get(j).getFieldID(), record.getRecordID());
					db.getCellDAO().create(cell);
				}
			}
			System.out.println("Records per project: " + project.getRecordsPerImage());
			db.getUserDAO().updateOut(user, project.getRecordsPerImage());
			db.endTransaction(true);
			return new SubmitBatchResult(true);
		} catch (Exception e) {
			db.endTransaction(false);
			 throw new ServerException(e.getMessage(), e);
		}
	}
	
	public GetFieldsResult getFields(int projectID) throws ServerException{
		Database db = new Database();
		try {
			db.startTransaction();
			ArrayList<FieldModels> fields = db.getFieldDAO().getFieldsFromProject(projectID);
			db.endTransaction(true);
			return new GetFieldsResult(fields);
		} catch (DatabaseException e) {
			 db.endTransaction(false);
			 throw new ServerException(e.getMessage(), e);
		}
	}
	
	public GetFieldsResult getFieldsFromNull() throws ServerException{
		Database db = new Database();
		try {
			db.startTransaction();
			ArrayList<FieldModels> result = db.getFieldDAO().getFieldsFromNull();
			db.endTransaction(true);
			return new GetFieldsResult(result);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public SearchResult search(SearchParams params) throws ServerException{
		Set<SearchTuple> result = new HashSet<SearchTuple>();
		ArrayList<Integer> fields = params.getSearchFields();
		ArrayList<String> terms = params.getTerms();
		ArrayList<SearchTuple> tupleList = new ArrayList<SearchTuple>();
		Database db = new Database();
		try {
			db.startTransaction();
			if(terms.size()<1){
				throw new Exception();
			}
			else{
				ArrayList<FieldModels> fieldsToCheck = db.getFieldDAO().getFieldsFromNull();
				for (int i = 0; i < fields.size(); i++) {
					boolean atLeastOneRightValue = false;
					for (int j = 0; j < fieldsToCheck.size(); j++) {
						if(fields.get(i) == fieldsToCheck.get(j).getFieldID()){
							atLeastOneRightValue = true;
						}
					}
					if (atLeastOneRightValue==false) {
						throw new Exception();
					}
				}
				for(int i = 0; i < fields.size(); i++){
					int fieldID = fields.get(i);
					for (int j = 0; j < terms.size(); j++) {
						String term = terms.get(j);
						ArrayList<Cell> cells = db.getCellDAO().readAllCellsWith(fieldID, term.toUpperCase());
						for (int k = 0; k < cells.size(); k++) {
							Cell cell = cells.get(k);
							Record record = db.getRecordDAO().read(cells.get(k).getRecordID());
							Batch batch = db.getBatchDAO().readFrom(record.getParentBatchID());
							FieldModels field = db.getFieldDAO().read(fieldID);
							SearchTuple tuple = new SearchTuple(cell, batch, field, record);
							result.add(tuple);
							tupleList.add(tuple);
//							result.add(cells.get(k));
						}
					}
				}
			}
			db.endTransaction(true);
			return new SearchResult(result);
		} catch (Exception e) {
			 db.endTransaction(false);
			 throw new ServerException(e.getMessage(), e);
		}
	}
}

	
//	public void downloadFile() throws ServerException{
//		Database db = new Database();
//		try {
//			db.startTransaction();
////			String url = db
//			db.endTransaction(true);
//		} catch (DatabaseException e) {
//			db.endTransaction(false);
//			throw new ServxerException(e.getMessage(), e);
//		}
//	}
//	
//	
//}
