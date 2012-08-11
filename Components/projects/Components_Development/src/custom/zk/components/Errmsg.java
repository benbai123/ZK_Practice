package custom.zk.components;


import org.zkoss.zul.impl.XulElement;
// this file will be loaded because the
// <component-class>custom.zk.components.Errmsg</component-class>
// specified in lang-addon.xml
public class Errmsg extends XulElement {
	private String _msg;
	private String _description;

	public void setMsg (String msg) {
		_msg = msg;
		// this will call client side widget's setMsg(_msg)
		smartUpdate("msg", _msg);
	}
	public String getMsg () {
		return _msg;
	}
	public void setDescription (String description) {
		_description = description;
		// this will call client side widget's setDescription(_description)
		smartUpdate("description", _description);
	}
	public String getDescription () {
		return _description;
	}
	public String getZclass() {
		return _zclass == null ? "z-errmsg" : _zclass;
	}
	//-- ComponentCtrl --//
//	the renderProperties will be called by framework automatically,
//	remember to render super's properties first
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);
		// this will call client side widget's setMsg(_msg)
		if (_msg != null)
			render(renderer, "msg", _msg);
		// this will call client side widget's setDescription(_description)
		if (_description != null)
			render(renderer, "description", _description);
	}
}
