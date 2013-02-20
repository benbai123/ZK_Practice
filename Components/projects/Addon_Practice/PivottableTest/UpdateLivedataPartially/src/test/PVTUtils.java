package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.PivotHeaderTree;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;

/**
 * Utilities to handle/retrieve pivot model status
 * @author benbai123
 *
 */
public class PVTUtils {
	public static int INDEX_NOT_FOUND = -2;
	public static PivotHeaderNode NODE_NOT_FOUND = null;

	/**
	 * Create snapshot of a pivot model
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TabularPivotModel createModelSnapshot (TabularPivotModel model) {
		// copy raw data
		List<List<Object>> rawData = new ArrayList<List<Object>>();
		rawData.addAll((List<List<Object>>)model.getRawData());

		TabularPivotField[] fields = model.getFields();

		// get columns from old model
		List<String> columns = new ArrayList<String>();
		// set field
		for (TabularPivotField tpf : fields) {
			columns.add(tpf.getFieldName());
		}

		TabularPivotModel snapShot = new TabularPivotModel(rawData, columns);
		syncModelStructure(model, snapShot);
		return snapShot;
	}
	/**
	 * Create a new pivot model based on
	 * current pivot model and new data 
	 * @param model
	 * @param newData
	 * @return
	 */
	public static TabularPivotModel cloneModelWithData (TabularPivotModel model, List<List<Object>>newData) {
		TabularPivotField[] fields = model.getFields();

		// get columns from old model
		List<String> columns = new ArrayList<String>();
		// set field
		for (TabularPivotField tpf : fields) {
			columns.add(tpf.getFieldName());
		}

		TabularPivotModel newModel = new TabularPivotModel(newData, columns);
		PVTUtils.syncModelStructure(model, newModel);
		return newModel;
	}
	/**
	 * called when the fields of first pivottable are changed
	 */
	public static void syncModelStructure (TabularPivotModel model, TabularPivotModel modelTwo) {
		syncFields(model.getRowFields(), modelTwo);
		syncFields(model.getColumnFields(), modelTwo);
		syncFields(model.getDataFields(), modelTwo);
		syncFields(model.getFields(PivotField.Type.UNUSED), modelTwo);
		syncOpenStatus(model.getRowHeaderTree().getRoot(), modelTwo.getRowHeaderTree().getRoot(), false);
		syncOpenStatus(model.getColumnHeaderTree().getRoot(), modelTwo.getColumnHeaderTree().getRoot(), false);
	}
	private static void syncFields (TabularPivotField[] fields, TabularPivotModel model) {
		for (TabularPivotField f : fields) {
			model.setFieldType(f.getFieldName(), f.getType());

			PivotField field = model.getField(f.getFieldName());
			model.setFieldSubtotals(field, f.getSubtotals());
		}
	}
	/**
	 * Synchronize the open status of two pivot header trees
	 * 
	 * @param root the root of the base pivot header tree (or its sub trees)
	 * @param rootTwo the root of the pivot header tree (or its sub trees) to sync
	 * @param checkAll whether sync whole tree, <br>
	 * true: sync whole tree, put every node of base pivot header tree into open list to sync<br>
	 * false: sync only current view, only put the displayed node into open list to sync
	 */
	private static void syncOpenStatus (PivotHeaderNode root, PivotHeaderNode rootTwo, boolean checkAll) {
		Map<Object, PivotHeaderNode> originalOpenMap = new HashMap<Object, PivotHeaderNode>();

		// sync displayed node only if not checkAll
		// so do not need to scan whole header tree
		for (PivotHeaderNode node : root.getChildren()) {
			// checkAll: sync each node
			// !checkAll: sync displayed node
			if (checkAll
				|| (node.getDepth() == 1 || node.getParent().isOpen())) {
				originalOpenMap.put(node.getKey(), node);
			}
		}
		// for each node in children of rootTwo
		for (PivotHeaderNode newNode : rootTwo.getChildren()) {
			PivotHeaderNode node = originalOpenMap.get(newNode.getKey());
			if (node != null) {
				newNode.setOpen(node.isOpen());
				// recursively sync sub trees
				syncOpenStatus(node, newNode, checkAll);
			}
		}
	}
	/**
	 * Check whether two pivot models are structure-equal
	 * @param modelOne the first pivot model
	 * @param modelTwo the second pivot model
	 * @param openedOnly whether only check the opened nodes and leaf
	 * @param openedOnly whether only check the leaf nodes
	 * @return boolean
	 */
	public static boolean isStructureEqual (TabularPivotModel modelOne, TabularPivotModel modelTwo, boolean openedOnly, boolean leafOnly) {
		boolean equal = true;

		List<PivotHeaderNode> rows = getNodeList(modelOne.getRowHeaderTree(), openedOnly, leafOnly);
		List<PivotHeaderNode> columns = getNodeList(modelOne.getColumnHeaderTree(), openedOnly, leafOnly);
		List<PivotHeaderNode> rowsTwo = getNodeList(modelTwo.getRowHeaderTree(), openedOnly, leafOnly);
		List<PivotHeaderNode> columnsTwo = getNodeList(modelTwo.getColumnHeaderTree(), openedOnly, leafOnly);
		TabularPivotField[] dataFields = modelOne.getDataFields();
		TabularPivotField[] dataFieldsTwo = modelTwo.getDataFields();

		if (rows.size() != rowsTwo.size()
			|| columns.size() != columnsTwo.size()
			|| dataFields.length != dataFieldsTwo.length) {
			equal = false;
		} else {
			equal = isNodesEqual(rows, rowsTwo, openedOnly) && isNodesEqual(columns, columnsTwo, openedOnly);
		}
		return equal;
	}
	/**
	 * check whether a node is the last child of specified parent node
	 * @param parent
	 * @param node
	 * @return
	 */
	public static boolean isLastChild (PivotHeaderNode parent, PivotHeaderNode node) {
		List<? extends PivotHeaderNode> children = parent.getChildren();
		// use == to make sure they are the same instance
		return node.getParent() == parent
				&& children.get(children.size()-1) == node;
	}
	public static List<PivotHeaderNode> getRowLeafList (TabularPivotModel model) {
		return PVTUtils.getNodeList(model.getRowHeaderTree(), true, true);
	}
	public static  List<PivotHeaderNode> getColumnLeafList (TabularPivotModel model) {
		return PVTUtils.getNodeList(model.getColumnHeaderTree(), true, true);
	}
	/**
	 * Get pivot nodes in a pivot header tree
	 * @param headerTree the pivot header tree to get pivot nodes
	 * @param openedOnly whether get only the opened nodes and leaf
	 * @param leafOnly whether get only leaf node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<PivotHeaderNode> getNodeList (PivotHeaderTree headerTree, boolean openedOnly, boolean leafOnly) {
		PivotHeaderNode root = headerTree.getRoot();
		List<PivotHeaderNode> all = new ArrayList<PivotHeaderNode>();
		List<PivotHeaderNode> nodes = new ArrayList<PivotHeaderNode>();
		List<PivotHeaderNode> tmp = new ArrayList<PivotHeaderNode>();
		nodes = (List<PivotHeaderNode>)root.getChildren();

		// all: all target nodes
		// nodes: the node list to loop through
		// tmp: temp store the children while loop through nodes
		boolean foundAllNodes = false;
		while (!foundAllNodes) {
			foundAllNodes = true;
			for (PivotHeaderNode phn : nodes) {
				// get only opened and leaf nodes
				// if opened only
				if (phn.isOpen() || !openedOnly) {
					List<PivotHeaderNode> children = (List<PivotHeaderNode>)phn.getChildren();
					if (children != null && children.size() > 0) {
						tmp.addAll(children);
						foundAllNodes = false;
					} else {
						// add to all if haven't found any children
						// so do not need to loop through them again
						if (foundAllNodes && leafOnly) {
							all.add(phn);
						} else {
							// already found some children,
							// add to tmp to make the order correct 
							tmp.add(phn);
						}
					}
				} else {
					if (foundAllNodes && leafOnly) {
						all.add(phn);
					} else {
						tmp.add(phn);
					}
				}
			}
			if (!leafOnly) {
				all.addAll(nodes);
			}
			nodes = tmp;
			tmp = new ArrayList<PivotHeaderNode>();
		}
		return all;
	}

	/**
	 * Compare the nodes under two node lists one by one
	 * @param list the first node list
	 * @param listTwo the second node list
	 * @param openedOnly whether compare calculator according to open status
	 * @return
	 */
	private static boolean isNodesEqual (List<PivotHeaderNode> list, List<PivotHeaderNode> listTwo, boolean openedOnly) {
		boolean equal = true;
		int i, j;
		// compare nodes
		for (i = 0; i < list.size() && equal; i++) {
			PivotHeaderNode node = list.get(i);
			PivotHeaderNode nodeTwo = listTwo.get(i);

			// key should be equal
			// depth should be equal
			// subtotal count should be equal
			//
			// openedOnly: get subtotal count according to the open stats of node
			// !openedOnly: get subtotal count as the node is opened
			if (!node.getKey().equals(nodeTwo.getKey())
				|| node.getDepth() != nodeTwo.getDepth()
				|| node.getSubtotalCount(node.isOpen() || !openedOnly) != nodeTwo.getSubtotalCount(nodeTwo.isOpen() || !openedOnly)) {
				equal = false;
				break;
			}

			// check calculators if any
			// openedOnly: get subtotal count according to the open stats of node
			// !openedOnly: get subtotal count as the node is opened
			if (node.getSubtotalCount(node.isOpen() || !openedOnly) > 0) {
				Calculator[] cals = node.getField().getSubtotals();
				Calculator[] calsTwo = nodeTwo.getField().getSubtotals();
				for (j = 0; j < cals.length; j++) {
					Calculator cal = cals[j];
					Calculator calTwo = calsTwo[j];

					// label and label key should be euqal
					if (!cal.getLabel().equals(calTwo.getLabel())
						|| !cal.getLabelKey().equals(calTwo.getLabelKey())) {
						equal = false;
						break;
					}
				}
			}
		}
		return equal;
	}

	/**
	 * get a list of key to this node
	 * @param node pivot header node
	 * @return
	 */
	public static List<Object> getNodeKeys (PivotHeaderNode node) {
		List<Object> keys = new ArrayList<Object>();
		if (node != null) {
			while (node.getDepth() > 0) {
				keys.add(0, node.getKey());
				node = node.getParent();
			}
		}
		return keys;
	}
	/**
	 * get the status of a cell
	 * @param model the base pivot model to compare
	 * @param cell the attributes denote a cell
	 * @param value the new value
	 * @return<br>
	 * "up": new value is larger than old value of that cell in base pivot model<br>
	 * "down": new value is smaller than old value of that cell in base pivot model<br>
	 * null: otherwise
	 */
	public static String getDirection (TabularPivotModel model, CellAttributes cell, Number value) {
		String dir = null;
		double base = 0.0;
		if (value != null) {
			Number oldValue = getValue(model, cell);
			if (oldValue != null) {
				base = oldValue.doubleValue();
			}
			double newValue = value.doubleValue();
			dir = newValue > base? "up" :
					newValue < base? "down" : null;
		}
		return dir;
	}
	/**
	 * get value from a model based on cellAttr
	 * @param model the model to get value
	 * @param cellAttr the attributes represent a specific cell
	 * @return
	 */
	public static Number getValue (TabularPivotModel model, CellAttributes cellAttr) {
		PivotHeaderNode row = findNode(model.getRowHeaderTree().getRoot(), cellAttr.getRowKeys());
		PivotHeaderNode col = findNode(model.getColumnHeaderTree().getRoot(), cellAttr.getColKeys());
		int dataIdx = findDataFieldIndex(model, cellAttr.getDataFieldName());
		int rowCalIdx = -1;
		int colCalIdx = -1;
		if (row != null) {
			rowCalIdx = findCalculatorIndex(row, cellAttr.getRowCalculatorLabelKey());
		}
		if (col != null) {
			colCalIdx = findCalculatorIndex(col, cellAttr.getColCalculatorLabelKey());
		}
		if (row == NODE_NOT_FOUND // row should exists
			|| col == NODE_NOT_FOUND // col should exists
			|| dataIdx == INDEX_NOT_FOUND // data field should exists
			|| rowCalIdx == INDEX_NOT_FOUND // row calculator should exists if _rowCalculatorLabelKey is not null
			|| colCalIdx == INDEX_NOT_FOUND) { // column calculator should exists if _colCalculatorLabelKey is not null
			return null;
		}
		return model.getValue(row, rowCalIdx, col, colCalIdx, dataIdx);
	}
	/**
	 * find the corresponding node in a pivot model
	 * @param root The root of pivot header tree to search the corresponding node
	 * @param keys list of node key
	 * @return PivotHeaderNode the corresponding node, NODE_NOT_FOUND denotes not found
	 */
	public static PivotHeaderNode findNode (PivotHeaderNode root, List<Object> keys) {
		PivotHeaderNode node = null;
		boolean found = true;
		node = root;
		if (keys == null || keys.size() == 0) {
			return root; // grand total
		}
		// for each key
		for (Object o : keys) {
			if (found) { // stop if not found in previous loop
				found = false;
				// search until found a row with the key
				for (PivotHeaderNode child : node.getChildren()) {
					if (child.getKey().equals(o)) {
						node = child;
						found = true;
						break;
					}
				}
			}
		}

		return found? node : // header nood
			NODE_NOT_FOUND; // not found
	}
	/**
	 * Search the corresponding data field index in a pivot model
	 * @param model the model to search
	 * @param fieldName the name of data field
	 * @return int the corresponding data field index, INDEX_NOT_FOUND denotes not found
	 */
	public static int findDataFieldIndex (TabularPivotModel model, String fieldName) {
		// field name should not be null
		if (fieldName != null) {
			TabularPivotField[] dataFields = model.getDataFields();
			for (int idx = 0; idx < dataFields.length; idx++) {
				if (dataFields[idx].getFieldName().equals(fieldName)) {
					return idx;
				}
			}
		}
		return INDEX_NOT_FOUND; // not found
	}
	/**
	 * Search the corresponding calculator index of a pivot field
	 * @param field the field to search
	 * @param labelKey the labelKey of calculator
	 * @return int the corresponding calculator index, INDEX_NOT_FOUND denotes not found
	 */
	public static int findCalculatorIndex (PivotHeaderNode node, String labelKey) {
		if (labelKey == null) return -1;
		Calculator[] cals = node.getField().getSubtotals();
		for (int i = 0; i < cals.length; i++) {
			if (cals[i].getLabelKey().equals(labelKey)) {
				return i;
			}
		}
		return INDEX_NOT_FOUND; // not found
	}
}