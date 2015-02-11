package rhnavigator;

import java.util.ArrayList;

import rhnavigator.costfunctions.CostFunction;
import rhnavigator.map.Map;


public class PathFinding {
	private MapPoint current;
	private MapPoint destination;
	private ArrayList<MapPoint> route;
	private int cost;
	private CostFunction preferences;
	private int expectedCost;
	
	
	
	public PathFinding(MapPoint current, MapPoint destination, CostFunction preferences, Map map){
		this.current=current;
		this.destination=destination;
		this.preferences=preferences;
		this.route=new ArrayList<MapPoint>();
		this.expectedCost=preferences.calculate(current, destination);
		this.cost=0;
	}
	
	public ArrayList<MapPoint> findShortestPath(MapPoint temp){
		 //while(preferences.calculate(temp.getNeighbors().peek().point, destination)
		return null;
	}
}
