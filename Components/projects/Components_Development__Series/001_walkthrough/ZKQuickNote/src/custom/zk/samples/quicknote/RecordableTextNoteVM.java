package custom.zk.samples.quicknote;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;

import custom.zk.components.quicknote.Data.TextNoteData;
import custom.zk.components.quicknote.event.TextNoteBlockUpdateEvent;

public class RecordableTextNoteVM extends SelectableTextNoteVM {

	// data from recordabletextnote component
	// there is a getter 'getSelectedTextNoteData' in
	// recordabletextnote so we can get data from it
	// via MVVM @save annotation
	TextNoteData _selectedTextNoteData;

	// data grabbed from TextNoteBlockUpdateEvent
	// there is no getter of changed text note data
	// so we need to grab it from event while
	// onTextNoteBlockChanged
	TextNoteData _changedTextNoteData;

	// index of changed text note data
	int _changedIndex;

	public void setSelectedTextNoteData (TextNoteData data) {
		if (data != null) {
			_selectedTextNoteData = data;
			// copy data for add function in super class (RenderableTextNoteVM.java)
			TextNoteData dataToAdd = super.getTextNoteData();
			dataToAdd.setPosX(data.getPosX());
			dataToAdd.setPosY(data.getPosY());
			dataToAdd.setWidth(data.getWidth());
			dataToAdd.setHeight(data.getHeight());
			dataToAdd.setText(data.getText());
		}
	}
	public TextNoteData getSelectedTextNoteData  () {
		return _selectedTextNoteData;
	}

	public TextNoteData getChangedTextNoteData () {
		return _changedTextNoteData;
	}
	public int getChangedIndex () {
		return _changedIndex;
	}
	// Override
	public TextNoteData getTextNoteData () {
		return _selectedTextNoteData != null? _selectedTextNoteData : super.getTextNoteData();
	}
	@Command
	@NotifyChange("textNoteData")
	public void updateAttrs () {
		// for trigger update
	}
	@Command
	@NotifyChange("model")
	public void updateModel () {
		// for trigger update
	}
	@Command
	@NotifyChange({"changedTextNoteData", "changedIndex"})
	public void updateChangedTextNoteData (@ContextParam(ContextType.TRIGGER_EVENT) TextNoteBlockUpdateEvent event) {
		// grab data from event then update
		_changedTextNoteData = event.getTextNoteData();
		_changedIndex = event.getIndex();
	}
}
