/*
Name: Robin Johnson
CSCI 4401/5401
Fall 2023
Extra Credit Assignment

Due: Tuesday, 11/28 @ 6:00pm (before we meet for the last lecture)

- This extra credit assignment contains a total of 25 points that can be applied to your final exam grade. Since this is extra credit, no partial credit for problems. 
- The problems you will solve all have ToDo: notes. 
- You can create new variables, but you cannot hardcode values. Rather than hardcoding values, use the global variables.
- The global variables and the arguments passed to the methods are example test values. You code should not be built to work for these specific values. I.e., it should still work if other test values were used. 
- Do not modify the current print statements. If you add print statements for testing & debugging, please remove them before submitting.
- Submit: this modified file containing your solutions.
*/

import java.util.*;
import java.io.*;
import java.lang.Math;

public class HW5{ //don't rename
	/*Part I: Main Memory Variables */
	public static int[] memoryHoles = {10, 4, 22, 18, 7, 9, 12, 15}; //List of holes in memory for contiguous allocation
	public static int pageSize = (int) Math.pow(2, 10) * 4; //Page Size is 4KB
	public static int[][] pageTable =  { {0, 3}, {1, 4}, {2, 6}, {3, 2} }; //Page Table is (Page#, Frame#)
	public static int ptrSize = 2; //Pointer size is 2 bytes

	/*Part II: Virtual Memory Variables */
	public static int[] refString = {1, 2, 3, 4 ,1 ,2, 5, 1, 2, 3, 4, 5};

	/*Part III: File System Variables */
	public static int directPtrs = 20; //20 direct pointers


	/*Part IV: Mass Storage Variables */
	public static int[] requests = {86, 1470, 913, 1774, 948, 1509, 1022, 1750, 130};
	public static int max = 4999;
	public static int min = 0;
	


	public static void main(String args[]){
		part1test(); //done
		part2test(); //done
		part3test(); //need to fix
		part4test(); //working,
	}

	/********************************************************************************************
	Part I: Main Memory
		- contiguousAllocation()
		- paging()
		- addressMapping()
	********************************************************************************************/

	public static void part1test(){
		/*	This is for testing your code. 
			DO NOT modify this method
		*/
		
		printSectionHeader("Part I: Main Memory");

		//Test contiguousAllocation()
		System.out.println("contiguousAllocation() [2 points]");
		System.out.println(String.format("\t%1$12s%2$12s%3$12s%4$12s", "Request(MB)", "FF Slot(MB)", "BF Slot(MB)", "WF Slot(MB)"));
		contiguousAllocation(12);
		contiguousAllocation(10);
		contiguousAllocation(9);

		//Test paging()
		System.out.println("\npaging() [2 points]");
		System.out.println(String.format("\t%1$10s%2$8s%3$8s", "Address", "Page#", "Offset"));
		paging(10275);
		paging(30600);
		paging(36543);

		//Test addressMapping()
		System.out.println("\naddressMapping() [2 points]");
		System.out.println(String.format("\t%1$10s%2$8s%3$8s%4$17s", "Address", "Frame#", "Offset", "PhysicalAddress"));
		addressMapping(10275);
		addressMapping(8600);
		addressMapping(6500);		
	}

	public static void contiguousAllocation(int requestSize){
		/*	Consider a swapping system in which memory consists of the hole sizes @memoryHoles in memory. 
			Write code that determines what a segment request @requestSize will take for first-fit @firstFit, 
			best-fit @bestFit, and worst-fit @worstFit.
		*/
		int firstFit = 0;
		int bestFit = 0;
		int worstFit = 0;
		//memoryHoles = {10, 4, 22, 18, 7, 9, 12, 15};
		
		//ToDo: add your code to calculate @firstFit
		for(int hole_0 : memoryHoles){
			// at the first "memory hole" that can fit, take it and done
			if(hole_0 >= requestSize){
				firstFit = hole_0;
				break;
			}
		}

		//ToDo: add your code to calculate @bestFit
		int best_hole = 0;
		for(int hole_1 : memoryHoles ){
			int leftovers = 100;
			// condition looking for the perfect size
			if(hole_1 == requestSize){
				best_hole = hole_1;
				break;
			}
			//condition looking for "memory hole" that is equal of greater than what is being looked for
			else if(hole_1 >= requestSize){
				// geting the remainder of how much memory would be left over
				int temp_dist = hole_1 - requestSize;
				//condition looking for if the new calc-ed distance is smaller 
				if(temp_dist < leftovers){
					leftovers = temp_dist;
					best_hole = hole_1;
				}
			}
		}
		bestFit = best_hole;

		//ToDo: add your code to calculate @worsFit
		int worst_hole = 0;
		int leftovers_2 = 0;
		for(int hole_2 : memoryHoles ){
			//condition looking for the "memory holes" that are larger or equal to the requestSize
			if(hole_2 >= requestSize){
				//variable for storing the left over memory
				int tempDist = hole_2 - requestSize;
				// condition for when the new measured left over memory is greater
				if(tempDist > leftovers_2){
					leftovers_2 = tempDist;
					worst_hole = hole_2;
				}
			}
		}
		worstFit = worst_hole;
		
		System.out.println(String.format("\t%1$12s%2$12s%3$12s%4$12s", requestSize, firstFit, bestFit, worstFit)); //Do not modify		
	}

	public static void paging(int addrRef) {
		//	Assuming a 4 KB (@pageSize), calculate the page number @pageNbr and offset @offset for a given 
		//	address reference @addrRef.
		
		int pageNbr = 0;
		int offset = 0;

		//ToDo: add your code to calculate the page number @pageNbr
		int temp_pageNbr = (int)Math.floor(addrRef/pageSize);
		pageNbr = temp_pageNbr;
		//ToDo: add your code to calculate the offset @offset
		int temp_offset = addrRef % pageSize;
		offset = temp_offset;

		System.out.println(String.format("\t%1$10s%2$8s%3$8s", addrRef, pageNbr, offset));		
	}

	public static void addressMapping(int addr){
		//	Assuming a 4 KB (@pageSize) and a frame size of 4 KB (@pageSize), calculate the frame number @frameNbr 
		//	and the physical address @phyAddr given a logical address @addr and the page table @pageTable
		//	for
		
		int pageNbr = addr/pageSize;
		int offset = 0;
		int frameNbr = 0;
		int phyAddr = 0;

		//ToDo: add your code to calculate the frameNbr @frameNbr
		//iterating through looking for the page number, then grabbing associated index containing the frame number
		for (int[] entry : pageTable){
				if(entry[0]== pageNbr){
					frameNbr = entry[1];
				}
			}

		//ToDo: add your code to calculate the offset @offset
		offset = addr%pageSize;

		//ToDo: add your code to calculate the physical address @phyAddr
		
		phyAddr = frameNbr * pageSize + offset;

		System.out.println(String.format("\t%1$10s%2$8s%3$8s%4$17s", addr, frameNbr, offset, phyAddr));		
	}

	/********************************************************************************************
	Part II: Virtual Memory
		- fifoPageReplacement()
		- lruPageReplacement()
	********************************************************************************************/

	public static void part2test(){
		printSectionHeader("Part II: Virtual Memory");


		//Test contiguousAllocation()
		System.out.println("fifoPageReplacement() [3 points]");
		fifoPageReplacement(3);
		fifoPageReplacement(4);
		System.out.println("lruPageReplacement() [3 points]");
		lruPageReplacement(3);
		lruPageReplacement(4);
	}


	public static void fifoPageReplacement(int frames) {
		// Consider the sequence of page accesses @refString. Your system has @frames number of frames. Write the first-in first-out page replacement algorithm to calculate the number of page faults @faults that will occur.
		//public static int[] refString = {1, 2, 3, 4 ,1 ,2, 5, 1, 2, 3, 4, 5};
		int faults = 0;
		
		//ToDo: add your code to calculate the faults @faults
		Queue<Integer> fifo = new LinkedList<>();
		for(int page : refString){
			//condition for when the frame window still have empty space and the page is not already in the window
			if(fifo.size() < frames && !fifo.contains(page)){
				faults++;
				fifo.add(page);
			}
			//condition for when the fifo size is at the desired window size and the page is not already in the window
			else if(fifo.size() == frames && !fifo.contains(page)){
				faults++;
				fifo.poll();
				fifo.add(page);
			}
			else{
				//do nothing, there is no fault
			}
		}
		
		System.out.println("\tFIFO Faults: " + faults);
	}

	
 	public static void lruPageReplacement(int frames) {
		// Consider the sequence of page accesses @refString. Your system has @frames number of frames. Write the least recently used page replacement algorithm to calculate the number of page faults @faults that will occur.
		//public static int[] refString = {1, 2, 3, 4 ,1 ,2, 5, 1, 2, 3, 4, 5};

		int faults = 0;

		//ToDo: add your code to calculate the faults @faults
		ArrayList<Integer> window = new ArrayList<>();
		
		//Collections.swap(window, 0, window.size()-1);
		for(int page : refString){
			if(window.size() < frames && !window.contains(page)){
				window.add(0, page);
				faults++;
			}
			else if(window.size() < frames && window.contains(page)){
				int swap_Index = window.indexOf(page);
				window.remove(swap_Index);
				window.add(0,page);	
			}
			else if(window.size() == frames && !window.contains(page)){
				window.remove(window.size()-1);
				window.add(0,page);
				faults++;
			}
			else if(window.size() == frames && window.contains(page)){
				int swap_Index = window.indexOf(page);
				window.remove(swap_Index);
				window.add(0,page);
			}
		}

		System.out.println("\tLRU Faults: " + faults);
	} 

	/********************************************************************************************
	Part III: File Systems
		- inode1()
		- inode2()
	********************************************************************************************/
	 
	public static void part3test(){
		printSectionHeader("Part III: File Systems");

		System.out.println("inode1() [2 points]");
		inode1();
		System.out.println("inode2() [2 points]");
		inode2();
	}

	//public static int pageSize = (int) Math.pow(2, 10) * 4; //Page Size is 4KB
	//public static int ptrSize = 2;
	//public static int directPtrs = 20;
	public static void inode1(){
		/* Assuming a 4 KB disk block size (@pageSize), calculate the largest file size @fileSize for an i-node that contains 20 direct pointers @directPtrs and one single indirect pointer. Make sure to convert your file size to GB.*/

		//double fileSize = 0;

		//ToDo: calculate the max file size @fileSize
		//number of pointers for each type
		int num_DirectPtrs = directPtrs;
    	int num_SingleIndirectPtr = pageSize/ptrSize;

		int total_Pointers = num_DirectPtrs + num_SingleIndirectPtr;
		long bytes = total_Pointers * pageSize;
		//System.out.print("\nnumber of pointer inode1: " + total_Pointers + "\n");
		
		//conversion to gig 
		 double fileSize = bytes / (1024.0 * 1024 * 1024);

		System.out.println(String.format("\tI-Node 1 File Size: %,.2f GB", fileSize));
	}

	public static void inode2(){
		/* Assuming a 4 KB disk block size (@pageSize), calculate the largest file size @fileSize for an i-node that contains 20 direct pointers @directPtrs, one single indirect pointer, and one double indirect pointer. Make sure to convert your file size to GB.*/

		//double fileSize = 0;

		//ToDo: calculate the max file size @fileSize
		int num_DirectPtrs = directPtrs;
    	int num_SingleIndirectPtr = pageSize/ptrSize;
		int num_DoubleIndirectPtr = num_SingleIndirectPtr * num_SingleIndirectPtr;
		

		int total_Pointers2 = num_DirectPtrs + num_SingleIndirectPtr + num_DoubleIndirectPtr;
		long bytes = total_Pointers2 * pageSize;
		
		//conversion to gig
		//double temp = total_Pointers2 / (1024.0 * 1024 * 1024);
		//System.out.print("\nnumber of pointer inode2: " + total_Pointers2 + "\n" + "bytes: " + temp);
		double fileSize = bytes / (1024.0 * 1024 * 1024);

		System.out.println(String.format("\tI-Node 2 File Size: %,.2f GB", fileSize));
	}

	/********************************************************************************************
	Part IV: Mass Storage
		- fcfsScheduling()
		- scanScheduling()
		- lookScheduling()
		- sstfScheduling()
	********************************************************************************************/

	public static void part4test(){
		printSectionHeader("Part IV: Disk Scheduling");

		//Test fcfsScheduling()
		System.out.println("fcfsScheduling() [2 points]");
		fcfsScheduling(143);

		//Test scanScheduling()
		System.out.println("scanScheduling() [2 points]");
		scanScheduling(143);

		//Test lookScheduling()
		System.out.println("lookScheduling() [2 points]");
		lookScheduling(143);

		//Test sstfScheduling()
		System.out.println("sstfScheduling() [3 points]");
		sstfScheduling(143);
	}

	//public static int[] requests = {86, 1470, 913, 1774, 948, 1509, 1022, 1750, 130};
	//public static int max = 4999;
	//public static int min = 0;

	public static void fcfsScheduling(int start){
		/*	Suppose a disk drive has 5000 cylinders numbered 0 – 4999. The drive is currently serving a request 
			at cylinder @start, and the previous request was at cylinder 125. The queue of pending requests, in FIFO order, is @requests. Starting from the current head position, calculate the total distance 
			(in cylinders) that the disk arm moves to satisfy all the pending requests for first-come first-serve @fcfsMovements.
		*/

		int fcfsMovements = 0;
		
		//ToDo: add your code to calculate @fcfsMovements
		int current_loc = start;

		for(int req : requests){
			fcfsMovements += Math.abs(current_loc - req);
			current_loc = req;
		}

		System.out.println(String.format("\tFCFS Movements: %s", fcfsMovements));
	}

	//public static int[] requests = {86, 1470, 913, 1774, 948, 1509, 1022, 1750, 130};
	//public static int max = 4999;
	//public static int min = 0;
	public static void scanScheduling(int start){
		/*	Suppose a disk drive has 5000 cylinders numbered 0 – 4999. The drive is currently serving a request 
			at cylinder @start, and the previous request was at cylinder 125. The queue of pending requests, in FIFO order, is @requests. Starting from the current head position, calculate the total distance 
			(in cylinders) that the disk arm moves to satisfy all the pending requests for the SCAN algorithm @scanMovements.
		*/		

		int scanMovements = 0;					
		
		//ToDo: add your code to calculate @scanMovements
		int upperbound = 4999;
		int current = start;
		int largest = 0;
		int smallest = 4999;
		int on_return = 0;
		
		for(int loc : requests){
			if(loc > largest){
				largest = loc;
			}
			if(loc < smallest){
				smallest = loc;
			}
			if(loc > smallest && loc < start){
				on_return = loc;
			}
		}
		
		scanMovements = (largest - current) + (upperbound - largest) + (upperbound - on_return) + (on_return - smallest);
		
		//System.out.print("Upper"+ upperbound + "Start: " + current + ", Largest: " + largest + ", Smallest: " + smallest + ", On_return: " + on_return);
		System.out.println(String.format("\tSCAN Movements: %s", scanMovements));
	}

	public static void lookScheduling(int start){
		/*	Suppose a disk drive has 5000 cylinders numbered 0 – 4999. The drive is currently serving a request 
			at cylinder @start, and the previous request was at cylinder 125. The queue of pending requests, in FIFO order, is @requests. Starting from the current head position, calculate the total distance 
			(in cylinders) that the disk arm moves to satisfy all the pending requests for the LOOK algorithm @lookMovements.
		*/		
		int lookMovements = 0;	

		//ToDo: add your code to calculate @lookMovements
		int current = start;
		int largest = 0;
		int smallest = 4999;
		int on_return = 0;
		
		for(int loc : requests){
			if(loc > largest){
				largest = loc;
			}
			if(loc < smallest){
				smallest = loc;
			}
			if(loc > smallest && loc < start){
				on_return = loc;
			}
		}
		
		lookMovements = (largest - current) + (largest - on_return) + (on_return - smallest);

		System.out.println(String.format("\tLOOK Movements: %s", lookMovements));
		
	}

	public static void sstfScheduling(int start) {
		/*	Suppose a disk drive has 5000 cylinders numbered 0 – 4999. The drive is currently serving a request 
			at cylinder @start, and the previous request was at cylinder 125. The queue of pending requests, in FIFO order, is @requests. Starting from the current head position, calculate the total distance 
			(in cylinders) that the disk arm moves to satisfy all the pending requests for the Shortest-seek-time-first algorithm @sstfMovements.
		*/
	
		int sstfMovements = 0;
		
		//ToDo: add your code to calculate @sstfMovements
		
		int current_loc = start;
		int request_length = requests.length;
		ArrayList<Integer> next_Movement = new ArrayList<>();
		ArrayList<Integer> requests2 = new ArrayList<>();
		for(int item : requests){
			requests2.add(item);
		}

			for(int i=0; i <= request_length; i++){
				if(!requests2.isEmpty()){
					next_Movement = nextMove(current_loc, requests2);
					int move_to = next_Movement.get(0);
					int index = next_Movement.get(1);
					//System.out.print("in sstfScheduling, index: " + index);
					sstfMovements += Math.abs(current_loc-move_to);
					current_loc = move_to;
					
					requests2.remove(index);
				}
				else{
					break;
				}
			}


		System.out.println(String.format("\tSSTF Movements: %s", sstfMovements));
	}


	public static void printSectionHeader(String sectionName) {
		System.out.println("\n" + "-".repeat(25));
		System.out.println(String.format("%1$-25s", sectionName));
		System.out.println("-".repeat(25));
	}

	// helper method for sstfScheduling
	public static ArrayList<Integer> nextMove(int a, ArrayList<Integer> requests2) {
		ArrayList<Integer> result = new ArrayList<>();
		
		int closestDistance = Integer.MAX_VALUE;
		int closestIndex = -1;
	
		for (int i = 0; i < requests2.size(); i++) {
			//int temp = requests[i];
			//System.out.print("current place: " + temp);
			int currentDistance = Math.abs(requests2.get(i) - a);
			if (currentDistance < closestDistance ) {
				closestDistance = currentDistance;
				closestIndex = i;	
			}
		}

		//System.out.print("Current: " + a + "Next move: " + requests[closestIndex] + "Its index: " + closestIndex);
		result.add(requests2.get(closestIndex));
    	result.add(closestIndex);
		return result;
	}
}