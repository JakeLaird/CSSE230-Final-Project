package rhnavigator.map;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.AbstractPainter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.LocalResponseCache;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import rhnavigator.MapPoint;
import rhnavigator.MapPoint.NeighboringPoint;
import rhnavigator.map.RouteMapPointRenderer.PointType;

/**
 * A view into a specific Map
 */

/**
 * @author Tayler
 */

public class MapView extends JXMapViewer {
	private Map map;
	private CompoundPainter<JXMapViewer> compPainter;
	private List<Painter<JXMapViewer>> defaultPainters;

	public MapView(Map map) {
		this.map = map;

		setupTileSource();		
		setupInteractionHandlers();

		GeoPosition rose = new GeoPosition(39.483192, -87.323958);
		this.setZoom(12);
		this.setAddressLocation(rose);

		defaultPainters = new ArrayList<Painter<JXMapViewer>>();

		// Paints lines to show all connectections between neighbors
//		defaultPainters.addAll(getNeighborPainters());
		
		// Create a waypoint painter that takes all the waypoints
		

		// If there are no routes, draw all places as waypoints.
		if (map.getRoutes().isEmpty()) {
			WaypointPainter<MapPoint> waypointPainter = new WaypointPainter<MapPoint>();

			waypointPainter.setWaypoints(new HashSet<MapPoint>(map.toArrayList()));
			waypointPainter.setRenderer(new SizedMapPointRenderer());

			defaultPainters.add(waypointPainter);
		}

		compPainter = new CompoundPainter<JXMapViewer>(defaultPainters);
		this.setOverlayPainter(compPainter);
	}
	
	public void addRoute(List<MapPoint> newRoute) {
		if (newRoute == null) {
			throw new IllegalArgumentException();
		}
		
		if (map.getRoutes().isEmpty()) {
			for (Painter<JXMapViewer> p : defaultPainters) {
				if (p instanceof AbstractPainter<?>) {
					((AbstractPainter<?>)p).setVisible(false);
				}
			}
		}
		
		map.addRoute(newRoute);
		fitScreenToRoute(map.getRoutes());
		List<Painter<JXMapViewer>> painters = getRoutePainters(newRoute); 
		for (Painter<JXMapViewer> p : painters) {
			compPainter.addPainter(p);
		}
	}


	private void fitScreenToRoute(List<List<MapPoint>> routes) {
		Set<GeoPosition> points = new HashSet<GeoPosition>();
		for (List<MapPoint> route : routes) {
			for (MapPoint p : route) {
				points.add(p.getPosition());
			}
		}
		this.zoomToBestFit(points, 0.8);
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

	private void setupInteractionHandlers() {
		// Add interactions
		MouseInputListener mia = new PanMouseInputListener(this);
		this.addMouseListener(mia);
		this.addMouseMotionListener(mia);
		this.addMouseListener(new CenterMapListener(this));
		this.addMouseWheelListener(new ZoomMouseWheelListenerCursor(this));
		this.addKeyListener(new PanKeyListener(this));
	}

	private void setupTileSource() {
		// Create a TileFactoryInfo for OpenStreetMap
		TileFactoryInfo info = new OSMTileFactoryInfo();
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);

		tileFactory.setThreadPoolSize(8);

		// Setup local file cache
		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
		LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);

		this.setTileFactory(tileFactory);
	}

	private List<Painter<JXMapViewer>> getNeighborPainters() {
		List<Painter<JXMapViewer>> painterList = new ArrayList<Painter<JXMapViewer>>();
		double maxCost = 15.0;
		 for (MapPoint p : map.toArrayList()) {
			for (NeighboringPoint n : p.getNeighborsWithCosts()) {
				List<MapPoint> temp = new ArrayList<MapPoint>();
				temp.add(p);
				temp.add(n.point);
				
				double hue = (1.0-(Math.sqrt(((double)n.getCost()))/maxCost))*(1.0/2.0);
				
				painterList.add(new RoutePainter(getGeoPosition(temp), new Color(Color.HSBtoRGB((float)hue, 1.0f, 1.0f))));
			}
		}
		return painterList;
	}

	private List<Painter<JXMapViewer>> getRoutePainters(List<MapPoint> route) {
		List<Painter<JXMapViewer>> painterList = new ArrayList<Painter<JXMapViewer>>();

		Stack<Color> colors = new Stack<Color>();
		colors.add(Color.RED);
		colors.add(Color.BLUE);
//		colors.add(Color.GREEN);

		WaypointPainter<MapPoint> routeEndPainter = new WaypointPainter<MapPoint>();
		routeEndPainter.setRenderer(new RouteMapPointRenderer(PointType.END));
		Set<MapPoint> routeEndPoints = new HashSet<MapPoint>();

		WaypointPainter<MapPoint> routeStartPainter = new WaypointPainter<MapPoint>();
		routeStartPainter.setRenderer(new RouteMapPointRenderer(PointType.START));
		Set<MapPoint> routeStartPoints = new HashSet<MapPoint>();
		
		WaypointPainter<MapPoint> routePointsPainter = new WaypointPainter<MapPoint>();
		routePointsPainter.setRenderer(new SimpleMapPointRenderer());
		HashSet<MapPoint> routePoints = new HashSet<MapPoint>();

		route = new ArrayList<MapPoint>(route);
		Color c = colors.pop();
		painterList.add(new RoutePainter(getGeoPosition(route), new Color(c.getRed(), c.getGreen(), c.getBlue(), 200)));

		routeEndPoints.add(route.remove(route.size()-1));
		routeStartPoints.add(route.remove(0));
		routePoints.addAll(route);
		
		routeEndPainter.setWaypoints(routeEndPoints);
		routeStartPainter.setWaypoints(routeStartPoints);
		routePointsPainter.setWaypoints(routePoints);
		
		painterList.add(routePointsPainter);
		painterList.add(routeEndPainter);
		painterList.add(routeStartPainter);
		return painterList;
	}
}
