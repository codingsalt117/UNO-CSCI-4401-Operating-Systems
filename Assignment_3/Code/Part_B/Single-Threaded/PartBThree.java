import java.util.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
import java.io.*;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartBThree {

	public static void readAndProcess(String fileName, ArrayList<String> wordList){
		wordList.clear();
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
	}// end of read and process

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
		}
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
		System.out.println("----------------------------------------");
	} // end of printResults

	public static void printTime(double totalTime){
		System.out.println("________________________________________");
		System.out.printf("Total time to process all files in the directory: %f seconds!\n", totalTime);
		System.out.println("________________________________________");
		
	}

	// main method
	public static void main(String[] args){
		// user input for file
		Scanner in = new Scanner(System.in);
		System.out.println("________________________________________");
		System.out.println("Welcome to the word Counter!");
		System.out.println("File Processing is starting, results are displayed per file.");
		System.out.println("________________________________________");

		String fileMaxWord = "";
		String fileName = "";
		String filePath = "D:\\School\\Fall 2023\\CSCI4401_Op_System\\Ass-3\\Data_Files";
		ArrayList<String> wordList = new ArrayList<String>();
		File directoryPath = new File(filePath);
		String[] filesList = directoryPath.list();
		
		// starting timer
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < filesList.length; i++){
			fileName = filesList[i];
			//processing the file in position i
			readAndProcess(fileName, wordList);
			// finding the most common word of file in position i
			printResults(fileName, (fileMaxWord = findMaxWord(wordList)));
		}

		// ending time
		long endTime = System.currentTimeMillis();
		// calculating time to complete in seconds
		double totalTime = (endTime - startTime) / 1000.0;

		printTime(totalTime);		
		
	}// end of main
}// end of class 