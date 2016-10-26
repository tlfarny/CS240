package communicationsClasses;

/**
 * Parameter to be used in the downloadBatch function in the ClientCommunicator
 * Constructed from a username, password, and project ID
 * @author travisfarnsworth
 *
 */

public class DownloadBatchParam {
	
	String username;
	String password;
	int projectID;
	
	public DownloadBatchParam(String uIn, String pIn, int idIn){
		username = uIn;
		password = pIn;
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
