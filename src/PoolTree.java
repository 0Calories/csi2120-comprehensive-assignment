import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Stack;

public class PoolTree {
	
	private static final double CONST = 6371.0;
	private static final double RAD_CONST = 180.0;
	
	public PoolNode root;
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
		double smallestDist = 9999;
		PoolNode secondNode = null;
		for (int i = 0; i < poolList.size(); i++) {
			double tempDist = calculateDistance(root, poolList.get(i));
			
			// If the calculated distance is less than the smallest distance so far
			if (tempDist < smallestDist) {
				smallestDist = tempDist;
				secondNode = poolList.get(i);
			}
		}
		
		
		// Now that the second node has been found, attach it to the root.
		PoolEdge rootEdge = new PoolEdge(root, secondNode, calculateDistance(root, secondNode));
		secondNode.parentEdge = rootEdge;
		root.getEdges().add(rootEdge);
		connectedNodes.add(secondNode);
		poolList.remove(secondNode);
		
		System.out.println("\n\nCONNECTION ROOT!");
		System.out.println("Parent: " + rootEdge.parentNode.poolName);
		System.out.println("Child: " + rootEdge.childNode.poolName);
		System.out.println("Dist: " + rootEdge.distance);
		System.out.println("Connectednodes size: " + connectedNodes.size());
		
		// Build the tree
		// Iterate through the poolList and compare it with each element in connectedNodes.
		// Starting at 1 because 0 is already set as the root node
		for (int i = 0; i < poolList.size(); i++) {
			PoolNode closestPool = findClosestPool(poolList.get(i));
			double dist = calculateDistance(poolList.get(i), closestPool);
			PoolEdge newEdge = new PoolEdge(closestPool, poolList.get(i), dist);
			closestPool.getEdges().add(newEdge);
			poolList.get(i).parentEdge = newEdge;
			connectedNodes.add(poolList.get(i));
			System.out.println("\n\nCONNECTION!");
			System.out.println("Parent: " + closestPool.poolName);
			System.out.println("Child: " + poolList.get(i).poolName);
			System.out.println("Dist: " + dist);
			System.out.println("Connectednodes size: " + connectedNodes.size());
		}
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
	
	public void traverse() {
		PrintWriter writer;
		Stack<PoolNode> poolStack = new Stack<>();
		poolStack.push(root);
		double currentDist = 0;
		System.out.println(root.getEdges());
		
		try {
			writer = new PrintWriter("route.txt", "UTF-8");
		
			// Iterate through the tree, starting with the root.
			// Pop the current node, push each child node into the stack.
			while (!poolStack.isEmpty()) {
				PoolNode currentNode = poolStack.pop();
				
				if (currentNode != root) {
					currentDist += currentNode.parentEdge.distance;
				}
				System.out.println(currentNode.poolName + " " + currentDist);
				writer.println(currentNode.poolName + " " + currentDist);
	
				// If not a leaf node, get the edges and push into the stack from right to left.
				if (!currentNode.getEdges().isEmpty()) {
					for (int i = currentNode.getEdges().size() - 1; i >= 0; i--) {
						poolStack.push(currentNode.getEdges().get(i).childNode);
					}
				}
			}
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
}
