package test;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Event used to pass the information of updated filter
 * 
 * Event name is "onFilterChanged"
 * 
 * Tested with ZK 6.0.2 EE and ZK Pivottable 2.0.0
 *
 * @author benbai123
 *
 */
public class FilterChangedEvent extends Event {

	private static final long serialVersionUID = 5055917746546499563L;
	/**
	 * whether a value is checked in filter list,
	 * true: checked (denotes this value is allowed)
	 * false: not checked (denotes this value is not allowed)
	 */
	private boolean _checked = true;
	/**
	 * the updated filter value
	 */
	private Object _value;
	/**
	 * the field related to the updated filter
	 */
	private String _fieldName;
	// constructor, 
	public FilterChangedEvent (Component target, String fieldName, Object value, boolean checked) {
		super("onFilterChanged", target);
		_checked = checked;
		_fieldName = fieldName;
		_value = value;
	}
	public String getFieldName () {
		return _fieldName;
	}
	public Object getValue () {
		return _value;
	}
	public boolean isChecked () {
		return _checked;
	}
}
