package test.data.bean;

public class Person {
	private String _firstName = "";
	private String _lastName = "";
	private int _age = 1;

	public Person(String firstName, String lastName, int age) {
		_firstName = firstName;
		_lastName = lastName;
		_age = age;
	}

	public String getFirstName() {
		return _firstName;
	}
	public String getLastName() {
		return _lastName;
	}
	public String getFullName() {
		return _firstName + " " + _lastName;
	}
	public int getAge () {
		return _age;
	}
}