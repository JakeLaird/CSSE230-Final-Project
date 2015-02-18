/**
 * 
 */
package test.rhnavigator.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import rhnavigator.map.Map;

/**
 * @author tayler
 *
 */
public class MapTest {

	/**
	 * Test method for {@link rhnavigator.map.Map#addPoint(double, double, java.lang.String, java.util.ArrayList, int)}.
	 */
	@Test
	public void testAddPoint() {
		Map m = new Map();
		// Simple case
		m.addPoint(0.0, 0.0, "A", null, 0);
		assertTrue(m.size() == 1);
		assertTrue(m.toArrayList().get(0).neighbors.size()==0);
		
		m.addPoint(0.0, 0.0, "A", null, 0);

		m.addPoint(1.0, 1.0, "B", new ArrayList<String>() {{ add("A"); }}, 0);
		assertTrue(m.size() == 2);
		assertTrue(m.toArrayList().get(0).neighbors.size()==1);
		assertTrue(m.toArrayList().get(1).neighbors.size()==1);
	}

	/**
	 * Test method for {@link rhnavigator.map.Map#size()}.
	 */
	@Test
	public void testSize() {
		Map m = new Map();
		m.addPoint(0.0, 0.0, "A", null, 0);
		assertTrue(m.size() == 1);
		
		for (int i = 2; i < 10; i++) {
			m.addPoint(i, 0.0, "A"+i, null, 0);
			assertTrue(m.size() == i);
		}
	}

	/**
	 * Test method for {@link rhnavigator.map.Map#findByName(java.lang.String)}.
	 */
	@Test
	public void testFindByName() {
		Map m = new Map();
		m.addPoint(0.0, 0.0, "A", null, 0);
		assertNotNull(m.findByName("A"));
	}

	/**
	 * Test method for {@link rhnavigator.map.Map#findInRange(double, double, double, double)}.
	 */
	@Test
	public void testFindInRange() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rhnavigator.map.Map#getNearest(double, double)}.
	 */
	@Test
	public void testGetNearest() {
		fail("Not yet implemented");
	}

}
