package com;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;

public class ToggleButton extends HtmlMacroComponent {
	private Image _img;
	private boolean _checked = true;
	private String _disableImage;
	private String _enableImage;
	public void afterCompose() {
		super.afterCompose();
		init();
	}

	private void init() {
		// this macro togglebutton
		final Component toggleButton = this;
		_img = (Image) getFirstChild().getFellow("mc_togglebutton_image");
		if (_checked)
			_img.setSrc(getEnableImage());
		else
			_img.setSrc(getDisableImage());
			
		final Image img = _img;
		img.addEventListener(Events.ON_CLICK, new EventListener(){
			public void onEvent(Event event) throws Exception {
				_checked = !_checked;
				if (_checked)
					_img.setSrc(getEnableImage());
				else
					_img.setSrc(getDisableImage());
				// create an onCheck event then post it back
				CheckEvent evt = new CheckEvent(Events.ON_CHECK, toggleButton, _checked);
				Events.postEvent(evt);
			}
		});
	}
	public void setChecked(boolean checked) {
		_checked = checked;
	}
	public boolean isChecked() {
		return _checked;
	}
	public void setDisableImage(String disableImage) {
		_disableImage = disableImage;
		if (_img != null && !_checked)
			_img.setSrc(_disableImage);
	}
	public void setEnableImage(String enableImage) {
		_enableImage = enableImage;
		if (_img != null && _checked)
			_img.setSrc(_enableImage);
	}
	public String getDisableImage() {
		return _disableImage;
	}
	public String getEnableImage() {
		return _enableImage;
	}
}
