package test;

import impl.serverpush.ServerPushUtil;

import java.util.concurrent.atomic.AtomicInteger;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;

/** Tested with ZK 6.5.2
 * 
 * @author benbai123
 *
 */
public class TestPushToAllVM {
	/** context listened by intboxes */
	private String _counter = "positive";
	private String _inverseCounter = "negative";
	/** desktop of this VM */
	private Desktop targetDesktop;

	/** counter used to update positive/negative integer */
	private static AtomicInteger _cntCounter = new AtomicInteger(0);

	// getters
	public String getCounter () {
		return _counter;
	}
	public String getInverseCounter () {
		return _inverseCounter;
	}
	/**
	 * update value to context 'positive' and 'negative' via
	 * WebSocket
	 * 
	 * All components that listen to these context will be updated
	 */
	@Command
	public void updateCounter () {
		int val = _cntCounter.incrementAndGet();
		push(val, "positive", false);
		push(-1*val, "negative", false);
	}
	/** switch listening components of 'positive' and 'negative' context
	 * current desktop only
	 * 
	 */
	@Command
	@NotifyChange({"counter", "inverseCounter"})
	public void switchCounter () {
		int val = _cntCounter.get();
		// push to opposite (for self desktop) before switch context
		push(val, "negative", true);
		push(-1*val, "positive", true);
		String tmp = _counter;
		_counter = _inverseCounter;
		_inverseCounter = tmp;
		
	}

	/** push value
	 * 
	 * @param val
	 * @param context
	 */
	private void push (Object val, String context, boolean desktopOnly) {
		try {
			if (targetDesktop == null) {
				targetDesktop = Executions.getCurrent().getDesktop();
			}
			if (desktopOnly) {
				// push to current desktop only
				ServerPushUtil.pushVlaue(val, context, targetDesktop);
			} else {
				// push to all desktop
				ServerPushUtil.pushVlaue(val, context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
