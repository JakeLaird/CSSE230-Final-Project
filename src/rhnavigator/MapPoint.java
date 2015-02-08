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
	PriorityQueue<NeighboringPoint> neighbors;
	
		public MapPoint(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
		
		public void addNeighbor(MapPoint point) {
			NeighboringPoint neighboringPoint = new NeighboringPoint(point);
//			neighbors.add(point.cost);
		}
	
	private class NeighboringPoint { // AKA roads
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
	}
}
