package components.serverpush;

import impl.serverpush.Binding;
import impl.serverpush.ServerPushUtil;

/** Tested with ZK 6.5.2<br>
 * 
 * Created for this POC, implements IWebSocketServerPushEnhancedComponent to
 * support WebSocket ServerPush
 * 
 * Almost the same with Intbox (completely the same after serialVersionUID)
 * 
 * @author benbai123
 *
 */
public class Textbox extends components.Textbox implements IWebSocketServerPushEnhancedComponent {

	private static final long serialVersionUID = 2354643632056197764L;

	@Override
	public Binding addSocketContextBinding(String field, String context) {
		return ServerPushUtil.addContextBinding(this, field, context);
	}

	@Override
	public void removeSocketContextBinding(String field, String context) {
		ServerPushUtil.removeContextBinding(this, field, context);
	}

}
