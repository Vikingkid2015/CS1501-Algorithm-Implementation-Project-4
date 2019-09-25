public class Prims
{
    private Wire[] wireTo;
    private double[] distTo;
    private boolean[] marked;
    private IndexMinPQ<Double> pq;
    // set the starting minimum bandwidth to be the max integer value
    private int minBandwidth = Integer.MAX_VALUE;

    // constructor given a network graph
    public Prims(NetworkGraph G)
    {
        // create the wireTo, distTo, and marked arrays to have their number of indices equal the graphs numVertices
        wireTo = new Wire[G.getNumVertices()];
        distTo = new double[G.getNumVertices()];
        marked = new boolean[G.getNumVertices()];

        // instantiate the priority q to be with number of vertices of the graph
        pq = new IndexMinPQ<Double>(G.getNumVertices());
        // for vertices 0 to numVertices-1
        for(int v = 0; v < G.getNumVertices(); v++)
        {
            // set the starting distTo for that vertex to be positive infinity
            distTo[v] = Double.POSITIVE_INFINITY;
        }

        // for vertices from 0 to numVertices-1
        for(int v = 0; v < G.getNumVertices(); v++)
        {
            // if the vertex has not been marked yet
            if(!marked[v])
            {
                // run prims on the graph from that vertex
                prims(G, v);
            }
        }
    }

    // run prims on the graph given a starting vertex
    private void prims(NetworkGraph G, int start)
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
        // print out the minimum bandwidth
        System.out.println("Minimum Bandwidth: " + minBandwidth);
    }

    // scan the graph given a vertex
    private void scan(NetworkGraph G, int v)
    {
        // mark the vertex v
        marked[v] = true;
        // print the vertex and a space
        System.out.print(v + " ");

        // if the wireTo index of v is not null, and the minBandwidth is greater than the bandwidth of the vertex
        if(wireTo[v] != null && minBandwidth > wireTo[v].bandwidth)
        {
            // set the minBandwidth to be the bandwidth of the given vertex
            minBandwidth = wireTo[v].bandwidth;
        }

        // for each wire e that are adjacent to v in G
        for(Wire e : G.adjacent(v))
        {
            // set w to be the other endpoint vertex of v
            int w = e.other(v);
            // if w has already been marked, then continue the loop
            if(marked[w]) continue;
            // if the latency of e is less than the distTo w
            if (e.latency() < distTo[w]) 
            {
                // set the distTo w to equal the latency of e
                distTo[w] = e.latency();
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
}