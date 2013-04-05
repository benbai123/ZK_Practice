package test.marcro.component.keypad;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Popup;
import org.zkoss.zul.impl.InputElement;

/**
 * java class of keypad macro component
 * 
 * @author benbai123
 *
 */
public class Keypad extends HtmlMacroComponent {

	private static final long serialVersionUID = 1436975152609984604L;

	@Wire
	Popup pp;

	/** char sequence, used to generate button in keypad */
	private String _charSeq;
	/** the related input element to update */
	private InputElement _inp;
	/** open status of keypad popup */
	private boolean _open;

	/**
	 * Constructor
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Keypad () {
		compose();
		final Keypad comp = this;
		// since there is no isOpen() in Popup,
		// update open status manually
		pp.addEventListener(Events.ON_OPEN, new EventListener () {
			public void onEvent (Event event) {
				comp.updateOpen(((OpenEvent)event).isOpen());
			}
		});
	}
	// setter/getter
	/**
	 * init buttons in keypad
	 * 
	 * parse format: <br>
	 * [break] denotes create a new line (hlayout) for remaining buttons <br>
	 * [...] denotes create button by string in [] <br>
	 * [Del] and [CapsLock] are two supported function key <br>
	 * a single char otherwise<br>
	 * 
	 * Note: '[' will be considered as start of [...], wrap it by [] (i.e., [[]) as needed
	 */
	public void setCharSeq (String charSeq) {
		if (charSeq != null) {
			if (!charSeq.equals(_charSeq)) {
				_charSeq = charSeq;
				initButtons();
			}
		} else {
			_charSeq = null;
		}
	}
	public String getCharSeq () {
		return _charSeq;
	}
	public void setInp (InputElement inp) {
		if (_inp != inp) {
			_inp = inp;
			if (_inp != null && _open) {
				openPopup();
			}
		}
	}
	public InputElement getInp () {
		return _inp;
	}
	public void setOpen (boolean open) {
		if (_open != open) {
			_open = open;
			if (_open
				&& _inp != null) {
				openPopup();
			} else {
				pp.close();
			}
		}
	}
	public boolean isOpen () {
		return _open;
	}
	/**
	 * open keypad popup
	 */
	private void openPopup () {
		pp.open(_inp, "after_center");
	}
	/**
	 * init buttons in keypad
	 * 
	 * parse format: <br>
	 * [break] denotes create a new line (hlayout) for remaining buttons <br>
	 * [Del] and [CapsLock] are two supported function keys <br>
	 * [...] denotes create button by string in [] <br>
	 * a single char otherwise<br>
	 * 
	 * Note: '[' will be considered as start of [...], wrap it by [] (i.e., [[]) as needed
	 */
	private void initButtons () {
		// clear old children
		pp.getChildren().clear();
		if (_charSeq != null) {
			int len = _charSeq.length();
			// used to build label of [...] 
			StringBuilder sb = new StringBuilder("");
			// used to contains a line of buttons
			Hlayout hl = new Hlayout();
			// label for create button
			String label = "";
			for (int i = 0; i < len; i++) {
				char ch = _charSeq.charAt(i);
				if (ch != '[') {
					// single char
					label = ch + "";
				} else {
					// find ... in [...]
					while (i < len) {
						i++;
						ch = _charSeq.charAt(i);
						if (ch == ']') {
							break;
						}
						sb.append(ch);
					}
					label = sb.toString();
					sb.setLength(0);
				}

				if ("break".equals(label)) {
					// break line
					hl.setParent(pp);
					hl = new Hlayout();
				} else {
					// create button
					createButton(label).setParent(hl);
					if (i+1 == len) {
						hl.setParent(pp);
					}
				}
			}
		}
	}

	private Button createButton (String label) {
		Button btn = new Button(label);
		String doOriginalClickAndFindParentPopup = "function (evt) {\n" +
							"		this.$doClick_(evt);\n" + // do original function
							"		var p = this.parent;\n" + // find the parent popup
							"		while (p && !p.$instanceof(zul.wgt.Popup))\n" +
							"			p = p.parent;\n" +
							"		if (p)\n";
		if ("Del".equals(label)) {
			btn.setWidgetOverride("doClick_",
					doOriginalClickAndFindParentPopup +
					"	p.executeDelete()\n" + // call delete function
					"}");
		} else if ("CapsLock".equals(label)) {
			btn.setSclass("capslock-btn");
			btn.setWidgetOverride("doClick_", 
					doOriginalClickAndFindParentPopup +
					"	p.switchCapsLock()\n" + // call switch (enable/disable) caps lock function
					"}");
		} else {
			if ("\\".equals(label)
				|| "'".equals(label)) {
				// special char, need one more escape char at client side
				label = "\\" + label;
			}
			btn.setWidgetOverride("doClick_",
					doOriginalClickAndFindParentPopup +
					"	p.executeInsert('"+label+"')\n" + // insert label
					"}");
		}

		return btn;
	}
	private void updateOpen (boolean open) {
		_open = open;
	}
}
