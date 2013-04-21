package test;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for hold limited values of a field
 * 
 * Tested with ZK 6.0.2 EE and ZK Pivottable 2.0.0
 *
 * @author benbai123
 *
 */
public class Limit {
	// the name to represent a specific field
	private String _fieldName;
	// the limited values
	private Set<Object> _limitedValues;
	public Limit (String fieldName, Set<Object> limitedValues) {
		_fieldName = fieldName;
		_limitedValues = limitedValues;
		if (_limitedValues == null) {
			_limitedValues = new HashSet<Object>();
		}
	}
	public String getFieldName () {
		return _fieldName;
	}
	public Set<Object> getLimitedValues () {
		return _limitedValues;
	}
}
