package rhnavigator.GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.json.Input;
import org.jxmapviewer.viewer.GeoPosition;

import rhnavigator.MapPoint;
import rhnavigator.map.Map;
import rhnavigator.map.MapView;

public class MapGUI {
	//Static because only one instance of each.
	static JFrame frame;
	static JSplitPane splitPane;
	static JPanel buttonPanel,mapPanel,homeScreen, settingsPanel;
	static JButton search,attractions, goHome, settings,homeButton,route, findCityButton;
	static JComboBox currentLocation,startLocation,endLocation;
	static JCheckBox checkBox;
	static String homeLocation,loc;
	static MapView view;
	static MapPoint start,end;
	
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
		
		instantiateButtons(); // All the button instantiation code here lol		
		
		homeScreen.add(search);
		homeScreen.add(attractions);
		homeScreen.add(goHome);
		homeScreen.add(settings);
		
		frame.setSize(HOME_WIDTH,HOME_HEIGHT);
		frame.setTitle("RHNavigators!");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public MapGUI(Map map) {
		this();
		this.map = map;
	}
	
	private void mainMenu(){
//		splitPane.setVisible(false);
		homeScreen.setVisible(true);
		frame.setSize(HOME_WIDTH,HOME_HEIGHT);
		GridLayout buttonLayout = new GridLayout(8,1);
		
		
	}
	private void settingsPanel(){
		buttonPanel = null;
		homeScreen.setVisible(false);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(9,1));
		
		settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridLayout(1,2));
		
		JComboBox<MapPoint> inputBox = new JComboBox<MapPoint>();
		
		buttonPanel.add(homeButton);
		
		settingsPanel.add(buttonPanel);	
		
		frame.add(settingsPanel);
		frame.repaint();
		
	}

	private void searchPanel(){
		JLabel label1 = new JLabel("Enter Location");
		JLabel label2 = new JLabel("Start Location");
		JLabel label3 = new JLabel("Final Location");
		
		currentLocation = new JComboBox(map.toArrayList().toArray());
		startLocation = new JComboBox(map.toArrayList().toArray());
		endLocation = new JComboBox(map.toArrayList().toArray());
		
		currentLocation.setEditable(true);
		startLocation.setEditable(true);
		endLocation.setEditable(true);
		
		AutoCompleteDecorator.decorate(currentLocation);
		AutoCompleteDecorator.decorate(startLocation);
		AutoCompleteDecorator.decorate(endLocation);

		splitPane.setDividerLocation((FRAME_WIDTH/4));
		
		
		ActionListener routeListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				start = map.findByName(startLocation.getSelectedItem().toString());
				end = map.findByName(endLocation.getSelectedItem().toString());
				map.addRoute(start.getShortestDistancePath(end));
				
			}
		};
		route.addActionListener(routeListener);
		
//		JButton inputButton = new JButton()
		buttonPanel.add(label1);
		buttonPanel.add(currentLocation);
		buttonPanel.add(findCityButton);
		buttonPanel.add(label2);
		buttonPanel.add(startLocation);
		buttonPanel.add(label3);
		buttonPanel.add(endLocation);
		buttonPanel.add(route);
	}
	
	private void attractionsPanel(){
		mapPanel();
		JLabel attractionsLabel = new JLabel("Nearby Attractions:");
		JComboBox nearAttractions = new JComboBox();
		
		final JButton inputButton = new JButton("Search!");
		ActionListener inputListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				addRadioButtons(inputButton);
			}
		};
		inputButton.addActionListener(inputListener);
		
		nearAttractions.addItem("Entertainment");
		nearAttractions.addItem("Food");
		nearAttractions.addItem("Historic");
		nearAttractions.addItem("Nearby Cities");
		
		buttonPanel.add(attractionsLabel);
		buttonPanel.add(nearAttractions);
		buttonPanel.add(inputButton);
		
	}
	
	private void addRadioButtons(JButton button){
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(6,1));
		JLabel panel = new JLabel("Attractions");
		checkBox = new JCheckBox();
		buttonPanel.add(panel);
		buttonPanel.add(checkBox);
		splitPane.setLeftComponent(buttonPanel);
	}
	
	private void findOnMap(MapPoint location){
//		System.out.println(location);
		view.setAddressLocation(new GeoPosition(location.latitude,location.longitude));
		view.setZoom(6);
		//Find a location on the map here
	}
	
	private void instantiateButtons(){

		search = new JButton("Search");
//		search.setIcon(new ImageIcon("searchIcon.png"));
//		search.setBorderPainted(false);
//		search.setFocusPainted(false);
//		search.setContentAreaFilled(false);
		ActionListener searchListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				mapPanel();
				searchPanel();
				// Do stuff for search function
			}
		};
		search.addActionListener(searchListener);
		
		attractions = new JButton("Attractions");
		ActionListener attractionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
//				mapPanel();
				attractionsPanel();
				// Do stuff for finding attractions function
			}
		};
		attractions.addActionListener(attractionListener);
		
		goHome = new JButton("Go Home");
		ActionListener homeListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				mapPanel();
				// Do stuff for finding route home function
			}
		};
		goHome.addActionListener(homeListener);
		
		settings = new JButton("Settings");
		ActionListener settingsListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				settingsPanel();
				// Do stuff for settings 
			}
		};
		
		settings.addActionListener(settingsListener);
		
		homeButton = new JButton("Home");
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
				findOnMap((MapPoint)currentLocation.getSelectedItem());
			}
		};
		findCityButton.addActionListener(findCityListener);
		
		route = new JButton("Route!");
		
	}
	private void mapPanel(){
		
		homeScreen.setVisible(false);
		buttonPanel = new JPanel();
		// Grid Layout for buttons
		buttonPanel.setLayout(new GridLayout(9,1));
		
		mapPanel = new JPanel();
		
		
		mapPanel.setLayout(new GridLayout(1,1));
		view = new MapView(this.map);
		mapPanel.add(view);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,buttonPanel,mapPanel);
		frame.add(splitPane);
		buttonPanel.add(homeButton);
		
		frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		
	}

public static void main(String[] args) {
	Map map = Input.output("ipython/USCities.txt");
	// List<MapPoint> r = new ArrayList<MapPoint>();
	
//	MapPoint start = map.findByName("Terre Haute_IN");
//	MapPoint end = map.findByName("Boulder_CO");
//	map.addRoute(start.getShortestDistancePath(end));
	
	MapGUI mapGUI = new MapGUI(map);
	}
}
