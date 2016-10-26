package modelClasses;

public class Batch {
	int batchID;
	int parentProjectID;
	String filePath;
	boolean isAvailable;
	
	public Batch(int parentIn, String pathIn){
		batchID = -1;
		parentProjectID = parentIn;
		filePath = pathIn;
		isAvailable = true;
	}

	public int getBatchID() {
		return batchID;
	}

	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

	public int getParentProjectID() {
		return parentProjectID;
	}

	public void setParentProjectID(int parentProjectID) {
		this.parentProjectID = parentProjectID;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
}
