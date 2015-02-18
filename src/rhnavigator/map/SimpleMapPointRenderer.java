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

public class SimpleMapPointRenderer implements WaypointRenderer<MapPoint> {
	private static Log log = LogFactory.getLog(DefaultWaypointRenderer.class);
	private BufferedImage img = null;

	/**
	 * Uses a default waypoint image
	 */
	public SimpleMapPointRenderer() {
		try {
			img = ImageIO.read(DefaultWaypointRenderer.class
					.getResource("/images/simple_waypoint.png"));
		} catch (Exception ex) {
			log.warn("couldn't read standard_waypoint.png", ex);
		}
	}

	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, MapPoint w) {
		if (img == null)
			return;

		int zoom = map.getZoom();
		
		Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), zoom);
		
		double scale = 1.0;
		
		if (zoom > 12) {
			scale *= (12.0/(2.0*zoom));
		}

		int width = (int) (img.getWidth() * scale);
		int height = (int) (img.getHeight() * scale);
		int x = (int) point.getX() - width / 2;
		int y = (int) point.getY() - height / 2;
		g.drawImage(img, x, y, width, height, null);
	}
}
