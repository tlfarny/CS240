package modelClasses;

public class SearchTuple implements Comparable<SearchTuple>{
	Cell cell;
	Batch batch;
	FieldModels field;
	Record record;
	
	public SearchTuple(Cell cell, Batch batch, FieldModels field, Record record) {
		this.cell = cell;
		this.batch = batch;
		this.field = field;
		this.record = record;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public FieldModels getField() {
		return field;
	}

	public void setField(FieldModels field) {
		this.field = field;
	}

	@Override
	public int compareTo(SearchTuple o) {
		return this.getCell().getValue().compareTo(o.getCell().getValue());
	}
	
}
