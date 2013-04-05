package test.marcro.component.keypad;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.impl.InputElement;

public class KeypadVM {
	// chars 
	private String _charSeq;
	private InputElement _inp;
	private boolean _open;

	private String _txtValue;
	private int _intValue;
	private double _doubleValue;

	public String getCharSeq () {
		return _charSeq;
	}
	public InputElement getInp () {
		return _inp;
	}
	public boolean isOpen () {
		return _open;
	}
	public void setTxtValue (String txtValue) {
		_txtValue = (txtValue == null? "" : txtValue);
	}
	public String getTxtValue () {
		return _txtValue;
	}
	public void setIntValue (Integer intValue) {
		_intValue = (intValue == null? 0 : intValue);
	}
	public Integer getIntValue () {
		return _intValue;
	}
	public void setDoubleValue (Double doubleValue) {
		_doubleValue = (doubleValue == null? 0.0 : doubleValue);
	}
	public Double getDoubleValue () {
		return _doubleValue;
	}
	@Command
	@NotifyChange ({"charSeq", "inp", "open"})
	public void focusInput (@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
		Component comp = event.getTarget();
		if (comp instanceof InputElement) {
			if (comp instanceof Intbox) {
				_charSeq = "123[break]456[break]789[break]0[Del]";
			} else if (comp instanceof Doublebox) {
				_charSeq = "123[break]456[break]789[break].0[Del]";
			} else { // textbox
				// note: wrap '[' by '[' and ']'
				_charSeq = "1234567890-=[break]QWERTYUIOP[[]]\\[break]ASDFGHJKL;'[CapsLock][break]ZXCVBNM,./[Del]";
			}
			_inp = (InputElement)comp;
			_open = true;
		}
	}
}
