package impl;

import java.util.Map;

import org.zkoss.zk.ui.Component;

public class RequestFromWebSocket {
	/** Event name */
	private String _command;
	/** Target component */
	private Component _comp;
	/** Data map */
	private Map<Object, Object> _data;
	// Constructor
	public RequestFromWebSocket (String command, Component comp, Map<Object, Object> data) {
		_command = command;
		_comp = comp;
		_data = data;
	}
	// getters
	public String getCommand () {
		return _command;
	}
	public Component getComponent () {
		return _comp;
	}
	public Map<Object, Object> getData () {
		return _data;
	}
}
