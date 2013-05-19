package custom.zk.components.quicknote;

import org.zkoss.zul.impl.XulElement;

/**
 * java class for EnhancedMask component,
 * extends XulElement and add attributes opacity and maskColor
 * 
 * Two new things:
 * 
 * smartUpdate: used to update client side attribute with the UI thread,
 * 				call it within ZK UI thread then it will bring the status
 * 				back to client side
 * 
 * renderProperties: used to render all properties to client side at the beginning,
 * 					it is a part of component life cycle in ZK,
 * 					will be called by ZK framework automatically.
 * 
 * @author benbai123
 *
 */
public class EnhancedMask extends XulElement {
	private static final long serialVersionUID = -2084534449227910442L;

	private int _opacity = 35;
	private String _maskColor = "#ccc";

	public void setOpacity (int opacity) {
		// no negative
		if (opacity < 0) {
			opacity = 0;
		}
		// cannot larger than 100
		if (opacity > 100) {
			opacity = 100;
		}
		// update if value is changed
		if (_opacity != opacity) {
			_opacity = opacity;
			smartUpdate("opacity", _opacity);
		}
	}
	public int getOpacity () {
		return _opacity;
	}
	public void setMaskColor (String maskColor) {
		// update if there is a different value
		if (maskColor != null // no null
			&& !maskColor.isEmpty() // no empty
			&& !maskColor.equals(_maskColor)) { // value is changed
			_maskColor = maskColor;
			smartUpdate("maskColor", _maskColor);
		}
	}
	public String getMaskColor () {
		return _maskColor;
	}
	//-- ComponentCtrl --//
	// the renderProperties method is a part of component life cycle in ZK,
	// it will be called by ZK framework automatically,
	// remember to render super's properties first
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		if (_opacity != 35) {
			// this will call setOpacity (opacity) in widget class at client side
			render(renderer, "opacity", _opacity);
		}
		if (!"#ccc".equals(_maskColor)) {
			// this will call setMaskColor (maskColor) in widget class at client side
			render(renderer, "maskColor", _maskColor);
		}
	}
}