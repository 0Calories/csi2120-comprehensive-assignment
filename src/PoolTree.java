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
		poolList.remove(0);
		System.out.println("ROOT: " + root.poolName + ", " + root.longitude);
		
		// Find the closest node to the root from the poolList
		PoolNode secondNode = null;
		for (int i = 0; i < poolList.size(); i++) {
			double smallestDist = 9999;
			
			double tempDist = calculateDistance(root, poolList.get(i));
			
			// If the calculated distance is less than the smallest distance so far
			if (tempDist < smallestDist) {
				smallestDist = tempDist;
				secondNode = poolList.get(i);
			}
		}
		
		// Now that the second node has been found, attach it to the root.
		PoolEdge rootEdge = new PoolEdge(root, secondNode, calculateDistance(root, secondNode));
		connectedNodes.add(secondNode);
		poolList.remove(secondNode);
		
		// Build the tree
		while(poolList.size() > 0) {
			
			// Iterate through the poolList and compare it with each element in connectedNodes.
			// Starting at 1 because 0 is already set as the root node
			for (int i = 0; i < poolList.size(); i++) {
				PoolNode closestPool = findClosestPool(poolList.get(i));
				double dist = calculateDistance(poolList.get(i), closestPool);
				PoolEdge newEdge = new PoolEdge(closestPool, poolList.get(i), dist);
				closestPool.getEdges().add(newEdge);
				connectedNodes.add(poolList.get(i));
				System.out.println("\n\nCONNECTION!");
				System.out.println("Parent: " + closestPool.poolName);
				System.out.println("Child: " + poolList.get(i).poolName);
				System.out.println("Dist: " + dist);
				System.out.println("Connectednodes size: " + connectedNodes.size());
				poolList.remove(i);
			}
			
		}
		
		
		//PoolNode temp = findClosestPool(root);
		//System.out.println("Closest pool to root: " + temp.poolName);
		//System.out.println("Distance: " + calculateDistance(root, temp));
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
		
		for (int i = 0; i < connectedNodes.size(); i++) {
			PoolNode currentPool = connectedNodes.get(i);
			// Ensure that we are not comparing the same pool
			if (pool.parkId != currentPool.parkId) {
				double tempDist = calculateDistance(pool, currentPool);
				
				// If the calculated distance is less than the smallest distance so far
				if (tempDist < smallestDist) {
					smallestDist = tempDist;
					closestPool = connectedNodes.get(i);
					
				}
			}
		}
		return closestPool;
	}
	

}
