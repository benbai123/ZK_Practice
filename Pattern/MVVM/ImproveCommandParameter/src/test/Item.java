package test;

/**
 * Dummy data
 * @author benbai123
 *
 */
public class Item {
	String _name = "";
	public Item (String name) {
		setName(name);
	}
	public void setName (String name) {
		if (name == null)
			name = "";
		_name = name;
	}
	public String getName () {
		return _name;
	}
}
