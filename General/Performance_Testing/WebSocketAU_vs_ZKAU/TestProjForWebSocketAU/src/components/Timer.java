package components;

import impl.DesktopUtils;
import impl.EventListener;
import impl.RequestFromWebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;

public class Timer extends org.zkoss.zul.Timer implements IWebSocketEnhancedComponent {

	private static final long serialVersionUID = 1006786038089103071L;
	private boolean _useWebSocketAU;
	// hold event listener, put it here since
	// we will not use customized AbstractComponent in this POC
	private Map<String, List<EventListener>> _listenerForWebSocket = new HashMap<String, List<EventListener>>();;

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

	// called by TestWebSocketServlet.TestMessageInbound#onTextMessage(java.nio.CharBuffer)
	@Override
	public void serviceWebSocket(RequestFromWebSocket request) {
		String command = request.getCommand();
		if ("onTimer".equals(command)) {
			for (EventListener l : _listenerForWebSocket.get("onTimer")) {
				l.onEvent(new Event("onTimer", this, null));
			}
		}
	}

	@Override
	public void addUpdateProp (String prop, String value) {
		// ignore, not used in this POC
	}

	// called by TestComposer
	/**
	 * register event listener with specific event name
	 */
	@Override
	public void registerListenerForWebSocketEvent (String evtnm, EventListener listener) {
		// try to get listener list with respect to specified event name
		List<EventListener> listeners = _listenerForWebSocket.get(evtnm);
		if (listeners == null) {
			// create new if list is not exists
			listeners = new ArrayList<EventListener>();
			_listenerForWebSocket.put(evtnm, listeners);
		}
		// add event listener into listener list
		listeners.add(listener);
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
}
