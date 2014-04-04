package zk.angular.components;

import javax.servlet.ServletException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;

/** Pane component
 * Extends NgComp so only need to take care of ZK part
 * 
 * @author benbai123
 *
 */
public class Pane extends NgComp {
	private static final long serialVersionUID = -2579851840773831931L;
	private String _title;
	public Pane () {
		setNgTag("pane");
		try {
			// default template url
			setNgTemplateUrl("~./ngzk/templates/tab/pane.html");
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	/** check parent
	 * 
	 */
	public void beforeParentChanged (Component parent) {
		if (parent != null
				&& !(parent instanceof Tabs))
			throw new UiException("Pane should under Tabs !");
	}
	public void setTitle (String title) {
		if (title == null) {
			title = "";
		}
		if (!title.equals(_title)) {
			_title = title;
			smartUpdate("title", _title);
		}
	}
	public String getTitle () {
		return _title;
	}
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		render(renderer, "title", _title == null? "" : _title);
	}
}
