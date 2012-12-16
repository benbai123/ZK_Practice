package test.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

import test.data.Item;

/**
 * Tested with ZK 6.0.2
 * @author benbai123
 *
 */
public class OrderEvent extends Event {
	private static final long serialVersionUID = 3645653880934243558L;

	private final String _itemName;
	// itemIndex denotes the index to restore the amount
	private final int _itemIndex;
	private final int _amount;

	public static OrderEvent getOrderEvent (Component target, Item data, int index) {
		return new OrderEvent("onOrderEvent", target, data.getItemName(), index, data.getAmount());
	}
	public OrderEvent (String name, Component target, String itemName, int itemIndex, int amount) {
		super(name, target);
		_itemName = itemName;
		_itemIndex = itemIndex;
		_amount = amount;
	}
	public String getItemName () {
		return _itemName;
	}
	public int getItemIndex () {
		return _itemIndex;
	}
	public int getAmount () {
		return _amount;
	}
}