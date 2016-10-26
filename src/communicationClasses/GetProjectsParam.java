package communicationsClasses;

import modelClasses.User;

/**
 * Parameters for the getParameters function in the ClientCommunicator.
 * Takes in a user.
 * @author travisfarnsworth
 */

public class GetProjectsParam {
	
	String username;
	String password;
	
	public GetProjectsParam(User user){
		username = user.getUserName();
		password = user.getPassword();
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

}
