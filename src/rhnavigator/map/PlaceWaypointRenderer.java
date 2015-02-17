package rhnavigator.map;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointRenderer;

public class PlaceWaypointRenderer implements WaypointRenderer<Waypoint> {
	private static Log log = LogFactory.getLog(DefaultWaypointRenderer.class);
	private BufferedImage img = null;
	/**
	* Uses a default waypoint image
	*/
	public PlaceWaypointRenderer()
	{
		try
		{
			img = ImageIO.read(DefaultWaypointRenderer.class.getResource("/images/simple_waypoint.png"));
		}
		catch (Exception ex)
		{
		log.warn("couldn't read standard_waypoint.png", ex);
		}
	}
	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint w)
	{
		if (img == null)
			return;
		Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
		int x = (int)point.getX() -img.getWidth() / 2;
		int y = (int)point.getY() -img.getHeight() / 2;
		g.drawImage(img, x, y, null);
	}
}
