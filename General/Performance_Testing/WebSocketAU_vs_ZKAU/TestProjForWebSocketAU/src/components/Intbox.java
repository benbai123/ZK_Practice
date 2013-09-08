package components;

import impl.DesktopUtils;
import impl.EventListener;
import impl.RequestFromWebSocket;

public class Intbox extends org.zkoss.zul.Intbox implements IWebSocketEnhancedComponent {

	private static final long serialVersionUID = -6488494817604420277L;
	private boolean _useWebSocketAU;

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
	public void setValue (int value) {
		if (_useWebSocketAU) { // use WebSocket to process AU request?
			// set value without smartUpdate
			super.setValueDirectly(value);
			// update client status via WebSocket
			addUpdateProp("value", value+"");
		} else {
			// original function
			super.setValue(value);
		}
	}
}
