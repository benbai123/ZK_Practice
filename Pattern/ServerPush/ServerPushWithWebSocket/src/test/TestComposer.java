package test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

/**
 * Tested with ZK 6.0.2
 * @author benbai123
 *
 */
@SuppressWarnings("rawtypes")
public class TestComposer extends SelectorComposer {

	private static final long serialVersionUID = 5928314519324520566L;
	@Wire
	Intbox ibx;
	private final AtomicInteger _cnt = new AtomicInteger(0);
	private final static AtomicInteger _cntCounter = new AtomicInteger(0);
	private Timer timer;
	/**
	 * start server push with WebSocket for
	 * specific component 'ibx'
	 */
	@Listen("onClick = #startBtn")
	public void start () {
		if (timer == null) {
			timer = new Timer();
			timer.schedule(getTask(), 0, 1000);
		}
	}
	/**
	 * stop server push with WebSocket for
	 * specific component 'ibx'
	 */
	@Listen("onClick = #stopBtn")
	public void stop () {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	/**
	 * update value to context 'counter' and 'negativeCounter' via
	 * WebSocket
	 * 
	 * All components that listen to these context will be updated
	 */
	@Listen("onClick = #updateCounterBtn")
	public void updateCounterBtn () {
		String msg = "" + _cntCounter.incrementAndGet();
		TestWebSocketServlet.sendBySocketContext(msg, "counter");
		TestWebSocketServlet.sendBySocketContext("-" + msg, "negativeCounter");
	}
	// task to be scheduled
	public TimerTask getTask () {
		return new TimerTask() {
			public void run () {
				update();
			}
		};
	}
	// update value of intbox 'ibx'
	public void update () {
		ibx.updateSelfValueWithWebSocket(_cnt.getAndIncrement());
	}
}