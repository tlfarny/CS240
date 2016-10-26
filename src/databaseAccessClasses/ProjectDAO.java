package databaseAccessClasses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modelClasses.Project;
import database.Database;
import database.DatabaseException;

public class ProjectDAO{
	
	Database db;
	
	public ProjectDAO(Database db){
		this.db = db;
	}
	
	public void create(Project project) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		try{
			String query = "insert into project (title, recordsperimage, firstycoord, recordheight) values (?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, project.getTitle());
			stmt.setInt(2, project.getRecordsPerImage());
			stmt.setInt(3, project.getFirstYCoordinate());
			stmt.setInt(4, project.getRecordHeight());
			if(stmt.executeUpdate()==1){
				Statement keyStmt = db.getConnection().createStatement();                           
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");                         
				keyRS.next();                                                                       
				int id = keyRS.getInt(1);                                                           
				project.setID(id);
			}
			else{
				throw new DatabaseException("Could not add project: 1");
			}
		}
		catch (SQLException e){
			throw new DatabaseException("Could not add project: 2");
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	public Project read(int projectID) throws DatabaseException{
		System.out.println("project read");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from project where id = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			while(rs.next()){
				int ID = rs.getInt("id");
				String title = rs.getString("title");
				int recordsPerImage = rs.getInt("recordsPerImage");
				int firstYCoord = rs.getInt("firstYCoord");
				int recordHeight = rs.getInt("recordHeight");
				Project result = new Project(title, recordsPerImage, firstYCoord, recordHeight);
				result.setID(ID);
				return result;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException("Could not add project");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		return null;
	}
	
	public ArrayList<Project> readAllProjects() throws DatabaseException{
		ArrayList<Project> result = new ArrayList<Project>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from project";
			stmt = db.getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				int ID = rs.getInt("ID");
				String title = rs.getString("title");
				int recordsPerImage = rs.getInt("recordsPerImage");
				int firstYCoord = rs.getInt("firstYCoord");
				int recordHeight = rs.getInt("recordHeight");
				Project temp = new Project(title, recordsPerImage, firstYCoord, recordHeight);
				temp.setID(ID);
				result.add(temp);
			}
			return result;
		} catch (SQLException e) {
			throw new DatabaseException("Could not get all projects");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
}