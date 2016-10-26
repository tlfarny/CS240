package communicationsClasses;

/**
 * to be used for the input parameters in the validateUser function
 * @author travisfarnsworth
 *
 */

public class ValidateUser_Params {
	String username;
	String password;
	
	public ValidateUser_Params(String userIn, String passIn){
		username = userIn;
		password = passIn;
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
