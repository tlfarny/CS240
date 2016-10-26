package communicationsClasses;

import java.util.ArrayList;
import modelClasses.FieldModels;

/**
 * Result uses toString to make the exact correct output. 
 * Parameters require an Array list of the matching FieldModels to iterate through.
 * @author travisfarnsworth
 *
 */

public class GetFieldsResult {
	
	ArrayList<FieldModels> fields;
	
	public GetFieldsResult(ArrayList<FieldModels> fIn){
		fields = fIn;
	}

	public ArrayList<FieldModels> getFields() {
		return fields;
	}

	public void setFields(ArrayList<FieldModels> fields) {
		this.fields = fields;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(FieldModels current : fields){
			s.append(current.getParentProjectID() + "\n");
			s.append(current.getFieldID() + "\n");
			s.append(current.getTitle() + "\n");
		}
		return s.toString();
	}

}
