package test.component.parameter;

import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.EventInterceptor;

import test.component.parameter.helper.Cparam;

/** EventInterceptor to get command data
 * 
 * @author benbai123
 *
 */
@SuppressWarnings("rawtypes")
public class ComponentParameterInterceptor implements EventInterceptor  {
	public Event beforeSendEvent(Event event) {
		return event;
	}
	public Event beforePostEvent(Event event) {
		return event;
	}
	public Event beforeProcessEvent(Event event) {
		// get command data of current event
		List data = Cparam.getCommandData(event.getTarget(), event.getName());
		if (data != null) {
			// store it to execution
			Executions.getCurrent().setAttribute("COMMAND_PARAMETER_DATA",
					data);
		}
		return event;
	}
	public void afterProcessEvent(Event event) {

	}
	public static List getCommandData () {
		// returns current command data
		return (List)Executions.getCurrent().getAttribute("COMMAND_PARAMETER_DATA");
	}
}
