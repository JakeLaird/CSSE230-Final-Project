import java.util.PriorityQueue;

/**
 * 
 */

/**
 * @author Jake
 *
 */
public class MapPoint {
	double latitude,longitude,cost;
	PriorityQueue<NeighboringPoint> neighbors;
	
		MapPoint(double latitude, double longitude){
			this.latitude = latitude;
			this.longitude = longitude;	
			
		}
		
		public void addNeighbor(MapPoint point){
			NeighboringPoint neighboringPoint = new NeighboringPoint(point);
//			neighbors.add(point.cost);
		}
	
	private class NeighboringPoint{ // AKA roads
		MapPoint point;
		double cost;
		NeighboringPoint(MapPoint point){
			this.point = point;
			cost = Math.sqrt(Math.pow(MapPoint.this.latitude- point.latitude,2) + Math.pow(MapPoint.this.longitude- point.longitude, 2));
		}
		
		
		
	}

	
}
