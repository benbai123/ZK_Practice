package org.zkoss.gmaps.extended.components;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Div;
import org.zkoss.zul.impl.XulElement;

public class Gdirection extends XulElement {
	private String _panelId;
	private Div _panel;
	private String _mapId;
	private Gmaps _map;
	private String _start;
	private String _end;
	private String[] _direction;

	/**
	 * Set panel to google direction display,
	 * note you should make sure the panel is already attached to page before call this function
	 * because the uuid will be changed when attache to page
	 * @param panel
	 */
	public void setPanel (Div panel) {
		_panel = panel;
		setPanelId(panel.getUuid());
	}
	/**
	 * set panel id directly
	 * @param panelId
	 */
	public void setPanelId (String panelId) {
		_panelId = panelId;
		smartUpdate("panelId", _panelId);
	}
	/**
	 * get panel id
	 * @return
	 */
	public String getPanelId () {
		return _panelId;
	}
	/**
	 * Set map to google direction display,
	 * note you should make sure the map is already attached to page before call this function
	 * because the uuid will be changed when attache to page
	 * @param map
	 */
	public void setMap (Gmaps map) {
		_map = map;
		setMapId(map.getUuid());
	}
	/**
	 * set map id directly
	 * @param mapId
	 */
	public void setMapId (String mapId) {
		_mapId = mapId;
		smartUpdate("mapId", _mapId);
	}
	/**
	 * get map id
	 * @return
	 */
	public String getMapId () {
		return _mapId;
	}
	/**
	 * Sets the start point of direction
	 * @param start
	 */
	public void setStart (String start) {
		_start = start;
		setDirection (_start, _end);
	}
	/**
	 * Sets the end point of direction
	 * @param end
	 */
	public void setEnd (String end) {
		_end = end;
		setDirection (_start, _end);
	}
	/**
	 * set direction to route
	 * @param start the start point
	 * @param end the end point
	 */
	public void setDirection (String start, String end) {
		if (start != null && end != null) {
			_direction = new String[] {start, end};
			smartUpdate("direction", _direction);
		} else {
			_direction = null;
		}
	}
	/**
	 * set direction to route
	 * @param direction the String array [start, end]
	 */
	public void setDirection (String[] direction) {
		if (direction.length != 2)
			throw new WrongValueException ("the direction should exactly contains [start point, end point]");
		_direction = direction;
		smartUpdate("direction", _direction);
	}
	public String[] getDirection () {
		return _direction;
	}
	public String getZclass() {
		return _zclass == null ? "z-gdirection" : _zclass;
	}
	//-- ComponentCtrl --//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);
		if (_mapId != null)
			render(renderer, "mapId", _mapId);
		if (_panelId != null)
			render(renderer, "panelId", _panelId);
		if (_direction != null)
			render(renderer, "direction", _direction);
	}
}
