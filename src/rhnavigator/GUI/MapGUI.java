package rhnavigator.GUI;

import javax.swing.*;
public class MapGUI {
	JFrame mapFrame;
	JPanel mapPanel;
	
	int MAP_WIDTH = 700;
	int MAP_HEIGHT = 500;
	int PANEL_WIDTH = 1000;
	int PANEL_HEIGHT = 500;
	
	MapGUI(){
		mapFrame = new JFrame();
		mapPanel = new JPanel();
		mapFrame.add(mapPanel);
		mapFrame.setSize(PANEL_WIDTH,PANEL_HEIGHT);
		mapPanel.setSize(MAP_WIDTH,MAP_HEIGHT);
		
		mapFrame.setVisible(true);
		mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
public static void main(String[] args) {
	MapGUI map = new MapGUI();	
	
	}
}
