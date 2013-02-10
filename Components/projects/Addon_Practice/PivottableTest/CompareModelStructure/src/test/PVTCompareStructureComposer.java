package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.PivotHeaderTree;
import org.zkoss.pivot.Pivottable;
import org.zkoss.pivot.impl.StandardCalculator;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

/**
 * Tested with ZK 6.0.2 EE and ZK Pivottable 2.0.0
 *
 */
@SuppressWarnings("rawtypes")
public class PVTCompareStructureComposer extends SelectorComposer {
	/**
	 * generated serial version UID
	 */
	private static final long serialVersionUID = -2897873399288955635L;

	@Wire
	private Pivottable pivottable;
	@Wire
	private Pivottable pivottableTwo;
	@Wire
	private Label lbOne;
	@Wire
	private Label lbTwo;
	private TabularPivotModel _pivotModel;
	private TabularPivotModel _pivotModelTwo;

	public TabularPivotModel getPivotModel () throws Exception {
		if (_pivotModel == null) {
			_pivotModel = new TabularPivotModel(getData(), getColumns());

			// assign rows, the order matches to the level of row node field
			_pivotModel.setFieldType("RowOne", PivotField.Type.ROW);
			_pivotModel.setFieldType("RowTwo", PivotField.Type.ROW);
			_pivotModel.setFieldType("RowThree", PivotField.Type.ROW);

			// assign columns, the order matches to the level of column node field
			_pivotModel.setFieldType("ColumnOne", PivotField.Type.COLUMN);
			_pivotModel.setFieldType("ColumnTwo", PivotField.Type.COLUMN);

			// assign datas, the order matches to the order of data field
			_pivotModel.setFieldType("DataOne", PivotField.Type.DATA);

			PivotField field = _pivotModel.getField("RowOne");
			_pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});

		}
		return _pivotModel;
	}
	public TabularPivotModel getPivotModelTwo () throws Exception {
		if (_pivotModelTwo == null) {
			_pivotModelTwo = new TabularPivotModel(getData(), getColumns());

			// assign rows, the order matches to the level of row node field
			_pivotModelTwo.setFieldType("RowOne", PivotField.Type.ROW);
			_pivotModelTwo.setFieldType("RowTwo", PivotField.Type.ROW);
			_pivotModelTwo.setFieldType("RowThree", PivotField.Type.ROW);

			// assign columns, the order matches to the level of column node field
			_pivotModelTwo.setFieldType("ColumnOne", PivotField.Type.COLUMN);
			_pivotModelTwo.setFieldType("ColumnTwo", PivotField.Type.COLUMN);

			// assign datas, the order matches to the order of data field
			_pivotModelTwo.setFieldType("DataOne", PivotField.Type.DATA);

			PivotField field = _pivotModelTwo.getField("RowOne");
			_pivotModelTwo.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
		}
		return _pivotModelTwo;
	}

	/**
	 * prepare the data for pivottable's model
	 * The order of object put into data list should match
	 * the order of column name's
	 * @return
	 * @throws Exception
	 */
	public List<List<Object>> getData() throws Exception {
		List<List<Object>> result = new ArrayList<List<Object>>();
		Random r = new Random();

		for (int i = 0; i < 100; i++) {
			List<Object> data = new ArrayList<Object>();
			data.add("RowOne - " + (r.nextInt(2) + 1));
			data.add("RowTwo - " + (r.nextInt(2) + 1));
			data.add("RowThree - " + (r.nextInt(2) + 1));
			data.add("ColumnOne - " + (r.nextInt(2) + 1));
			data.add("ColumnTwo - " + (r.nextInt(2) + 1));
			data.add(r.nextInt(10));
			result.add(data);
		}
		return result;
	}

	/**
	 * prepare columns name for pivottable's model
	 * @return
	 */
	public List<String> getColumns() {
		return Arrays.asList(new String[]{
				"RowOne", "RowTwo", "RowThree",
				"ColumnOne", "ColumnTwo",
				"DataOne"
		});
	}

	@Listen ("onClick = #addRowBtn")
	public void addRow () {
		Random r = new Random();
		List<Object> data = new ArrayList<Object>();
		data.add("RowOne - 1");
		data.add("RowTwo - 1");
		data.add("RowThree - 3");
		data.add("ColumnOne - 1");
		data.add("ColumnTwo - 1");
		data.add(r.nextInt(10));
		((List<List<Object>>)_pivotModelTwo.getRawData()).add(data);
		_pivotModelTwo.updateRawData();
		pivottableTwo.setModel(null);
		pivottableTwo.setModel(_pivotModelTwo);
	}

	@Listen ("onClick = #addCalBtn")
	public void addCalculator () {
		PivotField field = _pivotModelTwo.getField("RowOne");
		_pivotModelTwo.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX, StandardCalculator.MIN});
	}

	@Listen ("onClick = #compareBtn")
	public void doCompare () {
		boolean isEqual = isStructureEqual(_pivotModel, _pivotModelTwo, true);
		lbOne.setValue(isEqual + "");
		lbOne.setStyle("font-size: 18px;"+(isEqual? "color: green;" : "color: red;"));
		isEqual = isStructureEqual(_pivotModel, _pivotModelTwo, false);
		lbTwo.setValue(isEqual + "");
		lbTwo.setStyle("font-size: 18px;"+(isEqual? "color: green;" : "color: red;"));
	}

	/**
	 * Check whether two pivot models are structure-equal
	 * @param modelOne the first pivot model
	 * @param modelTwo the second pivot model
	 * @param openedOnly whether only check the opened node and leaf
	 * @return boolean
	 */
	private boolean isStructureEqual (TabularPivotModel modelOne, TabularPivotModel modelTwo, boolean openedOnly) {
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
	public List<PivotHeaderNode> getNodeList (PivotHeaderTree headerTree, boolean openedOnly) {
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
	private boolean compareNodeList (List<PivotHeaderNode> list, List<PivotHeaderNode> listTwo, boolean openedOnly) {
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