package test.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * tested with ZK 6.0.2
 * @author ben
 *
 */
public class ModelDataChangeEvent extends Event {
	private static final long serialVersionUID = 3645653880934243558L;

	// the changed data, can be any Object
	private final Object _data;
	// the index to apply this change
	private int _index;

	/**
	 * Get a ModelDataChangeEvent
	 * @param target the target to post this event
	 * @param data the changed data
	 * @param index the index to apply this change
	 * @return ModelDataChangeEvent
	 */
	public static ModelDataChangeEvent getModelDataChangeEvent (Component target, Object data, int index) {
		return new ModelDataChangeEvent("onModelDataChange", target, data, index);
	}
	/**
	 * 
	 * @param name event name, should starts with "on"
	 * @param target
	 * @param data
	 * @param index
	 */
	public ModelDataChangeEvent (String name, Component target, Object data, int index) {
		super(name, target);
		_data = data;
		_index = index;
	}
	public Object getData () {
		return _data;
	}
	public int getIndex () {
		return _index;
	}
}