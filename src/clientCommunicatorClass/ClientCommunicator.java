package clientCommunicatorClass;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import communicationsClasses.DownloadBatchParam;
import communicationsClasses.DownloadBatchResult;
import communicationsClasses.GetFieldsParam;
import communicationsClasses.GetFieldsResult;
import communicationsClasses.GetProjectsParam;
import communicationsClasses.GetProjectsResult;
import communicationsClasses.GetSampleImageParam;
import communicationsClasses.GetSampleImageResult;
import communicationsClasses.SearchParams;
import communicationsClasses.SearchResult;
import communicationsClasses.SubmitBatchParam;
import communicationsClasses.SubmitBatchResult;
import communicationsClasses.ValidateUser_Params;
import communicationsClasses.ValidateUser_Result;
import modelClasses.User;

public class ClientCommunicator {
	
	String host;
	int port;
	User user;
	String username;
	String password;
	DownloadBatchResult result;
	
	public ClientCommunicator(String hIn, String portIn){
		host = hIn;
		port = Integer.parseInt(portIn);
	}
	
	public String getHost(){
		return host;
	}
	
	public String getPortString(){
		return Integer.toString(port);
	}
	
	public User getUser(){
		return user;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public DownloadBatchResult getResult(){
		return result;
	}
	
	public void setResult(DownloadBatchResult result){
		this.result = result;
	}
	
	/**
	 * @author travisfarnsworth;
	 * 
	 * this will take in a username and password to find a user. If no user matches both the username and the password, reject with an error message. 
	 * @param take in validateUserParams, which contains username and password
	 * @return validateUserResult type which contains the first name, last name, a boolean value on whether the user exists or not, the number of batches completed by the user, and (if it doesn't exist, returns null) the User object
	 * 
	 */
	public ValidateUser_Result validateUser(ValidateUser_Params params)throws Exception{
		return (ValidateUser_Result) doPost("/validateUser", params);
	}
	
	/**
	 * @param uses GetProjectsParam which contains a username and password gleaned from a user
	 * @return GetProjectResult which contains an ArrayList of Project objects. 
	 */
	public GetProjectsResult getProjects(GetProjectsParam params)throws Exception{
		return (GetProjectsResult) doPost("/getProjects", params);
	}
	
	/**
	 * 
	 * @param Uses GetSampleImageParam which contains username, password, and projectID.
	 * @return GetSampleImageResult containing the imageURL as a String
	 */
	
	public GetSampleImageResult getSampleImage(GetSampleImageParam params)throws Exception{
		return (GetSampleImageResult) doPost("/getSampleImage", params);
	}
	
	/**
	 * @param Uses DownloadBatchParam which contains username, password and projectID.
	 * @return DownloadBatchResult which contains a batch, a project, an int which is the number of records per image, an ArrayList of the fields in the image, and the User which is downloading the batch.
	 */
	
	public DownloadBatchResult downloadBatch(DownloadBatchParam param)throws Exception{
		return (DownloadBatchResult) doPost("/downloadBatch", param);
	}
	
	/**
	 * @param Uses SubmitBatchParam which contains username, password, batch ID, and an ArrayList of ArrayLists of the values for the batch that is being submitted.
	 * @return SubmitBatchResult which contains a boolean value determining if the function worked or not
	 */
	
	public SubmitBatchResult submitBatch(SubmitBatchParam param)throws Exception{
		return (SubmitBatchResult) doPost("/submitBatch", param);
	}
	
	/**
	 * @param Uses GetFieldsParam which contains username, password, and projectID
	 * @return GetFieldsResult which is an array list of fields from the given project
	 */
	
	public GetFieldsResult getFields(GetFieldsParam param)throws Exception{
		return (GetFieldsResult) doPost("/getFields", param);
	}
	
	/**
	 * @param Uses SearchParams which contain username, password, and arrays for the different search fields and the different terms to be searched within those fields
	 * @return SearchResult contains each instance of the search term being matched
	 */
	
	public SearchResult search(SearchParams params)throws Exception{
		return (SearchResult) doPost("/search", params);
	}
	
	private Object doPost(String context, Object params)throws Exception{
		 XStream xs = new XStream(new DomDriver());
		 URL myURL;
		try {
			myURL = new URL("http://" + host + ":" + port + context);
			HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
			 conn.setRequestMethod("POST");
//			 conn.setDoInput(true);
			 conn.setDoOutput(true);
			 conn.connect();
			 xs.toXML(params, conn.getOutputStream());
			 conn.getOutputStream().close();
			 //if header == 200 (conn.getResponseCode())
			 int header = conn.getResponseCode();
			 Object o = null;
			 if(header == 200){
				 o = xs.fromXML(conn.getInputStream());
			 }
			 return o;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new Exception();
		} catch (ProtocolException e) {
			e.printStackTrace();
			throw new Exception();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	
}
