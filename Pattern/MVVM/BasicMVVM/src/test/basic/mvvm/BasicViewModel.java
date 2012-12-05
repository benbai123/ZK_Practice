package test.basic.mvvm;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * Tested with ZK 6.0.2
 * @author ben
 *
 */
public class BasicViewModel {
	String _msg = "The message label";
	String _name = "defaultName";

	public String getMessage () {
		return _msg;
	}

	public String getName () {
		return _name;
	}
	public void setName (String name) {
		_name = name;
	}
	/**
	 * Annotations:
	 * @Command denotes the function below is a command
	 * @NotifyChange denotes the properties should be updated after this command
	 * 
	 * The annotations below means sayHello method is a command, and
	 * the message property should be updated after sayHello command
	 * is executed
	 */
	@Command
	@NotifyChange("message")
	public void sayHello () {
		_msg = "Hello " + _name;
	}
}
