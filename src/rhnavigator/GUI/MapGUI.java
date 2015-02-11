package rhnavigator.GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
public class MapGUI {
	JFrame frame;
	JSplitPane splitPane;
	JPanel buttonPanel,mapPanel,homeScreen;
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
				mapPanel();
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
				mapPanel();
				// Do stuff for search function
			}
		};
		settings.addActionListener(settingsListener);
		
		
		homeScreen.add(search);
		homeScreen.add(attractions);
		homeScreen.add(goHome);
		homeScreen.add(settings);
		frame.setSize(HOME_WIDTH,HOME_HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	private void mainMenu(){
		splitPane.setVisible(false);
		homeScreen.setVisible(true);
		frame.setSize(HOME_WIDTH,HOME_HEIGHT);
	}
	
	private void mapPanel(){
		homeScreen.setVisible(false);
		buttonPanel = new JPanel();
		GridLayout buttonLayout = new GridLayout(8,1); // Grid Layout for buttons
		buttonPanel.setLayout(buttonLayout);
		
		mapPanel = new JPanel();
		mapPanel.setBackground(Color.black);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,buttonPanel,mapPanel);
		frame.add(splitPane);
		
		homeButton = new JButton("Home");
		ActionListener homeListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
//				splitPane.setVisible(false);
				mainMenu();
				// Back to main
			}
		};
		homeButton.addActionListener(homeListener);
		buttonPanel.add(homeButton);
		
		frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		splitPane.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		frame.repaint();
		
	}
	private void searchPanel(){
		
		JLabel label1 = new JLabel("Enter Current Location");
		JTextField currentLocation = new JTextField("");
		JLabel label2 = new JLabel("Enter destination");
		JTextField endLocation = new JTextField("");
		splitPane.setDividerLocation((homeButton.WIDTH));
		buttonPanel.add(label1);
		buttonPanel.add(currentLocation);
		buttonPanel.add(label2);
		buttonPanel.add(endLocation);
	}
	
public static void main(String[] args) {
	MapGUI map = new MapGUI();	
	
	}
}
