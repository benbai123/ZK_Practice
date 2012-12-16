package test.renderer;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;

import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Listitem;

import test.data.Item;
import test.event.OrderEvent;

/**
 * Tested with ZK 6.0.2
 * 
 * ItemRenderer render Item class to listitem
 * @author benbai123
 *
 */
public class ItemRenderer implements ListitemRenderer<Item> {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void render (final Listitem listitem, final Item data, final int index) {
		final Listbox lb = listitem.getListbox();
		// item name and available amount
		new Listcell(data.getItemName()).setParent(listitem);
		new Listcell(data.getAmount() + "").setParent(listitem);

		// intbox for input the amount to order
		Listcell lc = new Listcell();
		final Intbox intbox = new Intbox();
		intbox.setParent(lc);
		lc.setParent(listitem);

		// button to perform the order action
		lc = new Listcell();
		Button orderBtn = new Button("Order Item");
		orderBtn.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent (Event event) {
				// if the value is not grater zero or over the available amount
				// show alert message
				int amount = intbox.getValue() == null? 0 : intbox.getValue();
				if (amount <= 0) {
					Clients.alert(" positive value only");
				} else if (amount > data.getAmount()) {
					Clients.alert(" over available amount ");
				} else {
					// post an OrderEvent to listbox
					// to inform order action with the
					// item name and the amount to order
					Item item = new Item(data.getItemName(), amount);
					Events.postEvent(OrderEvent.getOrderEvent(lb, item, index));
				}
			}
		});
		orderBtn.setParent(lc);
		lc.setParent(listitem);
	}
}