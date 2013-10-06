package impl;

import java.io.Serializable;

import org.zkoss.zk.ui.event.Event;

/** Tested with ZK 6.5.2<br>
 * 
 * Copied from AURequestWithWebSocket, completely the same excepts this fragment.
 * 
 * @author benbai123
 *
 */
/**
 * Define an Event listener used to process event from
 * Not used in this POC
 * 
 * @author benbai123
 *
 */
public interface EventListener extends Serializable {
	/** Called when the registered event is triggered
	 * 
	 * @param event
	 * @see TestComposer#registerEventListenerForWebSocketEnhancedTimer()
	 */
	public void onEvent (Event event) ;
}
