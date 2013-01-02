package test.tree.rod.sample;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.TreeModel;

import test.tree.rod.RODTreeModel;
import test.tree.rod.RODTreeNode;
/**
 * tested with ZK 6.0.2
 * 
 * @author benbai123
 *
 */
public class TestVM {
	RODTreeModel<FileBean> _directoryTreeModel;
	FileBean _selectedDirectory;
	File file = new File("C:" + File.separator + "Program Files");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TreeModel<RODTreeNode<FileBean>> getDirectoryModel () {
		RODTreeNode root = new RODTreeNode(null,
				new RODTreeNode[] {new RODTreeNode(new FileBean(file), (List)null)
		});
		if (_directoryTreeModel == null) {
			_directoryTreeModel = new RODTreeModel<FileBean>(root);
		}
		return _directoryTreeModel;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Command
	public void updateSelectedDirectory (@ContextParam(ContextType.TRIGGER_EVENT) SelectEvent event) {
		Set s = event.getSelectedObjects();
		if (s != null && s.size() > 0) {
			_selectedDirectory = ((RODTreeNode<FileBean>)s.iterator().next()).getData();
			System.out.println("selected: " + _selectedDirectory.getName() + ", path = " + _selectedDirectory.getPath());
		}
	}
}