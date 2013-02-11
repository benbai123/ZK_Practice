package test;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.PivotHeaderTree;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;

public class PVTUtils {
	/**
	 * Check whether two pivot models are structure-equal
	 * @param modelOne the first pivot model
	 * @param modelTwo the second pivot model
	 * @param openedOnly whether only check the opened node and leaf
	 * @return boolean
	 */
	public static boolean isStructureEqual (TabularPivotModel modelOne, TabularPivotModel modelTwo, boolean openedOnly) {
		boolean equal = true;

		List<PivotHeaderNode> rows = getNodeList(modelOne.getRowHeaderTree(), openedOnly);
		List<PivotHeaderNode> columns = getNodeList(modelOne.getColumnHeaderTree(), openedOnly);
		List<PivotHeaderNode> rowsTwo = getNodeList(modelTwo.getRowHeaderTree(), openedOnly);
		List<PivotHeaderNode> columnsTwo = getNodeList(modelTwo.getColumnHeaderTree(), openedOnly);
		TabularPivotField[] dataFields = modelOne.getDataFields();
		TabularPivotField[] dataFieldsTwo = modelTwo.getDataFields();

		if (rows.size() != rowsTwo.size()
			|| columns.size() != columnsTwo.size()
			|| dataFields.length != dataFieldsTwo.length) {
			equal = false;
		} else {
			equal = compareNodeList(rows, rowsTwo, openedOnly) && compareNodeList(columns, columnsTwo, openedOnly);
		}
		return equal;
	}
	/**
	 * Get pivot nodes in a pivot header tree
	 * @param headerTree the pivot header tree to get pivot nodes
	 * @param openedOnly whether get only the opened nodes and leaf
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<PivotHeaderNode> getNodeList (PivotHeaderTree headerTree, boolean openedOnly) {
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
				// get only onened and leaf nodes
				// if opened only
				if (phn.isOpen() || !openedOnly) {
					List<PivotHeaderNode> children = (List<PivotHeaderNode>)phn.getChildren();
					if (children != null && children.size() > 0) {
						tmp.addAll(children);
						foundAllNodes = false;
					} else {
						tmp.add(phn);
					}
				} else {
					tmp.add(phn);
				}
			}
			all.addAll(nodes);
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
	private static boolean compareNodeList (List<PivotHeaderNode> list, List<PivotHeaderNode> listTwo, boolean openedOnly) {
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
}
