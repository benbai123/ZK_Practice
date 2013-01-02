package test.tree.rod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * tested with ZK 6.0.2
 * 
 * The TreeNode that supports ROD, with any data bean
 * that implements interface RODTreeNodeData<br><br>
 *
 * The basic rules:<br>
 * 
 * 1. Only call getChildren while you really want
 * to get children or the children has been loaded<br>
 * 2. Let data bean handle the 'data' side works
 * 
 * 
 * @author benbai123
 *
 * @param <T>
 */
public class RODTreeNode<T extends RODTreeNodeData> {

	private static final long serialVersionUID = -5106504924526127666L;

	// the data bean
	private T _data;
	// parent tree node
	private RODTreeNode<T> _parent;
	// child list
	private List<RODTreeNode<T>> _children;
	// whether this node is open
	private boolean _open;


	// constructor for receiving List children
	public RODTreeNode(T data, List<RODTreeNode<T>> children) {
		_data = data;
		_children = children;
		init();
	}
	// constructor for receiving Array children
	public RODTreeNode(T data, RODTreeNode<T>[] children) {
		_data = data;
		if (children != null
			&& children.length > 0)
			_children = Arrays.asList(children);
		init();
	}

	/**
	 * initial the parent-child relation
	 */
	public void init () {
		if (_children != null) {
			for (RODTreeNode<T> child : _children) {
				child.setParent(this);
			}
		}
	}
	//setter/getter
	/**
	 * @return T the data bean
	 */
	public T getData () {
		return _data;
	}

	/**
	 * @param open whether the node is open
	 */
	public void setOpen (boolean open) {
		_open = open;
	}
	public boolean isOpen () {
		return _open;
	}
	public boolean getOpen () {
		return _open;
	}
	/**
	 * @param parent parent tree node
	 */
	public void setParent (RODTreeNode<T> parent) {
		_parent = parent;
	}
	public RODTreeNode<T> getParent () {
		return _parent;
	}
	/**
	 * create TreeNode children based on the children of data bean
	 * 
	 * @see {@link RODTreeNodeData#getChildren()}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RODTreeNode<T>> getChildren () {
		if (_children == null) {
			_children = new ArrayList<RODTreeNode<T>>();
			List<T> childrenData = (List<T>)_data.getChildren();
			for (T data : childrenData) {
				RODTreeNode<T> child = new RODTreeNode<T>(data, (List<RODTreeNode<T>>)null);
				child.setParent(this);
				_children.add(child);
			}
		}

		return _children;
	}
	/**
	 * get child count from children size if children created,
	 * or get child count from data
	 * 
	 * @see RODTreeNodeData#getChildCount()
	 * @return
	 */
	public int getChildCount () {
		int count = _children == null?
					(_data == null? 0
						: _data.getChildCount())
				: _children.size();
		return count;
	}
}
