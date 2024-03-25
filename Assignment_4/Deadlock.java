import java.io.*;
import java.util.*;


public class Deadlock {
	public static void main( String [] args ) {
		//… here lies code to process the input file and simulate resource allocations …
       try { 
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter the file name: ");
            String file_Name = scanner.nextLine();
            RAG rag = new RAG();
            Map<String, String> proc_wants = new HashMap<>();
            ArrayList<String> allocated_Recs = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(file_Name));
            String line;
            
                while((line = reader.readLine()) != null){
                    String[] line_Parts = line.split("\\s+");
                    String proc = line_Parts[0];
                    String type = line_Parts[1];
                    String recs = line_Parts[2];

                    //checking for the nodes for that given type exists before adding a process and resources
                    if(!rag.nodes.containsKey(proc)){
                        rag.addNode(proc, "Process");
                    }
                    if(!rag.nodes.containsKey(recs)){
                        rag.addNode(recs, "Resource");
                    }

                    //checking line for "W"ants, if so the associated print line
                    if(type.equals("W")){
                        System.out.print("Process "+ proc + " wants resource " + recs + " - ");
                        //checking if the resource can be allocated
                        if(allocated_Recs.contains(recs)){
                           System.out.print("Process " + proc + " must wait.\n"); 
                           proc_wants.put(recs,proc);
                        }
                        else if(!rag.detectDeadlock()){
                            System.out.print("Resource "+ recs + " is allocated to process " + proc + "." + "\n");
                            //add egde
                            rag.addEdge(proc, recs);
                            allocated_Recs.add(recs);
                        }
                    }
                    //checking line for "R"elease, if so the associated print line
                    else if(type.equals("R")){
                        System.out.print("Process " + proc + " releases resource " + recs + " - ");
                        rag.removeEdge(proc, recs);
                        allocated_Recs.remove(recs);
                        //System.out.print("Resource "+ recs + " is allocated to process " + proc_value + "." + "\n");
                        System.out.print("Resource " + recs + " is now free." + "\n");
                    }

                    //check for deadlock
                    if(rag.detectDeadlock()){
                        System.out.print("A Deadlock has been detected!!!");
                        rag.detectDeadlock();
                        //return;
                    }
                }
                for (Map.Entry<String, String> entry : proc_wants.entrySet()) {
                    if(!allocated_Recs.contains(entry.getKey())){
                        String proc_value = entry.getValue();
                        String recs_value = entry.getKey();
                        System.out.print("Process "+ proc_value + " can now get Resource " + recs_value + "." + "\n");
                        rag.addEdge(proc_value, recs_value);
                        allocated_Recs.add(recs_value);
                    }
                }
        System.out.println("EXECUTION COMPLETED: No deadlock encountered.");
        scanner.close();
        reader.close();
        
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    
              
	}
    private static void printMap(Map<String, String> map) {
        for (String key : map.keySet()) {
            System.out.println("Key: " + key + ", Value: " + map.get(key));
        }
    }
}

class RAG {
	//… here lies code to implement your Resource Allocation Graph data structure class …
    //needs to be public
    public Map<String, Node> nodes;

    //constructor for the RAG
    public RAG(){
        this.nodes = new HashMap<>();
    }

    //add node to the RAG
    public void addNode(String n_Id, String n_Type){
        if(!nodes.containsKey(n_Id)){
            nodes.put(n_Id, new Node(n_Id, n_Type));
        }    
    }

    //add edge to a node on the RAG
    public void addEdge(String proc_Id, String rec_Id){
        nodes.get(proc_Id).addEdge(nodes.get(rec_Id));
    }

    //remove edge to a node on the RAG
    public void removeEdge(String proc_Id, String rec_Id){
        nodes.get(proc_Id).removeEdge(nodes.get(rec_Id));
    }

    //deadlock detection
    public boolean detectDeadlock(){
        List<Node> cycle_List = new ArrayList<>();
        
        //iterate through the nodes
        for (Node node : nodes.values()){
            if(node.getTracker() == 0){
                boolean temp = cycleCheck(node, cycle_List); //, visited, processing_List, cycle_List
                if(temp){
                    printDeadLock(cycle_List);
                    return true;
                }
            }

        }
        //no deadlock found 
        return false;
    }

    public boolean cycleCheck(Node node, List<Node> cycle_list){/* , Set<Node> visited, Set<Node> processing_List, List<Node> cycle_list */
        //add the current node to both sets. so now its tracked as visited and on the set tracking what nodes are current 
        node.setTraker(1);

        if(node.edges != null){
            //iterate through the nodes neighbors via node attribute -edges, DFS
            for(Node neighbor : node.edges){
                //if !visited then that node, the neighbor, needs to be explored before preceding
                if(neighbor.getTracker() == 0){
                    //recursive call to explore the unvisted neighbor looking for a cycle
                    if(cycleCheck(neighbor, cycle_list/* , visited, processing_List */)){
                        return true;
                    }
                }
                // if the nodes neighbor is in processing_List, then there is a cycle
                else if(neighbor.getTracker() == 1){
                    cycle_list.add(node);
                    cycle_list.add(neighbor); 
                    return true;
                }
            }
        }

        // no cycle was detected
        return false;
    }

    // print out for deadlock found, grabbing all the processes and resources involved
    public void printDeadLock(List<Node> cycle_List){
        System.out.print("DEADLOCK DETECTED: Processes ");

        //look for all the processes involved and print their ids
        for(Node node : cycle_List){
            if("Process".equals(node.getType())){
                System.out.print(node.getID() + ", ");
            }
        }
        System.out.print("and Resources ");
        //look for all the resources involved and print their ids
        for(Node node : cycle_List){
            if("Resource".equals(node.getType())){
                System.out.print(node.getID() + ", ");
            }
        }
        System.out.print("are found in a cycle.");
    }
    
    //private inner class of RAG, only RAG needs to know and manipulate a node
    private static class Node {
        //… here lies the code for a node object, …
        private String id;
        private String type;
        private int tracker;
        private Set<Node> edges;

        // node constructor
        public Node(String id, String type){
            this.id = id;
            this.type = type;
            this.tracker = 0;
            //needs to be unique edges
            this.edges = new HashSet<>();
        }

        //add edge to a nod
        public void addEdge(Node neighbor){
            edges.add(neighbor);
        }
        
        //remove edge from node
        public void removeEdge(Node neighbor){
            edges.remove(neighbor);
        }

        //getter methods
        public String getID(){
            return id;
        }
        public String getType(){
            return type;
        }
        public int getTracker(){
            return tracker;
        }
        //setter for tracker
        public void setTraker(int i){
            this.tracker = i;
        }

        @Override
        public String toString(){
            return type + " " + id;
        }
    }
}




