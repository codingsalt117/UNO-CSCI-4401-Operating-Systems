import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class PartAOne{
	
	public static void processLine(String line, ArrayList<String> wordList){
		String text = line;			
		// reg exp section
		String pattern = "[a-zA-Z]+";	
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(text);
		
		while (m.find()) {			
			//only grabbing words 7 characters or longer and storing them
			if(m.group().length() >= 7){
				wordList.add(m.group());
			}
		}//System.out.println(wordList);
	}// end of processLine	

	public static String findMaxWord(ArrayList<String> wordList){
		if(wordList.isEmpty()){
			System.out.println("The arraylist is empty!");
			return null;
		}

		Map<String, Integer> wordMap = new HashMap<>();
		String maxWord = "";
		int maxCount = 0;

		for(String word : wordList){
			// checking if the word occured or not, grabbing and inc or creating a count
			int wordCount = wordMap.getOrDefault(word, 0) + 1;
			wordMap.put(word, wordCount);

			// tracking for max word and count
			if(wordCount > maxCount){
				maxWord = word;
				maxCount = wordCount;
			}
		}	
		return maxWord;
	}// end of findMaxWord

	public static void printResults(String fileName, String fileMaxWord){
		System.out.printf("%s: %s\n", fileName, fileMaxWord);
		System.out.println("________________________________________");
	} // end of printResults

	public static void readAndProcess(String fileName, ArrayList<String> wordList){
		String line = "";
		try{
			FileReader dataFile = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(dataFile);

			// process by line
			while((line = reader.readLine()) != null){
				processLine(line, wordList);
			}// end of processing

			// closing readers
			if(dataFile != null){
				dataFile.close();
			}
			if(reader != null){
				reader.close();
			}
		}//end of file read try
		catch(IOException e){
			e.printStackTrace();
		}		
	}

	// main method
	public static void main(String[] args){
		// user input for file
		Scanner in = new Scanner(System.in);
		System.out.println("________________________________________");
		System.out.println("Enter the file name including file type: ");
		String fileName = in.nextLine();
		System.out.println("----------------------------------------");
		//System.out.println(fileName);

		//reading in the file
		String fileMaxWord = "";
		ArrayList<String> wordList = new ArrayList<String>();

		readAndProcess(fileName,wordList);

		// finding the most common word
		printResults(fileName, (fileMaxWord = findMaxWord(wordList)));
	}// end of main

}// end of class