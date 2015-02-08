package rhnavigator.map;
import java.util.ArrayList;
import rhnavigator.MapPoint;
/**
 * Stores the all of the MapPoints in the currently loaded map
 */

/**
 * @author Tayler
 *
 */

public class Map {
	private ArrayList<MapPoint> mapPoints;

  public Map() {
    mapPoints = new ArrayList<MapPoint>();
  }

  public Boolean addPoint(double latitude, double longitude, 
                          String name, ArrayList<String> neighbors,
                          int interestLevel) {
    MapPoint newPoint = new MapPoint(latitude, longitude);
    throw new UnsupportedOperationException();
  }
}
