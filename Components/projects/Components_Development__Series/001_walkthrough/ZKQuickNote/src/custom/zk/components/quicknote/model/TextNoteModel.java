package custom.zk.components.quicknote.model;

import java.util.ArrayList;
import java.util.List;

import custom.zk.components.quicknote.Data.TextNoteData;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TextNoteModel {
	private List _textNodeDatas;

	public TextNoteModel (List textNodeDatas) {
		if (textNodeDatas == null) {
			textNodeDatas = new ArrayList();
		}
		_textNodeDatas = textNodeDatas;
	}
	public void add (TextNoteData data) {
		_textNodeDatas.add(data);
	}
	public void add (int index, TextNoteData data) {
		_textNodeDatas.add(index, data);
	}
	public void remove (TextNoteData data) {
		_textNodeDatas.remove(data);
	}
	public void update (int index, TextNoteData data) {
		_textNodeDatas.remove(index);
		add(index, data);
	}
	public List getTextNoteData () {
		return _textNodeDatas;
	}
	public void clear () {
		_textNodeDatas.clear();
	}
}
