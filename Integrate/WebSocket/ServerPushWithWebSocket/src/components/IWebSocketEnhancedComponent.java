package components;

import impl.EventListener;
import impl.RequestFromWebSocket;

/**
 * Define a "WebSocket Enhanced Component"
 * for request-response pattern
 * 
 * Copied from AURequestWithWebSocket, completely the same except this fragment
 * 
 * @author benbai123
 *
 */
public interface IWebSocketEnhancedComponent {
	/**
	 * Whether use WebSocket to process AU request
	 */
	public void setUseWebSocketAU (boolean useWebSocketAU) ;
	/**
	 * Help desktop to register itself into session
	 * since we need it in WebSocket and we will not
	 * use custom desktop for this POC
	 */
	public void helpDesktopToRegister () ;
	/**
	 * Process event sent from client
	 * @see TestWebSocketServlet.TestMessageInbound#onTextMessage(java.nio.CharBuffer)
	 */
	public void serviceWebSocket (RequestFromWebSocket request) ;
	/**
	 * Update status to client
	 */
	public void addUpdateProp (String prop, String value) ;
	/**
	 * Register Event Listener at Component itself since
	 * Events.postEvent cannot work with WebSocket without 
	 * an active Execution, need to register listener manually in
	 * Composer
	 * @param listener EventListener used to process event from WebSocket request
	 */
	public void registerListenerForWebSocketEvent (String evtnm, EventListener listener) ;
}
