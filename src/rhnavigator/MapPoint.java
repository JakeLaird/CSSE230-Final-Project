package rhnavigator;

import java.util.PriorityQueue;
import rhnavigator.costfunctions.*;

/**
 * 
 */

/**
 * @author Jake Taylor David Runzhi
 *
 */

public class MapPoint {
	public double latitude,longitude,cost;
	private String name;
	
	public PriorityQueue<NeighboringPoint> neighbors;
	
		public MapPoint(double latitude, double longitude, String name) {
			neighbors = new PriorityQueue<NeighboringPoint>();
			this.latitude = latitude;
			this.longitude = longitude;
			this.name = name;
		}
		
		public void addNeighbor(MapPoint point) {
			NeighboringPoint neighboringPoint = new NeighboringPoint(point);
			neighbors.add(neighboringPoint);
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	
	private class NeighboringPoint implements Comparable<NeighboringPoint> { // AKA roads
		MapPoint point;
		int cost;

		public NeighboringPoint(MapPoint point) {
			this.point = point;
			CostFunction func = new DistanceCostFunction();
			UpdateCost(func);
		}

		public NeighboringPoint(MapPoint point, CostFunction func) {
			this.point = point;
			UpdateCost(func);
		}

		public void UpdateCost(CostFunction func) {
			cost = func.calculate(MapPoint.this, point);
		}
		
		public int compareTo(NeighboringPoint neighbor) {
			return this.cost - neighbor.cost;
		}
		
		public String toString() {
			return point.getName();
		}
	}

	public String toString() {
		return "\nMapPoint: " + name + ", Neighbors: " + neighbors.toString() + "\n";
	}
}
