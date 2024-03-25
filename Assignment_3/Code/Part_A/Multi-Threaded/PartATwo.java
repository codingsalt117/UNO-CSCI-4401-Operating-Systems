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

public class PartATwo implements Runnable {
	String threadName;
	String threadFile;
	String fileMaxWord;
	ArrayList<String> wordList;

	// constructor for runnables, has the two passed in variables, a string and arraylist for task
	public PartATwo(String name, String fileName){
		this.threadName = name;
		this.threadFile = fileName;
		this.fileMaxWord = "";
		this.wordList = new ArrayList<String>();
	}

	public static void processLine(String line, ArrayList<String> wordList){
		String text = line;
					
		// reg exp section
		String pattern = "[a-zA-Z]+";	
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(text);
		while (m.find()) {
			//System.out.println(m.group());
						
			//only grabbing words 5 characters or longer and storing them
			if(m.group().length() >= 5){
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
		}// end of finding max	

		return maxWord;
	}// end of findMaxWord

	public static void printResults(String threadName, String threadFile, String fileMaxWord){
		System.out.printf("%s - %s: %s\n", threadName, threadFile, fileMaxWord);
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
	}// end of read and process

	// run method for threads
	@Override
	public void run(){
		//reading in the file
		readAndProcess(threadFile, wordList);
		// finding the most common word
		printResults(threadName, threadFile, (fileMaxWord = findMaxWord(wordList)));
	}// end of run

	// main method
	public static void main(String[] args){

		// user input for file
		Scanner in = new Scanner(System.in);
		System.out.println("________________________________________");
		System.out.println("Enter the file name including file type: ");
		String fileName = in.nextLine();
		System.out.println("----------------------------------------");
		String thread1Name = "Thread 1";
		String thread2Name = "Thread 2";

		// Creating the runnables with passing the name of the thread and the name provided by user
		PartATwo runnable1 = new PartATwo(thread1Name, fileName);
		PartATwo runnable2 = new PartATwo(thread2Name, fileName);
		// Creating the threads
		Thread thread1 = new Thread(runnable1);
		Thread thread2 = new Thread(runnable2);

		//starting threads
		thread1.start();
		thread2.start();
		
	}// end of main
}// end of class