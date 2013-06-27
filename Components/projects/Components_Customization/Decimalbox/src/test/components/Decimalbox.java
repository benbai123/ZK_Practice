package test.components;

import java.math.BigDecimal;
import java.util.Map;

import org.zkoss.zk.au.out.AuInvoke;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;


/** Tested with ZK 6.0.2
 * 
 * @author benbai123
 *
 */
public class Decimalbox extends org.zkoss.zul.Decimalbox {

	private String _textAlign = "left";
	private static final long serialVersionUID = -4797186118579295417L;
	static {
		// listen custom event
		addClientEvent(Decimalbox.class, "onCheckErrorValue", CE_IMPORTANT | CE_DUPLICATE_IGNORE | CE_NON_DEFERRABLE);
	}
	public void setTextAlign (String textAlign) {
		// update if there is a different value
		if (textAlign != null // no null
			&& !textAlign.isEmpty() // no empty
			&& !textAlign.equals(_textAlign)) { // value is changed
			_textAlign = textAlign;
			smartUpdate("textAlign", _textAlign);
		}
	}
	public String getTextAlign () {
		return _textAlign;
	}

	// override
	// process client event
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		// process custom event
		if (cmd.equals("onCheckErrorValue")) {
			// get data map
			Map<String, Object> data = request.getData();
			// get value
			String value = (String)data.get("value");
			boolean valid = true;
			BigDecimal val = null;
			// try create a BigDecimal with the wrong value
			try {
				val = new BigDecimal(value);
			} catch (Exception e) {
				// failed to create BigDecimal, not valid
				valid = false;
			}
			if (valid) {
				// it is valid for BigDecimal
				setValueDirectly(val);
				// post onScientificNotation event
				Events.postEvent("onScientificNotation", this, null);
				// clear the error status if any
				clearErrorMessage();
				Clients.clearWrongValue(this);
			} else {
				// it is invalid for BigDecimal
				// really do _markError at client side
				// the code below will call function _realMarkError
				// of the widget class at client side 
				response("DecimalboxSupportScientificNotation", new AuInvoke(this, "_realMarkError", (String[])null));
			}
		} else 
			super.service(request, everError);
	}
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		if (!"left".equals(_textAlign)) {
			render(renderer, "textAlign", _textAlign);
		}
	}
}
