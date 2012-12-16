package test.viewmodel;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModel;

import test.data.Item;
import test.data.Order;
import test.data.ShoppingCart;
import test.event.OrderEvent;

/**
 * Tested with ZK 6.0.2
 * 
 * The VM interact with the view
 * 
 * @author benbai123
 *
 */
public class TestVM {
	ShoppingCart _cart = new ShoppingCart();

	public ListModel<Item> getItemModel () {
		return _cart.getItemModel();
	}
	public ListModel<Order> getOrderModel () {
		return _cart.getOrderModel();
	}
	@Command
	@NotifyChange({"itemModel", "orderModel"})
	public void orderItem (
			@ContextParam(ContextType.TRIGGER_EVENT) OrderEvent event) {
		_cart.orderItem(event);
	}
	@Command
	@NotifyChange({"itemModel", "orderModel"})
	public void cancelOrder (
			@ContextParam(ContextType.TRIGGER_EVENT) OrderEvent event) {
		_cart.cancelOrder(event);
	}
}