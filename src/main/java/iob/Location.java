package iob;

import java.util.Objects;

public class Location {
	private Double lat;
	private Double lng;

	public Location() {
	}

	public Location(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "Location [lat=" + lat + ", lng=" + lng + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(lat, lng);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(lat, other.lat) && Objects.equals(lng, other.lng);
	}

	public boolean isNear(Location other, double distance) {
		double other_lat = other.getLat();
		double other_lng = other.getLng();

		if (other_lat < this.lat - distance || other.lat > this.lat + distance) { // check if lat isn't in range
			return false;
		}
		if (other_lng < this.lng - distance || other_lng > this.lng + distance) { // check if lat isn't in range
			return false;
		}

		return true;
	}
}
