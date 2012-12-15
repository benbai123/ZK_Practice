package test.listbox.renderer;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;

import test.data.bean.Person;

/**
 * Tested with ZK 6.0.2
 * @author benbai
 *
 */
public class PersonListitemRenderer implements ListitemRenderer {
	/**
	 * The method to implements of a renderer,
	 * will be called by listbox automatically while render items
	 */
	public void render (Listitem listitem, Object value, int index) {
		Person p = (Person)value;

		// keep value in listitem
		listitem.setValue(value);

		addListcell(listitem, p.getFirstName());
		addListcell(listitem, p.getLastName());
		addListcell(listitem, p.getAge() + "");
		addListcell(listitem, p.getFullName());

		// create list headers while render first item
		if (index == 0) {
			renderListheads(listitem.getListbox());
		}
	}
	private void addListcell (Listitem listitem, String value) {
		Listcell lc = new Listcell ();
		Label lb = new Label(value);
		lb.setParent(lc);
		lc.setParent(listitem);
	}
	private void renderListheads (Listbox listbox) {
		Listhead lh = new Listhead();

		new Listheader("First Name").setParent(lh);
		new Listheader("Last Name").setParent(lh);
		new Listheader("Age").setParent(lh);
		new Listheader("Full Name").setParent(lh);

		lh.setParent(listbox);
	}
}