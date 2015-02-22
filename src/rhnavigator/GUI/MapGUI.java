package rhnavigator.GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.json.Input;
import org.jxmapviewer.viewer.GeoPosition;

import rhnavigator.CurrentLocation;
import rhnavigator.MapLandmark;
import rhnavigator.MapPoint;
import rhnavigator.map.Map;
import rhnavigator.map.MapView;

public class MapGUI {
	// Static because only one instance of each.
	static JFrame frame;
	static JSplitPane splitPane;
	static JPanel buttonPanel, mapPanel, homeScreen, settingsPanel;
	static JButton search, attractions, tripPlanner, settings, homeButton,
			route, clear_route, findCityButton, trip_plan;
	static JComboBox<MapPoint> searchLocation, startLocation, endLocation,
			nearbyAttractions, trip_location;
	static JComboBox<String> routeChoice;
	// static JCheckBox checkBox;
	static String loc;
	static MapView view;
	static MapPoint start, end, currentLocation, homeLocation;
	static List<MapPoint> pointList;
	final static int MAP_WIDTH = 700;
	final static int MAP_HEIGHT = 500;
	final static int FRAME_WIDTH = 1000;
	final static int FRAME_HEIGHT = 500;
	
	final static int MAP_FRAME_WIDTH = 1100;
	final static int MAP_FRAME_HEIGHT = 600;
	private static final String SpringUtilities = null;

	final int HOME_WIDTH = 500;
	final int HOME_HEIGHT = HOME_WIDTH;
	private Map map;
	private boolean ischanged;

	public MapGUI() {
		GridLayout homeLayout = new GridLayout(2, 2);
		frame = new JFrame();
		homeScreen = new JPanel();
		homeScreen.setLayout(homeLayout);
		frame.add(homeScreen);

		instantiateButtons(); // All the button instantiation code here

		homeScreen.add(search);
		homeScreen.add(attractions);
		homeScreen.add(tripPlanner);
		homeScreen.add(settings);

		frame.setSize(HOME_WIDTH, HOME_HEIGHT);
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

	private void mainMenu() {
		homeScreen.setVisible(true);
		frame.setSize(HOME_WIDTH, HOME_HEIGHT);
	}

	private void settingsPanel() {
		buttonPanel = null;
		homeScreen.setVisible(false);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(9, 1));

		settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridLayout(1, 2));

		buttonPanel.add(homeButton);

		settingsPanel.add(buttonPanel);

		frame.add(settingsPanel);
		frame.repaint();

	}

	private void searchPanel() {
		SpringLayout layout = new SpringLayout();
		buttonPanel.setLayout(layout);
		
		// JPanel panel1 = new JPanel();
		// panel1.setLayout(new GridLayout(1,2));

		JLabel label1 = new JLabel("Enter Location");
		JLabel label2 = new JLabel("Start Location");
		JLabel label3 = new JLabel("Final Location");
		JLabel label4 = new JLabel("Prefer: ");
		
		List<MapPoint> cities = new ArrayList<MapPoint>(map.getCities());
		Collections.sort(cities);
		Collections.reverse(cities);
		
		Object[][] data = new Object[cities.size()][2];
		
		for (int i = 0; i < cities.size(); i++) {
			MapPoint city = cities.get(i);
			data[i][0] = city.getName();
			data[i][1] = city.getInterestLevel();
		}
		
		JTable cityTable = new JTable(data, new String[] {"City", "Interest"});
		cityTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		cityTable.getColumnModel().getColumn(1).setPreferredWidth(30);
		
		JScrollPane cityScrollPane = new JScrollPane(cityTable);
		
		JSeparator searchSeparator = new JSeparator(SwingConstants.HORIZONTAL);
		searchSeparator.setPreferredSize(new Dimension(200,3));

		searchLocation = new JComboBox<MapPoint>(
				new DefaultComboBoxModel<MapPoint>(
						pointList.toArray(new MapPoint[pointList.size()])));
		startLocation = new JComboBox<MapPoint>(
				new DefaultComboBoxModel<MapPoint>(
						pointList.toArray(new MapPoint[pointList.size()])));
		endLocation = new JComboBox<MapPoint>(
				new DefaultComboBoxModel<MapPoint>(
						pointList.toArray(new MapPoint[pointList.size()])));

		searchLocation.setEditable(true);
		startLocation.setEditable(true);
		endLocation.setEditable(true);

		AutoCompleteDecorator.decorate(searchLocation);
		AutoCompleteDecorator.decorate(startLocation);
		AutoCompleteDecorator.decorate(endLocation);
		
		frame.setSize(MAP_FRAME_WIDTH, MAP_FRAME_HEIGHT);

		splitPane.setDividerLocation((MAP_FRAME_WIDTH / 4));
		splitPane.setEnabled(false);

		ActionListener routeListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start = map.findByName(startLocation.getSelectedItem()
						.toString());
				end = map.findByName(endLocation.getSelectedItem().toString());
				if (start.equals(end))
					JOptionPane
							.showMessageDialog(frame, "Cities are the same!");

				else {
					if (routeChoice.getSelectedItem().toString() == "Shortest Distance")
						view.setRoute(start.getShortestDistancePath(end));
					else
						view.setRoute(start.getShortestTimePath(end));
				}
			}
		};
		route.addActionListener(routeListener);
		
		ActionListener clearRouteListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.clearRoutes();
			}
		};
		clear_route.addActionListener(clearRouteListener);
		
		layout.putConstraint(SpringLayout.WEST, label1, 1, SpringLayout.WEST, buttonPanel);
		layout.putConstraint(SpringLayout.NORTH, homeButton, 5, SpringLayout.NORTH, buttonPanel);

		layout.putConstraint(SpringLayout.NORTH, label1, 5, SpringLayout.SOUTH, homeButton);
		layout.putConstraint(SpringLayout.NORTH, searchLocation, 5, SpringLayout.SOUTH, label1);
		layout.putConstraint(SpringLayout.EAST, searchLocation, -5, SpringLayout.EAST, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, searchLocation, 5, SpringLayout.WEST, buttonPanel);
		
		layout.putConstraint(SpringLayout.NORTH, findCityButton, 5, SpringLayout.SOUTH,	searchLocation);
		layout.putConstraint(SpringLayout.WEST, findCityButton, 5, SpringLayout.WEST, buttonPanel);
		
		layout.putConstraint(SpringLayout.NORTH, searchSeparator, 5, SpringLayout.SOUTH, findCityButton);
		layout.putConstraint(SpringLayout.EAST, searchSeparator, -10, SpringLayout.EAST, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, searchSeparator, 10, SpringLayout.WEST, buttonPanel);

		layout.putConstraint(SpringLayout.NORTH, label2, 5, SpringLayout.SOUTH, searchSeparator);
		layout.putConstraint(SpringLayout.WEST, label2, 1, SpringLayout.WEST, buttonPanel);
		
		layout.putConstraint(SpringLayout.NORTH, startLocation, 5, SpringLayout.SOUTH, label2);
		layout.putConstraint(SpringLayout.EAST, startLocation, -5, SpringLayout.EAST, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, startLocation, 5, SpringLayout.WEST, buttonPanel);
		
		layout.putConstraint(SpringLayout.NORTH, label3, 5, SpringLayout.SOUTH, startLocation);
		layout.putConstraint(SpringLayout.WEST, label3, 1, SpringLayout.WEST, buttonPanel);
		
		layout.putConstraint(SpringLayout.NORTH, endLocation, 5, SpringLayout.SOUTH, label3);
		layout.putConstraint(SpringLayout.EAST, endLocation, -5, SpringLayout.EAST, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, endLocation, 5, SpringLayout.WEST, buttonPanel);
		
		layout.putConstraint(SpringLayout.NORTH, routeChoice, 5, SpringLayout.SOUTH, endLocation);
		layout.putConstraint(SpringLayout.EAST, routeChoice, -5, SpringLayout.EAST, buttonPanel);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, label4, 0, SpringLayout.VERTICAL_CENTER, routeChoice);
		layout.putConstraint(SpringLayout.WEST, label4, 5, SpringLayout.WEST, buttonPanel);
		
		layout.putConstraint(SpringLayout.NORTH, route, 10, SpringLayout.SOUTH, routeChoice);
		layout.putConstraint(SpringLayout.WEST, route, 5, SpringLayout.WEST, buttonPanel);
		layout.putConstraint(SpringLayout.EAST, route, -5, SpringLayout.HORIZONTAL_CENTER, buttonPanel);
		
		layout.putConstraint(SpringLayout.NORTH, clear_route, 10, SpringLayout.SOUTH, routeChoice);
		layout.putConstraint(SpringLayout.EAST, clear_route, -5, SpringLayout.EAST, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, clear_route, 5, SpringLayout.HORIZONTAL_CENTER, buttonPanel);
		
		layout.putConstraint(SpringLayout.NORTH, cityScrollPane, 5, SpringLayout.SOUTH, clear_route);
		layout.putConstraint(SpringLayout.EAST, cityScrollPane, -5, SpringLayout.EAST, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, cityScrollPane, 5, SpringLayout.WEST, buttonPanel);
		layout.putConstraint(SpringLayout.SOUTH, cityScrollPane, -5, SpringLayout.SOUTH, buttonPanel);

		buttonPanel.add(label1);
		buttonPanel.add(searchLocation);
		buttonPanel.add(findCityButton);
		buttonPanel.add(searchSeparator);

		buttonPanel.add(label2);
		buttonPanel.add(startLocation);
		buttonPanel.add(label3);
		buttonPanel.add(endLocation);
		buttonPanel.add(routeChoice);
		buttonPanel.add(label4);
		buttonPanel.add(route);
		buttonPanel.add(clear_route);
		buttonPanel.add(cityScrollPane);
	}

	private void attractionsPanel() {
		try {
			currentLocation = CurrentLocation.getMapPoint(map);
		} catch (Exception e) {
			System.err.println("Unable to get current location.");
		}
		
		if (currentLocation == null) {
			currentLocation = (MapPoint) JOptionPane.showInputDialog(frame,
					"Please input your current location", "Error",
					JOptionPane.QUESTION_MESSAGE, null,
					pointList.toArray(new MapPoint[pointList.size()]),
					pointList.get(0));
			if (currentLocation != null)
				attractionsPanel();

		} else {
			if (currentLocation == null)
				return;
			
			mapPanel();
			view.setZoom(8);
			view.setAddressLocation(new GeoPosition(currentLocation.latitude,
					currentLocation.longitude));
			JLabel attractionsLabel = new JLabel("Nearby Attractions:");
			List<MapLandmark> attractionList = map.getNearest(currentLocation,
					25);
			nearbyAttractions = new JComboBox<MapPoint>(
					attractionList.toArray(new MapPoint[attractionList.size()]));

			final JButton nearAttractionButton = new JButton(
					"Go to Attraction!");
			ActionListener inputListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MapPoint point = (MapPoint) nearbyAttractions
							.getSelectedItem();
					view.setAddressLocation(new GeoPosition(point.latitude,
							point.longitude));
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
			public void actionPerformed(ActionEvent e) {
				mapPanel();
				searchPanel();
				// Do stuff for search function
			}
		};
		search.addActionListener(searchListener);

		attractions = new JButton();
		ImageIcon attractionsIcon = new ImageIcon(MapGUI.class.getResource("/images/entertainment.png"));
		attractions.setIcon(attractionsIcon);
		ActionListener attractionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// mapPanel();
				attractionsPanel();
				// Do stuff for finding attractions function
			}
		};
		attractions.addActionListener(attractionListener);
		
		tripPlanner = new JButton();
		ImageIcon tripIcon = new ImageIcon(MapGUI.class.getResource("/images/car.png"));
		tripPlanner.setIcon(tripIcon);


		
		ActionListener homeListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mapPanel();
				tripPlannerPanel();
				// Do stuff for finding route home function
			}
		};
		tripPlanner.addActionListener(homeListener);

		settings = new JButton();
		ImageIcon settingsIcon = new ImageIcon(MapGUI.class.getResource("/images/settings.png"));
		settings.setIcon(settingsIcon);
		ActionListener settingsListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsPanel();
				// Do stuff for settings
			}
		};

		settings.addActionListener(settingsListener);

		homeButton = new JButton("Menu");
		ActionListener mainMenuListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (settingsPanel != null)
					settingsPanel.setVisible(false);
				if (splitPane != null)
					splitPane.setVisible(false);
				mainMenu();
				// Back to main
			}
		};
		homeButton.addActionListener(mainMenuListener);

		findCityButton = new JButton("Search!");
		ActionListener findCityListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findOnMap((MapPoint) searchLocation.getSelectedItem());
			}
		};
		findCityButton.addActionListener(findCityListener);

		trip_plan = new JButton("route");
		
		
		route = new JButton("Route!");
		String[] choices = new String[] { "Shortest Distance", "Shortest Time" };
		routeChoice = new JComboBox<String>(choices);
		
		clear_route = new JButton("Clear");

	}

	protected void tripPlannerPanel() {
		ischanged = false;
		List<MapPoint> pointList = map.getCities();
		startLocation = new JComboBox<MapPoint>(
				new DefaultComboBoxModel<MapPoint>(
						pointList.toArray(new MapPoint[pointList.size()])));
		startLocation.setEditable(true);
		AutoCompleteDecorator.decorate(startLocation);



		JLabel label1 = new JLabel("Select a city");
		JLabel label2 = new JLabel("Miles:");

		final JSlider slider = new JSlider(JSlider.HORIZONTAL, 50, 200, 140);


		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		JButton get_desti = new JButton("Get destinations");

		ActionListener getlistener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!ischanged) {
					start = map.findByName(startLocation.getSelectedItem()
							.toString());
					List<MapPoint> triplist = map.TripPlan(start.latitude,
							start.longitude, slider.getValue());
					if (triplist.size() == 0)
						JOptionPane.showMessageDialog(frame,
								"No destinations found. Enter a larger distance");

					trip_location = new JComboBox<MapPoint>(
							new DefaultComboBoxModel<MapPoint>(
									triplist.toArray(new MapPoint[triplist
											.size()])));
					trip_location.setEditable(true);
					AutoCompleteDecorator.decorate(trip_location);
					buttonPanel.add(trip_location);
					trip_plan = new JButton("route");
					ActionListener sec_routeListener = new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							start = map.findByName(startLocation
									.getSelectedItem().toString());
							end = map.findByName(trip_location
									.getSelectedItem().toString());
							if (start.equals(end))
								JOptionPane.showMessageDialog(frame,
										"Cities are the same!");

							else {
								if (routeChoice.getSelectedItem().toString() == "Shortest Distance")
									view.setRoute(start
											.getShortestDistancePath(end));
								else
									view.setRoute(start
											.getShortestTimePath(end));
							}
						}
					};
					trip_plan.addActionListener(sec_routeListener);
					buttonPanel.add(trip_plan);
					buttonPanel.setVisible(false);
					buttonPanel.setVisible(true);
					
					ischanged = true;
				}

			}
		};
		get_desti.addActionListener(getlistener);

		// trip_plan = new JButton("trip");
		// List<MapPoint> triplist = map.TripPlan();
		// trip_location = new JComboBox<MapPoint>(
		// new DefaultComboBoxModel<MapPoint>(
		// triplist.toArray(new MapPoint[triplist.size()])));
		//
		// ActionListener tripListener = new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// start = map.findByName(startLocation.getSelectedItem()
		// .toString());
		//
		// end = map
		// .findByName(trip_location.getSelectedItem().toString());
		// view.setRoute(start.getShortestDistancePath(end));
		// view.addRoute(start.getShortestTimePath(end));
		// }
		// };

		buttonPanel.add(label1);
		buttonPanel.add(startLocation);
		buttonPanel.add(label2);
		buttonPanel.add(slider);
		buttonPanel.add(get_desti);

	}

	private void mapPanel() {

		homeScreen.setVisible(false);
		buttonPanel = new JPanel();
		// Grid Layout for buttons
		buttonPanel.setLayout(new GridLayout(9, 1));

		mapPanel = new JPanel();

		mapPanel.setLayout(new GridLayout(1, 1));
		view = new MapView(this.map);
		view.setZoom(14);
		mapPanel.add(view);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonPanel,
				mapPanel);
		frame.add(splitPane);
		buttonPanel.add(homeButton);

		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

	}

	public static void main(String[] args) {
		Map map = Input.output("ipython/USCities.txt");

		MapGUI mapGUI = new MapGUI(map);
	}
}
