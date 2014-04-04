package zk.angular.components;

import javax.servlet.ServletException;

/** Simple button component
 * Extends ZK Button so only need to take care of AngularJS part
 * 
 * @author benbai123
 *
 */
public class SimpleButton extends org.zkoss.zul.Button {
	private static final long serialVersionUID = -4633623332761253847L;
	private String _ngTag;
	private String _ngTemplateUrl;
	public SimpleButton () {
		init();
	}
	public SimpleButton (String label) {
		super(label);
		init();
	}
	private void init () {
		setNgTag("simplebutton");
		try {
			// default template url
			setNgTemplateUrl("~./ngzk/templates/wgt/simplebutton.html");
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	public void setNgTag (String ngTag) {
		if (ngTag == null) {
			ngTag = "";
		}
		if (!ngTag.equals(_ngTag)) {
			_ngTag = ngTag;
			smartUpdate("ngTag", _ngTag);
		}
	}
	public void setNgTemplateUrl (String ngTemplateUrl) throws ServletException {
		if (ngTemplateUrl == null) {
			ngTemplateUrl = "";
		}
		ngTemplateUrl = org.zkoss.web.fn.ServletFns.encodeURL(ngTemplateUrl);
		if (!ngTemplateUrl.equals(_ngTemplateUrl)) {
			_ngTemplateUrl = ngTemplateUrl;
			setNgTag(NgComp.fixTemplateMapping(_ngTag, getNgTemplateUrl()));
			smartUpdate("ngTemplateUrl", _ngTemplateUrl);
		}
	}
	public String getNgTemplateUrl () {
		return _ngTemplateUrl;
	}
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		_ngTag = NgComp.fixTemplateMapping(_ngTag, getNgTemplateUrl());

		render(renderer, "ngTag", _ngTag);
		render(renderer, "ngTemplateUrl", _ngTemplateUrl);
	}
}
