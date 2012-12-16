package test.data;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import test.event.OrderEvent;

/**
 * The shoppingcart contains the item model and order model
 * also handle the orderItem and cancelOrder here
 * 
 * @author benbai123
 *
 */
public class ShoppingCart {
	private ListModelList<Item> _itemModel;
	private ListModelList<Order> _orderModel;

	public ListModel<Item> getItemModel () {
		if (_itemModel == null) {
			List<Item> items = new ArrayList<Item>();
			items.add(new Item("Item One", 5));
			items.add(new Item("Item Two", 3));
			items.add(new Item("Item Three", 10));
			_itemModel = new ListModelList<Item>(items);
		}
		return _itemModel;
	}
	public ListModel<Order> getOrderModel () {
		if (_orderModel == null) {
			_orderModel = new ListModelList<Order>(new ArrayList<Order>());
		}
		return _orderModel;
	}

	public void orderItem (OrderEvent event) {
		addOrder (event.getItemName(), event.getAmount(), event.getItemIndex());
		decreaseItemAmount (event.getItemIndex(), event.getAmount());
	}

	public void cancelOrder (OrderEvent event) {
		removeOrder (event.getItemName());
		increaseItemAmount (event.getItemIndex(), event.getAmount());
	}
	private void decreaseItemAmount (int index, int amount) {
		Item item = _itemModel.getElementAt(index);
		item.setAmount(item.getAmount() - amount);
	}
	private void increaseItemAmount (int index, int amount) {
		Item item = _itemModel.getElementAt(index);
		item.setAmount(item.getAmount() + amount);
	}
	private void addOrder (String itemName, int amount, int itemIndex) {
		Order order = getOrderByName(itemName);
		if (order != null) {
			Item item = order.getItem();
			item.setAmount(item.getAmount() + amount);
		} else {
			order = new Order(new Item(itemName, amount), itemIndex);
			_orderModel.add(order);
		}
	}
	private void removeOrder (String itemName) {
		Order order = getOrderByName(itemName);;
		if (order != null) {
			_orderModel.remove(order);
		}
	}
	private Order getOrderByName (String itemName) {
		int size = _orderModel.getSize();
		Order order;
		for (int i = 0; i < size; i++) {
			order = _orderModel.getElementAt(i);
			if (itemName.equals(order.getItem().getItemName())) {
				return order;
			}
		}
		return null;
	}
}
