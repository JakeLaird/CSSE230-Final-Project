package rhnavigator;

public class MapLandmark extends MapPoint {

	public MapLandmark(double latitude, double longitude, String name, int interestLevel) {
		super(latitude, longitude, name, interestLevel);
	}
	
	public boolean isLandmark() {
		return true;
	}

}
