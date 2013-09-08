package impl;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Sessions;

public class DesktopUtils {
	public static final String ATTRIBUTES_TO_UPDATE = "ATTRIBUTES_TO_UPDATE";
	public static final String REGISTERED_DESKTOPS = "REGISTERED_DESKTOPS";
	public static final String ALREADY_REGISTERED = "ALREADY_REGISTERED";

	public static void updateComponentProp (Component comp, String prop, String val) {
		String id = comp.getUuid();

		getPropMap(comp.getDesktop(), id).put(prop, val);
	}
	/** Register desktop so we can try to find it when
	 * WebSocket receive AU request from client side
	 */
	public static void register (Desktop desktop) {
		if (desktop.getAttribute(ALREADY_REGISTERED) == null) {
			getRegisteredDesktops(null).put(desktop.getId(), desktop);
			desktop.setAttribute(ALREADY_REGISTERED, ALREADY_REGISTERED);
		}
	}
	/** Get registered desktop
	 * 
	 * @param sess HttpSession
	 * @param id desktop ID
	 * @return Desktop
	 */
	public static Desktop getRegisteredDesktop (HttpSession sess, String id) {
		return getRegisteredDesktops(sess).get(id);
	}
	/** Remove registered desktop
	 * 
	 * @param sess HttpSession
	 * @param id desktop ID
	 */
	public static void removeRegisteredDesktop (HttpSession sess, String id) {
		getRegisteredDesktops(sess).remove(id);
	}
	/** Get Map for registered desktops
	 * 
	 * @param sess HttpSession
	 * @return Map<String, Desktop>
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Desktop> getRegisteredDesktops (HttpSession sess) {
		Map<String, Desktop> registeredDesktops = null;
		if (sess == null) {
			// try to find session if not specified
			sess = (HttpSession)Sessions.getCurrent().getNativeSession();
		}
		synchronized (sess) {
			// try to find Map for registered desktops
			registeredDesktops = (Map<String, Desktop>)sess.getAttribute(REGISTERED_DESKTOPS);
			if (registeredDesktops == null) {
				// create Map for registered desktops and store it
				// into session if not exists
				registeredDesktops = new Hashtable<String, Desktop>();
				sess.setAttribute(REGISTERED_DESKTOPS, registeredDesktops);
			}
		}
		return registeredDesktops;
	}
	/** Build AU response of a desktop
	 * 
	 * @param desktop the desktop to build AU response
	 * @return String response content
	 */
	public static String buildResponse (Desktop desktop) {
		Map<String, Map<String, String>> compMap = getAttributesToUpdate(desktop);
		// response JSON object
		JSONObject resp = new JSONObject();
		// for each component data
		for (Entry<String, Map<String, String>> entry : compMap.entrySet()) {
			Map<String, String> propMap = entry.getValue();
			// prop/value JSON object
			JSONObject propAndVal = new JSONObject();
			// for each prop/value pair
			for (Entry<String, String> propEntry : propMap.entrySet()) {
				propAndVal.put(propEntry.getKey(), propEntry.getValue());
			}
			resp.put(entry.getKey(), propAndVal);
		}
		// clear data
		getAttributesToUpdate(desktop).clear();
		return resp.toJSONString();
	}
	/** Get Map that contains properties to update of a component
	 * 
	 * @param desktop specific desktop
	 * @param id component id
	 * @return
	 */
	private static Map<String, String> getPropMap (Desktop desktop, String id) {
		// try to get propMap
		Map<String, Map<String, String>> compMap = getAttributesToUpdate(desktop);
		Map<String, String> propMap;
		propMap = compMap.get(id);
		if (propMap == null) {
			// create propMap if not exists
			propMap = new Hashtable<String, String>();
			compMap.put(id, propMap);
		}
		return propMap;
	}

	/** Get Map that contains components Map to update of a desktop
	 * 
	 * structure: componentMap<key, propertyValueMap<prop, value>>
	 * 
	 * @param desktop
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Map<String, String>> getAttributesToUpdate (Desktop desktop) {
		// try to get map
		Map<String, Map<String, String>> attributesToUpdate = 
			(Map<String, Map<String, String>>)desktop.getAttribute(ATTRIBUTES_TO_UPDATE);
		if (attributesToUpdate == null) {
			// create if not exists
			attributesToUpdate = new Hashtable<String, Map<String, String>>();
			desktop.setAttribute(ATTRIBUTES_TO_UPDATE, attributesToUpdate);
		}
		return attributesToUpdate;
	}
}
