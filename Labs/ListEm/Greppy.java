package listem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import listem.IGrep;


public class Greppy extends SharedWork implements IGrep {
	
	Map<File, List<String>> map;
	String substringPattern;
	String filePattern;
	
	public Greppy(){
		String substringPattern = "";
		String filePattern = "";
		map = new TreeMap<File, List<String>>();
	}

	@Override
	public Map<File, List<String>> grep(File directory,
			String fileSelectionPattern, String substringSelectionPattern,
			boolean recursive) {
		substringPattern = substringSelectionPattern;
		filePattern  = fileSelectionPattern;
		findFiles(directory, fileSelectionPattern, recursive);
		
		return map;
	}

	@Override
	public void doStuff(File fileName){
		List<String> list = null;
		String line = "";
		Scanner s;
		try {
			s = new Scanner(new BufferedInputStream(new FileInputStream(fileName.getName())));
			while(s.hasNext()){
				line = s.nextLine();
				if(line.matches(".*" + substringPattern + ".*")){
					list.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(list!=null){
			map.put(fileName, list);
		}
	}

}


