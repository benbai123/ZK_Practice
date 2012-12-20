package test.data;

import java.util.Comparator;

/**
 * the Comparator to compare a Person of fields firstName,
 * lastName or age in ascending or descending order
 * @author ben
 *
 */
public class PersonComparator implements Comparator<Person> {
	private boolean _asc;
	private String _field;

	/**
	 * Constructor
	 * @param asc true: ascending, false: descending
	 * @param field to compare, firstName, lastName or age
	 */
	public PersonComparator (boolean asc, String field) {
		_asc = asc;
		_field = field;
	}
	/**
	 * compare two person
	 */
	public int compare(Person p1, Person p2) {
		int result = 0,
			mult =  _asc ? 1 : -1;
		result = "firstName".equals(_field)?
					p1.getFirstName().compareTo(p2.getFirstName()) * mult
				: "lastName".equals(_field)?
					p1.getLastName().compareTo(p2.getLastName()) * mult
				: "age".equals(_field)?
					compareAge(p1.getAge(), p2.getAge()) * mult
				: 0;
		return result;
	}
	/**
	 * function for compare age
	 */
	private int compareAge (Integer a1, Integer a2) {
		int a = (a1 == null || a1 < 0)? 0 : a1.intValue();
		int b = (a2 == null || a2 < 0)? 0 : a2.intValue();
		return a > b? 1
				: a < b? -1
				: 0;
	}
}
