package rhnavigator.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;

/**
 * Paints a route
 * @author Martin Steiger
 */
public class RoutePainter implements Painter<JXMapViewer>
{
  private static Log log = LogFactory.getLog(RoutePainter.class);
  private BufferedImage startImg = null;
  private BufferedImage endImg = null;
  private Color color;
  private boolean antiAlias = true;
  
  private List<GeoPosition> track;
  
  /**
   * @param track the track
   */
  public RoutePainter(List<GeoPosition> track, Color color)
  {
    // copy the list so that changes in the 
    // original list do not have an effect here
    this.track = new ArrayList<GeoPosition>(track);
    this.color = color;

    try {
      startImg = ImageIO.read(RoutePainter.class.getResource("/images/start_waypoint.png"));
    } catch (Exception ex) {
      log.warn("couldn't read start_waypoint.png", ex);
    }
    try {
      endImg = ImageIO.read(RoutePainter.class.getResource("/images/end_waypoint.png"));
    } catch (Exception ex) {
      log.warn("couldn't read end_waypoint.png", ex);
    }
  }

  @Override
  public void paint(Graphics2D g, JXMapViewer map, int w, int h)
  {
    g = (Graphics2D) g.create();

    // convert from viewport to world bitmap
    Rectangle rect = map.getViewportBounds();
    g.translate(-rect.x, -rect.y);

    if (antiAlias)
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // do the drawing
    g.setColor(Color.BLACK);
    g.setStroke(new BasicStroke(4));

    drawRoute(g, map);

    // do the drawing again
    g.setColor(color);
    g.setStroke(new BasicStroke(2));

    drawRoute(g, map);

    g.dispose();
  }

  /**
   * @param g the graphics object
   * @param map the map
   */
  private void drawRoute(Graphics2D g, JXMapViewer map)
  {
    int lastX = 0;
    int lastY = 0;
    
    boolean first = true;
    
    for (GeoPosition gp : track)
    {
      // convert geo-coordinate to world bitmap pixel
      Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());

      if (first)
      {
//        int x = (int)pt.getX() -startImg.getWidth() / 2;
//        int y = (int)pt.getY() -startImg.getHeight();
//        g.drawImage(startImg, x, y, null);
        first = false;
      }
      else
      {
        g.drawLine(lastX, lastY-10, (int) pt.getX(), (int) pt.getY()-10);
      }
      
      lastX = (int) pt.getX();
      lastY = (int) pt.getY();
    }
  }
}
