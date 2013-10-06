package test;

import java.util.concurrent.atomic.AtomicInteger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Intbox;

/** Tested with ZK 6.5.3
 * 
 * @author benbai123
 *
 */
public class TestComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = -5014610291543614202L;
	@Wire Intbox ibx;
	@Wire Intbox ibx2;
	@Wire Intbox ibx3;
	@Wire Intbox ibx4;
	@Wire Intbox ibx5;
	@Wire Intbox ibx6;
	@Wire Intbox ibx7;
	@Wire Intbox ibx8;
	@Wire Intbox ibx9;
	@Wire Intbox ibx10;
	@Wire Intbox ibx11;
	@Wire Intbox ibx12;
	@Wire Intbox ibx13;
	@Wire Intbox ibx14;
	@Wire Intbox ibx15;
	@Wire Intbox ibx16;
	@Wire Intbox ibx17;
	@Wire Intbox ibx18;
	@Wire Intbox ibx19;
	@Wire Intbox ibx20;
	@Wire Intbox nibx;
	@Wire Intbox nibx2;
	@Wire Intbox nibx3;
	@Wire Intbox nibx4;
	@Wire Intbox nibx5;
	@Wire Intbox nibx6;
	@Wire Intbox nibx7;
	@Wire Intbox nibx8;
	@Wire Intbox nibx9;
	@Wire Intbox nibx10;
	@Wire Intbox nibx11;
	@Wire Intbox nibx12;
	@Wire Intbox nibx13;
	@Wire Intbox nibx14;
	@Wire Intbox nibx15;
	@Wire Intbox nibx16;
	@Wire Intbox nibx17;
	@Wire Intbox nibx18;
	@Wire Intbox nibx19;
	@Wire Intbox nibx20;
	private AtomicInteger _cntCounter = new AtomicInteger(0);
	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
		final Desktop desktop = ibx.getDesktop();
		desktop.enableServerPush(true);
		new Thread(new Runnable () {
			public void run () {
				while (true) {
					try {
						try {
							// active desktop
							Executions.activate(desktop);
							int value = _cntCounter.incrementAndGet();
							int nvalue = -1*value;
							// update UI
							ibx.setValue(value);
							ibx2.setValue(nvalue);
							ibx3.setValue(value);
							ibx4.setValue(nvalue);
							ibx5.setValue(value);
							ibx6.setValue(nvalue);
							ibx7.setValue(value);
							ibx8.setValue(nvalue);
							ibx9.setValue(value);
							ibx10.setValue(nvalue);
							ibx11.setValue(value);
							ibx12.setValue(nvalue);
							ibx13.setValue(value);
							ibx14.setValue(nvalue);
							ibx15.setValue(value);
							ibx16.setValue(nvalue);
							ibx17.setValue(value);
							ibx18.setValue(nvalue);
							ibx19.setValue(value);
							ibx20.setValue(nvalue);

							nibx.setValue(value);
							nibx2.setValue(nvalue);
							nibx3.setValue(value);
							nibx4.setValue(nvalue);
							nibx5.setValue(value);
							nibx6.setValue(nvalue);
							nibx7.setValue(value);
							nibx8.setValue(nvalue);
							nibx9.setValue(value);
							nibx10.setValue(nvalue);
							nibx11.setValue(value);
							nibx12.setValue(nvalue);
							nibx13.setValue(value);
							nibx14.setValue(nvalue);
							nibx15.setValue(value);
							nibx16.setValue(nvalue);
							nibx17.setValue(value);
							nibx18.setValue(nvalue);
							nibx19.setValue(value);
							nibx20.setValue(nvalue);
							Thread.sleep(10);
						} finally {
							// deactive desktop
							Executions.deactivate(desktop);
						}
					} catch (Exception e) {
						break;
					}
				}
			}
		}).start();
	}
}
