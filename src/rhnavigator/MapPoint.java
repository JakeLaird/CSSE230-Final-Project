package rhnavigator;

import java.util.ArrayList;
import java.util.PriorityQueue;

import javax.swing.text.html.HTMLDocument.Iterator;

import com.sun.corba.se.spi.copyobject.CopyobjectDefaults;

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
	private PriorityQueue<NeighboringPoint> neighbors;
	
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
		
		
	
	class NeighboringPoint implements Comparable<NeighboringPoint> { // AKA roads
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
			return "<" + point.getName() + ", " + cost + ">";
		}
	}

	public String toString() {
		return "\n(" + name + ", " + neighbors.toString() + ")\n";
	}
}
