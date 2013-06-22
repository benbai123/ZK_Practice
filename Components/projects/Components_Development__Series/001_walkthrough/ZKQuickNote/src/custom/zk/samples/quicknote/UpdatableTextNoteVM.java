package custom.zk.samples.quicknote;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import custom.zk.components.quicknote.Data.TextNoteData;
import custom.zk.components.quicknote.model.UpdatableTextNoteModel;
/**
 * VM used by updatabletextnote.zul<br>
 * <br>
 * As you can see, simply provide data and handle command,<br>
 * will not refresh model by VM<br>
 * 
 * @author benbai123
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UpdatableTextNoteVM {
	private UpdatableTextNoteModel _model;
	private int _selectedIndex = -1;
	private String _result = "Wrong";

	// getters, setters
	public UpdatableTextNoteModel getModel () {
		if (_model == null) {
			List l = new ArrayList();
			l.add(new TextNoteData(5, 10, 25, 25, ""));
			l.add(new TextNoteData(50, 10, 25, 25, ""));
			_model = new UpdatableTextNoteModel(l);
		}
		return _model;
	}
	public void setSelectedIndex (int selectedIndex) {
		_selectedIndex = selectedIndex;
	}
	public String getResult () {
		return _result;
	}
	@Command
	@NotifyChange("result")
	public void checkResult () {
		List<TextNoteData> datas = getModel().getTextNoteData();
		try {
			int valOne = Integer.parseInt(datas.get(0).getText());
			int valTwo = Integer.parseInt(datas.get(1).getText());
	
			if (valOne*valTwo == 6
				&& valOne+valTwo == 5) {
				_result = "Correct";
			} else {
				_result = "Wrong";
			}
		} catch (Exception e) {
			_result = "Wrong";
		}
	}
	@Command
	public void removeSelected () {
		List<TextNoteData> datas = getModel().getTextNoteData();
		if (_selectedIndex >= 0
			&&datas.size() > _selectedIndex) {
			getModel().remove(datas.get(_selectedIndex));
		}
	}
	@Command
	public void clear () {
		getModel().clear();
	}
}
