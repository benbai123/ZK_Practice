package test.custom.component;

import org.zkoss.zul.Tabs;

public class FrozenTabs extends Tabs {

	private static final long serialVersionUID = -5439094036809527719L;
	private Integer _frozen = 0;

	public void setFrozen (Integer frozen) {
		if (frozen == null || frozen < 0) {
			frozen = 0;
		}
		if (_frozen != frozen) {
			_frozen = frozen;
			smartUpdate("frozen", _frozen);
		}
	}
	public Integer getFrozen () {
		return _frozen;
	}
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		if (_frozen != 0) {
			renderer.render("frozen", _frozen);
		}
	}
}
