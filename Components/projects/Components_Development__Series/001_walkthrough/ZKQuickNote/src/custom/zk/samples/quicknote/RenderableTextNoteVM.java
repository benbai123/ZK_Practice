package custom.zk.samples.quicknote;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import custom.zk.components.quicknote.Data.TextNoteData;
import custom.zk.components.quicknote.model.TextNoteModel;

/** VM used in renderabletextnote.zul
 * 
 * @author benbai123
 *
 */
public class RenderableTextNoteVM {
	private TextNoteModel _model;
	private TextNoteData _textNoteDataToUpdate;
	private int _indexToUpdate = -1;
	// getters, setters
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TextNoteModel getModel () {
		if (_model == null) {
			List l = new ArrayList();
			l.add(new TextNoteData(5, 5, 100, 35, "test"));
			l.add(new TextNoteData(55, 55, 150, 50, "test 2"));
			l.add(new TextNoteData(105, 105, 75, 25, "test 3"));
			_model = new TextNoteModel(l);
		}
		return _model;
	}
	public TextNoteData getTextNoteData () {
		if (_textNoteDataToUpdate == null) {
			_textNoteDataToUpdate = new TextNoteData(0, 0, 0, 0, "");
		}
		return _textNoteDataToUpdate;
	}
	public int getIndexToUpdate () {
		return _indexToUpdate;
	}
	public void setIndexToUpdate (int indexToUpdate) {
		_indexToUpdate = indexToUpdate;
	}
	// add note block then update model to client
	@Command
	@NotifyChange ("model")
	public void addNoteBlock () {
		_model.add(new TextNoteData(_textNoteDataToUpdate));
	}
	// update note block then update model to client
	@Command
	@NotifyChange ("model")
	public void updateNoteBlock () {
		_model.update(_indexToUpdate, new TextNoteData(_textNoteDataToUpdate));
	}
	// clear note blocks then update model to client
	@Command
	@NotifyChange ("model")
	public void clearAllBlocks () {
		_model.clear();
	}
}
