package rhnavigator.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.LocalResponseCache;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
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
	private List<Painter<JXMapViewer>> routePainters;
	private WaypointPainter<Waypoint> pinPainter;

	public MapView(Map map) {
		this.map = map;

		setupTileSource();		
		setupInteractionHandlers();

		GeoPosition rose = new GeoPosition(39.483192, -87.323958);
		this.setZoom(12);
		this.setAddressLocation(rose);

		defaultPainters = new ArrayList<Painter<JXMapViewer>>();
		routePainters = new ArrayList<Painter<JXMapViewer>>();

		// Paints lines to show all connectections between neighbors
		defaultPainters.addAll(getNeighborPainters());
		
		map.clearRoutes();
		// By default, draw all places as waypoints.
		WaypointPainter<MapPoint> waypointPainter = new WaypointPainter<MapPoint>();

		waypointPainter.setWaypoints(new HashSet<MapPoint>(map.toArrayList()));
		waypointPainter.setRenderer(new SizedMapPointRenderer());		

		defaultPainters.add(waypointPainter);

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
			routePainters.add(p);
		}
	}
	
	public void setRoute(List<MapPoint> newRoute) {		
		List<List<MapPoint>> newRoutes = new ArrayList<List<MapPoint>>();
		newRoutes.add(newRoute);
		setRoutes(newRoutes);
	}
	
	public void setRoutes(List<List<MapPoint>> newRoutes) {
		if (map.getRoutes().isEmpty()) {
			for (Painter<JXMapViewer> p : defaultPainters) {
				if (p instanceof AbstractPainter<?>) {
					((AbstractPainter<?>)p).setVisible(false);
				}
			}
		} else {
			for (Painter<JXMapViewer> p : routePainters) {
				compPainter.removePainter(p);
			}
			routePainters.clear();
			map.clearRoutes();
		}
		
		for (List<MapPoint> route : newRoutes) {
			map.addRoute(route);
			List<Painter<JXMapViewer>> painters = getRoutePainters(route); 
			for (Painter<JXMapViewer> p : painters) {
				compPainter.addPainter(p);
				routePainters.add(p);
			}
		}

		fitScreenToRoute(newRoutes);
	}
	
	public void clearRoutes() {
		if (!map.getRoutes().isEmpty()) {
			for (Painter<JXMapViewer> p : defaultPainters) {
				if (p instanceof AbstractPainter<?>) {
					((AbstractPainter<?>)p).setVisible(true);
				}
			}
		}
		
		map.clearRoutes();
		
		for (Painter<JXMapViewer> p : routePainters) {
			compPainter.removePainter(p);
		}
		
		routePainters.clear();
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
	
	public void fitScreenToRouteAndPoint(GeoPosition p) {
		Set<GeoPosition> points = new HashSet<GeoPosition>();
		points.add(p);
		for (List<MapPoint> route : map.getRoutes()) {
			for (MapPoint point : route) {
				points.add(point.getPosition());
			}
		}
		setAddressLocation(p);
		
		if (pinPainter != null) {
			compPainter.removePainter(pinPainter);
		}
		
		pinPainter = new WaypointPainter<Waypoint>();
		pinPainter.setRenderer(new PinMapPointRenderer());
		Set<Waypoint> centerPoints = new HashSet<Waypoint>();
		centerPoints.add(new DefaultWaypoint(p));
		pinPainter.setWaypoints(centerPoints);
		compPainter.addPainter(pinPainter);
		
		if (map.getRoutes().isEmpty()) {
			setZoom(6);
			return;
		}
		
		setZoom(1);
		int zoom = getZoom();
		Rectangle2D rect = generateBoundingRect(points, zoom);
		// Rectangle2D viewport = map.getViewportBounds();
		int count = 0;
		while (!getViewportBounds().contains(rect)) {
			count++;
			if (count > 30) {
				break;
			}
			if (getViewportBounds().contains(rect)) {
				System.err.println("did it finally");
				break;
			}
			zoom += 1;
			
			if (zoom > 20)
			{
				System.err.println("zoom failed");
				break;
			}
			setZoom(zoom);
			rect = generateBoundingRect(points, zoom);
		}
	}
	
	private Rectangle2D generateBoundingRect(final Set<GeoPosition> positions, int zoom)
	{
		Point2D point1 = getTileFactory().geoToPixel(positions.iterator().next(), zoom);
		Rectangle2D rect = new Rectangle2D.Double(point1.getX(), point1.getY(), 0, 0);
		for (GeoPosition pos : positions) {
			Point2D point = getTileFactory().geoToPixel(pos, zoom);
			rect.add(point);
		}
		return rect;
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
				
				double hue = (1.0-(Math.sqrt(((double)n.getDistanceCost()))/maxCost))*(1.0/2.0);
				
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
