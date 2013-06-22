package custom.zk.components.quicknote;

import org.zkoss.json.JSONObject;

import custom.zk.components.quicknote.Data.TextNoteData;
import custom.zk.components.quicknote.event.TextNoteDataChangeEvent;
import custom.zk.components.quicknote.event.TextNoteDataListener;
import custom.zk.components.quicknote.model.UpdatableTextNoteModel;
import custom.zk.components.quicknote.model.TextNoteModel;

/** UpdatableTextNote, extends {@link custom.zk.components.quicknote.RecordableTextNote},<br>
 * hold UpdatableTextNoteModel ({@link custom.zk.components.quicknote.model.UpdatableTextNoteModel})<br>
 * and add event listener ({@link custom.zk.components.quicknote.event.TextNoteDataListener})<br>
 * into model<br>
 * <br>
 * One new thing:<br>
 * 		Add event listener into model, the onChange function of event listener<br>
 * 		will be called when data of model is changed<br>
 * <br>
 * Two effects:<br>
 * 		will update change from server side to client side if<br>
 * 		the change is made within UI thread<br>
 * <br>
 * 		If two or more components use the same UpdatableTextNoteModel,<br>
 * 		and one of them is changed at client side, all other components<br>
 * 		will be updated when the change event is sent to server<br>
 * <br>
 * 		the effects is similar to multiple components use<br>
 * 		the same model and refresh whole model when there are<br>
 * 		any changes<br>
 * <br>
 * 		but there are two advantages with the listener pattern<br>
 * <br>
 * Advantages:<br>
 * 		Clean code: the update is handled by model and listener in component,<br>
 * 			you do not need to add a lot of @Command, @NotifyChange<br>
 * 			to update the model<br>
 * <br>
 * 		Save bandwidth and improve client side performance:<br>
 * 			it can update only the specific text note block<br>
 * 			instead of refresh whole model<br>
 * <br>
 * @author benbai123
 *
 */
public class UpdatableTextNote extends RecordableTextNote {

	private static final long serialVersionUID = -5105526564611614334L;

	// prevent unnecessary update
	private boolean _ignoreModelChangedEvent = false;
	// model, support text note data event listener
	private UpdatableTextNoteModel _model;
	// event listener, listen the change of model
	private TextNoteDataListener _textNoteDataListener;

	// setter
	public void setModel (TextNoteModel model) {
		// model is changed
		if (model != _model) {
			if (_model != null) {
				// remove listener from old model
				_model.removeListDataListener(_textNoteDataListener);
			}
			if (model != null) {
				if (model instanceof UpdatableTextNoteModel) {
					_model = (UpdatableTextNoteModel)model;
				} else {
					// create new model if the instance of model
					// is not UpdatableTextNoteModel
					// in this case, each component will use different instance of
					// UpdatableTextNoteModel even they use the same model
					_model = new UpdatableTextNoteModel(model.getTextNoteData());
				}
				// add listener into model
				initTextNoteDataListener();
			} else {
				_model = null;
			}
		}
		// call super
		super.setModel(_model);
	}
	private void initTextNoteDataListener () {
		if (_textNoteDataListener == null) {
			// create listener as needed
			_textNoteDataListener = new TextNoteDataListener () {
				public void onChange (TextNoteDataChangeEvent event) {
					onTextNoteDataChange(event);
				}
			};
		}
		// add listener into model
		_model.addTextNoteDataListener(_textNoteDataListener);
	}
	// called by listener
	private void onTextNoteDataChange (TextNoteDataChangeEvent event) {
		// change type
		int type = event.getType();
		if (!_ignoreModelChangedEvent) {
			if (type == TextNoteDataChangeEvent.UPDATE_NOTE_BLOCK) {
				// update specific text note block
				smartUpdate("textNoteBlockToUpdate", getBlockInfo(event));
			} else if (type == TextNoteDataChangeEvent.ADD_NOTE_BLOCK) {
				// add a new text note block
				smartUpdate("textNoteBlockToAdd", getBlockInfo(event));
			} else if (type == TextNoteDataChangeEvent.REMOVE_NOTE_BLOCK) {
				// remove a specific text note block
				smartUpdate("textNoteBlockToRemove", getBlockInfo(event));
			} else if (type == TextNoteDataChangeEvent.REFRESH_MODEL) {
				// re-render all text note blocks
				updateNoteBlocks();
			}
		}
	}
	// get the JSON String that represents a text note block
	private String getBlockInfo (TextNoteDataChangeEvent event) {
		// index of changed note block
		int index = event.getIndex();
		// data of changed note block
		TextNoteData data = event.getData();
		JSONObject obj = new JSONObject();
		obj.put("index", index);
		obj.put("left", data.getPosX());
		obj.put("top", data.getPosY());
		obj.put("width", data.getWidth());
		obj.put("height", data.getHeight());
		obj.put("text", data.getText());
		return obj.toJSONString();
	}
	// override
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		_ignoreModelChangedEvent = true;
		super.service(request, everError);
		_ignoreModelChangedEvent = false;
	}
}
