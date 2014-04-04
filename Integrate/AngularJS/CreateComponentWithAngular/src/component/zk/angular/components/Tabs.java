package zk.angular.components;

import java.util.Map;

import javax.servlet.ServletException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;

/** Tabs component
 * Extends NgComp so only need to take care of ZK part
 * 
 * @author benbai123
 *
 */
@SuppressWarnings("rawtypes")
public class Tabs extends NgComp {

	private static final long serialVersionUID = -3566865325628275809L;

	/** index of selected pane */
	private int _selectedIndex = 0;
	// listen to onSelect event
	static {
		addClientEvent(Tabs.class, "onSelect",
				CE_IMPORTANT | CE_DUPLICATE_IGNORE | CE_NON_DEFERRABLE);
	}
	public Tabs () {
		setNgTag("tabs");
		try {
			// default template url
			setNgTemplateUrl("~./ngzk/templates/tab/tabs.html");
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	// setter/getter
	public void setSelectedIndex (int selectedIndex) {
		if (selectedIndex < 0) {
			selectedIndex = 0;
		}
		if (_selectedIndex != selectedIndex) {
			_selectedIndex = selectedIndex;
			smartUpdate("selectedIndex", _selectedIndex);
		}
	}
	public int getSelectedIndex () {
		return _selectedIndex;
	}
	/** check child
	 * 
	 */
	public void beforeChildAdded(Component child, Component insertBefore) {
		if (!(child instanceof Pane))
			throw new UiException("Tabs only accept Pane !");
	}
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		render(renderer, "selectedIndex", _selectedIndex);
	}
	// service and post event
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals("onSelect")) {
			Map data = request.getData(); // get data map
			Integer index = (Integer)data.get("index");
			_selectedIndex = index;
			Events.postEvent(cmd, this, data);
		} else {
			super.service(request, everError);
		}
	}
}
