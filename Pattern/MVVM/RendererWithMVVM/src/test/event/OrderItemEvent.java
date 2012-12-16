package test.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public class OrderItemEvent extends Event {
	private final String _itemName;
	private final int _amount;

	public OrderItemEvent (String name, Component target, String itemName, int amount) {
		super("OrderItemEvent", target);
		_itemName = itemName;
		_amount = amount;
	}
	public String getItemName () {
		return _itemName;
	}
	public int getAmount () {
		return _amount;
	}
}
