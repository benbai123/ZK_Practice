package test.data;

public class Person {
	private String _firstName;
	private String _lastName;
	private Integer _age;

	public Person (String firstName, String lastName, Integer age) {
		_firstName = firstName;
		_lastName = lastName;
		_age = age;
	}
	public void setFirstName (String firstName) {
		_firstName = firstName;
	}
	public void setLastName (String lastName) {
		_lastName = lastName;
	}
	public void setAge (Integer age) {
		_age = age;
	}
	public String getFirstName () {
		return _firstName;
	}
	public String getLastName () {
		return _lastName;
	}
	public Integer getAge () {
		return _age;
	}
}
