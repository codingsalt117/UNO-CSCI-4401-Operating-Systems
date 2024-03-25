import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class test {
/*     public static void main(String[] args) {
        // Initialize an ArrayList
        ArrayList<String> list = new ArrayList<>();

        // Add elements to the ArrayList
        list.add("Element 1");
        list.add("Element 2");
        list.add("Element 3");

        // Print the ArrayList before adding at index 0
        System.out.println("Before adding at index 0: " + list);

        // Add a new element at index 0
        String newElement = "New Element";
        list.add(0, newElement);

        // Print the ArrayList after adding at index 0
        System.out.println("After adding at index 0: " + list);
    }*/
        public static void main(String[] args) {
        int start = 143;
        int previousRequest = 125;
        int[] requests = {86, 1470, 913, 1774, 948, 1509, 1022, 1750, 130};

        scanScheduling(start, previousRequest, requests);
        // Add more method calls for other disk-scheduling algorithms if needed
    }

    public static void scanScheduling(int start, int previousRequest, int[] requests) {
        int scanMovements = 0;

        // Sort the requests in ascending order
        Arrays.sort(requests);

        // Determine the direction of the disk arm movement
        int direction = (start >= previousRequest) ? 1 : -1;

        Queue<Integer> leftQueue = new LinkedList<>();
        Queue<Integer> rightQueue = new LinkedList<>();

        // Divide the requests into two queues based on the direction
        for (int request : requests) {
            if (request < start) {
                leftQueue.add(request);
            } else {
                rightQueue.add(request);
            }
        }

        // Traverse the requests in the determined direction
        while (!leftQueue.isEmpty() || !rightQueue.isEmpty()) {
            Queue<Integer> currentQueue = (direction == 1) ? rightQueue : leftQueue;

            while (!currentQueue.isEmpty()) {
                int request = currentQueue.poll();
                // Calculate the distance between consecutive requests
                scanMovements += Math.abs(request - start);
                // Update the current head position
                start = request;
            }

            // Change direction
            direction *= -1;
        }

        // Add the distance to the end if the direction is towards the end
        if (direction == 1) {
            scanMovements += Math.abs(start - previousRequest);
        } else {
            // Add the distance to the beginning if the direction is towards the beginning
            scanMovements += Math.abs(start - 0);
        }

        System.out.println(String.format("\tSCAN Movements: %s", scanMovements));
    }


} 