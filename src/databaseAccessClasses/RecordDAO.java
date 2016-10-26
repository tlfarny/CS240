package databaseAccessClasses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modelClasses.Record;
import database.Database;
import database.DatabaseException;

public class RecordDAO {
	
	Database db;
	
	public RecordDAO(Database db){
		this.db = db;
	}
	
	public void create(Record record) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		try {
			String sql = "insert into Record (parentBatchID, rowNumber) values (?, ?)";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, record.getParentBatchID());
			stmt.setInt(2, record.getRowNumber());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();                           
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");                         
				keyRS.next();                                                                       
				int id = keyRS.getInt(1);                                                           
				record.setRecordID(id);
			}
			else{
				db.endTransaction(false);
				throw new DatabaseException("Could not add record: 1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			 throw new DatabaseException("Could not add record : 2");
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	public Record read(int recordIDIn)throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from Record where recordID = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, recordIDIn);
			rs = stmt.executeQuery();
			while(rs.next()){
				int recordID = rs.getInt("recordID");
				int parentBatchID = rs.getInt("parentBatchID");
				int rowNumber = rs.getInt("rowNumber");
				Record result = new Record(parentBatchID, rowNumber);
				result.setRecordID(recordID);
				return result;
			}
			
		} catch (SQLException e) {
			 throw new DatabaseException("Could not update record");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		return null;
		
	}
	
	public ArrayList<Record> readAllRecordsFrom(int batchID) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Record> result = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Record where parentBatchID = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, batchID);
			rs = stmt.executeQuery();
			int recordID = rs.getInt("recordID");
			int parentBatchID = rs.getInt("parentBatchID");
			int rowNumber = rs.getInt("rowNumber");
			Record record = new Record(parentBatchID, rowNumber);
			record.setRecordID(recordID);
			result.add(record);
		} 
		catch (SQLException e) {
			throw new DatabaseException("Could not get all records");
		}
		return result;
	}

}
