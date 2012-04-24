package test.data.bean;

public class Person {
	private String firstName = "";
	private String lastName = "";
	private int age = 1;

	public Person(String f, String l, int a) {
		firstName = (f == null)? "" : f;
		lastName = (l == null)? "" : l;
		age = (a < 1)? 1 : a;
	}

	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getFullName() {
		return firstName + " " + lastName;
	}
	public int getAge () {
		return age;
	}
	public boolean equals(Object obj) {
		if (obj instanceof Person) {
			Person np = (Person)obj;
			if (np.getFirstName().equals(getFirstName())
				&& np.getLastName().equals(getLastName())
				&& np.getAge() == getAge())
				return true;
		}
		return false;
	}
	public int hashCode () {
		return (getFirstName().getBytes().length
				+ getLastName().getBytes().length
				+ getAge()) % 313;
	}
}
