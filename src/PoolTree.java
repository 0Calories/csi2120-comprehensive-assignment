import java.util.LinkedList;

public class PoolTree {
	
	private static final double CONST = 6371.0;
	private static final double RAD_CONST = 180.0;
	
	private PoolNode root;
	private LinkedList<PoolNode> poolList;
	private LinkedList<PoolNode> connectedNodes;
	
	public PoolTree(LinkedList<PoolNode> poolList) {
		this.poolList = poolList;
		connectedNodes = new LinkedList<>();
		
		// The most Western pool needs to be the root.
		// The list is sorted so the very first element is the farthest West.
		root = poolList.get(0);
		connectedNodes.add(root);
		
		// Build the tree
		for (int i = 1; i < poolList.size(); i++) {
			
			// Iterate through the current nodes in the tree
			for (int j = 0; j < connectedNodes.size(); j++) {
				PoolNode closestPool = findClosestPool(connectedNodes.get(j));
				double dist = calculateDistance(poolList.get(i), closestPool);
				PoolEdge newEdge = new PoolEdge(poolList.get(i), closestPool, dist);
			}
			
		}
		
		PoolNode temp = findClosestPool(root);
		System.out.println("Closest pool to root: " + temp.poolName);
		System.out.println("Distance: " + calculateDistance(root, temp));
	}
	
	public double calculateDistance(PoolNode node1, PoolNode node2) {
		
		double lat1 = degreesToRadians(node1.latitude);
		double lat2 = degreesToRadians(node2.latitude);
		double lon1 = degreesToRadians(node1.longitude);
		double lon2 = degreesToRadians(node2.longitude);
		
		double a1 = Math.pow(Math.sin((lat1 - lat2) / 2), 2);
		double a2 = Math.cos(lat1) * Math.cos(lat2);
		double a3 = Math.pow(Math.sin((lon1 - lon2) / 2), 2);
		
		double dRad = 2 * Math.asin( Math.sqrt(a1 + (a2 * a3)) );
		
		return CONST * dRad;
	}
	
	private static double degreesToRadians(double angle) {
		return (Math.PI / RAD_CONST) * angle;
	}
	
	private PoolNode findClosestPool(PoolNode pool) {
		double smallestDist = 9999;
		PoolNode closestPool = null;
		
		for (int i = 0; i < poolList.size(); i++) {
			PoolNode currentPool = poolList.get(i);
			// Ensure that we are not comparing the same pool
			if (pool.parkId != currentPool.parkId) {
				double tempDist = calculateDistance(pool, currentPool);
				
				// If the calculated distance is less than the smallest distance so far
				if (tempDist < smallestDist) {
					smallestDist = tempDist;
					closestPool = poolList.get(i);
					
				}
			}
		}
		return closestPool;
	}
	

}
