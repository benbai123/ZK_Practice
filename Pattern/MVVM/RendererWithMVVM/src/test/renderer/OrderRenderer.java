package test.renderer;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import test.data.Item;
import test.data.Order;
import test.event.OrderEvent;

/**
 * Tested with ZK 6.0.2
 * 
 * OrderRenderer render Order class to listitem
 * @author benbai123
 *
 */
public class OrderRenderer implements ListitemRenderer<Order> {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void render (final Listitem listitem, final Order data, final int index) {
		final Listbox lb = listitem.getListbox();

		// get item from order
		Item item = data.getItem();
		// item name and ordered amount
		new Listcell(item.getItemName()).setParent(listitem);
		new Listcell(item.getAmount() + "").setParent(listitem);

		// button to perform the cancel action
		Listcell lc = new Listcell();
		Button cancelBtn = new Button("Cancel Order");
		cancelBtn.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent (Event event) {
				// post an OrderEvent to listbox
				// to inform cancel action with the
				// item name and the ordered amount to restore
				Events.postEvent(
					OrderEvent.getOrderEvent(lb, data.getItem(), data.getItemIndex())
				);
			}
		});
		cancelBtn.setParent(lc);
		lc.setParent(listitem);
	}
}