
public class PoolEdge {
	
	public PoolNode parentNode;
	public PoolNode childNode;
	public double distance;
	
	public PoolEdge(PoolNode parentNode, PoolNode childNode, double distance) {
		this.parentNode = parentNode;
		this.childNode = childNode;
		this.distance = distance;
	}
	
}
