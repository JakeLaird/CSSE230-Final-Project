package rhnavigator;

import java.util.ArrayList;

import rhnavigator.map.Map;

/**
 * 
 */

/**
 * @author tayler
 *
 */
public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map map = Map.getSample();
    System.out.println(map);

		// 39.483861, -87.330348
		// 39.481886, -87.324785

    ArrayList<MapPoint> result = map.findInRange(39.481886, 39.483861, -87.330348, -87.324785);
    System.out.println("All:" + result.size());

    // 39.482942, -87.325220

    result = map.findInRange(39.482942, 39.483861, -87.330348, -87.324785);
    System.out.println("Less:" + result);
	}

}
