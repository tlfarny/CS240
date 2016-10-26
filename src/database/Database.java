package database;

import java.io.*;
import java.sql.*;

import databaseAccessClasses.BatchDAO;
import databaseAccessClasses.CellDAO;
import databaseAccessClasses.FieldDAO;
import databaseAccessClasses.ProjectDAO;
import databaseAccessClasses.RecordDAO;
import databaseAccessClasses.UserDAO;


public class Database {
	
	private static final String DATABASE_DIRECTORY = "database";
	private static final String DATABASE_FILE = "db1.sqlite";
	private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_DIRECTORY +
												File.separator + DATABASE_FILE;

	public static void initialize() throws DatabaseException {
		try {
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}
		catch(ClassNotFoundException e) {
			
			DatabaseException serverEx = new DatabaseException("Could not load database driver", e);

			throw serverEx; 
		}
	}

	private BatchDAO batchDAO;
	private CellDAO cellDAO;
	private FieldDAO fieldDAO;
	private ProjectDAO  projectDAO;
	private RecordDAO recordDAO;
	private UserDAO userDAO;
	private Connection connection;
	
	public Database() {
		batchDAO = new BatchDAO(this);
		cellDAO = new CellDAO(this);
		fieldDAO = new FieldDAO(this);
		projectDAO = new  ProjectDAO(this);
		recordDAO = new RecordDAO(this);
		userDAO = new UserDAO(this);
		connection = null;
	}
	
	public BatchDAO getBatchDAO() {
		return batchDAO;
	}
	
	public CellDAO getCellDAO() {
		return cellDAO;
	}

	public FieldDAO getFieldDAO() {
		return fieldDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public RecordDAO getRecordDAO() {
		return recordDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public Connection getConnection() {
		return connection;
	}

	public void startTransaction() throws DatabaseException {
		try {
			System.out.println("Starting Transaction");
			connection = DriverManager.getConnection(DATABASE_URL);
			connection.setAutoCommit(false);
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not connect to database. Make sure " + 
				DATABASE_FILE + " is available in ./" + DATABASE_DIRECTORY, e);
		}
	}
	
	public void endTransaction(boolean commit) {
		{		
			System.out.println("Ending Transaction");
			try {
				if (commit) {
					connection.commit();
				}
				else {
					connection.rollback();
				}
			}
			catch (SQLException e) {
				System.out.println("Could not end transaction");
				e.printStackTrace();
			}
			finally {
				safeClose(connection);
				connection = null;
			}
		}
	}
	
	public static void safeClose(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
}
