import java.util.Stack;

public class Dijkstra
{
    private Wire[] wireTo;
    private double[] distTo;
    private int[] bandwidthTo;
    private boolean[] marked;
    private IndexMinPQ<Double> pq;
    private int start;
    private boolean copperOnly;

    // constructor given a network graph
    public Dijkstra(NetworkGraph G, int start, boolean copperOnly)
    {
        this.start = start;
        this.copperOnly = copperOnly;

        // create the wireTo, distTo, and marked arrays to have their number of indices equal the graphs numVertices
        wireTo = new Wire[G.getNumVertices()];
        distTo = new double[G.getNumVertices()];
        marked = new boolean[G.getNumVertices()];
        bandwidthTo = new int[G.getNumVertices()];

        // instantiate the priority q to be with number of vertices of the graph
        pq = new IndexMinPQ<Double>(G.getNumVertices());
        // for vertices 0 to numVertices-1
        for(int v = 0; v < G.getNumVertices(); v++)
        {
            // set the starting distTo for that vertex to be positive infinity
            distTo[v] = Double.POSITIVE_INFINITY;
            bandwidthTo[v] = Integer.MAX_VALUE;
        }
        
        // run dijkstra on the graph from that vertex
        dijkstra(G, start);
        
    }

    // run prims on the graph given a starting vertex
    private void dijkstra(NetworkGraph G, int start)
    {
        // set the distance to the start vertex to be 0, because it is the starting vertex
        distTo[start] = 0;
        // insert the start vertex (with its distance) to the pq
        pq.insert(start, distTo[start]);

        // while the priority queue is not empty
        while(!pq.isEmpty())
        {
            // vertex v is the minimum of priority queue, delete min of priority queue
            int v = pq.delMin();
            // scan the graph from the given vertex
            scan(G, v);
        }
    }

    // scan the graph given a vertex
    private void scan(NetworkGraph G, int v)
    {
        // mark the vertex v
        marked[v] = true;

        // for each wire e that are adjacent to v in G
        for(Wire e : G.adjacent(v))
        {
            if(copperOnly && e.type.equals(Wire.WireType.OPTICAL)) continue;
            // set w to be the other endpoint vertex of v
            int w = e.other(v);
            // if w has already been marked, then continue the loop
            if(marked[w]) continue;
            // if the latency of e is less than the distTo w
            if ((e.latency() + distTo[v]) < distTo[w]) 
            {
                // set the distTo w to equal the latency of e
                distTo[w] = e.latency() + distTo[v];
                // find the minimum bandwidth to the wanted vertex
                bandwidthTo[w] = Math.min(bandwidthTo[w], e.bandwidth);
                // the wireTo w set to e
                wireTo[w] = e;
                // if w is already in the pq
                if (pq.contains(w)) 
                    // then decrease the key of w
                    pq.decreaseKey(w, distTo[w]);
                // else    
                else   
                // insert w into the pq             
                    pq.insert(w, distTo[w]);
            }
        }
    }

    public int getBandwidth(int end)
    {
        return bandwidthTo[end];
    }

    public double getDistance(int end)
    {
        return distTo[end];
    }

    public boolean visited(int vertex)
    {
        return marked[vertex];
    }

    public boolean visitedAllVertices()
    {
        for(int v = 0; v < marked.length; v++)
        {
            if(!visited(v))
            {
                return false;
            }
        }
        return true;
    }

    public void printPath(int end)
    {
        Stack<Integer> stack = new Stack<Integer>();

        int v = end;

        while(v != start)
        {
            stack.push(v);
            v = wireTo[v].other(v);
        }
        System.out.print(start + " ");
        while(!stack.isEmpty())
        {
            System.out.print(stack.pop() + " ");
        }
    }
}