package custom.zk.components.quicknote.model;

import java.util.ArrayList;
import java.util.List;

import custom.zk.components.quicknote.Data.TextNoteData;

/** basic model to hold text note block datas
 * 
 * @author benbai123
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class TextNoteModel {
	// text note block data list
	private List _textNodeDatas;

	// constructor
	public TextNoteModel (List textNodeDatas) {
		if (textNodeDatas == null) {
			textNodeDatas = new ArrayList();
		}
		_textNodeDatas = textNodeDatas;
	}
	// add
	public void add (TextNoteData data) {
		_textNodeDatas.add(data);
	}
	// add at specific position
	public void add (int index, TextNoteData data) {
		_textNodeDatas.add(index, data);
	}
	// remove
	public void remove (TextNoteData data) {
		_textNodeDatas.remove(data);
	}
	// update specific data (by position)
	public void update (int index, TextNoteData data) {
		_textNodeDatas.remove(index);
		add(index, data);
	}
	// return data list
	public List getTextNoteData () {
		return _textNodeDatas;
	}
	// clear all data
	public void clear () {
		_textNodeDatas.clear();
	}
}
