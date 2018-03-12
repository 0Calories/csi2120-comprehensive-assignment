
public class Utils {

	private static final double CONST = 6371.0;
	private static final double RAD_CONST = 180.0;
	
	public static double calculateDistance(PoolNode node1, PoolNode node2) {
		
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
	
}
