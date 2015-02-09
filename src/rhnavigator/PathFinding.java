package rhnavigator;

import java.util.ArrayList;

import rhnavigator.costfunctions.CostFunction;


public class PathFinding {
	private MapPoint current;
	private MapPoint destination;
	private ArrayList<MapPoint> route;
	private int cost;
	private CostFunction preferences;
	private int expectedCost;
	
	
	public PathFinding(MapPoint current, MapPoint destination, CostFunction preferences){
		this.current=current;
		this.destination=destination;
		this.preferences=preferences;
		this.route=new ArrayList<MapPoint>();
		this.expectedCost=preferences.calculate(current, destination);
		this.cost=0;
	}
	
	public ArrayList<MapPoint> findShortestPath(MapPoint temp){
		 
		return null;
	}
}
