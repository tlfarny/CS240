package communicationsClasses;

import java.util.ArrayList;

import modelClasses.Project;

/**
 * The return class for the getProject function
 * Generates a string of the projectID, new line, projectTitle, newline.
 * @author travisfarnsworth
 *
 */

public class GetProjectsResult {
	
	ArrayList<Project> projects;
	
	public GetProjectsResult(ArrayList<Project> projectsIn){
		projects = projectsIn;
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}
	
	public String toString(){
		if(projects != null){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < projects.size(); i++) {
				Project project = projects.get(i);
				sb.append(project.getID());
				sb.append("\n");
				sb.append(project.getTitle());
				sb.append("\n");
			}
			return sb.toString();
		}
		return null;
	}
	
}