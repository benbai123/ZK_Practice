package blog.ben.test.mvvm.formbinding;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;

/**
 * tested with ZK 6.0.2
 * @author benbai123
 *
 */
public class FormBindingWithValidatorTestVM {
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
	/**
	 * get the validator that will do validation while save
	 * @return
	 */
	public Validator getFormValidator(){
	    return new AbstractValidator() {
	         
	        public void validate(ValidationContext ctx) {
	        	// get value from form context,
	        	// ctx.getProperties("firstName")[0].getValue() will
	        	// get the value that bind with @bind(fx.firstName)
	        	// in zul page
	        	String firstName = (String)ctx.getProperties("firstName")[0].getValue();
	        	String lastName = (String)ctx.getProperties("lastName")[0].getValue();
	        	Integer age = (Integer)ctx.getProperties("age")[0].getValue();
	        	if (firstName == null || firstName.isEmpty()) {
	        		// put error message into validationMessages map
	        		addInvalidMessage(ctx, "firstNameContentError", "firstName is required ");
	        	} else if (firstName.length() < 3) {
	            	addInvalidMessage(ctx, "firstNameError", "firstName at least 3 chars ");
	            }
	            if (lastName == null || lastName.isEmpty()) {
	        		addInvalidMessage(ctx, "lastNameContentError", "lastName is required");
	        	} else if (lastName.length() < 3) {
	            	addInvalidMessage(ctx, "lastNameError", "lastName at least 3 chars ");
	            }
	            if (age == null) {
	            	addInvalidMessage(ctx, "ageContentError", "age is required");
	            } else {
		            if (age < 0) {
		            	addInvalidMessage(ctx, "ageTooSmallError", "age should not be negative");
		            }
		            if (age > 130) {
		            	addInvalidMessage(ctx, "ageTooLargeError", "age should smaller than 130");
		            }
	            }
	        }
	    };
	}
}
