package test.tree.rod.sample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import test.tree.rod.RODTreeNodeData;

/**
 * tested with ZK 6.0.2
 * 
 * The data bean of File, implements RODTreeNodeData
 * to work with RODTreeModel/RODTreeNode
 * 
 * @author benbai123
 *
 */
public class FileBean extends RODTreeNodeData {
	private List<FileBean> _children;

	private File _file;
	private String _name;
	private String _path;

	// constructor
	public FileBean (File file) {
		_file = file;
		_name = file.getName();
		_path = file.getAbsolutePath();
	}
	// getter, setter
	public String getName () {
		return _name;
	}
	public String getPath () {
		return _path;
	}
	/**
	 * implement {@link RODTreeNodeData#getChildren()}
	 */
	public List<FileBean> getChildren() {
		if (_children == null) {
			_children = new ArrayList<FileBean>();
			File f = new File(_path);
			File[] filelist = f.listFiles();
			if (filelist != null) {
				for (File file : filelist) {
					_children.add(new FileBean(file));
				}
			}
		}
		return _children;
	}
	/**
	 * implement {@link RODTreeNodeData#getChildCount()}
	 */
	public int getChildCount () {
		int childCount = 0;
		if (_children != null) {
			childCount =  _children.size();
		} else if (_file.isDirectory()) {
			File[] filelist = new File(_path).listFiles();
			childCount =  filelist == null? 0 : filelist.length;
		}
		return childCount;
	}
}
