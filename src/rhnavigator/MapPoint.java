package rhnavigator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import rhnavigator.costfunctions.*;

/**
 * @author Jake Taylor David Runzhi
 *
 */

public class MapPoint implements Waypoint {
	public double latitude, longitude;
	private int interestLevel;
	private String name;
	public Map<MapPoint, NeighboringPoint> neighbors;
	private CostFunction costEstimate;
	
	private enum PathType {
		TIME, DISTANCE
	}

	public MapPoint(double latitude, double longitude, String name, int interestLevel) {
		neighbors = new Hashtable<MapPoint, NeighboringPoint>();
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.interestLevel = interestLevel;
	}

	public void addNeighbor(MapPoint point) {
		NeighboringPoint neighboringPoint = new NeighboringPoint(point);
		neighbors.put(point, neighboringPoint);
	}

	public int getInterestLevel() {
		return interestLevel;
	}

 	public boolean equals(Object p) {
 		if (!(p instanceof MapPoint)) {
 			return false;
 		}
		return name.equals(((MapPoint)p).getName());
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
		java.util.Iterator<NeighboringPoint> i = neighbors.values().iterator();
		while (i.hasNext()) {
			temp.add(i.next().point);
		}
		return temp;
	}
	
	public List<NeighboringPoint> getNeighborsWithCosts() {
		return new ArrayList<NeighboringPoint>(neighbors.values());
	}

	public String getName() {
		return name;
	}

	public List<MapPoint> getShortestDistancePath(MapPoint goal) {
		this.costEstimate = new DistanceCostFunction();
		return findShortestPath(goal, PathType.DISTANCE);
	}

	public List<MapPoint> getShortestTimePath(MapPoint goal) {
		this.costEstimate=new TimeCostFunction();
		return findShortestPath(goal, PathType.TIME);
	}

	public GeoPosition getPosition() {
		return new GeoPosition(latitude, longitude);
	}
	
	public List<MapPoint> findShortestPath(MapPoint goal, PathType type) {
		Queue<PathNode> openNodes = new PriorityQueue<PathNode>();
		Map<String,PathNode> closedNodes = new HashMap<String,PathNode>();

		int tempHeuristicCost = costEstimate.calculate(this, goal);
		openNodes.add(new PathNode(0, tempHeuristicCost, null, this));
		
		while (!openNodes.isEmpty()) {
			PathNode current = openNodes.poll();

			if (current.point.name.equals(goal.name)) {
				return current.getPath(); // To list
			}
			closedNodes.put(current.point.getName(), current);

			List<NeighboringPoint> neighbors = current.point.getNeighborsWithCosts();
			for (NeighboringPoint n : neighbors) {
				int cost = current.currentCost;
				if (type == PathType.DISTANCE) {
					cost += n.getDistanceCost();
				} else {
					cost += n.getTimeCost();
				}

				// Check if already in the open set
				boolean nodeInOpenSet = false;
				Iterator<PathNode> openIterator = openNodes.iterator();
				while (openIterator.hasNext()) {
					PathNode openNode = openIterator.next();
					if (openNode.point.equals(n.point)) {
						if (cost < openNode.currentCost) {
							openIterator.remove();
						} else {
							nodeInOpenSet = true;
						}
						break;
					}
				}
				
				// Inadmissable heuristic, revisit
				PathNode closedNeighbor = closedNodes.get(n.point.getName());
				if (closedNeighbor != null) {
					if (cost < closedNeighbor.currentCost) {
						closedNodes.remove(n.point.getName());
						closedNeighbor = null;
					}
				}
				// Not in open or closed, add to open set
				if (!nodeInOpenSet && closedNeighbor == null) {
					int newHeuristicCost = costEstimate.calculate(n.point, goal);
					PathNode newNode = new PathNode(cost, newHeuristicCost, current, n.point);
					openNodes.add(newNode);
				}
			}
		}
		return null;
	}

	private boolean setContains(Set<PathNode> set, MapPoint p) {
		PathNode tempNode = new PathNode(0, 0, null, p);
		boolean temp = set.contains(tempNode);
		return temp;
	}

	private class PathNode implements Comparable<PathNode> {
		int currentCost;
		int heuristicCost;
		PathNode parent;
		MapPoint point;

		private PathNode() {
			this.currentCost = 0;
			this.heuristicCost = 0;
		}

		private PathNode(int currentCost, int heuristicCost, PathNode parent, MapPoint point) {
			this.currentCost = currentCost;
			this.heuristicCost = heuristicCost;
			this.parent = parent;
			this.point = point;
		}

		@Override
		public int compareTo(PathNode arg0) {
			return (heuristicCost + currentCost) - (arg0.heuristicCost + arg0.currentCost);
		}
		
		public List<MapPoint> getPath() {
			Stack<PathNode> path = new Stack<PathNode>();
			PathNode current = this;
			while (current != null) {
				path.push(current);
				current = current.parent;
			}
			ArrayList<MapPoint> result = new ArrayList<MapPoint>();
			while (!path.isEmpty()) {
				result.add(path.pop().point);
			}
			return result;
		}
		
		public String toString() {
			return point.toString();
		}
		
		public boolean equals(Object o) {
			if (!(o instanceof PathNode)) {
				return false;
			}
			return ((PathNode)o).point.equals(point);
		}
		
		public int hashCode() {
			return point.name.hashCode();
		}

	}

	public class NeighboringPoint { 
		public MapPoint point;
		private int distanceCost;
		private int timeCost;

		public NeighboringPoint(MapPoint point) {
			this.point = point;
		}
		
		public NeighboringPoint(MapPoint point, int distanceCost, int timeCost) {
			this.point = point;
			this.distanceCost = distanceCost;
			this.timeCost = timeCost;
		}
		
		public int getDistanceCost() {
			return distanceCost;
		}		

		public int getTimeCost() {
			return timeCost;
		}

		public String toString() {
			return "<" + point.getName() + ", " + distanceCost + ", " + timeCost + ">";
		}
	}
	
	public boolean isLandmark() {
		return false;
	}

	public String toString() {
		// return "\n(" + name + ", " + neighbors.toString() + ")\n";
		return name;
	}

	public void addNeighbor(MapPoint neighbor, int distanceCost, int timeCost) {
		NeighboringPoint neighboringPoint = new NeighboringPoint(neighbor, distanceCost, timeCost);
		neighbors.put(neighbor, neighboringPoint);
	}
}
