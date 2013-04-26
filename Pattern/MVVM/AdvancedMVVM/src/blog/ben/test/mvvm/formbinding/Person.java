package blog.ben.test.mvvm.formbinding;

public class Person {
	String _firstName;
	String _lastName;
	Integer _age;
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
	public void setAge (int age) {
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
