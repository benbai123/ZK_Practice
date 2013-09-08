package test;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Intbox;

/** Tested with ZK 6.5.2
 * 
 * @author benbai123
 *
 */
public class TestComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = -5014610291543614202L;
	@Wire
	Intbox ibx;
	@Wire
	Intbox ibx2;

	@Listen("onTimer = #timer")
	public void update () {
		ibx.setValue(ibx.getValue() + 1);
	}
	@Listen("onTimer = #timer2")
	public void update2 () {
		ibx2.setValue(ibx2.getValue() - 1);
	}
}
