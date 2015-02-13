package rhnavigator.GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import rhnavigator.map.Map;
import rhnavigator.map.MapView;

public class MapGUI {
	JFrame frame;
	JSplitPane splitPane;
	JPanel buttonPanel,mapPanel,homeScreen, settingsPanel;
	JButton search,attractions, goHome, settings,homeButton;
	
	
	final int MAP_WIDTH = 700;
	final int MAP_HEIGHT = 500;
	final int FRAME_WIDTH = 1000;
	final int FRAME_HEIGHT = 500;
	
	final int HOME_WIDTH = 500;
	final int HOME_HEIGHT = HOME_WIDTH;
	
	public MapGUI(){
		GridLayout homeLayout = new GridLayout(2,2);
		frame = new JFrame();
		homeScreen = new JPanel();
		homeScreen.setLayout(homeLayout);
		frame.add(homeScreen);
		
		search = new JButton("Search");
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
				splitPane.setVisible(false);
				mainMenu();
				// Back to main
			}
		};
		homeButton.addActionListener(mainMenuListener);
		
		
		homeScreen.add(search);
		homeScreen.add(attractions);
		homeScreen.add(goHome);
		homeScreen.add(settings);
		frame.setSize(HOME_WIDTH,HOME_HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	private void mainMenu(){
//		splitPane.setVisible(false);
		homeScreen.setVisible(true);
		frame.setSize(HOME_WIDTH,HOME_HEIGHT);
		GridLayout buttonLayout = new GridLayout(8,1);
		
		
	}
	private void settingsPanel(){
		homeScreen.setVisible(false);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(8,1));
		
		settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridLayout(2,1));
		
		buttonPanel.add(homeButton);
		
		settingsPanel.add(buttonPanel);	
		
		frame.add(settingsPanel);
		frame.repaint();
		
	}
	private void mapPanel(){
		
		homeScreen.setVisible(false);
		buttonPanel = new JPanel();
		// Grid Layout for buttons
		buttonPanel.setLayout(new GridLayout(8,1));
		
		mapPanel = new JPanel();
		
		
		mapPanel.setLayout(new GridLayout(1,1));
		mapPanel.add(new MapView(Map.getSample()));
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,buttonPanel,mapPanel);
		frame.add(splitPane);
		buttonPanel.add(homeButton);
		
		frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		
	}
	private void searchPanel(){
		JLabel label1 = new JLabel("Enter Location");
		JComboBox currentLocation = new JComboBox();
		currentLocation.setEditable(true);
		splitPane.setDividerLocation((FRAME_WIDTH/4));
		
		JButton inputButton = new JButton("Search!");
		ActionListener inputListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				findOnMap();
			}
		};
		inputButton.addActionListener(inputListener);
		
		buttonPanel.add(label1);
		buttonPanel.add(currentLocation);
		buttonPanel.add(inputButton);
	}
	
	private void attractionsPanel(){
		
	}
	private void findOnMap(){
		//Find a location on the map here
	}
	
public static void main(String[] args) {
	MapGUI map = new MapGUI();	
	
	}
}
