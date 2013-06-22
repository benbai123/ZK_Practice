package custom.zk.components.quicknote;

import java.util.List;

import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;

import custom.zk.components.quicknote.Data.TextNoteData;
import custom.zk.components.quicknote.event.TextNoteBlockUpdateEvent;
import custom.zk.components.quicknote.model.TextNoteModel;

/** RecordableTextNote, will receive and store which note block is changed from client side action
 * 
 * Two new things:
 * 
 * Convert request to an event by static method defined in TextNoteBlockUpdateEvent
 * 		two benefits:
 * 		1. Do not need to write the code to retrieve data in service method
 * 		2. Can grab data from event easily in event listener since we can wrap data properly at first
 * 
 * UiException: throw this exception to notify user something wrong,
 * 		will alert at client side
 * 
 * @author benbai123
 *
 */
public class RecordableTextNote extends SelectableTextNote {

	private static final long serialVersionUID = -4769807131469685854L;
	static {
		addClientEvent(SelectableTextNote.class, "onTextNoteBlockChanged", CE_IMPORTANT | CE_DUPLICATE_IGNORE | CE_NON_DEFERRABLE);
		addClientEvent(SelectableTextNote.class, "onSelectedTextNoteBlockChanged", CE_IMPORTANT | CE_DUPLICATE_IGNORE | CE_NON_DEFERRABLE);
	}
	// getter, so can save data to vm
	public TextNoteData getSelectedTextNoteData () {
		int index = getSelectedTextNoteIndex();
		if (index >= 0) {
			return getTextNoteData(index);
		}
		return null;
	}
	public TextNoteData getTextNoteData (int index) {
		List datas = getModel().getTextNoteData();
		return (datas != null && datas.size() > index)?
				(TextNoteData)datas.get(index) : null;
	}
	// process client event
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		// any text note block changed
		if (cmd.equals("onTextNoteBlockChanged")) {
			TextNoteModel model = getModel();
			if (model != null) {
				TextNoteBlockUpdateEvent event = TextNoteBlockUpdateEvent.getTextNoteBlockUpdateEvent(cmd, this, request);
				// created at client side
				if (model.getTextNoteData().size() <= event.getIndex())
					model.add(event.getTextNoteData());
				else // already in model
					model.update(event.getIndex(), event.getTextNoteData());
				// post event to trigger listeners if any
				Events.postEvent(event);
			} else {
				throw new UiException("Model is required !!");
			}
			// selected text note block changed
		} else if (cmd.equals("onSelectedTextNoteBlockChanged")) {
			TextNoteModel model = getModel();
			if (model != null) {
				TextNoteBlockUpdateEvent event = TextNoteBlockUpdateEvent.getTextNoteBlockUpdateEvent(cmd, this, request);
				// simply post event to trigger listeners if any
				Events.postEvent(event);
			} else {
				throw new UiException("Model is required !!");
			}
		} else 
			super.service(request, everError);
	}
}
