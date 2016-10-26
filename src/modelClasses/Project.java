package modelClasses;

//import java.util.ArrayList;

public class Project {
	int ID;
	String title;
	int recordsPerImage;   //AKA rows
	int firstYCoordinate;
	int recordHeight;
	
	public Project(String titIn, int recsIn, int yIn, int heightIn){
		ID = -1;
		title = titIn;
		recordsPerImage = recsIn;
		firstYCoordinate = yIn;
		recordHeight = heightIn;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRecordsPerImage() {
		return recordsPerImage;
	}

	public void setRecordsPerImage(int recordsPerImage) {
		this.recordsPerImage = recordsPerImage;
	}

	public int getFirstYCoordinate() {
		return firstYCoordinate;
	}

	public void setFirstYCoordinate(int firstYCoordinate) {
		this.firstYCoordinate = firstYCoordinate;
	}

	public int getRecordHeight() {
		return recordHeight;
	}

	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}
	
}
