package databaseAccessClasses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.Database;
import database.DatabaseException;
import modelClasses.Batch;

public class BatchDAO {
	
	Database db;

	public BatchDAO(Database dbIn){
		db = dbIn;
	}
	
	
	/**
	 * Use the batch as input to add it to the database.
	 * @param batch
	 * @return void 
	 */
	public void create(Batch batch) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		try{
			String query = "insert into batch (parentProjectID, filePath, isAvailable) values (?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
//			stmt.setInt(1, batch.getBatchID());
			stmt.setInt(1, batch.getParentProjectID());
			stmt.setString(2, batch.getFilePath());
			stmt.setBoolean(3, batch.isAvailable());
			if(stmt.executeUpdate()==1){
				Statement keyStmt = db.getConnection().createStatement();                           
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");                         
				keyRS.next();                                                                       
				int id = keyRS.getInt(1);                                                           
				batch.setBatchID(id);
			}
			else{
				throw new DatabaseException("Could not add batch: 1");
			}
		}
		catch (SQLException e){
			throw new DatabaseException("Could not add batch: 2");
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * Take the batchID and use it to find and return the matching batch.
	 * @param batchID
	 * @return the batch which matches the batchID
	 */
	
	public Batch read(int projectID) throws DatabaseException{
		System.out.println("batch read");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql = "select * from batch where parentProjectID = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			while(rs.next()){
				int batchID = rs.getInt("batchID");
				int parentProjectID = rs.getInt("parentProjectID");
				String filePath = rs.getString("filePath");
				boolean isAvailable = rs.getBoolean("isAvailable");
				Batch result = new Batch(parentProjectID, filePath);
				result.setBatchID(batchID);
				result.setAvailable(isAvailable);
				return result;
			}
		}
		catch (SQLException e){
			throw new DatabaseException("Could not find batch: 1");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		return null;
	}
	
	public Batch readFrom(int batchIDIn) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql = "select * from batch where batchID = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, batchIDIn);
			rs = stmt.executeQuery();
			while(rs.next()){
				int batchID = rs.getInt("batchID");
				int parentProjectID = rs.getInt("parentProjectID");
				String filePath = rs.getString("filePath");
				boolean isAvailable = rs.getBoolean("isAvailable");
				Batch result = new Batch(parentProjectID, filePath);
				result.setBatchID(batchID);
				result.setAvailable(isAvailable);
				return result;
			}
		}
		catch (SQLException e){
			throw new DatabaseException("Could not find batch: 1");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		return null;
	}
	
	public Batch readAvailable(int projectID) throws DatabaseException{
		System.out.println("batch read");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from batch where parentProjectID = ? and isAvailable = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, projectID);
			stmt.setBoolean(2, true);
			rs = stmt.executeQuery();
			rs.next();
			int batchID = rs.getInt("batchID");
			int parentProjectID = rs.getInt("parentProjectID");
			String filePath = rs.getString("filePath");
			Batch result = new Batch(parentProjectID, filePath);
			result.setBatchID(batchID);
			PreparedStatement psUpdate = null;
			String update = "update batch set isAvailable = ? where batchID = ?";
			psUpdate = db.getConnection().prepareStatement(update);
			psUpdate.setBoolean(1, false);
			psUpdate.setInt(2, batchID);
			psUpdate.execute();
			result.setAvailable(false);
			return result;
		} 
		catch (Exception e) {
			e.printStackTrace();
			 throw new DatabaseException("Could not find available batch");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Use the batchID to find and update the isAvailable to value
	 * @param batchID
	 * @param value
	 * @return void
	 */
	
	public void update(Batch batch, boolean value) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql = "select * from batch";
			stmt = db.getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				String query = "update batch set isAvailable = ? where batchID = ?";
				stmt = db.getConnection().prepareStatement(query);
				int batchID = rs.getInt("batchID");
				if(batchID == batch.getBatchID()){
					stmt.setBoolean(1, value);
					stmt.setInt(2, batch.getBatchID());
					if(stmt.executeUpdate() != 1){
						throw new DatabaseException("Could not update batch");
					}
				}
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not update batch");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
}
