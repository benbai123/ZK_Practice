package test.geo.autocomplete;

import org.zkoss.zk.ui.event.Event;

/**
 * Tested with ZK 6.0.2 and ZK-Gmaps 3.0.1
 */
public class PlaceChangedEvent extends Event {

	private static final long serialVersionUID = 7538146314204639298L;
	double _lat;
	double _lng;
	public PlaceChangedEvent(String name, double lat, double lng) {
		super(name);
		_lat = lat;
		_lng = lng;
	}
	public Double getLat () {
		return _lat;
	}
	public Double getLng () {
		return _lng;
	}
}
