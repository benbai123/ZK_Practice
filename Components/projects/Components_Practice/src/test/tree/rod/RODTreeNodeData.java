package test.tree.rod;

import java.util.List;

/**
 * tested with ZK 6.0.2
 * 
 * The interface for data bean that used in RODTreeModel and RODTreeNode
 * 
 * @author benbai123
 *
 */
public abstract class RODTreeNodeData {
	/**
	 * get children of this data
	 * @return
	 */
	public abstract List<? extends RODTreeNodeData> getChildren();
	/**
	 * get child count of this data, do not need to really get children
	 * @return
	 */
	public abstract int getChildCount ();
}
