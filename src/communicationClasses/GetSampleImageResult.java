package communicationsClasses;

/**
 * The class that is returned from the getSampleImage function in the ClientCommunicator
 * Creates a string which is a URL passed into the function constrcutor. 
 * @author travisfarnsworth
 *
 */

public class GetSampleImageResult {
	String imageURL;
	
	public GetSampleImageResult(String in){
		imageURL = in;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public String toString(String host, String port){
		StringBuilder sb  = new StringBuilder();
		sb.append("http://" + host + ":" + port + "/" + imageURL + "\n");
		return sb.toString();
	}
}
