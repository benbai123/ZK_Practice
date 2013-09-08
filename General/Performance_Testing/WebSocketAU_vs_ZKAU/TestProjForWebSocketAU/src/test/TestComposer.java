package test;


import impl.EventListener;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;

import components.IWebSocketEnhancedComponent;
import components.Intbox;
import components.Timer;

/** Tested with ZK 6.5.2
 * 
 * @author benbai123
 *
 */
public class TestComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = -5014610291543614202L;
	// custom timer and intbox
	@Wire
	Timer timer;
	@Wire
	Timer timer2;
	@Wire
	Intbox ibx;
	@Wire
	Intbox ibx2;

	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
		registerEventListenerForWebSocketEnhancedTimer();
	}

	/** Register EventListener for WebSocket,
	 * Events.postEvent cannot work with WebSocket (without HttpRequest),
	 * so cannot use @Listen in Composer
	 * 
	 */
	protected void registerEventListenerForWebSocketEnhancedTimer () {
		// cast to IWebSocketEnhancedComponent
		IWebSocketEnhancedComponent enhancedTimer = (IWebSocketEnhancedComponent)timer;
		// register event listener for onTimer event of timer
		enhancedTimer.registerListenerForWebSocketEvent("onTimer",
			new EventListener () {
				private static final long serialVersionUID = -8920291597084200994L;

				public void onEvent (Event event) {
					// increase value of intbox
					ibx.setValue(ibx.getValue() + 1);
				}
			}
		);
		enhancedTimer = (IWebSocketEnhancedComponent)timer2;
		// register event listener for onTimer event of timer2
		enhancedTimer.registerListenerForWebSocketEvent("onTimer",
			new EventListener () {
				private static final long serialVersionUID = -5721055473084949736L;

				public void onEvent (Event event) {
					// increase value of intbox
					ibx2.setValue(ibx2.getValue() - 1);
				}
			}
		);
	}
}
