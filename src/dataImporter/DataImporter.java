package dataImporter;

import java.io.File;
import org.apache.commons.io.*;

public class DataImporter {
	
	public static void main(String[] args){
		
		if(args.length != 1){
			System.out.println("Could not parse object. INCORRECT INPUT");
		}
		else{
			try {
				
				String xmlFilename = args[0];
				File xmlFile = new File(xmlFilename);
				File dest = new File("Records");
				if(!xmlFile.getParentFile().getCanonicalPath().equals(dest.getCanonicalPath())){
					FileUtils.deleteDirectory(dest);
					FileUtils.copyDirectory(xmlFile.getParentFile(), dest);
				}
				//	Copy the directories (recursively) from source to destination.
				new XMLParser(args[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
