package communicationsClasses;

import java.util.ArrayList;

public class SubmitBatchParam {
	String username;
	String password;
	int batchID;
	ArrayList<ArrayList<String>> values;
	
	public SubmitBatchParam(String userIn, String passIn, int batchIn, ArrayList<ArrayList<String>> values){
		username = userIn;
		password = passIn; 
		batchID = batchIn;
		this.values = values;
	}

	public ArrayList<ArrayList<String>> getValues() {
		return values;
	}

	public void setValues(ArrayList<ArrayList<String>> values) {
		this.values = values;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getBatchID() {
		return batchID;
	}

	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}
}
