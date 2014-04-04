package zk.angular.components;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.zkoss.zul.impl.XulElement;

/** The basic class of AngularJS Component
 * contains basic API and Util
 * 
 * @author benbai123
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class NgComp extends XulElement {
	private static final long serialVersionUID = -8937842458512715432L;

	/** tag to output AngularJS directive component*/
	private String _ngTag;
	/** template url with respect to the tag above */
	private String _ngTemplateUrl;
	/** whether use faker dom to prevent blinking of AngularJS component
	 * Note: This will affect client side performance
	 */
	private boolean _preventBlink;

	/** Map that hold _ngTag / _ngTemplateUrl */
	private static Map<String, String> _tagTemplateMapping = new HashMap();
	// setter/getter
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
		// encode url
		// this will convert the url to ZK accessible path
		// or append context path as needed
		ngTemplateUrl = org.zkoss.web.fn.ServletFns.encodeURL(ngTemplateUrl);
		if (!ngTemplateUrl.equals(_ngTemplateUrl)) {
			_ngTemplateUrl = ngTemplateUrl;
			// also update _ngTag
			setNgTag(fixTemplateMapping(_ngTag, getNgTemplateUrl()));
			smartUpdate("ngTemplateUrl", _ngTemplateUrl);
		}
	}
	public String getNgTemplateUrl () {
		return _ngTemplateUrl;
	}
	public void setPreventBlink (boolean preventBlink) {
		if (preventBlink != _preventBlink) {
			_preventBlink = preventBlink;
			smartUpdate("preventBlink", _preventBlink);
		}
	}
	public boolean isPreventBlink () {
		return _preventBlink;
	}
	/** register given ngTemplateUrl with given gnTag
	 * reset ngTag if needed
	 * then return the (updated) ngTag
	 * 
	 * @param ngTag
	 * @param ngTemplateUrl
	 * @return
	 */
	protected static String fixTemplateMapping (String ngTag, String ngTemplateUrl) {
		// create a new mapping if
		// ngTemplateUrl is not in the map
		// or
		// the given ngTag is not mapping to the ngTemplateUrl specified
		if (!_tagTemplateMapping.containsValue(ngTemplateUrl)
			|| !ngTemplateUrl.equals(_tagTemplateMapping.get(ngTag))) {
			int i = 0;
			String oldTag = ngTag;
			// continue update if
			// ngTag is mapping to different template url
			while (_tagTemplateMapping.get(ngTag) != null
					&& !ngTemplateUrl.equals(_tagTemplateMapping.get(ngTag))) {
				ngTag = oldTag + i;
				i++;
			}
			_tagTemplateMapping.put(ngTag, ngTemplateUrl);
		}
		return ngTag;
	}
	// super
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
		throws java.io.IOException {
		super.renderProperties(renderer);
		_ngTag = fixTemplateMapping(_ngTag, getNgTemplateUrl());

		render(renderer, "ngTag", _ngTag);
		render(renderer, "ngTemplateUrl", _ngTemplateUrl);
		render(renderer, "preventBlink", _preventBlink);
	}
}
