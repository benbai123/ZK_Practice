package test;

import impl.serverpush.ServerPushUtil;

import java.util.Timer;
import java.util.TimerTask;
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
public class TestAutoPushVM {

	/** Counter used to update integer */
	private AtomicInteger _cnt = new AtomicInteger(0);
	/** ServerPush task timer */
	private Timer timer;
	/** Desktop of this VM */
	private Desktop targetDesktop;
	/** Binded field of textbox */
	private String _prop = "rows";

	// getters
	public String getTask () {
		return "timerTask";
	}
	public String getProp () {
		return _prop;
	}
	/**
	 * start server push with WebSocket for
	 * specific context "timerTask"
	 * 
	 * current desktop only
	 */
	@Command
	public void start () {
		if (timer == null) {
			// update once immediately when first time start
			if (_cnt.get() == 0) {
				push(_cnt.incrementAndGet(), "timerTask");
			}
			timer = new Timer();
			timer.schedule(getTimerTask(), 1000, 1000);
		}
	}
	/**
	 * stop server push with WebSocket for
	 * specific context "timerTask"
	 * 
	 * current desktop only
	 */
	@Command
	public void stop () {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	@Command
	@NotifyChange("prop")
	public void switchRowCol () {
		if ("rows".equals(_prop)) {
			_prop = "cols";
		} else {
			_prop = "rows";
		}
	}
	// task to be scheduled to update context "timerTask" every second
	private TimerTask getTimerTask () {
		return new TimerTask() {
			public void run () {
				push(_cnt.incrementAndGet(), "timerTask");
			}
		};
	}
	/** push value to specific context
	 * 
	 * @param val value to push
	 * @param context context to push
	 */
	private void push (Object val, String context) {
		try {
			if (targetDesktop == null) {
				targetDesktop = Executions.getCurrent().getDesktop();
			}
			// push to current desktop only
			ServerPushUtil.pushVlaue(val, context, targetDesktop);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
