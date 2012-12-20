package test.renderer;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import test.data.Person;
import test.data.PersonComparator;
import test.event.ModelDataChangeEvent;

/**
 * tested with ZK 6.0.2
 * @author ben
 *
 */
public class InplaceEditingPersonRenderer implements ListitemRenderer<Person> {
	@SuppressWarnings("unchecked")
	/**
	 * render Person to a listitem
	 */
	public void render (Listitem listitem, Person data, int index) {
		Listbox listbox = listitem.getListbox();
		if (index == 0 && listbox.getListhead() == null) {
			createListhead().setParent(listbox);
		}
		listitem.setValue(data);
		
		addTextboxCell(listitem, data.getFirstName())
			.addEventListener(Events.ON_CHANGE, getFirstnameChangedListener(listbox, data, listitem));
		addTextboxCell(listitem, data.getLastName())
			.addEventListener(Events.ON_CHANGE, getLastnameChangedListener(listbox, data, listitem));
		Intbox ibx = addIntboxCell(listitem, data.getAge());
		ibx.addEventListener(Events.ON_CHANGE, getAgeChangedListener(listbox, ibx, data, listitem));
	}
	/**
	 * create listhead
	 * @param listbox, append the listhead to it
	 */
	private Listhead createListhead () {

		Listhead lh = new Listhead();
		createListheader("First Name", "firstName").setParent(lh);
		createListheader("Last Name", "lastName").setParent(lh);
		createListheader("Age", "age").setParent(lh);
		return lh;
	}
	/**
	 * create a listheader
	 * @param label the label of the listheader
	 * @param fieldToCompare the value to set to PersonComparator's 'field' field
	 * @return
	 */
	private Listheader createListheader (String label, String fieldToCompare) {
		Listheader lhr = new Listheader(label);
		lhr.setSortAscending(new PersonComparator(true, fieldToCompare));
		lhr.setSortDescending(new PersonComparator(false, fieldToCompare));
		return lhr;
	}
	/**
	 * add a listcell contains a textbox and return the textbox
	 * @param listitem the listitem to append this listcell
	 * @param value the value to set to the textbox
	 * @return Textbox the created textbox
	 */
	private Textbox addTextboxCell (Listitem listitem, String value) {
		Listcell lc = new Listcell();
		Textbox tbx = new Textbox(value);
		tbx.setParent(lc);
		lc.setParent(listitem);
		return tbx;
	}
	/**
	 * add a listcell contains an intbox and returh the intbox
	 * @param listitem the listitem to append this listcell
	 * @param value the value to set to the intbox
	 * @return Intbox the created intbox
	 */
	private Intbox addIntboxCell (Listitem listitem, Integer value) {
		Listcell lc = new Listcell();
		Intbox ibx = new Intbox();
		if (value != null) {
			ibx.setValue(value);
		}
		ibx.setParent(lc);
		lc.setParent(listitem);
		return ibx;
	}
	/**
	 * The EventListener for firstName changed
	 * @param listbox the listbox, post event to it
	 * @param oldData the original Person, need the lastName and age of it
	 * @param listitem the listitem contains the textbox of this event, need the index of it
	 * @return EventListener that listen to firstName changed
	 */
	@SuppressWarnings("rawtypes")
	private EventListener getFirstnameChangedListener (final Listbox listbox, final Person oldData, final Listitem listitem) {
		return new EventListener () {
			public void onEvent (Event event) {
				InputEvent ievent = (InputEvent)event;
				Events.postEvent(ModelDataChangeEvent
					.getModelDataChangeEvent(listbox,
						new Person(ievent.getValue(), oldData.getLastName(), oldData.getAge()),
						listitem.getIndex()));
			}
		};
	}
	/**
	 * The EventListener for lastName changed
	 * @param listbox the listbox, post event to it
	 * @param oldData the original Person, need the firstName and age of it
	 * @param listitem the listitem contains the textbox of this event, need the index of it
	 * @return EventListener that listen to lastName changed
	 */
	@SuppressWarnings("rawtypes")
	private EventListener getLastnameChangedListener (final Listbox listbox, final Person oldData, final Listitem listitem) {
		return new EventListener () {
			public void onEvent (Event event) {
				InputEvent ievent = (InputEvent)event;
				Events.postEvent(ModelDataChangeEvent
					.getModelDataChangeEvent(listbox,
						new Person(oldData.getFirstName(), ievent.getValue(), oldData.getAge()),
						listitem.getIndex()));
			}
		};
	}
	/**
	 * The EventListener for age changed
	 * @param listbox the listbox, post event to it
	 * @param oldData the original Person, need the firstName and lastName of it
	 * @param listitem the listitem contains the intbox of this event, need the index of it
	 * @return EventListener that listen to age changed
	 */
	@SuppressWarnings("rawtypes")
	private EventListener getAgeChangedListener (final Listbox listbox, final Intbox intbox, final Person oldData, final Listitem listitem) {
		return new EventListener () {
			public void onEvent (Event event) {
				Events.postEvent(ModelDataChangeEvent
					.getModelDataChangeEvent(listbox,
						new Person(oldData.getFirstName(), oldData.getLastName(), intbox.getValue()),
						listitem.getIndex()));
			}
		};
	}
}
