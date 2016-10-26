package communicationsClasses;

/**
 * Parameter to be used in the getFields function of the ClientCommunicator class.
 * Takes the username, password and projectID as input. 
 * @author travisfarnsworth
 *
 */

public class GetFieldsParam {
	String username;
	String password;
	String projectIDString;
	int projectID = -1;
	
	public GetFieldsParam(String uIn, String pIn, String idIn){
		username = uIn;
		password = pIn;
		projectIDString = idIn;
		if (projectIDString != null) {
			projectID = Integer.parseInt(projectIDString);
		}
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public void setProjectIDString(int projectID) {
		this.projectID = projectID;
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

	public String getProjectIDString() {
		return projectIDString;
	}

	public void setProjectID(String projectIDString) {
		this.projectIDString = projectIDString;
	}
	
}
