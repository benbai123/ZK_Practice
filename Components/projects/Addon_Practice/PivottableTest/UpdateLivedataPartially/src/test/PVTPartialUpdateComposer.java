package test;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.Pivottable;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;
import org.zkoss.pivot.ui.PivotFieldControl;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;

/**
 * Tested with ZK 6.0.2 EE and ZK Pivottable 2.0.0
 * @author benbai123
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
	@Wire
	private PivotFieldControl pfc;

	// model used by pivottable
	private TabularPivotModel _pivotModel;

	// copy of first model
	private TabularPivotModel _pivotModelFirstSnapshot;

	// model contains the newest data
	private TabularPivotModel _latestPivotModel;

	// used to store the value of current view
	private List<Number> _currentValues = null;

	// newest raw data
	private List<List<Object>> _latestRawData;
	@SuppressWarnings("unchecked")
	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
		pfc.setModel((TabularPivotModel)pivottable.getModel());
	}
	/**
	 * Get pivottable's model, also make a snapshot of it
	 * @return TabularPivotModel the pivottable's model
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TabularPivotModel getPivotModel () throws Exception {
		if (_pivotModel == null) {
			_pivotModel = PVTModelProvider.getPivotModel();

			_pivotModelFirstSnapshot = PVTUtils.createModelSnapshot(_pivotModel);

			// init latest raw data
			_latestRawData = new ArrayList<List<Object>>();
			_latestRawData.addAll((List<List<Object>>)_pivotModel.getRawData());
			_latestPivotModel = _pivotModel;
		}
		return _pivotModel;
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
		if (newData != null && newData.size() > 0) {
			tbx.setValue(tbx.getValue() + "\n" + sb.toString());
			_latestRawData.addAll(newData);
			_latestPivotModel = PVTUtils.cloneModelWithData(_pivotModel, _latestRawData); 
			if (PVTUtils.isStructureEqual(_pivotModel, _latestPivotModel, true, false)) {
				updateChangedData(_pivotModel, _latestPivotModel);
			} else {
				// clear stored value if structure is changed
				_currentValues = null;
				pivottable.setModel(_latestPivotModel);
				pfc.setModel((TabularPivotModel)pivottable.getModel());
				doUpdate(true);
				_pivotModel = _latestPivotModel;
			}
		}
	}
	/**
	 * called when a node is opened/closed
	 */
	@Listen ("onPivotNodeOpen = #pivottable")
	public void onPvtOpen () {
		syncOrReplace();
	}
	/**
	 * called when the fields of first pivottable are changed
	 */
	@Listen ("onPivotFieldControlChange = #pfc")
	public void syncModelStructure () {
		PVTUtils.syncModelStructure(_pivotModel, _pivotModelFirstSnapshot);
		syncOrReplace();
	}
	/**
	 * sync model structure and all cell status as needed
	 */
	private void syncOrReplace () {
		// drop old values however
		_currentValues = null;
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
			// clear stored value if structure is changed
			pivottable.setModel(_latestPivotModel);
			pfc.setModel((TabularPivotModel)pivottable.getModel());
			doUpdate(true);
			_pivotModel = _latestPivotModel;
		}
	}
	/**
	 * update only the changed data
	 * @param currentModel
	 * @param newModel
	 */
	private void updateChangedData (TabularPivotModel currentModel, TabularPivotModel newModel) {
		// keep it as long as possible to reduce processing time,
		// another choice is drop it each time to reduce memory consumption
		if (_currentValues == null) {
			storeValue();
		}
		doUpdate(false);
	}
	/**
	 * store values of current view of pivottable
	 */
	private void storeValue () {

		_currentValues = new ArrayList<Number>();

		List<PivotHeaderNode> rows = PVTUtils.getRowLeafList(_pivotModel);
		List<PivotHeaderNode> columns = PVTUtils.getColumnLeafList(_pivotModel);

		rows.add(_pivotModel.getRowHeaderTree().getRoot());
		columns.add(_pivotModel.getColumnHeaderTree().getRoot());

		for (PivotHeaderNode row : rows) { // for each row
			// show original data
			storeCurrentValues(_pivotModel, row, columns, -1);

			// not first level row node
			if (row.getDepth() > 1) {
				PivotHeaderNode tmpRow = row;
				PivotHeaderNode parentRow = tmpRow.getParent();
				while (tmpRow.getDepth() > 1
						&& PVTUtils.isLastChild(parentRow, tmpRow)) {
					// is last child
					for (int calIdx = 0; calIdx < parentRow.getSubtotalCount(false); calIdx++) {
						// show row subtotal
						storeCurrentValues(_pivotModel, parentRow, columns, calIdx);
					}
					tmpRow = parentRow;
					parentRow = tmpRow.getParent();
				}
			}
		}
	}
	/**
	 * execute script to update client side cells of pivottable
	 * @param syncAll
	 */
	private void doUpdate (boolean syncAll) {

		StringBuilder sb = new StringBuilder("");


		List<PivotHeaderNode> rows = PVTUtils.getRowLeafList(_latestPivotModel);
		List<PivotHeaderNode> columns = PVTUtils.getColumnLeafList(_latestPivotModel);
		columns.add(_latestPivotModel.getColumnHeaderTree().getRoot());

		rows.add(_latestPivotModel.getRowHeaderTree().getRoot());
		Position pos = new Position(0, 0);

		// script start
		sb.append("update([");
		CellAttributes cell = new CellAttributes(null, null, null, null, null);
		// iterate through current view to add the elements to update
		for (PivotHeaderNode row : rows) { // for each row
			// clear row calculator if any before next loop
			cell.updateAttributes(PVTUtils.getNodeKeys(row), null, null, null, null);
			// show original data
			addUpdateElements(_latestPivotModel, row, columns, -1, sb, pos, cell, syncAll);
			pos.toNextRow();

			// not first level row node,
			// have to display parent sub-total if
			// current row is the last child
			if (row.getDepth() > 1) {
				PivotHeaderNode tmpRow = row;
				PivotHeaderNode parentRow = tmpRow.getParent();
				// continuously check ancestors
				while (tmpRow.getDepth() > 1
						&& PVTUtils.isLastChild(parentRow, tmpRow)) {
					parentRow = tmpRow.getParent();
					// is last child
					for (int calIdx = 0; calIdx < parentRow.getSubtotalCount(false); calIdx++) {
						cell.setRowKeys(PVTUtils.getNodeKeys(parentRow));
						cell.setRowCalculatorLabelKey(parentRow.getField().getSubtotals()[calIdx].getLabelKey());
						// show row subtotal
						addUpdateElements(_latestPivotModel, parentRow, columns, calIdx, sb, pos, cell, syncAll);
						
						pos.toNextRow();
					}
					tmpRow = parentRow;
					parentRow = tmpRow.getParent();
				}
			}
		}

		if (sb.substring(sb.length()-1).equals(",")) {
			sb.replace(sb.length()-1, sb.length(), "");
		}
		sb.append("]);");

		String cmd = sb.toString();
		if (!"update([]);".equals(cmd)) {
			System.out.println();
			Clients.evalJavaScript(sb.toString());
		}
		sb.setLength(0);
	}
	/** loop through cells of a row
	 * store values of current view of pivottable
	 * @param model the model of current pivottable
	 * @param row the row to loop through
	 * @param columns columns of row
	 * @param rowCalIdx calculator index of row
	 */
	private void storeCurrentValues (TabularPivotModel model, 
			PivotHeaderNode row, List<PivotHeaderNode> columns, int rowCalIdx) {
		// the length of data fields under a column
		int dataFieldsLength = model.getDataFields().length;
		// for each column
		for (PivotHeaderNode column : columns) {
			// for each data field
			for (int i = 0; i < dataFieldsLength; i++) {
				// get data value from pivot model by row node, column node and data index
				_currentValues.add(model.getValue(row, rowCalIdx, column, -1, i));
				//System.out.print(value + ",");
				
				// last data and
				// not first level node
				if (i+1 == dataFieldsLength
					&& column.getDepth() > 1) {
					PivotHeaderNode tmpCol = column;
					PivotHeaderNode parentColumn = tmpCol.getParent();
					while (tmpCol.getDepth() > 1
							&& PVTUtils.isLastChild(parentColumn, tmpCol)) {
						// for each column calculator
						for (int calIdx = 0; calIdx < parentColumn.getSubtotalCount(false); calIdx++) {
							// again, for each data field
							for (int j = 0; j < dataFieldsLength; j++) {
								// get data value from pivot model by row node, column node and data index
								_currentValues.add(model.getValue(row, rowCalIdx, parentColumn, calIdx, j));
								//System.out.print(value + ",");
							}
						}
						tmpCol = parentColumn;
						parentColumn = tmpCol.getParent();
					}
				}
			}
		}
		//System.out.println();
	}
	/**
	 * loop through whole row to check
	 * and add elements to update as needed
	 * @param model the latest model
	 * @param cRoot column root of model
	 * @param row the row to loop through
	 * @param columns all columns of row
	 * @param rowCalIdx calculator index of row
	 * @param sb StringBuilder to append elements to update
	 * @param pos Position class to track current position and pointer of old value list
	 * @param cell CellAttributes class to track cell information
	 * @param syncAll whether to sync all cell status or update changed value only
	 */
	private void addUpdateElements (TabularPivotModel model, 
			PivotHeaderNode row, List<PivotHeaderNode> columns, int rowCalIdx,
			StringBuilder sb, Position pos, CellAttributes cell, boolean syncAll) {
		TabularPivotField[] dataFields = model.getDataFields();
		// the length of data fields under a column
		int dataFieldsLength = dataFields.length;

		for (PivotHeaderNode column : columns) { // for each column
			cell.setColKeys(PVTUtils.getNodeKeys(column));
			// for each data field
			for (int i = 0; i < dataFieldsLength; i++) {
				cell.setDataFieldName(dataFields[i].getFieldName());

				addUpdateElement(_pivotModelFirstSnapshot, _currentValues, cell,
						model.getValue(row, rowCalIdx, column, -1, i), pos, sb, syncAll);
				// get data value from pivot model by row node, column node and data index

				//System.out.print(oldValue + "__" + value + ",");
				pos.increaseCol();
				// last data and
				// not first level node
				if (i+1 == dataFieldsLength
					&& column.getDepth() > 1) {
					PivotHeaderNode tmpCol = column;
					PivotHeaderNode parentColumn = tmpCol.getParent();
					while (tmpCol.getDepth() > 1
							&& PVTUtils.isLastChild(parentColumn, tmpCol)) {
						cell.setColKeys(PVTUtils.getNodeKeys(parentColumn));

						// for each column calculator
						for (int calIdx = 0; calIdx < parentColumn.getSubtotalCount(false); calIdx++) {
							Calculator cal = parentColumn.getField().getSubtotals()[calIdx];

							cell.setColCalculatorLabelKey(cal.getLabelKey());
							// again, for each data field
							for (int j = 0; j < dataFieldsLength; j++) {
								cell.setDataFieldName(dataFields[j].getFieldName());
								// get data value from pivot model by row node, column node and data index

								addUpdateElement(_pivotModelFirstSnapshot, _currentValues, cell,
										model.getValue(row, rowCalIdx, parentColumn, calIdx, j), pos, sb, syncAll);
								//System.out.print(oldValue + "__" + value + ",");
								pos.increaseCol();
							}
						}
						tmpCol = parentColumn;
						parentColumn = tmpCol.getParent();
					}
					// clear column calculator if any before next loop
					cell.setColCalculatorLabelKey(null);
				}
			}
		}
	}
	/**
	 * add the element to update to StringBuilder as needed
	 * @param basemodel the model as the compare base
	 * @param valueList the list of old values (might be null if not syncAll)
	 * @param cell attributes describe current cell
	 * @param value latest value of current cell
	 * @param pos position of the row/column in current view of pivottable
	 * @param sb StringBuilder contains update script
	 * @param syncAll whether sync all cell or update changed cell only
	 */
	private void addUpdateElement (TabularPivotModel basemodel, List<Number> valueList,
			CellAttributes cell, Number value, Position pos, StringBuilder sb, boolean syncAll) {
		String dir;
		if (syncAll) {
			dir = PVTUtils.getDirection(basemodel, cell, value);
			if (dir != null && !dir.isEmpty()) {
				addUpdateElement(pos.getRowIdx(), pos.getColIdx(), value, dir, sb);
			}
		} else {
			int ptr = pos.getPtr();
			if (value != null) {
				Number oldv = valueList.get(ptr);
				double valToComp = (oldv == null? 0 : oldv.doubleValue());
				// value changed
				if (value.doubleValue() != valToComp) {
					dir = PVTUtils.getDirection(basemodel, cell, value);
					addUpdateElement(pos.getRowIdx(), pos.getColIdx(), value, dir, sb);
					// update value
					valueList.set(ptr, value);
				}
			}
		}
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