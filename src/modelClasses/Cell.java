package modelClasses;

public class Cell {
	int cellID;
	String value;
	int fieldID;
	int recordID;
	
	public Cell(String valIn, int fIn, int recIn){
		cellID = -1;
		value = valIn;
		fieldID = fIn;
		recordID = recIn;
	}

	public int getCellID() {
		return cellID;
	}

	public void setCellID(int cellID) {
		this.cellID = cellID;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getFieldID() {
		return fieldID;
	}

	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}

	public int getRecordID() {
		return recordID;
	}

	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}
	
}
