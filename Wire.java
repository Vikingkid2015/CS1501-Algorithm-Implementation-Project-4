public class Wire
{
    // add fields for the different variables in the wire edges
    public int start, end, bandwidth, length;
    public WireType type;

    // this is an enum which can only be one of two variables, optical or copper
    public enum WireType
    {
        OPTICAL, COPPER
    }

    // this method calculates the latency that this wire will have
    public double latency()
    {
        // if the wire is copper, then its latency is calculated here
        if(type.equals(WireType.COPPER))
        {
            return length/230000000.0;
        }
        // if the wire is optical, then its latency is calculated here
        else
        {
            return length/200000000.0;
        }
    }

    // this method return the endpoint of the wire that is not being used to call this method
    public int other(int vertex)
    {
        // if user calls this method with the start vertex, then the end vertex is returned
        if(vertex == start)
        {
            return end;
        }
        // if the user did not call this method with the start vertex (meaning that it must have been called with the end vertex)
        // then the start vertex is returned
        return start;
    }
}