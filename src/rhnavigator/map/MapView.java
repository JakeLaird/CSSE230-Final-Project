package rhnavigator.map;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.LocalResponseCache;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import rhnavigator.MapPoint;

/**
 * A view into a specific Map
 */

/**
 * @author Tayler
 */

public class MapView extends JXMapViewer {
	private Map map;

	public MapView(Map map) {
		this.map = map;

		// Create a TileFactoryInfo for OpenStreetMap
		TileFactoryInfo info = new OSMTileFactoryInfo();
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);

		tileFactory.setThreadPoolSize(8);

		// Setup local file cache
		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
		LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);

		this.setTileFactory(tileFactory);

		GeoPosition rose = new GeoPosition(39.483192, -87.323958);
		this.setZoom(12);
		this.setAddressLocation(rose);

		// Add interactions
		MouseInputListener mia = new PanMouseInputListener(this);
		this.addMouseListener(mia);
		this.addMouseMotionListener(mia);
		this.addMouseListener(new CenterMapListener(this));
		this.addMouseWheelListener(new ZoomMouseWheelListenerCursor(this));
		this.addKeyListener(new PanKeyListener(this));

		ArrayList<MapPoint> pointsArray = map.toArrayList();

		ArrayList<GeoPosition> geolist = new ArrayList<GeoPosition>();

		for (MapPoint p : pointsArray) {
			geolist.add(new GeoPosition(p.latitude, p.longitude));
		}

		// Create waypoints from the geo-positions
		HashSet<Waypoint> waypoints = new HashSet<Waypoint>();

		for (GeoPosition p : geolist) {
			waypoints.add(new DefaultWaypoint(p));
		}
		
		// Create a waypoint painter that takes all the waypoints
		WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
		waypointPainter.setRenderer(new PlaceWaypointRenderer());
		waypointPainter.setWaypoints(waypoints);
		// Create a compound painter that uses both the route-painter and the waypoint-painter
		List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();

		Stack<Color> colors = new Stack<Color>();
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);

		for (List<MapPoint> route : map.getRoutes()) {
			painters.add(new RoutePainter(getGeoPosition(route), colors.pop()));
		}

		painters.add(waypointPainter);
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		this.setOverlayPainter(painter);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	protected static GeoPosition getGeoPosition(MapPoint p) {
		return new GeoPosition(p.latitude, p.longitude);
	}

	protected static List<GeoPosition> getGeoPosition(List<MapPoint> points) {
		List<GeoPosition> returnList = new ArrayList<GeoPosition>();
		for (MapPoint p : points) {
			returnList.add(getGeoPosition(p));
		}
		return returnList;
	}
}
