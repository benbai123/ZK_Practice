package test.listbox.composer;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import test.data.bean.Person;
import test.listbox.renderer.PersonListitemRenderer;

/**
 * Tested with ZK 6.0.2
 * @author benbai
 *
 */
public class ListboxModelRendererComposer extends GenericForwardComposer {
	Listbox lbOne;
	Listbox lbTwo;
	Listbox lbThree;

	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);

		// set models and render to listbox after comopsed
		lbOne.setModel(getSimpleStringModel());
		lbTwo.setModel(getPersonModel());
		lbThree.setModel(getPersonModel());

		lbThree.setItemRenderer(new PersonListitemRenderer());
	}

	public ListModel getSimpleStringModel () {
		List l = new ArrayList();

		// simply add some Strings to a list
		// than wrap the list by a ListModelList
		l.add("data one");
		l.add("data two");
		l.add("data three");

		return new ListModelList(l);
	}
	public ListModel getPersonModel () {
		List l = new ArrayList();

		// simply add some Persons to a list
		// than wrap the list by a ListModelList
		l.add(new Person("First Name One", "Last Name One", 21));
		l.add(new Person("First Name Two", "Last Name Two", 22));
		l.add(new Person("First Name Three", "Last Name Three", 23));

		return new ListModelList(l);
	}
}