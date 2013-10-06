package impl.serverpush;

import impl.TestWebSocketServlet;

import java.beans.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;

/** Tested with ZK 6.5.2<br>
 * 
 * Utilities for ServerPush with WebSocket, concept is similar to
 * ServerPushWithWebSocket -- Component Oriented Version, rewritten it to
 * Desktop Oriented Version.
 * 
 * @author benbai123
 *
 */
@SuppressWarnings("unchecked")
public class ServerPushUtil {
	/** keep all binded Desktop */
	private static List<Desktop> bindedDesktops = new ArrayList<Desktop>();
	/** Lock for push to all desktops */
	private static Integer LOCK_FOR_PUSH_TO_ALL = 0;
	/** Lock for access bindedDesktops */
	private static Integer LOCK_FOR_ACCESS_BINDED_DESKTOPS = 0;
	/** attribute kay for binding map */
	private static final String BINDING_MAP = "BINDING_MAP_FOR_WEBSOCKET_SERVERPUSH";

	/** Add a Binding to a Component
	 * 
	 * @param comp Component to bind member field with update context
	 * @param field field of component to bind
	 * @param context context to bind with field
	 * @return Binding the added Binding object
	 */
	public static Binding addContextBinding (Component comp, String field, String context) {
		Binding binding = null;
		synchronized (comp.getDesktop()) {
			// get binding list of specified component
			List<Binding> bindingList = getBindingList(comp);
			// create a binding object with specified field/context
			binding = new Binding(field, context);
			// add binding into binding list if not exists
			if (!bindingList.contains(binding)) {
				bindingList.add(binding);
			}
		}
		// return created binding
		return binding;
	}
	/** Remove a Binding from a Component
	 * 
	 * @param comp Component to remove Binding
	 * @param field field of the field-context pair to remove
	 * @param context context of the field-context pair to remove
	 */
	public static void removeContextBinding (Component comp, String field, String context) {
		synchronized (comp.getDesktop()) {
			Map<Component, List<Binding>> bindingMap = getBindingMap(comp.getDesktop());
			// get binding list of specified component
			List<Binding> bindingList = bindingMap.get(comp);
			if (bindingList != null) {
				// create a binding object with specified field/context
				Binding target = new Binding (field, context);
				// remove binding if exists
				for (Binding b : bindingList) {
					if (b.equals(target)) {
						bindingList.remove(b);
						break;
					}
				}
			}
		}
	}

	/** Push a value to a context for all desktops 
	 * 
	 * @param value value to push
	 * @param context context to push to
	 * @throws Exception whatever
	 */
	public static void pushVlaue (Object value, String context) throws Exception {
		synchronized (LOCK_FOR_PUSH_TO_ALL) {
			// used to store dead desktop
			List<Desktop> deadDesktops = new ArrayList<Desktop>();
			// make a copy to reduce lock
			List<Desktop> desktopToPush = new ArrayList<Desktop>();
			synchronized (LOCK_FOR_ACCESS_BINDED_DESKTOPS) {
				desktopToPush.addAll(bindedDesktops);
			}
			// for each binded desktop
			for (Desktop bindedDesktop : desktopToPush) {
				if (bindedDesktop.isAlive()) {
					// push value to context if alive
					execPush(value, context, bindedDesktop);
				} else {
					// store it to deadDesktops otherwise
					deadDesktops.add(bindedDesktop);
				}
			}
			// remove all dead desktops
			synchronized (LOCK_FOR_ACCESS_BINDED_DESKTOPS) {
				bindedDesktops.removeAll(deadDesktops);
			}
			desktopToPush.removeAll(deadDesktops);
			for (Desktop dt : desktopToPush) {
				// send response via WebSocket
				TestWebSocketServlet.sendResponse(dt);
			}
		}
	}
	/** Push a value to a context for specified desktop
	 * 
	 * @param value value to push
	 * @param context context to push to
	 * @param desktop desktop to apply this push
	 * @throws Exception whatever
	 */
	public static void pushVlaue (Object value, String context, Desktop desktop) throws Exception {
		if (desktop.isAlive()) {
			// push value to context for specified desktop if alive
			execPush(value, context, desktop);
		} else {
			synchronized (LOCK_FOR_ACCESS_BINDED_DESKTOPS) {
				// remove specified desktop from bindedDesktops otherwise
				bindedDesktops.remove(desktop);
			}
		}
		// send response via WebSocket
		TestWebSocketServlet.sendResponse(desktop);
	}
	/** Apply "push value to context" for specified desktop
	 * 
	 * @param value value to push
	 * @param context context to push to
	 * @param desktop desktop to apply
	 * @throws Exception whatever
	 */
	public static void execPush (Object value, String context, Desktop desktop) throws Exception {
		// execute push if WebSocket of desktop is ready
		if (TestWebSocketServlet.isWebSocketReady(desktop)) {
			synchronized (desktop) {
				// get binding map
				// (Map<String, List> where String is component ID and List is Binding objects)
				Map<Component, List<Binding>> bindingMap = (Map<Component, List<Binding>>)desktop.getAttribute(BINDING_MAP);
				if (bindingMap != null) {
					for (Map.Entry<Component, List<Binding>> e : bindingMap.entrySet()) {
						// get component by ID
						Component comp = e.getKey();
						// get binding list
						List<Binding> bindings = e.getValue();
						// for each binding
						for (Binding binding : bindings) {
							// push value to component if
							// context of binding is equal to specified context
							if (binding.getContext().equals(context)) {
								// get field from binding
								String field = binding.getField();
								// build setter name
								String method = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
								// call setter to set value to component
								Statement stat = new Statement(comp, method, new Object[]{value});
								stat.execute();
							}
						}
					}
				}
			}
		}
	}
	/** Get Binding list of a Component
	 * 
	 * @param comp Component to get Binding list
	 * @return Binding list for specified Component
	 */
	private static List<Binding> getBindingList (Component comp) {
		// get binding map
		Map<Component, List<Binding>> bindingMap = getBindingMap(comp.getDesktop());
		// try to get binding list
		List<Binding> bindingList = bindingMap.get(comp);
		// create and add binding list if not exists
		if (bindingList == null) {
			bindingList = new ArrayList<Binding>();
			bindingMap.put(comp, bindingList);
		}
		// return (created) binding list
		return bindingList;
	}
	/** Get Binding map of a Desktop
	 * 
	 * @param dt specified Desktop
	 * @return Map<Component, List<Binding>> Binding map of specified Desktop
	 */
	private static Map<Component, List<Binding>> getBindingMap (Desktop dt) {
		// try to get binding map
		Map<Component, List<Binding>> bindingMap = (Map<Component, List<Binding>>)dt.getAttribute(BINDING_MAP);
		// create and add binding map if not exists
		if (bindingMap == null) {
			bindingMap = new Hashtable<Component, List<Binding>>();
			dt.setAttribute(BINDING_MAP, bindingMap);
			synchronized (LOCK_FOR_ACCESS_BINDED_DESKTOPS) {
				bindedDesktops.add(dt);
			}
		}
		// return (created) binding map
		return bindingMap;
	}
}
