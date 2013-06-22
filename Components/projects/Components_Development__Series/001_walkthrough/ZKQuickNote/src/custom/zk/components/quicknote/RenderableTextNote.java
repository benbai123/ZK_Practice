package custom.zk.components.quicknote;

import java.util.List;

import org.zkoss.json.JSONArray;
import custom.zk.components.quicknote.Data.TextNoteData;
import custom.zk.components.quicknote.model.TextNoteModel;

/** RenderableTextNote, can render text note block with server side data
 * 
 * @author benbai123
 *
 */
public class RenderableTextNote extends EnhancedMask {

	private static final long serialVersionUID = -6824067586007799573L;

	/** data model that contains information of text note blocks
	 * 
	 */
	private TextNoteModel _model;
	/**
	 * setter
	 * @param model
	 */
	public void setModel (TextNoteModel model) {
		_model = model;
		updateNoteBlocks();
	}
	/**
	 * getter
	 * @return
	 */
	public TextNoteModel getModel () {
		return _model;
	}
	/**
	 * used to update text note blocks at client side
	 */
	public void updateNoteBlocks () {
		String blockToRender = getRenderedTextNoteData();
		if (blockToRender == null) {
			blockToRender = "";
		}
		// update client note blocks while setter is called
		smartUpdate("noteBlocks", blockToRender);
	}
	/**
	 * create a json string used to update text note blocks at client side
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getRenderedTextNoteData () {
		if (_model != null
			&& _model.getTextNoteData() != null
			&& _model.getTextNoteData().size() > 0) {
			// json arrays of
			// left, top, width, height and text of note blocks
			JSONArray jsArr = new JSONArray();
			JSONArray jsXposArr = new JSONArray(); // left
			JSONArray jsYposArr = new JSONArray(); // top
			JSONArray jsWidthArr = new JSONArray(); // width
			JSONArray jsHeightArr = new JSONArray(); // height
			JSONArray jsTextArr = new JSONArray(); // text
			// get all data
			List datas = _model.getTextNoteData();
			// put each data into json array accordingly
			for (int i = 0; i < datas.size(); i++) {
				TextNoteData data = (TextNoteData)datas.get(i);
				jsXposArr.add(data.getPosX());
				jsYposArr.add(data.getPosY());
				jsWidthArr.add(data.getWidth());
				jsHeightArr.add(data.getHeight());
				jsTextArr.add(data.getText());
			}
			// put each data array into another json array
			// i.e., will be a 2-D array at client side
			jsArr.add(jsXposArr);
			jsArr.add(jsYposArr);
			jsArr.add(jsWidthArr);
			jsArr.add(jsHeightArr);
			jsArr.add(jsTextArr);
			return jsArr.toJSONString();
		} else {
			return null;
		}
	}
	// render noteBlocks as needed
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		String blockToRender = getRenderedTextNoteData();
		if (blockToRender != null) {
			render(renderer, "noteBlocks", blockToRender);
		}
	}
}
