import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Parser {

	private LinkedList<PoolNode> parseJson(File jsonFile) {
		JSONParser parser = new JSONParser();
		Object obj = null;
		LinkedList<PoolNode> poolList = new LinkedList<>();
		

		try {
			obj = parser.parse(new FileReader(jsonFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray features = (JSONArray) jsonObject.get("features");
		
		for (Object pool : features) {
			JSONObject jsonPool = (JSONObject) pool;
			JSONObject poolProperties = (JSONObject) jsonPool.get("properties");
			JSONObject geometry = (JSONObject) jsonPool.get("geometry");
			
			int parkId = ((Long) poolProperties.get("PARK_ID")).intValue();
			int facilityId = ((Long) poolProperties.get("FACILITYID")).intValue();
			String poolName = poolProperties.get("NAME").toString().replace("Wading Pool - ", "");
			Object[] coordsArray = ((JSONArray) geometry.get("coordinates")).toArray();
			double latitude = (double) coordsArray[0];
			double longitude = (double) coordsArray[1];
			
			PoolNode poolObj = new PoolNode(parkId, facilityId, poolName, latitude, longitude);
			poolList.add(poolObj);
		}
		
		Collections.sort(poolList);
		
		

		return poolList;
	}
	
	public static void main(String[] args) {
		File jsonFile = new File(args[0]);
		Parser p = new Parser();
		LinkedList<PoolNode> poolList = p.parseJson(jsonFile);
		PoolTree tree = new PoolTree(poolList);
		System.out.println("\n\n");
		tree.traverse();
	}
	
}


