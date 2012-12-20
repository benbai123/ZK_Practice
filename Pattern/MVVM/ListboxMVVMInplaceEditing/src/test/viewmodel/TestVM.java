package test.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import test.data.Person;
import test.event.ModelDataChangeEvent;

/**
 * tested with ZK 6.0.2
 * @author ben
 *
 */
public class TestVM {
	private ListModelList<Person> _personModel;
	private int _selectedIndex = -1;
	/**
	 * The hard coded person model
	 * @return ListModelList contains several Person
	 */
	public ListModel<Person> getPersonModel () {
		if (_personModel == null) {
			List<Person> l = new ArrayList<Person>();
			l.add(new Person("First Name One", "Last Name One", 21));
			l.add(new Person("First Name Two", "Last Name Two", 23));
			l.add(new Person("First Name Three", "Last Name Three", 25));
			_personModel = new ListModelList<Person>(l);
		}
		return _personModel;
	}
	/**
	 * save the selected index while select a listitem
	 * @param selectedIndex
	 */
	public void setSelectedIndex (int selectedIndex) {
		_selectedIndex = selectedIndex;
	}
	/**
	 * update selected index while delSel
	 * @return
	 */
	public int getSelectedIndex () {
		return _selectedIndex;
	}
	/**
	 * return all Persons' data, can be considered as "save data"
	 * @return
	 */
	public String getCurrentData () {
		StringBuilder sb = new StringBuilder("");
		if (_personModel != null) {
			if (_selectedIndex >= 0) {
				Person p = _personModel.getElementAt(_selectedIndex);
				if (isValidPerson(p)) {
					sb.append("Selected Person: \r\n")
						.append(p.getFirstName()).append(" ")
						.append(p.getLastName()).append(", age = ")
						.append(p.getAge()).append("\r\n\r\n");
				}
			}
			for (Person p : _personModel.getInnerList()) {
				if (isValidPerson(p)) {
					sb.append(p.getFirstName()).append(" ")
						.append(p.getLastName()).append(", age = ")
						.append(p.getAge()).append("\r\n");
				}
			}
		}
		return sb.toString();
	}
	/**
	 * add a new Person to _personModel,
	 * added to tail if no selected index,
	 * added below current selected item if selected idnex >= 0
	 */
	@Command
	public void addNew () {
		if (_selectedIndex >= 0) {
			_personModel.add(_selectedIndex+1, new Person("", "", null));
		} else {
			_personModel.add(new Person("", "", null));
		}
	}
	/**
	 * remove selected Person from model
	 */
	@Command
	@NotifyChange("_selectedIndex")
	public void delSel () {
		if (_selectedIndex >= 0) {
			_personModel.remove(_selectedIndex);
			_selectedIndex = -1;
		}
	}
	/**
	 * update the value of a Person based on the index and Data
	 * in the ModelDataChangeEvent
	 * @param event ModelDataChangeEvent contains the information with respect the index and changed data
	 */
	@Command
	public void updateModelData (
			@ContextParam(ContextType.TRIGGER_EVENT) ModelDataChangeEvent event) {
		Person oldPerson = _personModel.get(event.getIndex());;
		Person newPerson = (Person)event.getData();
		oldPerson.setFirstName(newPerson.getFirstName());
		oldPerson.setLastName(newPerson.getLastName());
		oldPerson.setAge(newPerson.getAge());
	}
	/**
	 * simply notify that the currentData should be updated
	 */
	@Command
	@NotifyChange("currentData")
	public void displayCurrentData () {
		
	}
	/**
	 * confirm all fields of a Person is valid
	 * @param p the Person to check
	 * @return whether all fields of the Person is valid
	 */
	private boolean isValidPerson (Person p) {
		return !(p.getFirstName() == null
				|| p.getFirstName().isEmpty()
				|| p.getLastName() == null
				|| p.getLastName().isEmpty()
				|| p.getAge() == null
				|| p.getAge() < 0);
	}
}
