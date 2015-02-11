package rhnavigator.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import rhnavigator.MapPoint;
import net.sf.javaml.core.kdtree.KDTree;

/**
 * Stores the all of the MapPoints in the currently loaded map
 */

/**
 * @author Tayler
 */

public class Map {
	private TreeMap<String,MapPoint> mapPoints;
	private KDTree kdMapPoints;

  /**
   * Creates an empty Map.
   */
  public Map() {
    mapPoints = new TreeMap<String,MapPoint>();
    kdMapPoints = new KDTree(2); // 2 dimensions for our application
  }

  /**
   * Creates and inserts a new MapPoint into the Map.
   * 
   * @param latitude
   *            latitude for the new MapPoint
   * @param longitude
   *            longitude for the new MapPoint
   * @param name 
   *            name for the new MapPoint
   * @param neighbors
   *            list of names of neighboring MapPoints
   * @param interestLevel
   *            interest level of the new MapPoint
   * @return 
   *            true if the map was changed
   */
  public Boolean addPoint(double latitude, double longitude, 
                          String name, ArrayList<String> neighbors,
                          int interestLevel) {
    if (mapPoints.containsKey(name)) {
      return false;
    }

    MapPoint newPoint = new MapPoint(latitude, longitude, name);
    kdMapPoints.insert(new double[] {latitude, longitude}, newPoint);
    mapPoints.put(name, newPoint);

    if (neighbors != null) {
      // Iterate over all the new neighbors to add
      for (String neighbor : neighbors) {
        MapPoint neighborMapPoint = mapPoints.get(neighbor);
        if (neighborMapPoint != null) {
          // Link them together
          newPoint.addNeighbor(neighborMapPoint);
          neighborMapPoint.addNeighbor(newPoint);
        }
      }
    }
    return true;
  }

  /**
   * Returns the number of MapPoints stored in this Map
   * @return
   *       the number of MapPoints in this Map
   */
  public int size() {
    return mapPoints.size();
  }

  public MapPoint findByName(String name) {
    return mapPoints.get(name);
  }

  public ArrayList<MapPoint> findInRange(double bottomBound, double topBound,
                                          double leftBound, double rightBound) {

    double[] lowerBounds = new double[] {bottomBound, leftBound};
    double[] upperBounds = new double[] {topBound, rightBound};

    MapPoint[] results = (MapPoint[])kdMapPoints.range(lowerBounds, upperBounds);

    return new ArrayList<MapPoint>(Arrays.asList(results));
  }

  public ArrayList<MapPoint> toArrayList() {
    return new ArrayList<MapPoint>(mapPoints.values());
  }

  public String toString() {
    return mapPoints.toString();
  }

  public static Map getSample() {
    Map map = new Map();

    map.addPoint(39.483559, -87.327731, "Mees Hall", null, 0);
    map.addPoint(39.483387, -87.328372, "Blumberg Hall", new ArrayList<String>() {{ add("Mees Hall"); }}, 0);
    map.addPoint(39.483660, -87.328109, "Scharpenberg Hall",
                                                          new ArrayList<String>() {{ 
                                                            add("Mees Hall");
                                                            add("Blumberg Hall");
                                                          }}, 0);
    map.addPoint(39.483590, -87.326964, "Hulman Memorial Union",
                                                          new ArrayList<String>() {{ 
                                                            add("Mees Hall");
                                                            add("Deming Hall");
                                                          }}, 0);
    map.addPoint(39.482501, -87.329308, "White Chapel",
                                                          new ArrayList<String>() {{ 
                                                            add("Percopo Hall");
                                                            add("Blumberg Hall");
                                                          }}, 0);
    map.addPoint(39.483459, -87.325719, "Deming Hall",
                                                          new ArrayList<String>() {{ 
                                                            add("Hulman Memorial Union");
                                                            add("BSB Hall");
                                                          }}, 0);
    map.addPoint(39.482463, -87.325689, "BSB Hall",
                                                          new ArrayList<String>() {{ 
                                                            add("Deming Hall");
                                                            add("Speed Hall");
                                                          }}, 0);
    map.addPoint(39.482148, -87.326724, "Speed Hall",
                                                          new ArrayList<String>() {{ 
                                                            add("Percopo Hall");
                                                            add("BSB Hall");
                                                          }}, 0);
    map.addPoint(39.482123, -87.328347, "Percopo Hall",
                                                          new ArrayList<String>() {{ 
                                                            add("White Chapel");
                                                            add("Speed Hall");
                                                          }}, 0);
    
    return map;
  }
}
