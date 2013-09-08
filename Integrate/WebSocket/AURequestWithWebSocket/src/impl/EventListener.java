package impl;

import java.io.Serializable;

import org.zkoss.zk.ui.event.Event;

/**
 * Define an Event listener used to process event from
 * WebSocket request
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
