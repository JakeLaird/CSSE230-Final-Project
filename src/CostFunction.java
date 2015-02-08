/**
 * Function Object for finding the cost between 2 MapPoints.
 */

/**
 * @author Tayler
 *
 */

public interface CostFunction {
  public int calculate(MapPoint p1, MapPoint p2);
}
