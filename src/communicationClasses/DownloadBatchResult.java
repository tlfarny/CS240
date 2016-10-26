package communicationsClasses;

import java.util.ArrayList;

import modelClasses.Batch;
import modelClasses.FieldModels;
import modelClasses.Project;
import modelClasses.User;

public class DownloadBatchResult {
	Batch batch;
	Project project;
	int recordsperimage;
	ArrayList<FieldModels> fields;
	User user;
	
	
	public DownloadBatchResult(Batch bIn, Project project, int recordsperimage, ArrayList<FieldModels> fields, User user){
		batch = bIn;
		this.project = project;
		this.recordsperimage = recordsperimage;
		this.fields = fields;
		this.user = user;
	}

	public Batch getBatch() {
		return batch;
	}
	
	public Project getProject(){
		return this.project;
	}
	
	public int getRecordsPerImage(){
		return this.recordsperimage;
	}
	
	public ArrayList<FieldModels> getFields(){
		return this.fields;
	}
	
	public User getUser(){
		return this.user;	
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	
	public String toString(String host, String port){
		StringBuilder sb = new StringBuilder();
		sb.append(batch.getBatchID() + "\n");
		sb.append(batch.getParentProjectID() + "\n");
		sb.append("http://"+host+":"+port+"/"+batch.getFilePath() + "\n");
		sb.append(project.getFirstYCoordinate() + "\n");
		sb.append(project.getRecordHeight() + "\n");
		sb.append(recordsperimage + "\n");
		sb.append(fields.size() + "\n");
		int counter = 0;
		for (FieldModels current : fields) {
			counter++;
			sb.append(current.getFieldID() + "\n");	
			sb.append(counter + "\n");
			sb.append(current.getTitle() + "\n");
			sb.append("http://" + host + ":" + port + "/" + current.getHelpHTML() + "\n");
			sb.append(current.getxCoord() + "\n");
			sb.append(current.getWidth() + "\n");
			if(current.getKnownData() != null){
				sb.append("http://" + host + ":" + port + "/" + current.getKnownData() + "\n");
			}
		}
	return sb.toString();
	}

}
