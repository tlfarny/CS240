package communicationsClasses;

import java.util.ArrayList;

public class SearchParams {
	String username;
	String password;
	ArrayList<Integer> searchFields;
	ArrayList<String> Terms;
	
	public SearchParams(String uIn, String pIn, ArrayList<Integer> fieldsIn, ArrayList<String> termsIn){
		username = uIn;
		password = pIn;
		searchFields = fieldsIn;
		Terms = termsIn;
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

	public ArrayList<Integer> getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(ArrayList<Integer> searchFields) {
		this.searchFields = searchFields;
	}

	public ArrayList<String> getTerms() {
		return Terms;
	}

	public void setTerms(ArrayList<String> terms) {
		Terms = terms;
	}
}
