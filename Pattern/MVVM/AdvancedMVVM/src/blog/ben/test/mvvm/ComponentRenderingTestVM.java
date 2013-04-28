package blog.ben.test.mvvm;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * tested with ZK 6.0.2
 * @author benbai123
 *
 */
@SuppressWarnings("rawtypes")
public class ComponentRenderingTestVM {
	private boolean _renderTextbox = true;
	private boolean _renderLabel = false;
	private List _renderNode;

	/**
	 * @return _renderNode to render textbox or null to detach textbox
	 */
	public List getTextboxNode () {
		return _renderTextbox? getRenderNode() : null;
	}
	/**
	 * @return _renderNode to render label or null to detach label
	 */
	public List getLabelNode () {
		return _renderLabel? getRenderNode() : null;
	}
	@Command
	@NotifyChange("textboxNode")
	public void changeTextboxRenderStatus () {
		_renderTextbox = !_renderTextbox;
	}
	@Command
	@NotifyChange("labelNode")
	public void changeLabelRenderStatus () {
		_renderLabel = !_renderLabel;
	}
	// returns a list contains only one element
	// the element can be anything
	// just used to trigger children binding
	@SuppressWarnings("unchecked")
	private List getRenderNode () {
		if (_renderNode == null) {
			_renderNode = new ArrayList();
			_renderNode.add("whatever");
		}
		return _renderNode;
	}
}
