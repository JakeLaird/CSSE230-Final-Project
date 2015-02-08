package rhnavigator.costfunctions;
import rhnavigator.MapPoint;
/**
 * Function Object for finding the distance between 2 MapPoints.
 */

/**
 * @author Tayler
 *
 */

public class DistanceCostFunction implements CostFunction {
  public int calculate(MapPoint p1, MapPoint p2) {

    // Taken from here: http://www.movable-type.co.uk/scripts/latlong.html    
    double R = 6371000; // metres
    double phi1 = Math.toRadians(p1.latitude);
    double phi2 = Math.toRadians(p2.latitude);

    double dphi = Math.toRadians(p2.latitude - p1.latitude);
    double dlambda = Math.toRadians(p2.longitude - p1.longitude);

    double a = Math.sin(dphi/2) * Math.sin(dphi/2) +
                Math.cos(phi1) * Math.cos(phi2) *
                Math.sin(dlambda/2) * Math.sin(dlambda/2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return (int)(R * c);
  }
}
