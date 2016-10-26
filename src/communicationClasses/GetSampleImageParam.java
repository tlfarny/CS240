package communicationsClasses;

/**
 * Parameters to be used by the getSampleImage function in the ClientCommunicator
 * takes in a username, password and a project ID;
 * @author travisfarnsworth
 *
 */

public class GetSampleImageParam {
	String username;
	String password;
	int projectID;
	
	public GetSampleImageParam(String uIn, String passIn, int idIn){
		username = uIn;
		password = passIn;
		projectID = idIn;
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

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

}
