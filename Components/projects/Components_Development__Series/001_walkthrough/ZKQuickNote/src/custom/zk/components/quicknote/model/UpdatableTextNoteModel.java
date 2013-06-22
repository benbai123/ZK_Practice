package custom.zk.components.quicknote.model;

import java.util.ArrayList;
import java.util.List;
import custom.zk.components.quicknote.Data.TextNoteData;
import custom.zk.components.quicknote.event.TextNoteDataChangeEvent;
import custom.zk.components.quicknote.event.TextNoteDataListener;
/** UpdatableTextNoteModel<br>
 * <br>
 * <p>Extends TextNoteModel and override functions to handle data change event,
 * will fire proper event to each event listener within this model to
 * update corresponding component</p>
 * 
 * <p>Used by {@link custom.zk.components.quicknote.UpdatableTextNote}</p>
 * 
 * @author benbai123
 *
 */
public class UpdatableTextNoteModel extends TextNoteModel {

	private List<TextNoteDataListener> _listeners = new ArrayList<TextNoteDataListener>();

	// Constructor
	@SuppressWarnings("rawtypes")
	public UpdatableTextNoteModel (List textNodeDatas) {
		super(textNodeDatas);
	}
	// fire data change event to each event listener
	protected void fireEvent(TextNoteData data, int index, int type) {
		final TextNoteDataChangeEvent evt = new TextNoteDataChangeEvent(this, data, index, type);
		for (TextNoteDataListener listener : _listeners) {
			listener.onChange(evt);
		}
	}
	// add an event listener
	public void addTextNoteDataListener (TextNoteDataListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener cannot be null");
		}
		_listeners.add(listener);
	}
	// remove an event listener
	public void removeListDataListener(TextNoteDataListener listener) {
		_listeners.remove(listener);
	}
	// get index of a text note data
	public int indexOf (TextNoteData data) {
		return super.getTextNoteData().indexOf(data);
	}
	// override
	// add
	public void add (TextNoteData data) {
		super.add(data);
		int index = getTextNoteData().indexOf(data);
		// fire event to add text note block at client side
		fireEvent(data, index, TextNoteDataChangeEvent.ADD_NOTE_BLOCK);
	}
	// add at specific position
	public void add (int index, TextNoteData data) {
		super.add(index, data);
		// fire event to add text note block at client side
		fireEvent(data, index, TextNoteDataChangeEvent.ADD_NOTE_BLOCK);
	}
	// remove
	public void remove (TextNoteData data) {
		int index = getTextNoteData().indexOf(data);
		super.remove(data);
		// fire event to remove text note block at client side
		fireEvent(data, index, TextNoteDataChangeEvent.REMOVE_NOTE_BLOCK);
	}
	public void update (int index, TextNoteData data) {
		super.getTextNoteData().remove(index);
		super.add(index, data);
		// fire event to update text note block at client side
		fireEvent(data, index, TextNoteDataChangeEvent.UPDATE_NOTE_BLOCK);
	}
	// clear all data
	public void clear () {
		super.clear();
		// refresh whole model
		fireEvent(null, -1, TextNoteDataChangeEvent.REFRESH_MODEL);
	}
}