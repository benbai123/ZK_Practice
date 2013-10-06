package components.serverpush;

import impl.serverpush.Binding;
import impl.serverpush.ServerPushUtil;

/** Tested with ZK 6.5.2<br>
 * 
 * Created for this POC, implements IWebSocketServerPushEnhancedComponent to
 * support WebSocket ServerPush
 * 
 * Almost the same with Textbox (completely the same after serialVersionUID)
 * 
 * @author benbai123
 *
 */
public class Intbox extends components.Intbox implements IWebSocketServerPushEnhancedComponent {

	private static final long serialVersionUID = -3277498174057967067L;

	@Override
	public Binding addSocketContextBinding(String field, String context) {
		return ServerPushUtil.addContextBinding(this, field, context);
	}

	@Override
	public void removeSocketContextBinding(String field, String context) {
		ServerPushUtil.removeContextBinding(this, field, context);
	}

}
