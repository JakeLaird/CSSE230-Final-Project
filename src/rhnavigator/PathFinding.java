package rhnavigator;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

import rhnavigator.MapPoint.NeighboringPoint;
import rhnavigator.costfunctions.CostFunction;
import rhnavigator.map.Map;

public class PathFinding {
	private MapPoint start;
	private MapPoint destination;
	private ArrayList<MapPoint> route;
	private int cost;
	private CostFunction c;
	private int expectedCost;

	public PathFinding(MapPoint start, MapPoint destination,
			CostFunction preferences, Map map) {
		this.start = start;
		this.destination = destination;
		this.c = preferences;
		this.route = new ArrayList<MapPoint>();
		this.expectedCost = preferences.calculate(start, destination);
		this.cost = 0;
	}

	public boolean findShortestPath() {
		// function A*(start,goal)
		ArrayList<MapPoint> closedSet = new ArrayList<MapPoint>();
		// closedset := the empty set // The set of nodes already evaluated.
		PriorityQueue<MapPoint> neighbors = start.getNeighbors();

		// The set of tentative nodes to be evaluated, initially containing the
		// start node
		// came_from := the empty map // The map of navigated nodes.
		ArrayList<MapPoint> cameFrom = new ArrayList<MapPoint>();
		cameFrom.add(start);
		//
		// g_score[start] := 0 // Cost from start along best known path.

		// // Estimated total cost from start to goal through y.
		// f_score[start] := g_score[start] + heuristic_cost_estimate(start,
		// goal)
		//
		// while neighbors is not empty
		while (neighbors.size() != 0) {
			if (neighbors.peek().equals(destination)) {
				route = reconstructPath(cameFrom, destination);
				return true;
			}

			// current := the node in neighbors having the lowest f_score[] value
			// if current = goal
			// return reconstruct_path(came_from, goal)
			//
			else{
				closedSet.add(neighbors.poll());
				
			}
			
			// remove current from neighbors
			// add current to closedset
			// for each neighbor in neighbor_nodes(current)
			// if neighbor in closedset
			// continue
			// tentative_g_score := g_score[current] +
			// dist_between(current,neighbor)
			//
			// if neighbor not in neighbors or tentative_g_score <
			// g_score[neighbor]
			// came_from[neighbor] := current
			// g_score[neighbor] := tentative_g_score
			// f_score[neighbor] := g_score[neighbor] +
			// heuristic_cost_estimate(neighbor, goal)
			// if neighbor not in neighbors
			// add neighbor to neighbors
			//
			// return failure
		}
		//

		return false;
	}

	private ArrayList<MapPoint> reconstructPath(ArrayList<MapPoint> cameFrom,
			MapPoint current) {
		ArrayList<MapPoint> totalPath = new ArrayList<MapPoint>();
		totalPath.addAll(cameFrom);
		totalPath.add(current);
		return totalPath;
	}

	// function reconstruct_path(came_from,current)
	// total_path := [current]
	// while current in came_from:
	// current := came_from[current]
	// total_path.append(current)
	// return total_path
}
