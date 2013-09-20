package helper;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

/** Tested with ZK 6.5.2
 * 
 * @author benbai123
 *
 */
public class Param extends Div {

	private static final long serialVersionUID = 8842930664701238051L;

	private String _name = "";
	private String _value = "";
	private String _target;
	private String _oldValue = "";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Param () {
		// update target url while created
		addEventListener(Events.ON_CREATE, new EventListener () {
			public void onEvent (Event event) {
				updateTargetUrl();
			}
		});
		// do not output any html
		setWidgetOverride ("redraw", "function (out) {}");
	}
	// setters
	public void setTarget (String target) {
		_target = target;
	}
	public void setName (String name) {
		if (name == null) {
			name = "";
		}
		_name = name;
	}
	public void setValue (String value) {
		if (value == null) {
			value = "";
		}
		_value = value;
		updateTargetUrl();
	}

	private void updateTargetUrl () {
		if (!_name.isEmpty() && !_value.isEmpty()) {
			Component target = findTarget();
			if (target instanceof A) {
				A link = (A)target;
				String url = updateUrl(link.getHref());
				link.setHref(url);
			} else if (target instanceof Button) {
				Button btn = (Button)target;
				String url = updateUrl(btn.getHref());
				btn.setHref(url);
			} else if (target instanceof Image) {
				Image img = (Image)target;
				String url = updateUrl(img.getSrc());
				img.setSrc(url);
			}
		}
	}
	// try to find target to update
	private Component findTarget () {
		if (_target != null && !_target.isEmpty()) {
			return getParent().getFellowIfAny(_target);
		} else if (getParent() instanceof A) {
			return getParent();
		}
		return null;
	}
	// update url with param/value
	private String updateUrl (String url) {
		String value;
		if (url.indexOf("?") == -1) {
			url += "?";
		} else {
			url += "&";
		}
		value = _name + "=" + _value;
		// replace old value or append new value at tail
		if (!_oldValue.isEmpty()) {
			url.replace(_oldValue, value);
		} else {
			url += value;
		}
		return url;
	}
}
