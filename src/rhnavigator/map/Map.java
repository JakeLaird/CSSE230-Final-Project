package rhnavigator.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import net.sf.javaml.core.kdtree.KDTree;
import rhnavigator.MapPoint;


/**
 * Stores the all of the MapPoints in the currently loaded map
 */

/**
 * @author Tayler
 */

public class Map {
	private TreeMap<String, MapPoint> mapPoints;
	private KDTree kdMapPoints;
	private List<List<MapPoint>> currentRoutes;
	private List<PendingConnection> pendingConnections;

	/**
	 * Creates an empty Map.
	 */
	public Map() {
		mapPoints = new TreeMap<String, MapPoint>();
		kdMapPoints = new KDTree(2); // 2 dimensions for our application
		currentRoutes = new ArrayList<List<MapPoint>>();
		pendingConnections = new ArrayList<PendingConnection>();
	}

	/**
	 * Creates and inserts a new MapPoint into the Map.
	 * 
	 * @param latitude
	 *            latitude for the new MapPoint
	 * @param longitude
	 *            longitude for the new MapPoint
	 * @param name
	 *            name for the new MapPoint
	 * @param neighbors
	 *            list of names of neighboring MapPoints
	 * @param interestLevel
	 *            interest level of the new MapPoint
	 * @return true if the map was changed
	 */
	public MapPoint addPointWithCost(double latitude, double longitude, String name,
			List<NeighborConnection> neighbors, int interestLevel) {
		MapPoint newPoint;
		if (mapPoints.containsKey(name)) {
			newPoint = mapPoints.get(name);
		} else {
			newPoint = new MapPoint(latitude, longitude, name, interestLevel);
			kdMapPoints.insert(new double[] { latitude, longitude }, newPoint);
			mapPoints.put(name, newPoint);
		}

		if (neighbors != null) {
			// Iterate over all the new neighbors to add
			for (NeighborConnection neighbor : neighbors) {
				MapPoint neighborMapPoint = mapPoints.get(neighbor.getNeighbor());
				if (neighborMapPoint != null) {
					// Link them together
					newPoint.addNeighbor(neighborMapPoint);
					neighborMapPoint.addNeighbor(newPoint);
				} else {
					PendingConnection newPending = new PendingConnection(newPoint, neighbor);
					pendingConnections.add(newPending);
				}
			}
		}
		return newPoint;
	}
	
	/**
	 * Creates and inserts a new MapPoint into the Map.
	 * 
	 * @param latitude
	 *            latitude for the new MapPoint
	 * @param longitude
	 *            longitude for the new MapPoint
	 * @param name
	 *            name for the new MapPoint
	 * @param neighbors
	 *            list of names of neighboring MapPoints
	 * @param interestLevel
	 *            interest level of the new MapPoint
	 * @return true if the map was changed
	 */
	public MapPoint addPoint(double latitude, double longitude, String name,
			List<String> neighbors, int interestLevel) {
		MapPoint newPoint;
		if (mapPoints.containsKey(name)) {
			newPoint = mapPoints.get(name);
		} else {
			newPoint = new MapPoint(latitude, longitude, name, interestLevel);
			kdMapPoints.insert(new double[] { latitude, longitude }, newPoint);
			mapPoints.put(name, newPoint);
		}

		if (neighbors != null) {
			// Iterate over all the new neighbors to add
			for (String neighbor : neighbors) {
				MapPoint neighborMapPoint = mapPoints.get(neighbor);
				if (neighborMapPoint != null) {
					// Link them together
					newPoint.addNeighbor(neighborMapPoint);
					neighborMapPoint.addNeighbor(newPoint);
				} else {
					PendingConnection newPending = new PendingConnection(newPoint, neighbor);
					pendingConnections.add(newPending);
				}
			}
		}
		return newPoint;
	}
	
	public boolean processPending() {
		if (pendingConnections.isEmpty()) {
			return false;
		}
		
		for (PendingConnection p : pendingConnections) {
			MapPoint neighbor = mapPoints.get(p.getNeighborConnection().getNeighbor());
			if (neighbor != null) {
				MapPoint point = p.getPoint();
				int cost = p.getNeighborConnection().getCost();
				point.addNeighbor(neighbor, cost);
				neighbor.addNeighbor(point, cost);
			}
		}
		pendingConnections.clear();
		
		return true;
	}
	
	public boolean hasPendingConnections() {
		return !pendingConnections.isEmpty();
	}

	public void addRoute(List<MapPoint> newRoute) {
		if (newRoute == null) {
			throw new IllegalArgumentException();
		}

		currentRoutes.add(newRoute);
	}

	public List<List<MapPoint>> getRoutes() {
		List<List<MapPoint>> returnRoutes = new ArrayList<List<MapPoint>>();
		for (List<MapPoint> route : currentRoutes) {
			returnRoutes.add(Collections.unmodifiableList(route));
		}
		return Collections.unmodifiableList(returnRoutes);
	}

	/**
	 * Returns the number of MapPoints stored in this Map
	 * 
	 * @return the number of MapPoints in this Map
	 */
	public int size() {
		return mapPoints.size();
	}

	public MapPoint findByName(String name) {
		return mapPoints.get(name);
	}

	public ArrayList<MapPoint> findInRange(double bottomBound, double topBound,
			double leftBound, double rightBound) {

		double[] lowerBounds = new double[] { bottomBound, leftBound };
		double[] upperBounds = new double[] { topBound, rightBound };

		Object[] results = kdMapPoints.range(lowerBounds, upperBounds);

		MapPoint[] mapPointsResult = Arrays.copyOf(results, results.length,
				MapPoint[].class);

		return new ArrayList<MapPoint>(Arrays.asList(mapPointsResult));
	}

	public MapPoint getNearest(double latitude, double longitude) {
		double[] key = new double[] { latitude, longitude };
		return (MapPoint) kdMapPoints.nearest(key);
	}

	public ArrayList<MapPoint> toArrayList() {
		return new ArrayList<MapPoint>(mapPoints.values());
	}

	public String toString() {
		return mapPoints.toString();
	}

	public String getstring(){
		String result="";
		for(String key:mapPoints.keySet()){
			double la=mapPoints.get(key).latitude;
			double lo=mapPoints.get(key).longitude;
			String name=mapPoints.get(key).getName();
			String nei=mapPoints.get(key).neighbors.toString();
			int in=0;
			String temp="["+la+","+lo+","+name+","+nei+","+in+"]\n";
			result+=temp;
		}
		return result;
	}
	
	public static class NeighborConnection {
		private String neighbor;
		private int cost;
		
		public NeighborConnection(String neighbor, int cost) {
			this.cost = cost;
			this.neighbor = neighbor;
		}
		
		public String getNeighbor() {
			return neighbor;
		}
		
		public int getCost() {
			return cost;
		}
	}
	
	private class PendingConnection {
		private MapPoint point;
		private NeighborConnection neighbor;
		
		public PendingConnection(MapPoint newPoint, NeighborConnection neighbor) {
			this.point = newPoint;
			this.neighbor = neighbor;
		}
		public PendingConnection(MapPoint newPoint, String neighbor) {
			this.point = newPoint;
			this.neighbor = new NeighborConnection(neighbor, 0);
		}
		public MapPoint getPoint() {
			return point;
		}
		public NeighborConnection getNeighborConnection() {
			return neighbor;
		}
	}

	public static Map getSample() {
		List<MapPoint> newRoute = new ArrayList<MapPoint>();
		Map map = new Map();

		newRoute.add(map.addPoint(39.483559, -87.327731, "Mees Hall", null, 0));
		newRoute.add(map.addPoint(39.483387, -87.328372, "Blumberg Hall",
				new ArrayList<String>() {
					{
						add("Mees Hall");
					}
				}, 0));
		newRoute.add(map.addPoint(39.483660, -87.328109, "Scharpenberg Hall",
				new ArrayList<String>() {
					{
						add("Mees Hall");
						add("Blumberg Hall");
					}
				}, 0));
		map.addPoint(39.483590, -87.326964, "Hulman Memorial Union",
				new ArrayList<String>() {
					{
						add("Mees Hall");
						add("Deming Hall");
					}
				}, 0);
		map.addPoint(39.482501, -87.329308, "White Chapel",
				new ArrayList<String>() {
					{
						add("Percopo Hall");
						add("Blumberg Hall");
					}
				}, 0);
		map.addPoint(39.483459, -87.325719, "Deming Hall",
				new ArrayList<String>() {
					{
						add("Hulman Memorial Union");
						add("BSB Hall");
					}
				}, 0);

		map.addRoute(newRoute);
		newRoute = new ArrayList<MapPoint>();

		newRoute.add(map.addPoint(39.482463, -87.325689, "BSB Hall",
				new ArrayList<String>() {
					{
						add("Deming Hall");
						add("Speed Hall");
					}
				}, 0));
		newRoute.add(map.addPoint(39.482148, -87.326724, "Speed Hall",
				new ArrayList<String>() {
					{
						add("Percopo Hall");
						add("BSB Hall");
					}
				}, 0));
		newRoute.add(map.addPoint(39.482123, -87.328347, "Percopo Hall",
				new ArrayList<String>() {
					{
						add("White Chapel");
						add("Speed Hall");
					}
				}, 0));

		map.addRoute(newRoute);

		return map;
	}
}
