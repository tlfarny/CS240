package communicationsClasses;

import java.util.Set;

import modelClasses.SearchTuple;

public class SearchResult {
	Set<SearchTuple> found;
	
	public SearchResult(Set<SearchTuple> fIn){
		found =  fIn;
	}

	public Set<SearchTuple> getFound() {
		return found;
	}

	public void setFound(Set<SearchTuple> found) {
		this.found = found;
	}
	
	public String toString(String host, String port){
		StringBuilder sb = new StringBuilder();
		for(SearchTuple current : found){
			sb.append(current.getBatch().getBatchID() + "\n");
			sb.append("http://" + host + ":" + port + "/" +  current.getBatch().getFilePath() + "\n");
			sb.append(current.getRecord().getRowNumber() + "\n");
			sb.append(current.getField().getFieldID() + "\n");
		}
		return sb.toString();
	}
}
