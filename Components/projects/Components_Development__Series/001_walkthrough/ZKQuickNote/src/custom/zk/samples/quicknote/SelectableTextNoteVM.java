package custom.zk.samples.quicknote;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

/** VM used in selectabletextnote.zul,
 * extends RenderableTextNoteVM then simply
 * define a member field and its getter/setter
 * 
 * @author benbai123
 *
 */
public class SelectableTextNoteVM extends RenderableTextNoteVM {

	private int _opacity = 20;
	private String _maskColor = "#00FF00";
	// getters/setters

	public int getOpacity () {
		return _opacity;
	}
	public void setOpacity (int opacity) {
		_opacity = opacity;
	}

	public String getMaskColor () {
		return _maskColor;
	}
	public void setMaskColor (String maskColor) {
		_maskColor = maskColor;
	}
	// commands
	@Command
	@NotifyChange("opacity")
	public void updateOpacity () {
		// do nothing, just for trigger NotifyChange
	}
	@Command
	@NotifyChange("maskCOlor")
	public void updateMaskColor () {
		// do nothing, just for trigger NotifyChange
	}
}
