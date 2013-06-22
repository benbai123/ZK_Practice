package custom.zk.components.quicknote.event;

import custom.zk.components.quicknote.Data.TextNoteData;
import custom.zk.components.quicknote.model.TextNoteModel;

/**
 * Defines an event that encapsulates changes to text note blocks. 
 *
 * @author benbai123
 */
public class TextNoteDataChangeEvent {
	/** Identifies whole model should be refreshed */
	public static final int REFRESH_MODEL = 0;
	/** Identifies a text note block should be updated */
	public static final int UPDATE_NOTE_BLOCK = 1;
	/** Identifies a new text note block should be addad */
	public static final int ADD_NOTE_BLOCK = 2;
	/** Identifies a text note block should be removed */
	public static final int REMOVE_NOTE_BLOCK = 3;
	/* the model that trigger this event */
	private TextNoteModel _model;
	/* the affected data (which represents a text note block) */
	private TextNoteData _data;
	/* index of affected text note block */
	private int _index;
	/* action type of this event */
	private int _type;

	/** Constructor
	 * 
	 * @param data the changed text note data for a text note block if any
	 * @param index the index of the changed text note block if any
	 * @param type one of {@link #REFRESH_MODEL}, {@link #UPDATE_NOTE_BLOCK},
	 * {@link #ADD_NOTE_BLOCK}, {@link #REMOVE_NOTE_BLOCK}
	 */
	public TextNoteDataChangeEvent (TextNoteModel model, TextNoteData data,
			int index, int type) {
		_model = model;
		_data = data;
		_index = index;
		_type = type;
	}
	// getters
	public TextNoteModel getModel () {
		return _model;
	}
	public TextNoteData getData () {
		return _data;
	}
	public int getIndex () {
		return _index;
	}
	public int getType () {
		return _type;
	}
}
