package test;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.PivotHeaderTree;
import org.zkoss.pivot.Pivottable;
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
	@SuppressWarnings("unchecked")
	public TabularPivotModel getPivotModel () throws Exception {
		if (_pivotModel == null) {
			_pivotModel = PVTModelProvider.getPivotModel();

			_pivotModelFirstSnapshot = createSnapshot(_pivotModel);

			// init latest raw data
			_latestRawData = new ArrayList<List<Object>>();
			_latestRawData.addAll((List<List<Object>>)_pivotModel.getRawData());
			_latestPivotModel = PVTUtils.cloneModelWithData(_pivotModel, _latestRawData);
		}
		return _pivotModel;
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
	 * Display the current content of pivottable
	 * @throws Exception 
	 */
	@Listen ("onClick = #btn")
	public void addData () throws Exception {
		//alert(isStructureEqual(_pivotModel, _pivotModelTwo)? "Their structure are equal" : "They are different");
		StringBuilder sb = new StringBuilder("");
		List<List<Object>> newData = PVTModelProvider.getNewDatas(sb);
		if (newData.size() > 0) {
			tbx.setValue(tbx.getValue() + "\n" + sb.toString());
			if (newData != null) {
				_latestRawData.addAll(newData);
			}
			_latestPivotModel = PVTUtils.cloneModelWithData(_pivotModel, _latestRawData); 
			if (PVTUtils.isStructureEqual(_pivotModel, _latestPivotModel, true, false)) {
				updateChangedData(_pivotModel, _latestPivotModel);
			} else {

				pivottable.setModel(_latestPivotModel);
				doUpdate(true);
				_pivotModel = _latestPivotModel;
			}
		}
	}
	@Listen ("onPivotNodeOpen = #pivottable")
	public void onPvtOpen () {
		// sync model structure if not the same instance
		if (_pivotModel != _latestPivotModel) {
			PVTUtils.syncModelStructure(_pivotModel, _latestPivotModel);
		}
		if (_pivotModel == _latestPivotModel
			|| PVTUtils.isStructureEqual(_pivotModel, _latestPivotModel, true, false)) {
			System.out.println("structure equal");
			doUpdate(true);
		} else {
			System.out.println("structure not equal");
			pivottable.setModel(_latestPivotModel);
			doUpdate(true);
			_pivotModel = _latestPivotModel;
		}
	}
	private void updateChangedData (TabularPivotModel currentModel, TabularPivotModel newModel) {
		storeValue();
		doUpdate(false);
	}

	public void storeValue () {

		_currentValues = new ArrayList();
		PivotHeaderTree rowHeaderTree = _pivotModel.getRowHeaderTree();
		PivotHeaderTree columnHeaderTree = _pivotModel.getColumnHeaderTree();
		PivotHeaderNode rRoot = rowHeaderTree.getRoot();
		PivotHeaderNode cRoot = columnHeaderTree.getRoot();
		// the length of data fields under a column
		int dataFieldsLength = _pivotModel.getDataFields().length;

		List<PivotHeaderNode> rows = PVTUtils.getNodeList(rowHeaderTree, true, true);
		List<PivotHeaderNode> columns = PVTUtils.getNodeList(columnHeaderTree, true, true);

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

		List<PivotHeaderNode> rows = PVTUtils.getNodeList(rowHeaderTree, true, true);
		List<PivotHeaderNode> columns = PVTUtils.getNodeList(columnHeaderTree, true, true);

		rows.add(rRoot);
		Position pos = new Position(0, 0);

		sb.append("update([");
		for (PivotHeaderNode row : rows) { // for each row
			CellAttributes cell = new CellAttributes(PVTUtils.getNodeKeys(row), null, null, null, null);
			// show original data
			addUpdateElements(_latestPivotModel, cRoot, row, columns, dataFieldsLength, -1, sb, pos, cell, syncAll);
			pos.increaseRow();
			pos.resetCol();
			// not first level row node
			if (row.getDepth() > 1) {
				PivotHeaderNode tmpRow = row;
				while (tmpRow.getDepth() > 1) {
					PivotHeaderNode parentRow = tmpRow.getParent();
					List children = parentRow.getChildren();
					// is last child
					if (children.get(children.size()-1).equals(tmpRow)) {
						for (int calIdx = 0; calIdx < parentRow.getSubtotalCount(false); calIdx++) {
							cell.setRowKeys(PVTUtils.getNodeKeys(parentRow));
							cell.setRowCalculatorLabelKey(parentRow.getField().getSubtotals()[calIdx].getLabelKey());
							// show row subtotal
							addUpdateElements(_latestPivotModel, cRoot, parentRow, columns, dataFieldsLength, calIdx, sb, pos, cell, syncAll);
							pos.increaseRow();
							pos.resetCol();
						}
					} else {
						break;
					}
					tmpRow = parentRow;
				}
				// clear row calculator if any before next loop
				cell.setRowCalculatorLabelKey(null);
			}
		}

		if (sb.substring(sb.length()-1).equals(",")) {
			sb.replace(sb.length()-1, sb.length(), "");
		}
		sb.append("]);");

		System.out.println(sb.toString());
		Clients.evalJavaScript(sb.toString());
		sb.setLength(0);
	}
	@SuppressWarnings("unchecked")
	private void storeCurrentValues (TabularPivotModel model, PivotHeaderNode cRoot, 
			PivotHeaderNode row, List<PivotHeaderNode> columns, int dataFieldsLength, int rowCalIdx) {
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
			StringBuilder sb, Position pos, CellAttributes cell, boolean syncAll) {
		Number value;
		Number oldValue;
		String dir = null;
		TabularPivotField[] dataFields = model.getDataFields();

		for (PivotHeaderNode column : columns) { // for each column
			cell.setColKeys(PVTUtils.getNodeKeys(column));
			// for each data field
			for (int i = 0; i < dataFieldsLength; i++) {
				cell.setDataFieldName(dataFields[i].getFieldName());
				value = model.getValue(row, rowCalIdx, column, -1, i);
				
				if (syncAll) {
					dir = PVTUtils.getDirection(_pivotModelFirstSnapshot, cell, value);
					if (dir != null && !dir.isEmpty()) {
						addUpdateElement(pos.getRowIdx(), pos.getColIdx(), value, dir, sb);
					}
				} else {
					oldValue = (Number)_currentValues.remove(0);
					if ( value != null && !value.equals(oldValue) ) {
						dir = PVTUtils.getDirection(_pivotModelFirstSnapshot, cell, value);
						addUpdateElement(pos.getRowIdx(), pos.getColIdx(), value, dir, sb);
					}
				}
				// get data value from pivot model by row node, column node and data index

				//System.out.print(oldValue + "__" + value + ",");
				pos.increaseCol();
				// last data and
				// not first level node
				if (i+1 == dataFieldsLength
					&& column.getDepth() > 1) {
					PivotHeaderNode tmpCol = column;
					while (tmpCol.getDepth() > 1) {
						PivotHeaderNode parentColumn = tmpCol.getParent();
						List children = parentColumn.getChildren();
						cell.setColKeys(PVTUtils.getNodeKeys(parentColumn));

						// is last child
						if (children.get(children.size()-1).equals(tmpCol)) {
							// for each column calculator
							for (int calIdx = 0; calIdx < parentColumn.getSubtotalCount(false); calIdx++) {
								Calculator cal = parentColumn.getField().getSubtotals()[calIdx];

								cell.setColCalculatorLabelKey(cal.getLabelKey());
								// again, for each data field
								for (int j = 0; j < dataFieldsLength; j++) {
									cell.setDataFieldName(dataFields[j].getFieldName());
									// get data value from pivot model by row node, column node and data index
									value = model.getValue(row, rowCalIdx, parentColumn, calIdx, j);
									if (syncAll) {
										dir = PVTUtils.getDirection(_pivotModelFirstSnapshot, cell, value);
										if (dir != null && !dir.isEmpty()) {
											addUpdateElement(pos.getRowIdx(), pos.getColIdx(), value, dir, sb);
										}
									} else {
										oldValue = (Number)_currentValues.remove(0);
										if ( value != null && !value.equals(oldValue) ) {
											dir = PVTUtils.getDirection(_pivotModelFirstSnapshot, cell, value);
											addUpdateElement(pos.getRowIdx(), pos.getColIdx(), value, dir, sb);
										}
									}
									//System.out.print(oldValue + "__" + value + ",");
									pos.increaseCol();
								}
							}
						} else {
							break;
						}
						tmpCol = parentColumn;
					}
				}
				// clear column calculator if any before next loop
				cell.setColCalculatorLabelKey(null);
			}
		}

		// grand total for columns
		cell.setColKeys(PVTUtils.getNodeKeys(cRoot));
		for (int i = 0; i < dataFieldsLength; i++) {
			cell.setDataFieldName(dataFields[i].getFieldName());

			// grand total for columns
			value = model.getValue(row, rowCalIdx, cRoot, -1, i);
			if (syncAll) {
				dir = PVTUtils.getDirection(_pivotModelFirstSnapshot, cell, value);
				if (dir != null && !dir.isEmpty()) {
					addUpdateElement(pos.getRowIdx(), pos.getColIdx(), value, dir, sb);
				}
			} else {
				oldValue = (Number)_currentValues.remove(0);
				if ( value != null && !value.equals(oldValue) ) {
					dir = PVTUtils.getDirection(_pivotModelFirstSnapshot, cell, value);
					addUpdateElement(pos.getRowIdx(), pos.getColIdx(), value, dir, sb);
				}
			}
			//System.out.print(oldValue + "__" + value + ",");
			pos.increaseCol();
		}
		System.out.println();
	}
	private void addUpdateElement (int rowIdx, int colIdx, Number val, String dir, StringBuilder sb) {
		sb.append("{rowIdx:")
			.append(rowIdx).append(",")
			.append("colIdx:")
			.append(colIdx).append(",")
			.append("val:")
			.append(val).append(",");
		if (dir != null) {
			sb.append("dir:")
				.append("'").append(dir).append("'");
		}
		sb.append("},");
	}
}