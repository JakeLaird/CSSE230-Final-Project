package rhnavigator.GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
public class MapGUI {
	JFrame frame;
	JSplitPane splitPane;
	JPanel buttonPanel,mapPanel,homeScreen;
	JButton search,attractions, goHome, settings;
	
	
	final int MAP_WIDTH = 700;
	final int MAP_HEIGHT = 500;
	final int FRAME_WIDTH = 1000;
	final int FRAME_HEIGHT = 500;
	
	final int HOME_WIDTH = 500;
	final int HOME_HEIGHT = HOME_WIDTH;
	
	public MapGUI(){
		GridLayout homeLayout = new GridLayout(2,2);
		frame = new JFrame();
		frame.setLayout(homeLayout);
		
		JButton search = new JButton("Search");
		ActionListener searchListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				// Do stuff for search function
			}
		};
		search.addActionListener(searchListener);
		frame.add(search);
		
		JButton attraction = new JButton("Attractions");
		ActionListener attractionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				// Do stuff for finding attractions function
			}
		};
		attraction.addActionListener(attractionListener);
		frame.add(attraction);
		
		JButton goHome = new JButton("Go Home");
		ActionListener homeListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				// Do stuff for finding route home function
			}
		};
		goHome.addActionListener(homeListener);
		frame.add(goHome);
		
		JButton settings = new JButton("Settings");
		ActionListener settingsListener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				// Do stuff for search function
			}
		};
		settings.addActionListener(settingsListener);
		frame.add(settings);
		
		frame.setSize(HOME_WIDTH,HOME_HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	private void goToMap(){
		buttonPanel = new JPanel();
		mapPanel = new JPanel();
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,buttonPanel,mapPanel);
		
		frame.add(splitPane);
		frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		splitPane.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		
		
	}
	
public static void main(String[] args) {
	MapGUI map = new MapGUI();	
	
	}
}
