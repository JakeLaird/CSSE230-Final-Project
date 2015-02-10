package rhnavigator.map;
import java.util.ArrayList;
import rhnavigator.MapPoint;
import net.sf.javaml.core.kdtree.KDTree;
/**
 * Stores the all of the MapPoints in the currently loaded map
 */

/**
 * @author Tayler
 *
 */

public class Map {
	private ArrayList<MapPoint> mapPoints;
	private KDTree mapPointsTree;

  public Map() {
    mapPoints = new ArrayList<MapPoint>();
    mapPointsTree = new KDTree(2); // 2 dimensions for our application
  }

  public Boolean addPoint(double latitude, double longitude, 
                          String name, ArrayList<String> neighbors,
                          int interestLevel) {
    MapPoint newPoint = new MapPoint(latitude, longitude);
    mapPointsTree.insert(new double[] {latitude, longitude}, newPoint);
    throw new UnsupportedOperationException();
  }
}
