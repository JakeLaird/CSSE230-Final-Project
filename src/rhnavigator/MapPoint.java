package rhnavigator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Stack;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import rhnavigator.costfunctions.CostFunction;
import rhnavigator.costfunctions.DistanceCostFunction;
/**
 * @author Jake Taylor David Runzhi
 *
 */

public class MapPoint implements Waypoint {
	public double latitude, longitude, cost;
	private int interestLevel;
	private String name;
	public LinkedList<NeighboringPoint> neighbors;
	private CostFunction costEstimate;

	public MapPoint(double latitude, double longitude, String name, int interestLevel) {
		neighbors = new LinkedList<NeighboringPoint>();
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.interestLevel = interestLevel;
	}

	public void addNeighbor(MapPoint point) {
		NeighboringPoint neighboringPoint = new NeighboringPoint(point);
		neighbors.add(neighboringPoint);
	}

	public int getInterestLevel() {
		return interestLevel;
	}

	/**
	 * This function returns a priority queue of closest neighbors to this
	 * mapPoint
	 * 
	 * @return PriorityQueue<MapPoint>
	 */
	public List<MapPoint> getNeighbors() {
		// This is a bandage way to use it, try to figure it out the best way to
		// do it
		List<MapPoint> temp = new ArrayList<MapPoint>();
		java.util.Iterator<NeighboringPoint> i = neighbors.iterator();
		while (i.hasNext()) {
			temp.add(i.next().point);
		}
		return temp;
	}
	
	public List<NeighboringPoint> getNeighborsWithCosts() {
		return neighbors;
	}

	public String getName() {
		return name;
	}

	public List<MapPoint> getShortestDistancePath(MapPoint goal) {
		this.costEstimate = new DistanceCostFunction();
		return findShortestPath(goal);
	}

	public List<MapPoint> getShortestTimePath(MapPoint goal) {
		// this.costEstimate=new TimeCostFunction();
		return findShortestPath(goal);
	}

	public GeoPosition getPosition() {
		return new GeoPosition(latitude, longitude);
	}

	public LinkedList<MapPoint> findShortestPath(MapPoint goal) {
		LinkedList<MapPoint> tempPath = new LinkedList<MapPoint>();
		tempPath.push(this);
		PathNode tempPathNode = new PathNode(tempPath, 0,
				costEstimate.calculate(this, goal));
		PriorityQueue<PathNode> pathes = new PriorityQueue<PathNode>();
		pathes.add(tempPathNode);
		while (!pathes.isEmpty()) {
			tempPath = pathes.poll().path;
			MapPoint current = tempPath.peekLast();
			if (current.name.equals(goal.name)) {
				return tempPath;
			}
			List<MapPoint> tempNeighbors = current.getNeighbors();
			for (int i = 0; i < tempNeighbors.size(); i++) {
				MapPoint n = tempNeighbors.get(i);
				if (tempPath.contains(n)) {
					continue;
				}
				@SuppressWarnings("unchecked")
				LinkedList<MapPoint> newPath = (LinkedList<MapPoint>) tempPath.clone();
				newPath.add(n);
				int tempCost = tempPathNode.currentCost
						+ costEstimate.calculate(current, n);
				int tempHeuristicCost = tempCost
						+ costEstimate.calculate(n, goal);
				pathes.add(new PathNode(newPath, tempCost, tempHeuristicCost));
			}
		}

		return null;
	}

	private class PathNode implements Comparable<PathNode> {
		private LinkedList<MapPoint> path = new LinkedList<MapPoint>();
		int currentCost;
		int heuristicCost;

		private PathNode() {
			this.path = new LinkedList<MapPoint>();
			this.currentCost = 0;
			this.heuristicCost = 0;
		}

		private PathNode(LinkedList<MapPoint> path, int currentCost,
				int heuristicCost) {
			this.path = path;
			this.currentCost = currentCost;
			this.heuristicCost = heuristicCost;
		}

		private ArrayList<MapPoint> toArrayList() {
			ArrayList<MapPoint> output = new ArrayList<MapPoint>();
			while (!path.isEmpty()) {
				output.add(path.poll());
			}
			return output;
		}

		@Override
		public int compareTo(PathNode arg0) {
			return this.heuristicCost - arg0.heuristicCost;
		}

	}

	public class NeighboringPoint implements Comparable<NeighboringPoint> { 
		public MapPoint point;
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
		
		public NeighboringPoint(MapPoint point, int cost) {
			this.point = point;
			this.cost = cost;
		}
		
		public int getCost() {
			return cost;
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
		// return "\n(" + name + ", " + neighbors.toString() + ")\n";
		return name;
	}

	public void addNeighbor(MapPoint neighbor, int cost) {
		NeighboringPoint neighboringPoint = new NeighboringPoint(neighbor, cost);
		neighbors.add(neighboringPoint);
	}
}
			return "<" + point.getName() + ", " + cost + ">";
		}
	}

	public String toString() {
		// return "\n(" + name + ", " + neighbors.toString() + ")\n";
		return name;
	}

	public void addNeighbor(MapPoint neighbor, int cost) {
		NeighboringPoint neighboringPoint = new NeighboringPoint(neighbor, cost);
		neighbors.add(neighboringPoint);
	}
}
