package spell;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector{
	TreeSet<String> found, notFound;
	Trie dictionary;
	
	public SpellCorrector() {
		dictionary = new Trie();
		found = new TreeSet<String>();
		notFound = new TreeSet<String>();
	}
	
	public void tryCorrections(String word){
		insertion(word);
		deletion(word);
		alteration(word);
		transposition(word);
	}
	
	public void insertion(String word){
		for (int i = 0; i <= word.length(); i++) {
			StringBuilder sb = new StringBuilder(word);
			sb.insert(i, 'a');
			for (char j = 'a'; j<='z'; j++){
				sb.setCharAt(i, j);
				checkWord(sb.toString());
			}
		}
	}
	
	public void deletion(String word){
		for (int i = 0; i < word.length(); i++) {
			StringBuilder sb = new StringBuilder();
			sb.deleteCharAt(i);
			checkWord(sb.toString());
		}
	}
	
	public void transposition(String word){
		for (int i = 0; i < word.length()-1; i++) {
			StringBuilder sb = new StringBuilder(word);
			char first, second;
			first = sb.charAt(i);
			second = sb.charAt(i+1);
			sb.setCharAt(i, second);
			sb.setCharAt(i+1, first);
			checkWord(sb.toString());
		}
	}
	
	public void alteration(String word){
		for (int i = 0; i < word.length(); i++) {
			StringBuilder sb = new StringBuilder();
			for(char j = 'a'; j < 'z'; j++){
				sb.setCharAt(i, j);
				checkWord(sb.toString());
			}
		}
	}
	
	public void checkWord(String checkedWord){
		if (dictionary.find(checkedWord) != null) {
			found.add(checkedWord);
		}
		else{
			notFound.add(checkedWord);
		}
	}

	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		dictionary = new Trie();
		String word = "";
		Scanner s = new Scanner(new BufferedInputStream(new FileInputStream(dictionaryFileName)));
		while(s.hasNext()){
			word = s.next();
			word = word.toLowerCase();
			dictionary.add(word);
		}
	}

	@Override
	public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
		found = new TreeSet<String>();
		notFound = new TreeSet<String>();
		inputWord = inputWord.toLowerCase();
		if (dictionary.find(inputWord) != null) {
			return inputWord;
		}
		else{
			TreeSet<String> bestWords = new TreeSet<String>();
			int freq = 0;
			tryCorrections(inputWord);
			if (found.size()==0) {
				TreeSet<String> secondAttempt = new TreeSet<String>(notFound);
				for(String current: secondAttempt){
					tryCorrections(current);
				}
				if(found.size()==0){
					throw new NoSimilarWordFoundException();
				}
				else{
					int highestFreq = 0;
					for(String current: found){
						freq = dictionary.getNodeFreq(current);
						if (freq > highestFreq) {
							bestWords.clear();
							bestWords.add(current);
							highestFreq = freq;
						}
						else if (freq == highestFreq) {
							bestWords.add(current);
						}
						else{
						}
					}
					for(String answer: bestWords){
						return answer;
					}
				}
			}
			else{
				int highestFreq = 0;
				for(String current: found){
					freq = dictionary.getNodeFreq(current);
					if (freq > highestFreq) {
						bestWords.clear();
						bestWords.add(current);
						highestFreq = freq;
					}
					else if (freq == highestFreq) {
						bestWords.add(current);
					}
					else{
					}
				}
				for(String answer: bestWords){
					return answer;
				}
			}
		}
		throw new NoSimilarWordFoundException();
	}

}
