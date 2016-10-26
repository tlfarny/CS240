package databaseAccessClasses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.Database;
import database.DatabaseException;
import modelClasses.User;

public class UserDAO {
	
	Database db;
	
	public UserDAO(Database dbIn){
		db = dbIn;
	}
	/**
	 * Takes a user in and adds all of the data to the database
	 * @param newUser
	 * @return void 
	 */
	public void create(User user) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into user (username, password, firstname, lastname, email, indexedrecords, batchnumber) values (?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.setString(5, user.getEmailAddress());
			stmt.setInt(6, user.getTotalNumberIndexed());
			stmt.setInt(7, user.getBatchNumber());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();                           
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");                         
				keyRS.next();                                                                       
				int id = keyRS.getInt(1);                                                           
				user.setUserID(id);	                                                               	
			}
			else {
				throw new DatabaseException("Could not insert contact");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert contact", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return user whose information matches the username and password
	 */
	
	public User read(String usernameIn, String passwordIn) throws DatabaseException{
		System.out.println("user read");
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try{
			String query = "select * from user where username = ? and password = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, usernameIn);
			stmt.setString(2, passwordIn);
			rs = stmt.executeQuery();
			while(rs.next()){
				int userID = rs.getInt(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String firstName = rs.getString(4);
				String lastName = rs.getString(5);
				String emailAddress = rs.getString(6);
				int totalNumberIndexed = rs.getInt(7);
				int batchNumber = rs.getInt(8);
				User result = new User(username, password, firstName, lastName, emailAddress, totalNumberIndexed);
				result.setUserID(userID);
				result.setBatchNumber(batchNumber);
				return result;
			}
			return null;
		}
		catch(Exception e){
			e.printStackTrace();
			throw new DatabaseException("Could not find user");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Use the username to find the user change the current batch to the BatchID and increment the total indexed by the increment 
	 * @param username
	 * @param batchID
	 * @return void
	 */
	
	public void update(User user, int batchIDIn, int increment) throws DatabaseException{
		System.out.println("update user");
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try{
			String sql = "select * from user where userID = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, user.getUserID());
			rs = stmt.executeQuery();
			PreparedStatement update = null;
			while(rs.next()){
				int indexedrecords = rs.getInt("indexedrecords");
				String query = "update user set batchnumber = ? and indexedrecords = ? where userID = ?";
				update = db.getConnection().prepareStatement(query);
				update.setInt(1, batchIDIn);
				update.setInt(2, increment + indexedrecords);
				update.setInt(3, user.getUserID());
				if(update.executeUpdate() != 1){
						throw new DatabaseException("Could not update user: 1");
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
			throw new DatabaseException("Could not update user: 2");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
//	/**
//	 * Finds every user and places them in an array list.
//	 * @param void 
//	 * @return array list of all users
//	 */
	
	public void updateOut(User user, int increment) throws DatabaseException{
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try{
			String sql = "select * from user where userID = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, user.getUserID());
			rs = stmt.executeQuery();
			PreparedStatement update = null;
			while(rs.next()){
				int indexedrecords = rs.getInt("indexedrecords");
				String query = "update user set batchnumber = ?, indexedrecords = ? where userID = ?";
				update = db.getConnection().prepareStatement(query);
				update.setInt(1, -1);
				update.setInt(2, increment + indexedrecords);
				update.setInt(3, user.getUserID());
				if(update.executeUpdate() != 1){
						throw new DatabaseException("Could not update user: 1");
				}
			}
		}
		catch (SQLException e){
			throw new DatabaseException("Could not update user: 2");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
//	
//	public ArrayList<User> getAllUsers(){
//		return null;
//	}
	
}
