package impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.json.JSONValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;

import components.IWebSocketEnhancedComponent;

/** Tested with Tomcat 7.0.42 and ZK 6.5.2<br>
 * 
 * Modified from AURequestWithWebSocket, almost the same, store channel at desktop so can
 * send response with desktop itself. 
 * 
 * @author benbai123
 *
 */
public class TestWebSocketServlet extends WebSocketServlet {

	private static final long serialVersionUID = -7663708549630020769L;
	public static final String WEBSOCKET_CHANNEL = "WEBSOCKET_CHANNEL";

	/**
	 * For create connection only, each connection will
	 * handle it self as needed
	 */
	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol,
			HttpServletRequest request) {
		// request uri, format is desktopId.wsreq
		String uri = request.getRequestURI();

		// get desktopId from uri
		String desktopId = uri.substring(uri.lastIndexOf("/")+1, uri.length()).replace(".wsreq", "");

		// create MessageInbound with desktop ID and session
		return new TestMessageInbound(desktopId, request.getSession());
	}
	private final class TestMessageInbound extends MessageInbound {
		// hold desktop id and session
		private String _desktopId;
		private HttpSession _session;
		// constructor
		public TestMessageInbound (String desktopId, HttpSession session) {
			_desktopId = desktopId;
			_session = session;
		}
		@Override
		protected void onOpen(WsOutbound outbound) {
			/* ignore */
			Desktop desktop = DesktopUtils.getRegisteredDesktop(_session, _desktopId);
			// store channel at desktop
			desktop.setAttribute(WEBSOCKET_CHANNEL, this);
		}
		// remove registered desktop
		@Override
		protected void onClose(int status) {
			try {
				DesktopUtils.removeRegisteredDesktop(_session, _desktopId);
			} catch (Exception e) {
				e.printStackTrace(); // session probably already invalidated so no need to remove
			}
		}
		// ignore binary message
		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {
			/* ignore */
		}
		/** Entry point, you can think it is similar to
		 * service method (doGet/doPost) in common Servlet
		 * 
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected void onTextMessage(CharBuffer message) throws IOException {
			// pass request to components
			// request from client: {
			// 			dtid: DESKTOP_ID,
			//			[
			//				{"uuid": COMPONENT_ID_1, "evtnm": EVENT_NAME_1, "data": DATA_1},
			//				{"uuid": COMPONENT_ID_2, "evtnm": EVENT_NAME_2, "data": DATA_2},
			//				...
			//			]
			//		}

			// parse to JSON object
			JSONObject jsObj = (JSONObject)JSONValue.parse(message.toString());
//			System.out.println("desktop: " + jsObj.get("dtid"));
			// enable the line below to see (merged) request from Client
//			System.out.println("requests: " + jsObj.get("requests"));

			// get desktop
			Desktop desktop = DesktopUtils.getRegisteredDesktop(_session, (String)jsObj.get("dtid"));
			JSONArray requests = (JSONArray)JSONValue.parse((String)jsObj.get("requests"));
			// for each request in requests
			for (int i = 0; i < requests.size(); i++) {
				// get request
				JSONObject reqObj = (JSONObject)requests.get(i);
				// find component
				Component target = desktop.getComponentByUuidIfAny((String)reqObj.get("uuid"));
				// get data
				Map<Object, Object> data = (Map<Object, Object>)reqObj.get("data");
				// create request object
				RequestFromWebSocket req = new RequestFromWebSocket((String)reqObj.get("evtnm"), target, data);
				// pass request object to service method of component
				((IWebSocketEnhancedComponent)target).serviceWebSocket(req);
			}
			// build and send response after service
			sendResponse(this, desktop);
		}
	}
	/** Send message via WebSocket to specific desktop
	 * 
	 * @param connection connection to use
	 * @param desktop used to build response
	 */
	public static void sendResponse (TestMessageInbound connection, Desktop desktop) {
		// build response
		String response = DesktopUtils.buildResponse(desktop);
		try {
			// send response
			connection.getWsOutbound().writeTextMessage(CharBuffer.wrap(response));
		} catch (IOException ignore) {
			/* ignore */
		}
	}
	public static void sendResponse (Desktop desktop) {
		// send response if WebSocket of desktop is ready
		if (isWebSocketReady(desktop)) {
			sendResponse((TestMessageInbound)desktop.getAttribute(TestWebSocketServlet.WEBSOCKET_CHANNEL), desktop);
		}
	}
	public static boolean isWebSocketReady (Desktop dt) {
		return dt.getAttribute(TestWebSocketServlet.WEBSOCKET_CHANNEL) != null;
	}
}