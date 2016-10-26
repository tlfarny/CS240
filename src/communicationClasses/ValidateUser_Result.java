package communicationsClasses;

import modelClasses.User;

/**
 * This will be the result produced from a successful username and password entry. 
 * If unsuccessful because of no match, return false. 
 * If could not contact the server etc, return failed.
 * @author travisfarnsworth
 *
 */

public class ValidateUser_Result {
	String firstName;
	String lastName;
	int numRecords;
	boolean exists = false;
	User user;
	
	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public ValidateUser_Result(User user){
		firstName = user.getFirstName();
		lastName = user.getLastName();
		numRecords = user.getTotalNumberIndexed();
		this.user = user;
		if(user != null){
			exists = true;
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getNumRecords() {
		return numRecords;
	}

	public void setNumRecords(int numRecords) {
		this.numRecords = numRecords;
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder("TRUE\n");
		result.append(firstName);
		result.append("\n");
		result.append(lastName);
		result.append("\n");
		result.append(numRecords);
		result.append("\n");
		return result.toString();
	}
}
