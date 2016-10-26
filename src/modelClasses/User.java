package modelClasses;

public class User {
	int userID;
	String userName;
	String password;
	String firstName;
	String lastName;
	String emailAddress;
	int totalNumberIndexed;
	int batchNumber;
	
	public User(String userIn, String pIn, String FN, String LN, String emailIn, int totalNumbIn){
		userID = -1;
		userName = userIn;
		password = pIn;
		firstName = FN;
		lastName = LN;
		emailAddress = emailIn;
		totalNumberIndexed = totalNumbIn;
		batchNumber = -1;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getTotalNumberIndexed() {
		return totalNumberIndexed;
	}

	public void setTotalNumberIndexed(int totalNumberIndexed) {
		this.totalNumberIndexed = totalNumberIndexed;
	}

	public int getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}
}
