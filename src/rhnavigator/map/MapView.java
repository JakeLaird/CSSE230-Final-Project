package rhnavigator.map;

import java.awt.Canvas;
import java.awt.Graphics;

/**
 * A view into a specific Map
 */

/**
 * @author Tayler
 */

public class MapView extends Canvas {
	private Map map;

	public MapView(Map map) {
		this.map = map;
	}

	public void paint(Graphics g) {
		g.drawOval(10, 10, 5, 8);
	}
}
