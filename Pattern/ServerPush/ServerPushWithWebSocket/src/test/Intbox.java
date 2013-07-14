package test;

/**
 * Enhanced Intbox that support Web Socket action
 * @author benbai123
 *
 */
public class Intbox extends org.zkoss.zul.Intbox implements IWebsocketEnhancedComponent {

	private static final long serialVersionUID = 1711581315927992296L;
	/** context used to create socket connection
	 * used by custom mapping rule
	 */
	private String _socketContext = "";
	private String _mappingId;
	public void setSocketContext (String socketContext) {
		// no need to clear old connection,
		// old connection will be closed at client side
		// then trigger onClose method of connection at
		// server side to clear it
		if (socketContext == null) {
			socketContext = "";
		}
		if (!socketContext.equals(_socketContext)) {
			_socketContext = socketContext;
			// register if has context
			if (!socketContext.isEmpty()) {
				register();
			}
			smartUpdate("socketContext", getMappingContext());
		}
	}
	// getter
	public String getSocketContext () {
		return _socketContext;
	}
	public String getBaseId () {
		return getUuid();
	}
	public String getMappingId () {
		return _mappingId;
	}
	/**
	 * append mapping id to context, will parse it in TestWebSocketServlet,
	 * @see test.TestWebSocketServlet#createWebSocketInbound(String, javax.servlet.http.HttpServletRequest)
	 * @return
	 */
	private String getMappingContext () {
		return _socketContext + "_" + _mappingId;
	}
	/**
	 * Update value to client side via Web Socket
	 * two steps:
	 * 1. Set value at server side without any 'update client' action
	 * 2. Update value to client side via Web Socket
	 * @param value
	 * @see test.TestWebSocketServlet#sendByComponent(String, org.zkoss.zk.ui.Component)
	 */
	public void updateSelfValueWithWebSocket (int value) {
		setValueDirectly(value);
		TestWebSocketServlet.sendByComponent(value+"", this);
	}

	public void notifyByWebSocket (String msg) {
		setValueDirectly(Integer.parseInt(msg));
	}
	/**
	 * Register this component for notify back
	 * @see test.TestWebSocketServlet#register(IWebsocketEnhancedComponent)
	 * @see test.TestWebSocketServlet#sendBySocketContext(String, String)
	 */
	private void register () {
		_mappingId = TestWebSocketServlet.register(this);
	}
	// render socketContext as needed
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		if (!"".equals(_socketContext)) {
			render(renderer, "socketContext", getMappingContext());
		}
	}
}
