package test.basic.mvc;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.MouseEvent;
/**
 * Tested with ZK 6.0.2 EE Eval
 *
 */
public class BasicGenericForwardComposer extends GenericForwardComposer<Component> {

	private static final long serialVersionUID = -4734679090474506071L;
	private String _name;
	/**
	 * Wire the Label with id "lb" in zul page into composer
	 * so you can control it by composer directly
	 * 
	 * The field name of declared component (Label here)
	 * will be mapped to the component id in zul page automatically,
	 * 
	 * e.g., Label lb will be mapped to <label id="lb" ... />
	 */
	private Label lb;

	/**
	 * Official: With its GenericForwardComposer.doAfterCompose(Component) method,
	 * the ZK components declared in mark up are wired with the component instances
	 * declared in the controller for our manipulation, while the events fired are
	 * automatically forwarded to this controller for event handling.
	 * 
	 * In short, you can do some initiation task here
	 */
	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
		_name = "Default Name";
		lb.setValue("The message label");
	}

	/**
	 * Declare an event listener, listen to the "onChange" event
	 * of the textbox with id "tb" in zul page,
	 * 
	 * the function name is combined by
	 * event name (onChange) + "$" + component id in zul page(tb)
	 * 
	 * Do NOT need to wire it before listen its event
	 * 
	 * @param event the input event fired by the textbox
	 * 
	 */
	public void onChange$tb (InputEvent event) {
		_name = event.getValue();
		if (_name == null || _name.isEmpty()) {
			_name = "Default Name";
		}
	}

	/**
	 * Listen to onClick event of the buttn with id "btn"
	 * @param event
	 */
	public void onClick$btn (MouseEvent event) {
		// update label lb's value
		lb.setValue("Hello " + _name);
	}
}