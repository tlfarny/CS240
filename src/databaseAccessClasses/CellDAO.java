package databaseAccessClasses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.Database;
import database.DatabaseException;
import modelClasses.Cell;

public class CellDAO {
	
	Database db;
	
	public CellDAO(Database db){
		this.db = db;
	}
	
	/**
	 * Uses a cell as input to create the cell in the database
	 * @param cell
	 * @return void 
	 */
	
	public void create(Cell cell)throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		try{
			String query = "insert into cell (value, fieldID, recordID) values (?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, cell.getValue());
			stmt.setInt(2, cell.getFieldID());
			stmt.setInt(3, cell.getRecordID());
			if(stmt.executeUpdate()==1){
				Statement keyStmt = db.getConnection().createStatement();                           
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");                         
				keyRS.next();                                                                       
				int id = keyRS.getInt(1);                                                           
				cell.setCellID(id);
				System.out.println("Cell created");
			}
			else{
				throw new DatabaseException("Could not add cell: 1");
			}
		}
		catch (SQLException e){
			throw new DatabaseException("Could not add cell: 2");
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * Uses a cell ID to find a specific cell to return.
	 * @param cellID
	 * @return the cell with the matching cellID
	 */
	
	public Cell read(int cellIDIn) throws DatabaseException{
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Cell result = null;
		try{
			String query = "select * from cell";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			while(rs.next()){
				int cellID = rs.getInt("cellID");
				String value = rs.getString("value");
				int fieldID = rs.getInt("fieldID");
				int recordID = rs.getInt("recordID");
				if(cellID == cellIDIn){
					result = new Cell(value, fieldID, recordID);
					result.setCellID(cellID);
					return result;
				}
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not find Cell");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		return result;
	}
	
	public ArrayList<Cell> readAllCellsWith(int fieldIDIn, String term) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Cell> result = new ArrayList<Cell>();
		try {
			String sql = "select * from Cell WHERE fieldID = ? AND value = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, fieldIDIn);
			stmt.setString(2, term);
			rs = stmt.executeQuery();
			while(rs.next()){
				int cellID = rs.getInt("cellID");
				String value = rs.getString("value");
				int fieldID = rs.getInt("fieldID");
				int recordID = rs.getInt("recordID");
				Cell cell = new Cell(value, fieldID, recordID);
				cell.setCellID(cellID);
				result.add(cell);
			}
			return result;
		} catch (SQLException e) {
			 throw new DatabaseException("Could not get all cells");
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(rs);
		}
	}

}
