package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.PivotHeaderTree;
import org.zkoss.pivot.impl.StandardCalculator;
import org.zkoss.pivot.impl.TabularPivotModel;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;

/**
 * Tested with ZK 6.0.2 EE and ZK Pivottable 2.0.0
 *
 */
@SuppressWarnings("rawtypes")
public class TestComposer extends SelectorComposer {
	/**
	 * generated serial version UID
	 */
	private static final long serialVersionUID = -2897873399288955635L;
	@Wire
	private Textbox tbx;
	private TabularPivotModel _pivotModel;

	/**
	 * Get pivottable's model
	 * @return TabularPivotModel the pivottable's model
	 * @throws Exception
	 */
	public TabularPivotModel getPivotModel () throws Exception {
		if (_pivotModel == null) {
			_pivotModel = new TabularPivotModel(getData(), getColumns());

			// assign rows, the order matches to the level of row node field
			_pivotModel.setFieldType("RowOne", PivotField.Type.ROW);
			_pivotModel.setFieldType("RowTwo", PivotField.Type.ROW);

			// assign columns, the order matches to the level of column node field
			_pivotModel.setFieldType("ColumnOne", PivotField.Type.COLUMN);
			_pivotModel.setFieldType("ColumnTwo", PivotField.Type.COLUMN);

			// assign datas, the order matches to the order of data field
			_pivotModel.setFieldType("DataOne", PivotField.Type.DATA);
			_pivotModel.setFieldType("DateTwo", PivotField.Type.DATA);

			PivotField field = _pivotModel.getField("RowOne");
			_pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
			field = _pivotModel.getField("ColumnOne");
			_pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
		}
		return _pivotModel;
	}
	/**
	 * prepare the data for pivottable's model
	 * The order of object put into data list matches
	 * the order of column name's order
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
			data.add("ColumnOne - " + (r.nextInt(2) + 1));
			data.add("ColumnTwo - " + (r.nextInt(2) + 1));
			data.add(r.nextInt(100)+1000);
			data.add(r.nextInt(100)+1000);
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
				"RowOne", "RowTwo",
				"ColumnOne", "ColumnTwo",
				"DataOne", "DateTwo"
		});
	}

	/**
	 * Display the current content of pivottable
	 */
	@Listen ("onClick = #btn")
	public void showCurrentContent () {
		StringBuilder sb = new StringBuilder("");

		PivotHeaderTree rowHeaderTree = _pivotModel.getRowHeaderTree();
		PivotHeaderTree columnHeaderTree = _pivotModel.getColumnHeaderTree();
		PivotHeaderNode rRoot = rowHeaderTree.getRoot();
		PivotHeaderNode cRoot = columnHeaderTree.getRoot();
		// the length of data fields under a column
		int dataFieldsLength = _pivotModel.getDataFields().length;

		List<PivotHeaderNode> rows = getNodeList(rowHeaderTree);
		List<PivotHeaderNode> columns = getNodeList(columnHeaderTree);

		rows.add(rRoot);
		for (PivotHeaderNode row : rows) { // for each row
			// show original data
			displayColumns(rRoot, cRoot, row, columns, dataFieldsLength, -1, sb);
			// not first level row node
			if (row.getDepth() > 1) {
				PivotHeaderNode parentRow = row.getParent();
				List children = parentRow.getChildren();
				// is last child
				if (children.get(children.size()-1).equals(row)) {
					for (int calIdx = 0; calIdx < parentRow.getSubtotalCount(false); calIdx++) {
						// show row subtotal
						displayColumns(rRoot, cRoot, parentRow, columns, dataFieldsLength, calIdx, sb);
					}
				}
			}
		}

		tbx.setValue(sb.toString());
	}
	/**
	 * Get all current "leaf" node of row/column
	 * @param headerTree
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PivotHeaderNode> getNodeList (PivotHeaderTree headerTree) {
		PivotHeaderNode root = headerTree.getRoot();
		List<PivotHeaderNode> nodes = new ArrayList<PivotHeaderNode>();
		List<PivotHeaderNode> tmp = new ArrayList<PivotHeaderNode>();
		nodes = (List<PivotHeaderNode>)root.getChildren();

		boolean foundAllRows = false;
		while (!foundAllRows) {
			foundAllRows = true;
			for (PivotHeaderNode phn : nodes) {
				// get only leaf nodes
				// you can adjust condition as needed
				if (phn.isOpen()) {
					List<PivotHeaderNode> children = (List<PivotHeaderNode>)phn.getChildren();
					if (children != null && children.size() > 0) {
						tmp.addAll(children);
						foundAllRows = false;
					} else {
						tmp.add(phn);
					}
				} else {
					tmp.add(phn);
				}
			}
			nodes = tmp;
			tmp = new ArrayList<PivotHeaderNode>();
		}
		return nodes;
	}
	private void displayColumns (PivotHeaderNode rRoot, PivotHeaderNode cRoot, 
			PivotHeaderNode row, List<PivotHeaderNode> columns, int dataFieldsLength, int rowCalIdx, StringBuilder sb) {
		for (PivotHeaderNode column : columns) { // for each column
			// for each data field
			for (int i = 0; i < dataFieldsLength; i++) {
				if (i > 0) {
					sb.append("\t");
				}
				// get data value from pivot model by row node, column node and data index
				sb.append(_pivotModel.getValue(row, rowCalIdx, column, -1, i) == null?
						"####" : _pivotModel.getValue(row, rowCalIdx, column, -1, i));

				// last data and
				// not first level node
				if (i+1 == dataFieldsLength
					&& column.getDepth() > 1) {
					PivotHeaderNode parentColumn = column.getParent();
					List children = parentColumn.getChildren();
					// is last child
					if (children.get(children.size()-1).equals(column)) {
						// for each column calculator
						for (int calIdx = 0; calIdx < parentColumn.getSubtotalCount(false); calIdx++) {
							sb.append("\t|\t");
							// again, for each data field
							for (int j = 0; j < dataFieldsLength; j++) {
								if (j > 0) {
									sb.append("\t");
								}
								// get data value from pivot model by row node, column node and data index
								sb.append(_pivotModel.getValue(row, rowCalIdx, parentColumn, calIdx, j) == null?
										"####" : _pivotModel.getValue(row, rowCalIdx, parentColumn, calIdx, j));
							}
						}
					}
				}
			}
			sb.append("\t|\t");
		}
		for (int i = 0; i < dataFieldsLength; i++) {
			if (i > 0) {
				sb.append("\t");
			}
			// grand total for columns
			sb.append(_pivotModel.getValue(row, rowCalIdx, cRoot, -1, i) == null?
					"####" : _pivotModel.getValue(row, rowCalIdx, cRoot, -1, i));
		}
		sb.append("\n");
	}
}
