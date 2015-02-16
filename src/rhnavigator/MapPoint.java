package rhnavigator;

import java.util.ArrayList;
import java.util.PriorityQueue;
import rhnavigator.costfunctions.*;


/**
 * @author Jake Taylor David Runzhi
 *
 */

public class MapPoint{
	public double latitude,longitude,cost;
	private String name;	
	public PriorityQueue<NeighboringPoint> neighbors;
	private CostFunction costEstimate;
	
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
		/**
		 * This function returns a priority queue of closest neighbors to this mapPoint
		 * @return PriorityQueue<MapPoint>
		 */
		public  PriorityQueue<MapPoint> getNeighbors(){
			//This is a bandage way to use it, try to figure it out the best way to do it
			PriorityQueue<MapPoint> temp=new PriorityQueue<MapPoint>();
			java.util.Iterator<NeighboringPoint> i= neighbors.iterator();
			while(i.hasNext()){
				temp.add(i.next().point);
			}
			return temp;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public ArrayList<MapPoint> getShortestDistancePath(MapPoint goal){
			this.costEstimate=new DistanceCostFunction();
			return findShortestPath(goal);
		}
		public ArrayList<MapPoint> getShortestTimePath(MapPoint goal){
//			this.costEstimate=new TimeCostFunction();
			return findShortestPath(goal);
		}
		
		private ArrayList<MapPoint> findShortestPath( MapPoint goal){
			ArrayList<MapPoint> closedset=new ArrayList<MapPoint>();
			PriorityQueue<MapPoint>openset=new PriorityQueue<MapPoint>();
			openset.add(this);
			ArrayList<MapPoint> cameFrom=new ArrayList<MapPoint>();
			int tripCost=0;
			int estimateCost=tripCost+costEstimate.calculate(this, goal);
//	 
			while(!openset.isEmpty()){
				MapPoint current = openset.poll();
//	        current := the node in openset having the lowest f_score[] value
				if(current.equals(goal)){
//	        if current = goal
					return reconstructPath(cameFrom, current);
				}
//	            return reconstruct_path(came_from, goal)
//	        remove current from openset
				closedset.add(current);
				cameFrom.add(current);
//	        add current to closedset
				java.util.Iterator<NeighboringPoint> t=current.neighbors.iterator();
				while(t.hasNext()){
//	        for each neighbor in neighbor_nodes(current)
					MapPoint neighbor=t.next().point;
				if(closedset.contains(neighbor)){
					continue;
				}
//				int neighborCost=costEstimate.calculate(neighbor, goal);
				
				cameFrom.add(neighbor);
				tripCost+=costEstimate.calculate(current, neighbor);
				if(neighbor.equals(goal)){
					break;
				}
				}
			}
	    return cameFrom;
		}
		
		private ArrayList<MapPoint> reconstructPath(ArrayList<MapPoint> cameFrom, MapPoint current) {
			ArrayList<MapPoint> totalPath = new ArrayList<MapPoint>();
			totalPath.addAll(cameFrom);
			totalPath.add(current);
			return totalPath;
		}
//		function reconstruct_path(came_from,current)
//	    total_path := [current]
//	    while current in came_from:
//	        current := came_from[current]
//	        total_path.append(current)
//	    return total_path
		
		
	
	private class NeighboringPoint implements Comparable<NeighboringPoint> { // AKA roads
		MapPoint point;
		private int cost;

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
			return "<" + point.getName() + ", " + cost + ">";
		}
	}

	public String toString() {
		return "\n(" + name + ", " + neighbors.toString() + ")\n";
	}
}
