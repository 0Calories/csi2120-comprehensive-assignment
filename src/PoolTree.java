import java.util.LinkedList;

public class PoolTree {
	
	private PoolNode root;
	
	public PoolTree(LinkedList<PoolNode> poolList) {
		// The most Western pool needs to be the root.
		// The list is sorted so the very first element is the furthest West.
		root = poolList.get(0);
	}
	
}
