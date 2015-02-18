package rhnavigator.map;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointRenderer;

import rhnavigator.MapPoint;

public class RouteMapPointRenderer implements WaypointRenderer<MapPoint> {
	private static Log log = LogFactory.getLog(DefaultWaypointRenderer.class);
	private BufferedImage img = null;
	
	public enum	PointType {
		START, END
	}
	/**
	* Uses a default waypoint image
	*/
	public RouteMapPointRenderer(PointType type) {
		if (type == PointType.START) {
			try {
		      img = ImageIO.read(RoutePainter.class.getResource("/images/start_waypoint.png"));
		    } catch (Exception ex) {
		      log.warn("couldn't read start_waypoint.png", ex);
		    }
		} else {
		    try {
		      img = ImageIO.read(RoutePainter.class.getResource("/images/end_waypoint.png"));
		    } catch (Exception ex) {
		      log.warn("couldn't read end_waypoint.png", ex);
		    }
		}
	}
	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, MapPoint w)
	{
		if (img == null)
			return;
		Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());

			
		int width = (int) (img.getWidth());
		int height = (int) (img.getHeight());
		int x = (int)point.getX() -width / 2;
		int y = (int)point.getY() -height;
		g.drawImage(img, x, y, width, height, null);
	}
}
