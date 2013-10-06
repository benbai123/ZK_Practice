package impl.serverpush;

import java.io.Serializable;

/** Binding that bind a field of a component to specific context for WebSocket ServerPush
 * 
 * @author benbai123
 *
 */
public class Binding implements Serializable {
	private static final long serialVersionUID = -1591783705902661276L;
	/** field to bind of component */
	private String _field;
	/** context to bind with field */
	private String _context;
	// Constructor
	public Binding (String field, String context) {
		if (field == null) {
			field = "";
		}
		if (context == null) {
			context = "";
		}
		_field = field;
		_context = context;
	}
	// getters, setters
	public void setField (String field) {
		if (field == null) {
			field = "";
		}
		_field = field;
	}
	public String getField () {
		return _field;
	}
	public void setContext (String context) {
		if (context == null) {
			context = "";
		}
		_context = context;
	}
	public String getContext () {
		return _context;
	}
	// super
	public int hashCode () {
		return (_field + _context).hashCode() * 31;
	}
	public boolean equals (Object obj) {
		if (obj != null
				&& (obj instanceof Binding)) {
			Binding b2 = (Binding) obj; 
			return (_field.equals(b2.getField())
					&& _context.equals(b2.getContext()));
		}
		return false;
	}
}
