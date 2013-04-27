package blog.ben.test.mvvm.formbinding;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
/**
 * tested with ZK 6.0.2
 * @author benbai123
 *
 */
public class FormBindingTestVM {
	private Person _person;
	public Person getPerson () {
		if (_person == null) {
			_person = new Person("Ben", "Bai", 123); // fake
		}
		return _person;
	}
	public String getPersonContent () {
		return _person.getFirstName() + " " + _person.getLastName() + ", age = " + _person.getAge();
	}
	/**
	 * empty function as a command for
	 * save in form binding and update personContent
	 */
	@Command
	@NotifyChange("personContent")
	public void savePerson () {
	}
}
