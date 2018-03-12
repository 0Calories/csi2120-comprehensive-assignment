import java.util.LinkedList;

public class PoolNode implements Comparable<PoolNode> {

	public int parkId;
	public int facilityId;
	public String poolName;
	public double longitude, latitude;
	private LinkedList<PoolEdge> edges;
	public PoolEdge parentEdge = null;
	
	public PoolNode(int parkId, int facilityId, String poolName, double longitude, double latitude) {
		this.parkId = parkId;
		this.facilityId = facilityId;
		this.poolName = poolName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.edges = new LinkedList<>();
	}

	// This will be used to sort the list of pools.
	// The Pool with the lesser longitude is further West.
	// The more Eastern pool is considered "greater"
	// This will allow sorting the list from West to East.
	@Override
	public int compareTo(PoolNode otherPool) {
		if (this.longitude > otherPool.longitude) {
			return 1;
		} else if (this.longitude == otherPool.longitude) {
			return 0;
		} else {
			return -1;
		}
	}
	
	public LinkedList<PoolEdge> getEdges() {
		return edges;
	}
}
