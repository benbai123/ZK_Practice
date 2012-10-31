package test.basic.mvc;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

/**
 * Tested with ZK 6.0.2 EE Eval
 *
 */
public class BasicSelectorComposer  extends SelectorComposer<Component> {

	private static final long serialVersionUID = -4527376919822049779L;

	private String _name;
	/**
	 * Wire the Label with component type path "div > label"
	 * this will wire the first label component under the first div component
	 */
	@Wire("div > label")
	private Label lb;

	/**
	 * Do some initiation task here
	 */
	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
		_name = "Default Name";
		lb.setValue("The message label");
	}

	/**
	 * Listen to the onChange event of a component with id 'tb'
	 * since wire/listen based on jquery-selector like way,
	 * the function name can be changed
	 */
	@Listen("onChange = #tb")
	public void doWhenTextboxChanged (InputEvent event) {
		_name = event.getValue();
		if (_name == null || _name.isEmpty()) {
			_name = "Default Name";
		}
	}

	/**
	 * Listen to the onClick event of a button with label 'Say Hello'
	 */
	@Listen("onClick = button[label='Say Hello']")
	public void afterButtonClicked (MouseEvent event) {
		// update label lb's value
		lb.setValue("Hello " + _name);
	}
}