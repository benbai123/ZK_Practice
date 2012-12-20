package test;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

/**
 * tested with ZK 6.0.2
 * @author ben
 *
 */
@SuppressWarnings("serial")
public class ListeningProcessingEventTestComposer extends GenericForwardComposer {
	Button btnTwo;
	Textbox tb;
	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
		btnTwo.addEventListener( // add an EventListener
			"onClick", // listen to onClick event
			new EventListener () { // create an EventListener instance to add
				public void onEvent (Event event) { // the method will be called while onClick event triggered
					tb.setValue("button two clicked !"); // set some string to the Textbox tb !
				}
			}
		);
	}
}
