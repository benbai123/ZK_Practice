package test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Intbox;
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
	private Timer timer;
	@Listen("onClick = #startBtn")
	public void start () {
		final Desktop desktop = Executions.getCurrent().getDesktop();
		if (!desktop.isServerPushEnabled()) {
			// enable server push if not enabled
			desktop.enableServerPush(true);
			timer = new Timer();
			// schedule task
			// getTask() -> get the task that to be scheduled
			// 0 -> no delay of first task, start it immediately
			// 1000 -> delay between each task after first task, 1 sec here
			timer.schedule(getTask(), 0, 1000);
		}
	}
	@Listen("onClick = #stopBtn")
	public void stop () {
		final Desktop desktop = Executions.getCurrent().getDesktop();
		if (desktop.isServerPushEnabled()) {
			// disable server push if enabled
			desktop.enableServerPush(false);
			// cancel scheduled task
			timer.cancel();
		}
	}
	// task to be scheduled
	public TimerTask getTask () {
		return new TimerTask() {
			public void run () {
				update();
			}
		};
	}
	// update value of intbox
	public void update () {
		Desktop desktop = ibx.getDesktop();
		try {
			if(desktop == null) {
				timer.cancel();
				return;
			}
			try {
				// active desktop
				Executions.activate(desktop);
				// update UI
				ibx.setValue(_cnt.getAndIncrement());
			} finally {
				// deactive desktop
				Executions.deactivate(desktop);
			}
		} catch (Exception ignore) {
			/* ignore */
		}
	}
}
