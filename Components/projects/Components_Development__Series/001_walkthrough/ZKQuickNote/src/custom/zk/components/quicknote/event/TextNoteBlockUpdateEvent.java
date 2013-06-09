package custom.zk.components.quicknote.event;

import java.util.Map;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

import custom.zk.components.quicknote.Data.TextNoteData;

/** TextNoteBlockUpdateEvent
 * 
 * Wrap the data of au request from text note block changed
 * 
 * One new thing: Extends org.zkoss.zk.ui.event.Event and call
 * 				'super(name, target);' constructor to make it
 * 				working with ZK Event processing flow
 * 
 * @author benbai123
 *
 */
public class TextNoteBlockUpdateEvent extends Event {

	private static final long serialVersionUID = 8137381810564543333L;

	private int _index;
	private TextNoteData _textNoteData;

	public static TextNoteBlockUpdateEvent getTextNoteBlockUpdateEvent (String name, Component target, AuRequest request) {
		// create and return event
		return new TextNoteBlockUpdateEvent(name, target, request);
	}
	// construct event with given name, target and au request
	@SuppressWarnings("rawtypes")
	public TextNoteBlockUpdateEvent (String name, Component target, AuRequest request) {
		super(name, target);
		Map data = request.getData(); // get data map
		_index = (Integer)data.get("index");
		_textNoteData = new TextNoteData((Integer)data.get("left"),
				(Integer)data.get("top"),
				(Integer)data.get("width"),
				(Integer)data.get("height"),
				(String)data.get("text"));
	}
	public int getIndex () {
		return _index;
	}
	public TextNoteData getTextNoteData () {
		return _textNoteData;
	}
}
