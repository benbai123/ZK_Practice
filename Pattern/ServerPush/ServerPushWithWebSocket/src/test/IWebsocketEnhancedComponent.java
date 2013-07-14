package test;

/**
 * Define a "WebSocket Enhanced Component"
 * @author benbai123
 *
 */
public interface IWebsocketEnhancedComponent {
	/**
	 * Called when the listening context is updated
	 * @see test.TestWebSocketServlet#sendBySocketContext(String, String)
	 */
	public void notifyByWebSocket (String msg);
	/**
	 * Base id of this component, components in different sessions probably use the same base id
	 * @return
	 */
	public String getBaseId ();
	/**
	 * Mapping id of this component, should be unique in whole application
	 * @return
	 */
	public String getMappingId ();
}
