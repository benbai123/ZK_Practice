package blog.ben.test.mvvm.formbinding;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Binder;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class ShowMessageInModalWinTestVM {
	private Person _person;
	private Component _formComponent;
	private BinderCtrl _binderCtrl;
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
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ListModel<String> getMessages () {
		List<String> result = new ArrayList<String>();
		if (_binderCtrl != null && _formComponent != null
			&& _binderCtrl.getValidationMessages() != null
			&& _binderCtrl.getValidationMessages().getMessages(_formComponent) !=  null) {
			for (String s : _binderCtrl.getValidationMessages().getMessages(_formComponent)) {
				result.add(s);
			}
		}
		return new ListModelList(result);
	}
	/**
	 * empty function as a command for
	 * save in form binding and update personContent
	 */
	@Command
	@NotifyChange("personContent")
	public void savePerson () {
	}
	@Command
	public void hideModal (@ContextParam(ContextType.TRIGGER_EVENT) MouseEvent event,
			@ContextParam(ContextType.BINDER) Binder binder) {
		event.getTarget().getParent().setVisible(false);
	}
	/**
	 * get the validator that will do validation while save
	 * @return
	 */
	public Validator getFormValidator(){
		final ShowMessageInModalWinTestVM vm = this;
	    return new AbstractValidator() {
	         
	        public void validate(ValidationContext ctx) {
	        	// get value from form context,
	        	// ctx.getProperties("firstName")[0].getValue() will
	        	// get the value that bind with @bind(fx.firstName)
	        	// in zul page
	        	_formComponent = ctx.getBindContext().getComponent();
	        	_binderCtrl = (BinderCtrl)ctx.getBindContext().getBinder();
	        	String firstName = (String)ctx.getProperties("firstName")[0].getValue();
	        	String lastName = (String)ctx.getProperties("lastName")[0].getValue();
	        	Integer age = (Integer)ctx.getProperties("age")[0].getValue();
	        	if (firstName == null || firstName.isEmpty()) {
	        		// put error message into validationMessages map
	        		addInvalidMessage(ctx, "firstName is required ");
	        	} else if (firstName.length() < 3) {
	            	addInvalidMessage(ctx, "firstName at least 3 chars ");
	            }
	            if (lastName == null || lastName.isEmpty()) {
	        		addInvalidMessage(ctx, "lastName is required");
	        	} else if (lastName.length() < 3) {
	            	addInvalidMessage(ctx, "lastName at least 3 chars ");
	            }
	            if (age == null) {
	            	addInvalidMessage(ctx, "age is required");
	            } else {
		            if (age < 0) {
		            	addInvalidMessage(ctx, "age should not be negative");
		            }
		            if (age > 130) {
		            	addInvalidMessage(ctx, "age should smaller than 130");
		            }
	            }
	            // notify messages is changed programmatically
	            BindUtils.postNotifyChange(null, null, vm, "messages");
	        }
	    };
	}
}
