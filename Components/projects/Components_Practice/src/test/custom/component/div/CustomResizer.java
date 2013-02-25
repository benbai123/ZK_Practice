package test.custom.component.div;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.impl.XulElement;

import test.custom.component.event.CustomResizeEvent;

/**
 * Tested with ZK 6.0.2
 * @author benbai123
 *
 */
public class CustomResizer extends Div {
	private static final long serialVersionUID = 5597493971151879186L;

	static {
		// listen to custom resize event
		addClientEvent(CustomResizer.class, CustomResizeEvent.ON_CUSTOM_RESIZE, CE_DUPLICATE_IGNORE
				| CE_IMPORTANT | CE_NON_DEFERRABLE);
	}
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();

		// custom resize event
		if (CustomResizeEvent.ON_CUSTOM_RESIZE.equals(cmd)) {
			CustomResizeEvent evt = CustomResizeEvent.getCustomResizeEvent(request);
			// get the component to resize
			Component ref = evt.getReference();
			if (ref != null) {
				// get new size
				int value = evt.getValue();
				// get direction (width/height)
				String dir = evt.getResizeAttribute();
				// do resize
				if ("width".equals(dir)) {
					if (ref instanceof XulElement) {
						((XulElement)ref).setWidth(value+"px");
					} else if (ref instanceof HtmlBasedComponent) {
						((HtmlBasedComponent)ref).setWidth(value+"px");
					} 
				} else if ("height".equals(dir)) {
					if (ref instanceof XulElement) {
						((XulElement)ref).setHeight(value+"px");
					} else if (ref instanceof HtmlBasedComponent) {
						((HtmlBasedComponent)ref).setHeight(value+"px");
					} 
				}
			}
			// post event
			Events.postEvent(evt);
		} else {
			super.service(request, everError);
		}
	}
}
