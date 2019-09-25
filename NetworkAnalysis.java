import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class NetworkAnalysis
{
    // graph will be the network graph that is being analyzed
    private NetworkGraph graph;
    // scanner to read in user input
    Scanner scan = new Scanner(System.in);

    // constructor for NetworkAnalysis
    public NetworkAnalysis(String filename)
    {
        // create a file scanner and have it set to null
        Scanner fileScan = null;
        // try to open the file that the user inputted
        try
        {
            fileScan = new Scanner(new File(filename));
        } 
        // if the file is not found, then throw a new FileNotFoundException
        catch(FileNotFoundException e)
        {
            // print the stack trace of the thrown exception
            e.printStackTrace();
        }

        // if the file scanner is still null
        if(fileScan == null)
        {
            // return, this will end the program
            return;
        }
        
        // if the file was found then the rest of the program will run
        // set the numVertices to be the first line of the given file
        // this should be the number of vertices in the graph (if the user inputted a properly formatted file)
        int numVertices = Integer.parseInt(fileScan.nextLine());

        // instantiate graph to be a new NetworkGraph with numVertices amount of vertices
        graph = new NetworkGraph(numVertices);

        // while there is still a next line to be read from the file
        while(fileScan.hasNextLine())
        {
            // read the line as a string
            String line = fileScan.nextLine();
            // split the line into a string array using spaces as splits
            String[] elements = line.split(" ");
            // create a new wire
            Wire wire = new Wire();
            // fill the wire's data fields with the corresponding array indices from the line read from the file
            wire.start = Integer.parseInt(elements[0]);
            wire.end = Integer.parseInt(elements[1]);
            // read the WireType in all upper case so that it can be read into the enum for WireType
            wire.type = Wire.WireType.valueOf(elements[2].toUpperCase());
            wire.bandwidth = Integer.parseInt(elements[3]);
            wire.length = Integer.parseInt(elements[4]);
            // add the filled out wire to the graph
            graph.addWire(wire);
        }

        // this loop will run until the user either crashes the program or chooses to exit the program
        while(true)
        {
            // print the menu
            printMenu();

            System.out.print("Select Option: ");
            // get the user's input
            int option = getOption();

            // if the user inputs 1
            if(option == 1)
            {
                // find the lowest latency path
                lowestLatencyPath();
            }
            // if the user inputs 2
            else if(option == 2)
            {
                // check if there is a copper only path
                copperOnly();
            }
            // if the user inputs 3
            else if(option == 3)
            {
                // find the lowest spanning tree
                lowestSpanTree();
            }
            // if the user inputs 4
            else if(option == 4)
            {
                // check if 2 vertices can be removed from graph
                twoVerticesRemoved();
            }
            // if the user inputs 5
            else if(option == 5)
            {
                // exit the program
                // thank the user for using the program
                System.out.println("Thank you for using this application.");
                // tell them the program is being exitted
                System.out.println("Exit Program.");
                // break the loop, exitting the program
                break;
            }
        }
        
    }

    // print the menu
    public void printMenu()
    {
        // print the menu options in an easy to read table
        System.out.println("\n|----------------------Menu----------------------|");
        System.out.printf("|%-48s|\n", "1. Find Lowest Latency Path");
        System.out.printf("|%-48s|\n", "2. Check if Network is Copper-Only Connected");
        System.out.printf("|%-48s|\n", "3. Find Lowest Average Latency Spanning Tree");
        System.out.printf("|%-48s|\n", "4. Check if Two Vertices Can Be Removed");
        System.out.printf("|%-48s|\n", "5. Exit Program");
        System.out.println("|------------------------------------------------|");
        System.out.println();
    }

    // get the user inputted option they chose from the menu
    public int getOption()
    {
        int ret = scan.nextInt();
        scan.nextLine();
        System.out.println();
        return ret;
    }

    public void lowestLatencyPath()
    {
        System.out.print("Please enter a vertex: ");
        int option1 = getOption();
        System.out.print("Please enter another vertex: ");
        int option2 = getOption();

        Dijkstra dijkstra = new Dijkstra(graph, option1, false);

        int bandwidthTo = dijkstra.getBandwidth(option2);
        double distanceTo = dijkstra.getDistance(option2);
        if(!dijkstra.visited(option2))
        {
            System.out.println("There is no path between the vertices entered.");
            return;
        }
        System.out.print("Best Path: ");
        dijkstra.printPath(option2);
        System.out.println();
        System.out.println("Total Latency of Path: " + distanceTo);
        System.out.println("Bandwidth of Path: " + bandwidthTo);
        return;
    }

    public void copperOnly()
    {
        if(graph.getNumVertices() == 0)
        {
            System.out.println("The graph, without vertices, can be copper only connected.");
            return;
        }
        Dijkstra dijkstra = new Dijkstra(graph, 0, true);
        if(dijkstra.visitedAllVertices())
        {
            System.out.println("The graph is copper only connected.");
            return;
        }
        System.out.println("The graph is not copper only connected.");
        return;
    }

    public void lowestSpanTree()
    {
        // run prims on the graph
        new Prims(graph);
    }

    public void twoVerticesRemoved()
    {
        for(int v = 0; v < graph.getNumVertices(); v++)
        {
            for(int w = 0; w < graph.getNumVertices(); w++)
            {
                if(v == w) continue;

                int start = 0;

                LinkedList<Wire> removeV = graph.remove(v);
                LinkedList<Wire> removeW = graph.remove(w);

                while(start == v || start == w)
                {
                    start++;
                }
                Dijkstra dijkstra = new Dijkstra(graph, start, false);

                boolean connected = true;
                for(int n = 0; n < graph.getNumVertices(); n++)
                {
                    if(n == v || n == w) continue;
                    if(!dijkstra.visited(n))
                    {
                        connected = false;
                    }
                }

                graph.addWires(removeV);
                graph.addWires(removeW);
                
                if(!connected)
                {
                    System.out.println("Removal of " + v + " and " + w + " disconnected the graph.");
                    return;
                }
            }
        }
        System.out.println("The removal of any two vertices does not disconnect the graph.");
        return;
    }

    // the main method of the program
    public static void main(String[] args)
    {
        // if the user did not input a filename for the program to read a graph from
        if(args.length < 1)
        {
            System.out.println("Please Specify Filename");
            return;
        }
        // if the user did input a filename, then run the program passing the given filename into the constructor
        new NetworkAnalysis(args[0]);

    }
}