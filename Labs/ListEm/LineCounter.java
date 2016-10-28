package listem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class LineCounter extends SharedWork implements ILineCounter{
	Map<File, Integer> map;
	String filePattern;
	public LineCounter(){
		map = null;
		filePattern = "";
	}

	@Override
	public Map<File, Integer> countLines(File directory,
			String fileSelectionPattern, boolean recursive) {
		filePattern = fileSelectionPattern;
		findFiles(directory, fileSelectionPattern, recursive);
		return map;
	}

	@Override
	public void doStuff(File fileName) {			//this file already matches
		Scanner s;
		int lineTotal=0;
		try {
			s = new Scanner(fileName);
			while(s.hasNext()){
				s.nextLine();
				lineTotal++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(lineTotal>0){
			Integer i = lineTotal;
			map.put(fileName, i);
		}
	}

}
