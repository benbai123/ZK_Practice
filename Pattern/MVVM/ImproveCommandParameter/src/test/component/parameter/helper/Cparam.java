package test.component.parameter.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

/** helper tag to bind data with event name
 * 
 * @author benbai123
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Cparam  extends Div {

	private static final long serialVersionUID = -7156149643515776677L;
	/** event name */
	private String _name = "";
	/** data that binded to event name */
	private Object _data;
	/** index of stored data in command data list */
	private int _index = -1;
	/** whether keep this component after created */
	private boolean _keep;
	/** used to get command parameter map */
	public static final String COMMAND_PARAMETER_KEYWORD = "COMMAND_PARAMETER_KEYWORD";

	/** the component to bind */
	private Component _foundTarget;

	public Cparam () {
		// do not output any html
		setWidgetOverride ("redraw", "function (out) {}");
	}

	// setters
	public void setName (String name) {
		if (name == null) {
			name = "";
		}
		if (!name.equals(_name)) {
			_name = name;
		}
	}
	public void setData (Object data) {
		if (data != null
			&& !data.equals(_data)) {
			_data = data;
			storeCommandData();
		}
	}
	public void setKeep (boolean keep) {
		_keep = keep;
	}

	/** get stored command data list
	 * 
	 * @param comp component to get data list
	 * @param name command name
	 * @return
	 */
	public static List getCommandData (Component comp, String name) {
		return getBindList(comp, name, false);
	}
	/** event listener
	 * bind data to event name then detach if needed
	 */
	public void onCreate () {
		storeCommandData();
		// detach if not keep
		if (!_keep) {
			detach();
		}
	}
	/** Update Binding of target Component
	 */
	private void storeCommandData () {
		// has name and data
		if (!_name.isEmpty() && _data != null) {
			_foundTarget = findTarget();
			if (_foundTarget != null) {
				// get bind list
				List bl = getBindList(_foundTarget, _name, true);
				if (_index == -1) { // not stored
					// update index and store data
					_index = bl.size();
					bl.add(_data);
				} else {
					// update data
					bl.set(_index, _data);
				}
			}
		}
	}
	/** Get binded command data list
	 * 
	 * @param comp component to get data list
	 * @param name event name
	 * @param autoCreate whether create map and list automatically if not exists
	 * @return
	 */
	private static List<Cparam> getBindList (Component comp, String name, boolean autoCreate) {
		// try to get map of command data list
		Map m = (Map)comp.getAttribute(COMMAND_PARAMETER_KEYWORD);
		List l = null;
		if (m == null) { // map does not exist
			if (autoCreate) { // create it
				m = new HashMap();
				// and store it to component
				comp.setAttribute(COMMAND_PARAMETER_KEYWORD, m);
			}
		}
		if (m != null) { // map exists or created
			// try to get command data list
			l = (List)m.get(name);
			if (l == null) { // list not exists
				if (autoCreate) { // create it
					l = new ArrayList();
					// and store it to map
					m.put(name, l);
				}
			}
		}
		return l;
	}
	/* package */ Component getFoundTarget () {
		return _foundTarget;
	}
	/** Try to find target to update
	 * The order to try: <br>
	 * 1. Try to find whether previous sibling is Component.<br>
	 * 2. Try to find whether previous sibling is Cparam and already found a target.<br>
	 * 3. Try to find whether parent is Component.<br>
	 * 
	 * @return Component if any
	 */
	/* package */ Component findTarget () {
		Component previous = getPreviousSibling();

		// return directly if already found
		if (_foundTarget != null) {
			return _foundTarget;
		}
		// Try to find whether previous sibling is Component.
		if (previous instanceof Component
			&& !(previous instanceof Cparam)) {
			return previous;
		}
		// Try to find whether previous sibling is Cparam and already found a target.
		if (previous instanceof Cparam) {
			Component previousTarget = ((Cparam) previous).getFoundTarget();
			if (previousTarget != null) {
				return previousTarget;
			}
		}
		// Try to find whether parent is Component.
		if (getParent() instanceof Component) {
			return getParent();
		}
		return null;
	}
}