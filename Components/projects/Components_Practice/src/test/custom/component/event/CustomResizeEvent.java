package test.custom.component.event;

import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Tested with ZK 6.0.2
 * Basically you can think an Event object
 * is just a POJO to keep the data within an event
 * @author benbai123
 *
 */
public class CustomResizeEvent extends Event {
	private static final long serialVersionUID = -8839289786864776054L;

	public static final String ON_CUSTOM_RESIZE = "onCustomResize";
	private int _value;
	private final Component _reference;
	private String _resizeAttribute;
	@SuppressWarnings("rawtypes")
	public static CustomResizeEvent getCustomResizeEvent (org.zkoss.zk.au.AuRequest request) {
		// get data map
		Map data = request.getData();
		// get values by keys
		int value = (Integer)data.get("value");
		final Component reference = request.getDesktop().getComponentByUuidIfAny((String)data.get("reference"));
		String resizeAttribute = (String)data.get("resizeAttribute");
		// create event instance, return it
		return new CustomResizeEvent(ON_CUSTOM_RESIZE, request.getComponent(), value, reference, resizeAttribute);
	}
	// Constructor
	public CustomResizeEvent (String name, Component target,
			int value, Component reference, String resizeAttribute) {
		super(name, target);
		_value = value;
		_reference = reference;
		_resizeAttribute = resizeAttribute;
	}
	// getters
	public int getValue () {
		return _value;
	}
	public Component getReference () {
		return _reference;
	}
	public String getResizeAttribute () {
		return _resizeAttribute;
	}
}