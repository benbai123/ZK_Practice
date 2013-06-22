package custom.zk.components.quicknote.event;

/** Define the method used to listen when the content of
 * UpdatableTextNoteModel ({@link custom.zk.components.quicknote.model.UpdatableTextNoteModel}) is changed
 * 
 * @author benbai123
 * @see custom.zk.components.quicknote.model.UpdatableTextNoteModel
 * @see custom.zk.components.quicknote.event.TextNoteDataChangeEvent
 */
public interface TextNoteDataListener {
	/** Sent when the contents of text note blocks have been changed.
	 */
	public void onChange(TextNoteDataChangeEvent event);
}
