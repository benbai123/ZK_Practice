package test.components;

import java.math.BigDecimal;

import org.zkoss.zk.au.out.AuInvoke;
import org.zkoss.zk.ui.event.ErrorEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Decimalbox;

/** Tested with ZK 6.0.2
 * 
 * @author benbai123
 *
 */
public class DecimalboxSupportScientificNotation extends Decimalbox {

	private static final long serialVersionUID = -4797186118579295417L;

	@SuppressWarnings("rawtypes")
	public DecimalboxSupportScientificNotation () {
		final DecimalboxSupportScientificNotation self = this;
		/**
		 * 				Override client side functions
		 */
		// add E into allow keys
		setWidgetOverride("getAllowedKeys_", "function () {\n"
				+"			return this.$getAllowedKeys_() + 'E';\n"
				+"		}\n");
		// call original _markError then hide error box at first
		// leave the red border
		// denotes 'error at client side but waiting for the confirmation from server side'
		// (okay, actually I'm lazy to remove that classes)
		setWidgetOverride("_markError", "function (msg, val, noOnError) {\n"
				+"			val = jq(this.getInputNode()).val();\n" // use value in input element
				+"			this.$_markError(msg, val, noOnError);\n"
				+"			var wgt = this;\n"
				+"			setTimeout(function () {\n"
				+"				wgt._hideError();\n"
				+"			});\n"
				+"		}\n");
		// hide error box
		setWidgetOverride("_hideError", "function () {\n"
				+"			var errbox = this._errbox;\n" // use value in input element
				+"			if (errbox) {\n"
				+"				if (errbox.$n())\n"
				+"					errbox.$n().style.display = 'none';\n"
				+"				else {\n"
				+"					var wgt = this;\n"
				+"					setTimeout( function () {\n"
				+"						wgt._hideError();\n"
				+"					}, 50);\n"
				+"				}\n"
				+"			}\n"
				+"		}\n");
		// unhide error box
		setWidgetOverride("_unhideError", "function () {\n"
				+"			var errbox = this._errbox;\n" // use value in input element
				+"			if (errbox) {\n"
				+"				errbox.$n().style.display = '';\n"
				+"			}\n"
				+"		}\n");
		/**
		 * 				Add event listener to process onError event
		 */
		addEventListener(Events.ON_ERROR, new EventListener () {
			public void onEvent (Event event) {
				ErrorEvent evt = (ErrorEvent)event;
				boolean valid = true;
				BigDecimal val = null;
				// try create a BigDecimal with the wrong value
				try {
					val = new BigDecimal(evt.getValue());
				} catch (Exception e) {
					valid = false;
				}
				if (valid) {
					// it is valid for BigDecimal
					self.setValueDirectly(val);
					// post onScientificNotation event
					Events.postEvent("onScientificNotation", self, null);
					// clear the error status
					self.clearErrorMessage();
					Clients.clearWrongValue(self);
				} else {
					// it is invalid for BigDecimal
					// unhide error box at client side
					self.response("DecimalboxSupportScientificNotation", new AuInvoke(self, "_unhideError", null));
				}
			}
		});
	}
}
