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

public class SizedMapPointRenderer implements WaypointRenderer<MapPoint> {
	private static Log log = LogFactory.getLog(DefaultWaypointRenderer.class);
	private BufferedImage img = null;

	/**
	 * Uses a default waypoint image
	 */
	public SizedMapPointRenderer() {
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
		
		Point2D point = map.getTileFactory().geoToPixel(w.getPosition(),
				map.getZoom());

		double scale = ((double) w.getInterestLevel()) / 10.0;
		if (scale == 0.0) {
			scale = 1.0;
		}

		int width = (int) (img.getWidth() * scale);
		int height = (int) (img.getHeight() * scale);
		int x = (int) point.getX() - width / 2;
		int y = (int) point.getY() - height;
		g.drawImage(img, x, y, width, height, null);
	}
}
