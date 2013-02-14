package test;

import org.zkoss.pivot.Pivottable;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.event.Events;

public class CustomPivottable  extends Pivottable {

	private static final long serialVersionUID = 4770700380255057252L;

	public void service(AuRequest request, boolean everError) {
		String cmd = request.getCommand();
		super.service(request, everError);
		// post onPivotNodeOpen event
		if ("onPivotNodeOpen".equals(cmd)) {
			Events.postEvent("onPivotNodeOpen", this, request.getData());
		}
	}
}