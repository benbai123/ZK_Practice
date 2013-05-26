package custom.zk.components.quicknote;

import java.util.List;

import org.zkoss.json.JSONArray;
import custom.zk.components.quicknote.Data.TextNoteData;
import custom.zk.components.quicknote.model.TextNoteModel;

public class RenderableTextNote extends EnhancedMask {

	private static final long serialVersionUID = -6824067586007799573L;
	private TextNoteModel _model;
	public void setModel (TextNoteModel model) {
		_model = model;
		String blockToRender = getRenderedTextNoteData();
		if (blockToRender == null) {
			blockToRender = "";
		}
		smartUpdate("noteBlocks", blockToRender);
	}
	@SuppressWarnings("rawtypes")
	public String getRenderedTextNoteData () {
		if (_model != null
			&& _model.getTextNoteData() != null
			&& _model.getTextNoteData().size() > 0) {
			JSONArray jsArr = new JSONArray();
			JSONArray jsXposArr = new JSONArray();
			JSONArray jsYposArr = new JSONArray();
			JSONArray jsWidthArr = new JSONArray();
			JSONArray jsHeightArr = new JSONArray();
			JSONArray jsTextArr = new JSONArray();
			List datas = _model.getTextNoteData();
			for (int i = 0; i < datas.size(); i++) {
				TextNoteData data = (TextNoteData)datas.get(i);
				jsXposArr.add(data.getPosX());
				jsYposArr.add(data.getPosY());
				jsWidthArr.add(data.getWidth());
				jsHeightArr.add(data.getHeight());
				jsTextArr.add(data.getText());
			}
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
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		String blockToRender = getRenderedTextNoteData();
		if (blockToRender != null) {
			render(renderer, "noteBlocks", blockToRender);
		}
	}
}
