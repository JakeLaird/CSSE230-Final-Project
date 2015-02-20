package rhnavigator.GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.json.Input;
import org.jxmapviewer.viewer.GeoPosition;

import rhnavigator.CurrentLocation;
import rhnavigator.MapLandmark;
import rhnavigator.MapPoint;
import rhnavigator.map.Map;
import rhnavigator.map.MapView;

public class MapGUI {
	//Static because only one instance of each.
	static JFrame frame;
	static JSplitPane splitPane;
	static JPanel buttonPanel,mapPanel,homeScreen, settingsPanel;
	static JButton search,attractions, tripPlanner, settings,homeButton,route, findCityButton;
	static JComboBox<MapPoint> searchLocation,startLocation,endLocation, nearbyAttractions;
	static JComboBox<String> routeChoice;
//	static JCheckBox checkBox;
	static String loc;
	static MapView view;
	static MapPoint start,end,currentLocation,homeLocation;
	static List<MapPoint> pointList;
	final static int MAP_WIDTH = 700;
	final static int MAP_HEIGHT = 500;
	final static int FRAME_WIDTH = 1000;
	final static int FRAME_HEIGHT = 500;
	
	final int HOME_WIDTH = 500;
	final int HOME_HEIGHT = HOME_WIDTH;
	private Map map;
	
	public MapGUI(){
		GridLayout homeLayout = new GridLayout(2,2);
		frame = new JFrame();
		homeScreen = new JPanel();
		homeScreen.setLayout(homeLayout);
		frame.add(homeScreen);
		
		instantiateButtons(); // All the button instantiation code here	
		
		homeScreen.add(search);
		homeScreen.add(attractions);
		homeScreen.add(tripPlanner);
		homeScreen.add(settings);
		
		frame.setSize(HOME_WIDTH,HOME_HEIGHT);
		frame.setTitle("RHNavigators!");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public MapGUI(Map map) {
		this();
		this.map = map;
		pointList = map.getCities();
	}
	
	private void mainMenu(){
		homeScreen.setVisible(true);
		frame.setSize(HOME_WIDTH,HOME_HEIGHT);
	}
	private void settingsPanel(){
		buttonPanel = null;
		homeScreen.setVisible(false);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(9,1));
		
		settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridLayout(1,2));
				
		buttonPanel.add(homeButton);
		
		settingsPanel.add(buttonPanel);	
		
		frame.add(settingsPanel);
		frame.repaint();
		
	}

	private void searchPanel(){
		buttonPanel.setLayout(new GridLayout(10,1));
		
//		JPanel panel1 = new JPanel();
//		panel1.setLayout(new GridLayout(1,2));
		
		JLabel label1 = new JLabel("Enter Location");
		JLabel label2 = new JLabel("Start Location");
		JLabel label3 = new JLabel("Final Location");
		
		searchLocation = new JComboBox<MapPoint>(new DefaultComboBoxModel<MapPoint>(pointList.toArray(new MapPoint[pointList.size()])));
		startLocation = new JComboBox<MapPoint>(new DefaultComboBoxModel<MapPoint>(pointList.toArray(new MapPoint[pointList.size()])));
		endLocation = new JComboBox<MapPoint>(new DefaultComboBoxModel<MapPoint>(pointList.toArray(new MapPoint[pointList.size()])));
		
		searchLocation.setEditable(true);
		startLocation.setEditable(true);
		endLocation.setEditable(true);
		
		AutoCompleteDecorator.decorate(searchLocation);
		AutoCompleteDecorator.decorate(startLocation);
		AutoCompleteDecorator.decorate(endLocation);

		splitPane.setDividerLocation((FRAME_WIDTH/4));
		
		
		ActionListener routeListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				start = map.findByName(startLocation.getSelectedItem().toString());
				end = map.findByName(endLocation.getSelectedItem().toString());
				if(start.equals(end)) JOptionPane.showMessageDialog( frame, "Cities are the same!");
				
					else{
				if(routeChoice.getSelectedItem().toString() == "Shortest Distance")view.setRoute(start.getShortestDistancePath(end));
				else view.setRoute(start.getShortestTimePath(end));
				}
			}
		};
		route.addActionListener(routeListener);
		
//		panel1.add(searchLocation);
//		panel1.add(findCityButton);
		
		buttonPanel.add(label1);
		buttonPanel.add(searchLocation);
		buttonPanel.add(findCityButton);
//		buttonPanel.add(panel1);

		buttonPanel.add(label2);
		buttonPanel.add(startLocation);
		buttonPanel.add(label3);
		buttonPanel.add(endLocation);
		buttonPanel.add(routeChoice);
		buttonPanel.add(route);
	}
	
	private void attractionsPanel(){
		try {
			currentLocation = CurrentLocation.getMapPoint(map);
		} catch (Exception e) {
			System.err.println("Unable to get current location.");			
		}
		
		if(currentLocation == null){
			currentLocation = (MapPoint) JOptionPane.showInputDialog(frame, 
			        "Please input your current location",
			        "Error",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			      pointList.toArray(new MapPoint[pointList.size()]), 
			        pointList.get(0));
			if(currentLocation!=null)attractionsPanel();
		}
		else {
		if(currentLocation == null) return;	
		mapPanel();
		view.setZoom(8);
		view.setAddressLocation(new GeoPosition(currentLocation.latitude,currentLocation.longitude));
		JLabel attractionsLabel = new JLabel("Nearby Attractions:");
		List<MapLandmark> attractionList = map.getNearest(currentLocation, 25);
		nearbyAttractions = new JComboBox<MapPoint>(attractionList.toArray(new MapPoint[attractionList.size()]));
		
		final JButton nearAttractionButton = new JButton("Go to Attraction!");
		ActionListener inputListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				MapPoint point = (MapPoint)nearbyAttractions.getSelectedItem();
				view.setAddressLocation(new GeoPosition(point.latitude,point.longitude));
				view.setZoom(4);
				// do stuff
			}
		};
		nearAttractionButton.addActionListener(inputListener);
	
		buttonPanel.add(attractionsLabel);
		buttonPanel.add(nearbyAttractions);
		buttonPanel.add(nearAttractionButton);
		}		
	}
	
	private void findOnMap(MapPoint location) {
		view.fitScreenToRouteAndPoint(location.getPosition());
	}
	
	private void instantiateButtons(){
		search = new JButton();
		ImageIcon searchIcon = new ImageIcon(MapGUI.class.getResource("/images/searchIcon.png"));
		search.setIcon(searchIcon);
		ActionListener searchListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				mapPanel();
				searchPanel();
				// Do stuff for search function
			}
		};
		search.addActionListener(searchListener);
		
		attractions = new JButton("Nearby Attractions");
		ActionListener attractionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
//				mapPanel();
				attractionsPanel();
				// Do stuff for finding attractions function
			}
		};
		attractions.addActionListener(attractionListener);
		
		
		tripPlanner = new JButton();
		ImageIcon tripIcon = new ImageIcon(MapGUI.class.getResource("/images/car.png"));
		tripPlanner.setIcon(tripIcon);
		ActionListener homeListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				mapPanel();
				// Do stuff for finding route home function
			}
		};
		tripPlanner.addActionListener(homeListener);
		
		settings = new JButton("Settings");
		ActionListener settingsListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				settingsPanel();
				// Do stuff for settings 
			}
		};
		
		settings.addActionListener(settingsListener);
		
		homeButton = new JButton("Menu");
		ActionListener mainMenuListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(settingsPanel!=null)settingsPanel.setVisible(false);
				if(splitPane!=null)splitPane.setVisible(false);
				mainMenu();
				// Back to main
			}
		};
		homeButton.addActionListener(mainMenuListener);
		
		findCityButton = new JButton("Search!");
		ActionListener findCityListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				findOnMap((MapPoint)searchLocation.getSelectedItem());
			}
		};
		findCityButton.addActionListener(findCityListener);
		
		route = new JButton("Route!");
		String[] choices = new String[]{"Shortest Distance","Shortest Time"};
		routeChoice = new JComboBox<String>(choices);
		
	}
	private void mapPanel(){
		
		homeScreen.setVisible(false);
		buttonPanel = new JPanel();
		// Grid Layout for buttons
		buttonPanel.setLayout(new GridLayout(9,1));
		
		mapPanel = new JPanel();
		
		
		mapPanel.setLayout(new GridLayout(1,1));
		view = new MapView(this.map);
		view.setZoom(14);
		mapPanel.add(view);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,buttonPanel,mapPanel);
		frame.add(splitPane);
		buttonPanel.add(homeButton);
		
		frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		
	}

public static void main(String[] args) {
	Map map = Input.output("ipython/USCities.txt");
	
	MapGUI mapGUI = new MapGUI(map);
	}
}
