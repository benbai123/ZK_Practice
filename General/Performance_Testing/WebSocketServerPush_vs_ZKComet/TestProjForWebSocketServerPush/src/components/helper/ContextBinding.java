package components.helper;

import impl.serverpush.Binding;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

import components.serverpush.IWebSocketServerPushEnhancedComponent;

/** Tested with ZK 6.5.2
 * 
 * Helper component to help a component to bind field with ServerPush context
 * 
 * @author benbai123
 *
 */
public class ContextBinding extends Div {

	private static final long serialVersionUID = -7156149643515776677L;
	/** field to bind with context (required) */
	private String _field = "";
	/** context to bind with field (required) */
	private String _context = "";
	/** specified id of target component (optional) */
	private String _target;
	/** used to store current binding (relatively old) for changing field/context */
	private Binding _oldBinding = null;
	/** found target */
	private IWebSocketServerPushEnhancedComponent _foundTarget;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ContextBinding () {
		// update target url while created
		addEventListener(Events.ON_CREATE, new EventListener () {
			public void onEvent (Event event) {
				updateTargetBinding();
			}
		});
		// do not output any html
		setWidgetOverride ("redraw", "function (out) {}");
	}
	// setters
	public void setTarget (String target) {
		_target = target;
	}
	public void setField (String field) {
		if (field == null) {
			field = "";
		}
		if (!field.equals(_field)) {
			_field = field;
			updateTargetBinding();
		}
	}
	public void setContext (String context) {
		if (context == null) {
			context = "";
		}
		if (!context.equals(_context)) {
			_context = context;
			updateTargetBinding();
		}
	}

	/** Update Binding of target Component when field/context is changed
	 * 
	 */
	private void updateTargetBinding () {
		if (!_field.isEmpty() && !_context.isEmpty()) {
			_foundTarget = findTarget();
			if (_foundTarget != null) {
				// remove old binding if exists
				if (_oldBinding != null) {
					if (_field.equals(_oldBinding.getField())
							&& _context.equals(_oldBinding.getContext())) {
						// already binded, do nothing
						return;
					}
					_foundTarget.removeSocketContextBinding(_oldBinding.getField(), _oldBinding.getContext());
				}
				// add new binding and store it to _oldBinding
				_oldBinding = _foundTarget.addSocketContextBinding(_field, _context);
			}
		}
	}
	/* package */ IWebSocketServerPushEnhancedComponent getFoundTarget () {
		return _foundTarget;
	}
	/** Try to find target to update
	 * The order to try: <br>
	 * 1. Try to find fellow under the same space owner with specified "target" attribute.<br>
	 * 2. Try to find whether previous sibling is IWebSocketServerPushEnhancedComponent.<br>
	 * 3. Try to find whether previous sibling is ContextBinding and already found a target.<br>
	 * 4. Try to find whether parent is IWebSocketServerPushEnhancedComponent.<br>
	 * 
	 * This way it can work without target attribute in most cases.
	 * @return IWebSocketServerPushEnhancedComponent if any
	 */
	/* package */ IWebSocketServerPushEnhancedComponent findTarget () {
		Component comp;
		Component previous = getPreviousSibling();
		// Try to find fellow under the same space owner with specified "target" attribute.
		if (_target != null && !_target.isEmpty()) {
			comp = getSpaceOwner().getFellowIfAny(_target);
			if (comp != null
				&& (comp instanceof IWebSocketServerPushEnhancedComponent)) {
				return (IWebSocketServerPushEnhancedComponent) comp;
			}
		}
		// Try to find whether previous sibling is IWebSocketServerPushEnhancedComponent.
		if (previous instanceof IWebSocketServerPushEnhancedComponent) {
			return (IWebSocketServerPushEnhancedComponent) previous;
		}
		// Try to find whether previous sibling is ContextBinding and already found a target.
		if (previous instanceof ContextBinding) {
			IWebSocketServerPushEnhancedComponent previousTarget = ((ContextBinding) previous).getFoundTarget();
			if (previousTarget != null) {
				return previousTarget;
			}
		}
		// Try to find whether parent is IWebSocketServerPushEnhancedComponent.
		if (getParent() instanceof IWebSocketServerPushEnhancedComponent) {
			return (IWebSocketServerPushEnhancedComponent)getParent();
		}
		return null;
	}
}
