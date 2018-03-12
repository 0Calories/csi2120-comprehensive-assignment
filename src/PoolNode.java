
public class PoolNode implements Comparable<PoolNode> {

	private int parkId;
	private int facilityId;
	private String poolName;
	double longitude, latitude;
	
	public PoolNode(int parkId, int facilityId, String poolName, double longitude, double latitude) {
		this.parkId = parkId;
		this.facilityId = facilityId;
		this.poolName = poolName;
		this.latitude = latitude;
		this.longitude = longitude;
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
	
}
