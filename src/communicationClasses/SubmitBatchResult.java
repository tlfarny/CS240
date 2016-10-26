package communicationsClasses;

public class SubmitBatchResult {
	boolean worked;
	
	public SubmitBatchResult(boolean in){
		worked = in;
	}

	public boolean isWorked() {
		return worked;
	}

	public void setWorked(boolean worked) {
		this.worked = worked;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if(worked == true){
			sb.append("TRUE\n");
		}
		else{
			sb.append("FALSE\n");
		}
		return sb.toString();
	}
}
