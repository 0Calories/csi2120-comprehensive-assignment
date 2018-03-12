
public class PoolEdge {
	
	public PoolNode parentNode = null;
	public PoolNode childNode = null;
	public double distance;
	
	public PoolEdge(PoolNode parentNode, PoolNode childNode, double distance) {
		this.parentNode = parentNode;
		this.childNode = childNode;
		this.distance = distance;
	}
	
}
