package rhnavigator.map;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * A view into a specific Map
 */

/**
 * @author Tayler
 */

public class MapView extends JComponent {
	private Map map;

	public MapView(Map map) {
		this.map = map;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(),getHeight());
//		System.out.println("W " + getWidth() + " H " + getHeight());
		g.setColor(Color.BLACK);
		g.drawOval(10, 10, 5, 8);
	}
}
