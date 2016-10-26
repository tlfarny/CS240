package modelClasses;

public class FieldModels {
	int fieldID;
	int parentProjectID;
	String title;
	int xCoord;
	int width;
	String helpHTML;
	String knownData;
	
	public FieldModels(int parIn, String titIn, int xIn, int widIn, String helpIn, String kIn){
		fieldID = -1;
		parentProjectID = parIn;
		title = titIn;
		xCoord = xIn;
		width = widIn;
		helpHTML = helpIn;
		knownData = kIn;
	}

	public String getHelpHTML() {
		return helpHTML;
	}

	public void setHelpHTML(String helpHTML) {
		this.helpHTML = helpHTML;
	}

	public String getKnownData() {
		return knownData;
	}

	public void setKnownData(String knownData) {
		this.knownData = knownData;
	}

	public int getFieldID() {
		return fieldID;
	}

	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}

	public int getParentProjectID() {
		return parentProjectID;
	}

	public void setParentProjectID(int parentProjectID) {
		this.parentProjectID = parentProjectID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
}
