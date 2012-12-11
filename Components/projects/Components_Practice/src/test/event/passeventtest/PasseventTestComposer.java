package test.event.passeventtest;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

/**
 * Tested with ZK 6.0.2
 * 
 * Test passing event with data from one component to another
 * 
 * This seems a little bit weird and useless here
 * but this would be helpful in some situation
 * such like implement inplace editing of grid with renderer
 * under MVVM
 * (will be described in another article later)
 * 
 * @author benbai
 *
 */
public class PasseventTestComposer extends SelectorComposer {
	@Wire
	Label lb;

	/**
	 * Post an event with the new value to label
	 * while the value of textbox is changed
	 * instead of modify the value of label directly
	 * @param event
	 */
	@Listen("onChange=#tbx")
	public void onChange$tbx (InputEvent event) {
		Map data = new HashMap();
		data.put("value", event.getValue());
		Events.postEvent("onValueChange", lb, data);
	}
	/**
	 * Really modify the value of label here
	 * @param event
	 */
	@Listen("onValueChange=#lb")
	public void onValueChange$lb (Event event) {
		Map data = (Map)event.getData();
		String value = (String)data.get("value");
		lb.setValue(value);
	}
}