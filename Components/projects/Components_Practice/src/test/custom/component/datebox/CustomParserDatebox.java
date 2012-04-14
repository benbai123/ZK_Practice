package test.custom.component.datebox;

import java.util.Date;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;

/**
 * The Custom Datebox that can apply the service side custom date parser
 *
 */
public class CustomParserDatebox extends Datebox {

	private static final long serialVersionUID = 3683801858115113682L;
	static {
		// listen to the "onCustomParse" event
		addClientEvent(CustomParserDatebox.class, "onCustomParse", CE_IMPORTANT|CE_DUPLICATE_IGNORE);
	}
	// override
	// the 'service' method is used to process the component's event from client side
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals("onCustomParse")) {  // the 'onCustomParse' event
			String value = (String)request.getData().get("value");
			// assume we have the custom parser that
			// only parse the value '1' and '2'
			if ("1".equals(value)) { // parse value to date
				setValue(new Date(System.currentTimeMillis()));
				smartUpdate("backToOriginal", "clear");
			} else if ("2".equals(value)) { // parse value to date
				setValue(new Date(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 365)));
				smartUpdate("backToOriginal", "clear");
			}
			else // can not parse, use original client side value
				smartUpdate("backToOriginal", "reset");
			Events.postEvent(new Event("onCustomParse", this, null));
		} else 
			super.service(request, everError);
	}
}