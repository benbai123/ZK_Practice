package components;

import impl.DesktopUtils;
import impl.EventListener;
import impl.RequestFromWebSocket;

/** Tested with ZK 6.5.2<br>
 * 
 * Copied from AURequestWithWebSocket, change _useWebSocketAU default to true and override smartUpdate instead of setter.
 *
 * Almost the same with Textbox (actually completely the same after serialVersionUID)
 * 
 * @author benbai123
 *
 */
public class Intbox extends org.zkoss.zul.Intbox implements IWebSocketEnhancedComponent {

	private static final long serialVersionUID = -6488494817604420277L;
	private boolean _useWebSocketAU = true; // default to true

	@Override
	public void setUseWebSocketAU (boolean useWebSocketAU) {
		if (_useWebSocketAU != useWebSocketAU) {
			_useWebSocketAU = useWebSocketAU;
			smartUpdate("useWebSocketAU", useWebSocketAU);
		}
	}

	@Override
	public void helpDesktopToRegister () {
		if (getDesktop() != null) {
			// register desktop
			DesktopUtils.register(getDesktop());
		}
	}

	@Override
	public void serviceWebSocket(RequestFromWebSocket request) {
		/* ignore, not used in this POC */
	}

	// public since defined in interface, should be private instead
	@Override
	public void addUpdateProp (String prop, String value) {
		// add prop/value to update
		DesktopUtils.updateComponentProp(this, prop, value);
	}

	@Override
	public void registerListenerForWebSocketEvent (String evtnm, EventListener listener) {
		/* ignore, not used in this POC */
	}

	// render useWebSocketAU as needed
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		if (_useWebSocketAU) {
			helpDesktopToRegister();
			// render useWebSocketAU to client side
			render(renderer, "useWebSocketAU", _useWebSocketAU);
		}
	}
	protected void smartUpdate (String attr, Object value) {
		if (_useWebSocketAU) { // use WebSocket to process AU request?
			// update client status via WebSocket
			addUpdateProp(attr, value.toString());
		} else {
			// original function
			super.smartUpdate(attr, value);
		}
	}
}
