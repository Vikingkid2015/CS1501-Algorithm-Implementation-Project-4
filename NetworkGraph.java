import java.util.LinkedList;

public class NetworkGraph
{
    // create an adjacency list using an array of linked lists
    private LinkedList<Wire>[] adjacency;
    // the number of vetex and wires
    private int numVertices;

    // supress any unchecked warning that pops up
    @SuppressWarnings("unchecked")
    // constructor for the network graph given the number of vertices in the network graph
    public NetworkGraph(int numVertices)
    {
        // set the numVertices to be the given numVertices
        this.numVertices = numVertices;
        // set the adjacency list to be a new linked list array with indices for each vertex
        this.adjacency = new LinkedList[numVertices];
        // create a linked list in each of the indices of the adjacency list
        for(int i = 0; i < numVertices; i++)
        {
            adjacency[i] = new LinkedList<Wire>();
        }
    }

    // this method will return the number of vertices that are in this graph
    public int getNumVertices()
    {
        return numVertices;
    }

    public LinkedList<Wire> remove(int vertex)
    {
        LinkedList<Wire> removed = adjacency[vertex];
        adjacency[vertex] = new LinkedList<Wire>();
        for(Wire e : removed)
        {
            int w = e.other(vertex);
            adjacency[w].remove(e);
        }

        return removed;
    }

    // this method will add a wire to the adjacency list
    public void addWire(Wire wire)
    {
        // add the given wire to the linked list at the indices for its start and end vertices
        adjacency[wire.start].add(wire);
        adjacency[wire.end].add(wire); 
    }

    public void addWires(LinkedList<Wire> wires)
    {
        for(Wire e : wires)
        {
            addWire(e);
        }
    }

    // return the linked list of vertices that are adjacent to the given vetex
    public LinkedList<Wire> adjacent(int vertex)
    {
        return adjacency[vertex];
    }
}