package test.geo.autocomplete;

import java.util.Map;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Textbox;
/**
 * Tested with ZK 6.0.2 and ZK-Gmaps 3.0.1
 */
public class CustomTextbox extends Textbox {
	// listen to onPlaceChanged event with specific config
	static {
		addClientEvent(CustomTextbox.class, "onPlaceChanged",
				CE_IMPORTANT|CE_DUPLICATE_IGNORE);
	}
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		// handle event if it is onPlaceChanged event
		if ("onPlaceChanged".equals(cmd)) {
			// get data
			final Map data = request.getData();
			double lat = ((Double) data.get("lat")).doubleValue();
			double lng = ((Double) data.get("lng")).doubleValue();
			// create and post event
			PlaceChangedEvent evt = new PlaceChangedEvent(cmd, lat, lng);
			// post event to the custom textbox
			Events.postEvent(this, evt);
		} else {
			super.service(request, everError);
		}
	}
}
