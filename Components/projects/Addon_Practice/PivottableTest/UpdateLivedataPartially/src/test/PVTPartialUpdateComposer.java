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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;

/**
 * Tested with ZK 6.0.2 EE and ZK Pivottable 2.0.0
 *
 */
@SuppressWarnings("rawtypes")
public class PVTPartialUpdateComposer extends SelectorComposer {
	/**
	 * generated serial version UID
	 */
	private static final long serialVersionUID = -2897873399288955635L;
	@Wire
	private Textbox tbx;
	@Wire
	private Pivottable pivottable;
	private TabularPivotModel _pivotModel;
	private TabularPivotModel _pivotModelFirstSnapshot;

	private TabularPivotModel _latestPivotModel;
	private List _currentValues = new ArrayList();

	private List<List<Object>> _latestRawData;
	/**
	 * Get pivottable's model, also make a snapshot of it
	 * @return TabularPivotModel the pivottable's model
	 * @throws Exception
	 */
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
			_pivotModel.setFieldType("ColumnThree", PivotField.Type.COLUMN);

			// assign datas, the order matches to the order of data field
			_pivotModel.setFieldType("DataOne", PivotField.Type.DATA);
			_pivotModel.setFieldType("DateTwo", PivotField.Type.DATA);

			PivotField field = _pivotModel.getField("RowOne");
			_pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
			field = _pivotModel.getField("RowTwo");
			_pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
			field = _pivotModel.getField("ColumnOne");
			_pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
			field = _pivotModel.getField("ColumnTwo");
			_pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});

			_pivotModelFirstSnapshot = createSnapshot(_pivotModel);
		}
		return _pivotModel;
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
			data.add("ColumnThree - " + (r.nextInt(2) + 1));
			data.add(r.nextInt(10));
			data.add(r.nextInt(10));
			result.add(data);
		}
		return result;
	}
	/**
	 * Generate some random data
	 * @return
	 */
	public List<List<Object>> getNewDatas() {
		List<List<Object>> result = new ArrayList<List<Object>>();
		Random r = new Random();
		int amount = 1;

		for (int i = 0; i < amount; i++) {
			List<Object> data = new ArrayList<Object>();
			Object o;
			o = "RowOne - " + (r.nextInt(5) + 1);
			System.out.print("add: " + o + "\t");
			data.add(o);
			o = "RowTwo - " + (r.nextInt(5) + 1);
			System.out.print(o + "\t");
			data.add(o);
			o = "RowThree - " + (r.nextInt(5) + 1);
			System.out.print(o + "\t");
			data.add(o);
			o = "ColumnOne - " + (r.nextInt(5) + 1);
			System.out.print(o + "\t");
			data.add(o);
			o = "ColumnTwo - " + (r.nextInt(5) + 1);
			System.out.print(o + "\t");
			data.add(o);
			o = "ColumnThree - " + (r.nextInt(5) + 1);
			System.out.print(o + "\t");
			data.add(o);
			o = -5 + r.nextInt(10);
			System.out.print(o + "\t");
			data.add(o);
			o = -5 + r.nextInt(10);
			System.out.print(o + "\t\n");
			data.add(o);
			result.add(data);
		}
		System.out.println();
		return result;
	}
	/**
	 * prepare columns name for pivottable's model
	 * @return
	 */
	public List<String> getColumns() {
		return Arrays.asList(new String[]{
				"RowOne", "RowTwo", "RowThree",
				"ColumnOne", "ColumnTwo", "ColumnThree",
				"DataOne", "DateTwo"
		});
	}
	/**
	 * Create a snapshot of current pivot model
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TabularPivotModel createSnapshot (TabularPivotModel model) {
		List<List<Object>> rawData = (List<List<Object>>)model.getRawData();
		TabularPivotField[] fields = model.getFields();

		// get columns from old model
		List<String> columns = new ArrayList<String>();
		// set field
		for (TabularPivotField tpf : fields) {
			columns.add(tpf.getFieldName());
		}

		TabularPivotModel snapShot = new TabularPivotModel(rawData, columns);
		for (TabularPivotField f : fields) {
			snapShot.setFieldType(f.getFieldName(), f.getType());

			PivotField field = snapShot.getField(f.getFieldName());
			snapShot.setFieldSubtotals(field, f.getSubtotals());
		}
		return snapShot;
	}
	/**
	 * Create a new pivot model based on
	 * current pivot model and new data 
	 * @param model
	 * @param newData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TabularPivotModel createNewModel (TabularPivotModel model, List<List<Object>>newData) {
		TabularPivotField[] fields = model.getFields();

		// get columns from old model
		List<String> columns = new ArrayList<String>();
		// set field
		for (TabularPivotField tpf : fields) {
			columns.add(tpf.getFieldName());
		}
		if (_latestRawData == null) {
			_latestRawData = new ArrayList<List<Object>>();
			_latestRawData.addAll((List<List<Object>>)model.getRawData());
		}
		_latestRawData.addAll(newData);
		TabularPivotModel newModel = new TabularPivotModel(_latestRawData, columns);
		for (TabularPivotField f : fields) {
			newModel.setFieldType(f.getFieldName(), f.getType());

			PivotField field = newModel.getField(f.getFieldName());
			newModel.setFieldSubtotals(field, f.getSubtotals());
		}
		syncOpenStatus(model.getRowHeaderTree().getRoot(), newModel.getRowHeaderTree().getRoot(), false);
		syncOpenStatus(model.getColumnHeaderTree().getRoot(), newModel.getColumnHeaderTree().getRoot(), false);
		return newModel;
	}
	private void syncOpenStatus (PivotHeaderNode root, PivotHeaderNode newRoot, boolean checkAll) {
		List<PivotHeaderNode> originalOpenList = new ArrayList<PivotHeaderNode>();
		List<PivotHeaderNode> newOpenList = new ArrayList<PivotHeaderNode>();

		// sync opened node only if not checkAll
		// so do not need to scan whole header tree
		for (PivotHeaderNode node : root.getChildren()) {
			if (checkAll || node.isOpen()) {
				originalOpenList.add(node);
			}
		}
		for (PivotHeaderNode node : originalOpenList) {
			boolean found = false;
			for (PivotHeaderNode newNode : newRoot.getChildren()) {
				if (node.getKey().equals(newNode.getKey())) {
					found = true;
					newNode.setOpen(node.isOpen());
					newOpenList.add(newNode);
				}
			}
			if (!found) {
				// sync list size
				newOpenList.add(null);
			}
		}

		for (int i = 0; i < originalOpenList.size(); i++) {
			if (newOpenList.get(i) != null) {
				syncOpenStatus(originalOpenList.get(i), newOpenList.get(i), checkAll);
			}
		}
	}

	/**
	 * Display the current content of pivottable
	 * @throws Exception 
	 */
	@Listen ("onClick = #btn")
	public void addData () throws Exception {
		//alert(isStructureEqual(_pivotModel, _pivotModelTwo)? "Their structure are equal" : "They are different");
		List<List<Object>> newData = getNewDatas();
		if (newData.size() > 0) {
			_latestPivotModel = createNewModel(_pivotModel, newData); 
			if (isStructureEqual(_pivotModel, _latestPivotModel)) {
				updateChangedData(_pivotModel, _latestPivotModel);
			} else {
				_currentValues = null;
				System.out.println(" Structure Changed ");

				pivottable.setModel(_latestPivotModel);
				doUpdate(true);
				_pivotModel = _latestPivotModel;
				_latestPivotModel = null;
			}
		}
	}
	private void updateChangedData (TabularPivotModel currentModel, TabularPivotModel newModel) {
		storeValue();
		doUpdate(false);
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
	private boolean isStructureEqual (TabularPivotModel modelOne, TabularPivotModel modelTwo) {
		boolean equal = true;

		List<PivotHeaderNode> rows = getNodeList(modelOne.getRowHeaderTree());
		List<PivotHeaderNode> columns = getNodeList(modelOne.getColumnHeaderTree());
		List<PivotHeaderNode> rowsTwo = getNodeList(modelTwo.getRowHeaderTree());
		List<PivotHeaderNode> columnsTwo = getNodeList(modelTwo.getColumnHeaderTree());
		TabularPivotField[] dataFields = modelOne.getDataFields();
		TabularPivotField[] dataFieldsTwo = modelTwo.getDataFields();

		if (rows.size() != rowsTwo.size()
			|| columns.size() != columnsTwo.size()
			|| dataFields.length != dataFieldsTwo.length) {
			equal = false;
		} else {
			equal = compareNodeList(rows, rowsTwo) && compareNodeList(columns, columnsTwo);
		}
		return equal;
	}
	/**
	 * Compare two list of PivotHeaderNode
	 * @param list
	 * @param listTwo
	 * @return
	 */
	private boolean compareNodeList (List<PivotHeaderNode> list, List<PivotHeaderNode> listTwo) {
		boolean equal = true;
		int i, j;
		// compare rows
		for (i = 0; i < list.size() && equal; i++) {
			PivotHeaderNode node = list.get(i);
			PivotHeaderNode nodeTwo = listTwo.get(i);

			// key should be equal
			// depth should be equal
			// subtotal count should be equal
			if (!node.getKey().equals(nodeTwo.getKey())
				|| node.getDepth() != nodeTwo.getDepth()
				|| node.getSubtotalCount(true) != nodeTwo.getSubtotalCount(true)) {
				equal = false;
				break;
			}

			// check calculators if any
			if (node.getSubtotalCount(true) > 0) {
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
	public void storeValue () {

		_currentValues = new ArrayList();
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
			storeCurrentValues(_pivotModel, cRoot, row, columns, dataFieldsLength, -1);

			// not first level row node
			if (row.getDepth() > 1) {
				PivotHeaderNode tmpRow = row;
				while (tmpRow.getDepth() > 1) {
					PivotHeaderNode parentRow = tmpRow.getParent();
					List children = parentRow.getChildren();
					// is last child
					if (children.get(children.size()-1).equals(tmpRow)) {
						for (int calIdx = 0; calIdx < parentRow.getSubtotalCount(false); calIdx++) {
							// show row subtotal
							storeCurrentValues(_pivotModel, cRoot, parentRow, columns, dataFieldsLength, calIdx);
						}
					} else {
						break;
					}
					tmpRow = tmpRow.getParent();
				}
			}
		}
	}
	public void doUpdate (boolean syncAll) {

		StringBuilder sb = new StringBuilder("");

		PivotHeaderTree rowHeaderTree = _latestPivotModel.getRowHeaderTree();
		PivotHeaderTree columnHeaderTree = _latestPivotModel.getColumnHeaderTree();
		PivotHeaderNode rRoot = rowHeaderTree.getRoot();
		PivotHeaderNode cRoot = columnHeaderTree.getRoot();
		// the length of data fields under a column
		int dataFieldsLength = _latestPivotModel.getDataFields().length;

		List<PivotHeaderNode> rows = getNodeList(rowHeaderTree);
		List<PivotHeaderNode> columns = getNodeList(columnHeaderTree);

		rows.add(rRoot);
		Position pos = new Position(0, 0);

		sb.append("update([");
		for (PivotHeaderNode row : rows) { // for each row
			// show original data
			addUpdateElements(_latestPivotModel, cRoot, row, columns, dataFieldsLength, -1, sb, pos, syncAll);
			pos._rowIdx++;
			pos._colIdx = 0;
			// not first level row node
			if (row.getDepth() > 1) {
				PivotHeaderNode tmpRow = row;
				while (tmpRow.getDepth() > 1) {
					PivotHeaderNode parentRow = tmpRow.getParent();
					List children = parentRow.getChildren();
					// is last child
					if (children.get(children.size()-1).equals(tmpRow)) {
						for (int calIdx = 0; calIdx < parentRow.getSubtotalCount(false); calIdx++) {
							// show row subtotal
							addUpdateElements(_latestPivotModel, cRoot, parentRow, columns, dataFieldsLength, calIdx, sb, pos, syncAll);
							pos._rowIdx++;
							pos._colIdx = 0;
						}
					} else {
						break;
					}
					tmpRow = parentRow;
				}
			}
		}

		sb.replace(sb.length()-1, sb.length(), "");
		sb.append("]);");

		System.out.println(sb.toString());
		Clients.evalJavaScript(sb.toString());
		sb.setLength(0);
	}
	private void storeCurrentValues (TabularPivotModel model, PivotHeaderNode cRoot, 
			PivotHeaderNode row, List<PivotHeaderNode> columns, int dataFieldsLength, int rowCalIdx) {
		TabularPivotField[] dFields = model.getDataFields();
		Number value;
		for (PivotHeaderNode column : columns) { // for each column
			// for each data field
			for (int i = 0; i < dataFieldsLength; i++) {
				// get data value from pivot model by row node, column node and data index
				value = model.getValue(row, rowCalIdx, column, -1, i);
				_currentValues.add(value);
				//System.out.print(value + ",");
				
				// last data and
				// not first level node
				if (i+1 == dataFieldsLength
					&& column.getDepth() > 1) {
					PivotHeaderNode tmpCol = column;
					while (tmpCol.getDepth() > 1) {
						PivotHeaderNode parentColumn = tmpCol.getParent();
						List children = parentColumn.getChildren();
						// is last child
						if (children.get(children.size()-1).equals(tmpCol)) {
							// for each column calculator
							for (int calIdx = 0; calIdx < parentColumn.getSubtotalCount(false); calIdx++) {
								// again, for each data field
								for (int j = 0; j < dataFieldsLength; j++) {
									// get data value from pivot model by row node, column node and data index
									value = model.getValue(row, rowCalIdx, parentColumn, calIdx, j);
									_currentValues.add(value);
									//System.out.print(value + ",");
								}
							}
						} else {
							break;
						}
						tmpCol = parentColumn;
					}
				}
			}
		}
		for (int i = 0; i < dataFieldsLength; i++) {
			// grand total for columns
			value = model.getValue(row, rowCalIdx, cRoot, -1, i);
			_currentValues.add(value);
			//System.out.print(value + ",");
		}
		//System.out.println();
	}
	private void addUpdateElements (TabularPivotModel model, PivotHeaderNode cRoot, 
			PivotHeaderNode row, List<PivotHeaderNode> columns, int dataFieldsLength, int rowCalIdx,
			StringBuilder sb, Position pos, boolean syncAll) {
		TabularPivotField[] dFields = model.getDataFields();
		Number value;
		Number oldValue;
		String dir = null;
		TabularPivotField[] dataFields = model.getDataFields();

		PivotHeaderNode snapshotRRoot = _pivotModelFirstSnapshot.getRowHeaderTree().getRoot();
		PivotHeaderNode snapshotCRoot = _pivotModelFirstSnapshot.getColumnHeaderTree().getRoot();
		PivotHeaderNode snapshotRow = findNode(_pivotModelFirstSnapshot, snapshotRRoot, row);
		PivotHeaderNode snapshotCol;
		int snapshotDataFieldIndex;

		int snapshotRowCalIdx = -1;
		int snapshotColCalIdx = -1;

		if (rowCalIdx > -1 && snapshotRow != null) {
			snapshotRowCalIdx = findCalculatorIndex((TabularPivotField)snapshotRow.getField(), row.getField().getSubtotals()[rowCalIdx]);
		}

		for (PivotHeaderNode column : columns) { // for each column
			snapshotCol = findNode(_pivotModelFirstSnapshot, snapshotCRoot, column);
			// for each data field
			for (int i = 0; i < dataFieldsLength; i++) {
				snapshotDataFieldIndex = findDataFieldIndex(_pivotModelFirstSnapshot, dataFields[i]);
				value = model.getValue(row, rowCalIdx, column, -1, i);
				
				if (syncAll) {
					System.out.println("syncAll");
					dir = getDirection(_pivotModelFirstSnapshot, snapshotRow, snapshotCol, snapshotDataFieldIndex, snapshotRowCalIdx, -1, value);
					if (dir != null && !dir.isEmpty()) {
						addUpdateElement(pos._rowIdx, pos._colIdx, value, dir, sb);
					}
				} else {
					oldValue = (Number)_currentValues.remove(0);
					if ( value != null && !value.equals(oldValue) ) {
						dir = getDirection(_pivotModelFirstSnapshot, snapshotRow, snapshotCol, snapshotDataFieldIndex, snapshotRowCalIdx, -1, value);
						addUpdateElement(pos._rowIdx, pos._colIdx, value, dir, sb);
					}
				}
				// get data value from pivot model by row node, column node and data index

				//System.out.print(oldValue + "__" + value + ",");
				pos._colIdx++;
				// last data and
				// not first level node
				if (i+1 == dataFieldsLength
					&& column.getDepth() > 1) {
					PivotHeaderNode tmpCol = column;
					while (tmpCol.getDepth() > 1) {
						PivotHeaderNode parentColumn = tmpCol.getParent();
						List children = parentColumn.getChildren();
						snapshotCol = findNode(_pivotModelFirstSnapshot, snapshotCRoot, parentColumn);
						// is last child
						if (children.get(children.size()-1).equals(tmpCol)) {
							// for each column calculator
							for (int calIdx = 0; calIdx < parentColumn.getSubtotalCount(false); calIdx++) {
								Calculator cal = parentColumn.getField().getSubtotals()[calIdx];
								snapshotColCalIdx = findCalculatorIndex((TabularPivotField)snapshotCol.getField(), cal);
								// again, for each data field
								for (int j = 0; j < dataFieldsLength; j++) {
									snapshotDataFieldIndex = findDataFieldIndex(_pivotModelFirstSnapshot, dataFields[j]);
									// get data value from pivot model by row node, column node and data index
									value = model.getValue(row, rowCalIdx, parentColumn, calIdx, j);
									if (syncAll) {
										System.out.println("syncAll");
										dir = getDirection(_pivotModelFirstSnapshot, snapshotRow, snapshotCol, snapshotDataFieldIndex, snapshotRowCalIdx, snapshotColCalIdx, value);
										if (dir != null && !dir.isEmpty()) {
											addUpdateElement(pos._rowIdx, pos._colIdx, value, dir, sb);
										}
									} else {
										oldValue = (Number)_currentValues.remove(0);
										if ( value != null && !value.equals(oldValue) ) {
											dir = getDirection(_pivotModelFirstSnapshot, snapshotRow, snapshotCol, snapshotDataFieldIndex, snapshotRowCalIdx, snapshotColCalIdx, value);
											addUpdateElement(pos._rowIdx, pos._colIdx, value, dir, sb);
										}
									}
									//System.out.print(oldValue + "__" + value + ",");
									pos._colIdx++;
								}
							}
						} else {
							break;
						}
						tmpCol = parentColumn;
					}
				}
			}
		}

		for (int i = 0; i < dataFieldsLength; i++) {
			snapshotDataFieldIndex = findDataFieldIndex(_pivotModelFirstSnapshot, dataFields[i]);

			// grand total for columns
			value = model.getValue(row, rowCalIdx, cRoot, -1, i);
			if (syncAll) {
				System.out.println("syncAll");
				dir = getDirection(_pivotModelFirstSnapshot, snapshotRow, snapshotCRoot, snapshotDataFieldIndex, snapshotRowCalIdx, -1, value);
				if (dir != null && !dir.isEmpty()) {
					addUpdateElement(pos._rowIdx, pos._colIdx, value, dir, sb);
				}
			} else {
				oldValue = (Number)_currentValues.remove(0);
				if ( value != null && !value.equals(oldValue) ) {
					dir = getDirection(_pivotModelFirstSnapshot, snapshotRow, snapshotCRoot, snapshotDataFieldIndex, snapshotRowCalIdx, -1, value);
					addUpdateElement(pos._rowIdx, pos._colIdx, value, dir, sb);
				}
			}
			//System.out.print(oldValue + "__" + value + ",");
			pos._colIdx++;
		}
		System.out.println();
	}
	private void addUpdateElement (int rowIdx, int colIdx, Number val, String dir, StringBuilder sb) {
		sb.append("{rowIdx:")
			.append(rowIdx).append(",")
			.append("colIdx:")
			.append(colIdx).append(",")
			.append("val:")
			.append(val).append(",")
			.append("dir:")
			.append("'").append(dir).append("'")
			.append("},");
	}
	private String getDirection (TabularPivotModel model, PivotHeaderNode row, PivotHeaderNode col, int dataFieldIndex, int rowCalIndex, int colCalIndex, Number value) {
		String dir = "";
		double oldValue = 0.0;
		if (value != null) {
			if (row == null
				|| col == null
				|| rowCalIndex == -2
				|| colCalIndex == -2
				|| dataFieldIndex == -2) {
				if (value != null) {
					if (value.doubleValue() > oldValue) {
						dir = "up";
					} else if (value.doubleValue() < oldValue) {
						dir = "down";
					}
				}
			} else {
				oldValue = model.getValue(row, rowCalIndex, col, colCalIndex, dataFieldIndex).doubleValue();
				if (value.doubleValue() > oldValue) {
					dir = "up";
				} else if (value.doubleValue() < oldValue) {
					dir = "down";
				}
			}
		}

		return dir;
	}
	private PivotHeaderNode findNode (TabularPivotModel model, PivotHeaderNode root, PivotHeaderNode node) {
		List keys = new ArrayList();
		boolean found = false;
		PivotHeaderNode foundNode = null;
		if (root.getDepth() == node.getDepth()) {
			foundNode = root;
		} else {
			while (node.getDepth() > 0) {
				keys.add(0, node.getKey());
				node = node.getParent();
			}
			node = root;
			for (Object o : keys) {
				found = false;
				for (PivotHeaderNode child : node.getChildren()) {
					if (child.getKey().equals(o)) {
						node = child;
						found = true;
						break;
					}
				}
				if (!found) {
					break;
				}
			}
			if (found) {
				foundNode = node;
			}
		}
		return foundNode;
	}
	private int findDataFieldIndex (TabularPivotModel model, TabularPivotField dataField) {
		TabularPivotField[] dataFields = model.getDataFields();
		for (int idx = 0; idx < dataFields.length; idx++) {
			if (dataFields[idx].getFieldName().equals(dataField.getFieldName())) {
				return idx;
			}
		}
		return -2; // not found
	}
	private int findCalculatorIndex (TabularPivotField field, Calculator cal) {
		String labelKey = cal.getLabelKey();
		Calculator[] cals = field.getSubtotals();
		for (int i = 0; i < cals.length; i++) {
			if (cals[i].getLabelKey().equals(labelKey)) {
				return i;
			}
		}
		return -2; // not found
	}
	private class Position {
		public int _rowIdx = 0;
		public int _colIdx = 0;
		public Position (int rowIdx, int colIdx) {
			_rowIdx = rowIdx;
			_colIdx = colIdx;
		}
	}
}