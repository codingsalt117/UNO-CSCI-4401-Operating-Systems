import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit; 

public class PartBFour   implements Runnable {
	String threadName;
	String threadFile;
	String fileMaxWord;
	ArrayList<String> wordList;

	/*
	   constructor for runnables, has the two passed in variables, a string and arraylist for task
	*/
	public PartBFour(String name, String fileName){
		this.threadName = name;
		this.threadFile = fileName;
		this.fileMaxWord = "";
		this.wordList = new ArrayList<String>();
	}

	/* 
	   method for reading in the file, takes in the file name and the objects arraylist for the file. 
	   passes each line and the arraylist to helper method, processLine()
	*/
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

	/* 
	   helper method to readAndProcess, takes in the line and array list
	   using a regular expression supplied by instructor, puts all words that fit the condition
	   into the arraylist
	*/ 
	public static void processLine(String line, ArrayList<String> wordList){
		String text = line;
		int length = 7;
					
		// reg exp section
		String pattern = "[a-zA-Z]+";	
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(text);
		while (m.find()) {			
			//only grabbing words 5 characters or longer and storing them
			if(m.group().length() >= length){
				wordList.add(m.group());
			}
		}
	}// end of processLine	

	/* 
	   Method for finding the the most common word in the passed in arraylist
	*/ 
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

	// printing method for the thread
	public static void printResults(String threadName, String threadFile, String fileMaxWord, Double totalTime){
		System.out.printf("%s - %s: %s --- Time Elapsed: %f seconds\n", threadName, threadFile, fileMaxWord, totalTime);
		System.out.println("----------------------------------------");
	} // end of printResults

	// printing method for total runtime
	public static void printTime(double totalTime){
		System.out.println("________________________________________");
		System.out.printf("Total time to process all files in the directory: %f seconds!\n", totalTime);
		System.out.println("________________________________________");	
	}// end of print time

	// run method for threads
	@Override
	public void run(){
		// starting timer
		long beginTime = System.currentTimeMillis();
		//reading in the file
		readAndProcess(threadFile, wordList);
		// finding the most common word
		this.fileMaxWord = findMaxWord(wordList);
		long stopTime = System.currentTimeMillis();
		// calculating time to complete in seconds
		double threadTime = (stopTime - beginTime) / 1000.0;
		printResults(threadName, threadFile, fileMaxWord, threadTime);
	}// end of run

	// main method
	public static void main(String[] args){

		// Print out to screen for aesthetics
		System.out.println("________________________________________");
		System.out.println("Welcome to the word Counter!");
		System.out.println("File Processing is starting, results are displayed per file.");
		System.out.println("________________________________________");

		int poolSize = 14;
		String fileMaxWord = "";
		String fileName = "";
		String filePath = "D:\\School\\Fall 2023\\CSCI4401_Op_System\\Ass-3\\Data_Files";
		ArrayList<String> wordList = new ArrayList<String>();
		// grabbing the directory
		File directoryPath = new File(filePath);
		// getting all the file names in the directory
		String[] filesList = directoryPath.list();
		// creating a thread pool for all the threads needed
		ExecutorService pool = Executors.newFixedThreadPool(poolSize);
			
		// starting timer
		long startTime = System.currentTimeMillis();

		// iterating through the directory assigning a name and file to a runnable, then giving runnable to thread pool
		for (int i = 0; i < filesList.length; i++){
			// changing the number component to the name so it doesnt start at zero
			int id = i + 1;
			fileName = filesList[i];
			String name = "Thread " + (id);
			Runnable task = new PartBFour(name, fileName);
			// giving the runnable object to the thread pool to execute task on a file
			pool.submit(task);
		}

		// Shuting down pool but not until all threads are finished, this is for measuring time
		pool.shutdown();
		try{
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}	
		
		// stopping timer
		long endTime = System.currentTimeMillis();
		// calculating time to complete in seconds
		double totalTime = (endTime - startTime) / 1000.0;
		// printing out the total time for all threads to finish executing
		printTime(totalTime);		
				
	}// end of main
}// end of class

