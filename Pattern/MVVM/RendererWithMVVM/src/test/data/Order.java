package test.data;

/**
 * Order class contains item (the Item class) and item index
 * (item index received while order that item) 
 * @author benbai123
 *
 */
public class Order {
	private Item _item;
	private int _itemIndex;

	public Order (Item item, int itemIndex) {
		_item = item;
		_itemIndex = itemIndex;
	}
	public Item getItem () {
		return _item;
	}
	public int getItemIndex () {
		return _itemIndex;
	}
}
