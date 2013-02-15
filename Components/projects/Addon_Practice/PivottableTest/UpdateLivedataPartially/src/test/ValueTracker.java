package test;

/**
 * tracker used to track the change of value
 * @author benbai123
 *
 */
public class ValueTracker {
	Number _oldValue;
	Number _latestValue;
	public ValueTracker (Number oldValue) {
		_oldValue = oldValue;
	}
	public void setOldValue (Number oldValue) {
		_oldValue = oldValue;
	}
	public Number getOldValue () {
		return _oldValue;
	}
	public void setLatestValue (Number latestValue) {
		_latestValue = latestValue;
	}
	public Number getLatestValue () {
		return _latestValue;
	}
	public boolean isValueChanged (Number newValue) {
		// no value -> has value or
		// value different with old one or
		// value different with latest one
		return (newValue != null && _oldValue == null)
			|| (newValue != null  && newValue.doubleValue() != _oldValue.doubleValue())
			|| (_latestValue != null && newValue.doubleValue() != _latestValue.doubleValue());
	}
}
