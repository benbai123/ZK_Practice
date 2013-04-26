package blog.ben.test.mvvm.formbinding;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.validator.AbstractValidator;

public class FormBindingWithValidatorTestVM extends FormBindingTestVM {
	public Validator getFormValidator(){
	    return new AbstractValidator() {
	         
	        public void validate(ValidationContext ctx) {
	        	String firstName = (String)ctx.getProperties("firstName")[0].getValue();
	        	String lastName = (String)ctx.getProperties("lastName")[0].getValue();
	        	Integer age = (Integer)ctx.getProperties("age")[0].getValue();
	        	if (firstName == null || firstName.isEmpty()) {
	        		addInvalidMessage(ctx, "firstNameContentError", "firstName cannot be empty ");
	        	} else if (firstName.length() < 3) {
	            	addInvalidMessage(ctx, "firstNameError", "firstName at least 3 chars ");
	            }
	            if (lastName == null || lastName.isEmpty()) {
	        		addInvalidMessage(ctx, "lastNameContentError", "lastName cannot be empty ");
	        	} else if (lastName.length() < 3) {
	            	addInvalidMessage(ctx, "lastNameError", "lastName at least 3 chars ");
	            }
	            if (age == null) {
	            	addInvalidMessage(ctx, "ageContentError", "age should not be empty");
	            } else {
		            if (age < 0) {
		            	addInvalidMessage(ctx, "ageTooSmallError", "age should not be negative");
		            }
		            if (age > 200) {
		            	addInvalidMessage(ctx, "ageTooLargeError", "age should smaller than 200");
		            }
	            }
	        }
	    };
	}
}
