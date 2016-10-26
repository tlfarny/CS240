package modelClasses;

public class Record {
	int recordID;
	int parentBatchID;
	int rowNumber;
	
	public Record(int parIn, int rowIn){
		recordID = -1;
		parentBatchID = parIn;
		rowNumber = rowIn;
	}

	public int getRecordID() {
		return recordID;
	}

	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}

	public int getParentBatchID() {
		return parentBatchID;
	}

	public void setParentBatchID(int parentBatchID) {
		this.parentBatchID = parentBatchID;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
}
