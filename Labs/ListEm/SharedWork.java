package listem;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

public abstract class SharedWork {
	
	public void findFiles(File directory, String fileSelectionPattern, boolean recursive){
		if(!directory.exists()){
			System.out.println("not there");
		}
		File[] files = directory.listFiles();
		if(recursive == true){
			for(int i = 0; i < files.length; i++){
				if(files[i].isFile()==true){											//if this file is a file
					if(files[i].getName().matches(fileSelectionPattern)){				//and its name matches the file selection pattern
						doStuff(files[i]);													//then do stuff to this file depending on which program I am running
					}
				}
				else if(files[i].isDirectory() == true){								//if another directory is found, 
					findFiles(files[i], fileSelectionPattern, recursive);				//recurse through the new directory
				}
			}
		}
		else{
			for(int i= 0; i < files.length; i++){
				if(files[i].isFile()==true){											//if this file is a file
					if(files[i].getName().matches(fileSelectionPattern)){				//and its name matches the file selection pattern
						doStuff(files[i]);													//then do stuff to this file depending on which program I am running
					}
				}
			}
		}
	}
	
	public abstract void doStuff(File fileName);

}
