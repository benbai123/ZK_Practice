package custom.zk.components.quicknote;

import java.util.Map;

import org.zkoss.zk.ui.event.Events;

/**
 * SelectableTextNote, will receive and store which note block is selected from client side action
 * 
 * Three new things:
 * 
 * addClientEvent: add an event that the client might send to server with specific settings,
 * 		refer to http://www.zkoss.org/javadoc/latest/zk/org/zkoss/zk/ui/AbstractComponent.html#addClientEvent(java.lang.Class, java.lang.String, int)
 * 
 * service: Handles an AU request. It is invoked internally.
 * 		refer to http://www.zkoss.org/javadoc/latest/zk/org/zkoss/zk/ui/AbstractComponent.html#service(org.zkoss.zk.au.AuRequest, boolean)
 *
 * postEvent: post an event to self instance so the composer can be notified
 * 			this is also required for save data with MVVM
 * 			refer to http://books.zkoss.org/wiki/ZK_Developer's_Reference/Event_Handling/Event_Firing
 * 
 * @author benbai123
 *
 */
public class SelectableTextNote extends RenderableTextNote {

	private static final long serialVersionUID = -6589891861074953359L;

	private int _selectedTextNoteIndex = -1;

	static {
		/* CE_IMPORTANT: always fire it to server,
		 * 			without this flag, the au engine will only fire event to server
		 * 			if and only if there is an EventListener listen to this event
		 * 
		 * CE_DUPLICATE_IGNORE: ignore multiple event in an au request
		 * 
		 * CE_NON_DEFERRABLE: always fire it immediately,
		 * 			without this flag, au engine will queue this event at client side
		 * 			and fire it to server with other non-defferrable event
		 */
		addClientEvent(SelectableTextNote.class, "onTextNoteBlockSelect", CE_IMPORTANT | CE_DUPLICATE_IGNORE | CE_NON_DEFERRABLE);
	}

	// setter/getter
	public void setSelectedTextNoteIndex (int selectedTextNoteIndex) {
		_selectedTextNoteIndex = selectedTextNoteIndex;
	}
	public int getSelectedTextNoteIndex () {
		return _selectedTextNoteIndex;
	}

	// process client event
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals("onTextNoteBlockSelect")) {
			Map data = request.getData(); // get data map
			// get index by the key "index" since
			// we define the data as {index: idx}
			// while firing onTextNoteBlockSelect event
			// in SelectableTextNote.js
			Integer index = (Integer)data.get("index");
			// store value
			_selectedTextNoteIndex = index;
			// post event to trigger listeners if any
			Events.postEvent("onTextNoteBlockSelect", this, data);
		} else 
			super.service(request, everError);
	}
}
