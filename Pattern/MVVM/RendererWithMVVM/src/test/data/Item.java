package test.data;

/**
 * Item class contains item name and amount
 * @author benbai123
 *
 */
public class Item {
	private String _itemName;
	private int _amount;

	public Item (String itemName, int amount) {
		_itemName = itemName;
		_amount = amount;
	}
	public String getItemName () {
		return _itemName;
	}
	public void setAmount (int amount) {
		_amount = amount;
	}
	public int getAmount () {
		return _amount;
	}
}
