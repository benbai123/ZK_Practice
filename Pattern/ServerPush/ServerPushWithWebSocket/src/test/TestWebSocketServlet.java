package test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

/**
 * Tested with Tomcat 7.0.42 and ZK 6.0.2
 * @author benbai123
 *
 */
public class TestWebSocketServlet extends WebSocketServlet {

	private static final long serialVersionUID = -7663708549630020769L;

	// hold each connection
	private static final Set<TestMessageInbound> connections =
		new CopyOnWriteArraySet<TestMessageInbound>();
	// hold each related component
	private static final Map<String, IWebsocketEnhancedComponent> registeredComponents =
		new Hashtable<String, IWebsocketEnhancedComponent>();

	/**
	 * For create connection only, each connection will
	 * handle it self as needed
	 */
	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol,
			HttpServletRequest request) {
		// request uri
		String uri = request.getRequestURI();

		// infos within uri, format is context_mappingId.wsreq e.g., counter_a1234.wsreq
		String infos = uri.substring(uri.lastIndexOf("/")+1, uri.length()).replace(".wsreq", "");
		String context = infos.substring(0, infos.lastIndexOf("_"));
		String compMappingId = infos.substring(infos.lastIndexOf("_")+1, infos.length());
		
		return new TestMessageInbound(context, compMappingId);
	}
	private final class TestMessageInbound extends MessageInbound {
		// hold context and mappingId of related component
		private String _context;
		private String _compMappingId;
		// constructor
		public TestMessageInbound (String context, String compMappingId) {
			_context = context;
			_compMappingId = compMappingId;
		}
		// add self instance into connections Set while opened
		@Override
		protected void onOpen(WsOutbound outbound) {
			connections.add(this);
		}
		// remove self instance from connections set and
		// clear component reference while closed
		@Override
		protected void onClose(int status) {
			connections.remove(this);
			registeredComponents.remove(_compMappingId);
		}
		// ignore binary message
		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {
			// ignore
		}
		// ignore text message
		@Override
		protected void onTextMessage(CharBuffer message) throws IOException {
			// ignore
		}
		/**
		 * get socket context of this connection
		 * @return
		 */
		public String getContext () {
			return _context;
		}
		/**
		 * get mappingId of related component of this connection
		 * @return
		 */
		public String getCompMappingId () {
			return _compMappingId;
		}
	}

	/**
	 * send message via Web Socket with specified socket context
	 * all components that connect to this context will be updated
	 * @param msg message to send
	 * @param socketContext target socket context
	 */
	public static void sendBySocketContext (String msg, String socketContext) {
		for (TestMessageInbound connection : connections) {
			
			try {
				// send message to specified socketContext
				// ignore self context and other different context
				if (!"self".equals(connection.getContext())
					&& connection.getContext().equals(socketContext)) {
					// send message via Web Socket
					connection.getWsOutbound().writeTextMessage(CharBuffer.wrap(msg));
					// pass sent message to component
					// so the component can update itself if needed
					registeredComponents.get(connection.getCompMappingId()).notifyByWebSocket(msg);
				}
			} catch (IOException ignore) {
				/* ignore */
			}
		}
	}
	/**
	 * send message via Web Socket to specific component
	 * @param msg message to send
	 * @param comp target component
	 */
	public static void sendByComponent (String msg, IWebsocketEnhancedComponent comp) {
		for (TestMessageInbound connection : connections) {
			try {
				// send message to specific component
				if (connection.getCompMappingId().equals(comp.getMappingId())) {
					connection.getWsOutbound().writeTextMessage(CharBuffer.wrap(msg));
				}
			} catch (IOException ignore) {
				/* ignore */
			}
		}
	}
	/**
	 * register component so can notify it as needed
	 * @param comp component to register
	 * @return String, mapping id for specified component
	 */
	public static String register (IWebsocketEnhancedComponent comp) {
		String id = comp.getBaseId();
		String mappingId = id;
		int i = 2;
		// check it since components in different sessions probably use the same id
		synchronized (registeredComponents) {
			while (registeredComponents.containsKey(mappingId)) {
				mappingId = id + i;
				i++;
			}
			registeredComponents.put(mappingId, comp);
		}
		return mappingId;
	}
}