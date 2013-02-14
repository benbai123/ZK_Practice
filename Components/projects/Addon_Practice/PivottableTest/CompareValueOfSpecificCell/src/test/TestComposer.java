package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.impl.StandardCalculator;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;

import org.zkoss.zk.ui.Component;
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
	Textbox tbx;

	private TabularPivotModel _pivotModel;
	private TabularPivotModel _pivotModelTwo;

	// A list of specific cell
	private List<CellAttr> cells;

	/**
	 * Add several CellAttr
	 */
	@SuppressWarnings("unchecked")
	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
		cells = new ArrayList<CellAttr>();
		// grand total of columns of RowOne - 1
		cells.add(new CellAttr(Arrays.asList(
				new String[]{"RowOne - 1"}
			), 
			null,
			"DataOne",
			null,
			null
		));
		// Row node: "RowOne - 1" - "RowTwo - 1" - "RowThree - 5"
		// Column node: "ColumnOne - 1" - "ColumnTwo - 1"
		// only exists in second pivottable
		cells.add(new CellAttr(Arrays.asList(
				new String[]{"RowOne - 1", "RowTwo - 1", "RowThree - 5"}
			), 
			Arrays.asList(
				new String[]{"ColumnOne - 1", "ColumnTwo - 1"}
			), 
			"DataOne",
			null,
			null
		));
		// Row node: "RowOne - 1"
		// Column node: "ColumnOne - 1"
		// Row Calculator Label Key: MAX Label Key
		// only exists in first pivottable
		cells.add(new CellAttr(Arrays.asList(
				new String[]{"RowOne - 1"}
			), 
			Arrays.asList(
				new String[]{"ColumnOne - 1"}
			), 
			"DataOne",
			StandardCalculator.MAX.getLabelKey(),
			null
		));
		// Row node: "RowOne - 1"
		// Column node: "ColumnOne - 1"
		// Row Calculator Label Key: SUM Label Key
		cells.add(new CellAttr(Arrays.asList(
				new String[]{"RowOne - 1"}
			), 
			Arrays.asList(
				new String[]{"ColumnOne - 1"}
			), 
			"DataOne",
			StandardCalculator.SUM.getLabelKey(),
			null
		));
	}
	/**
	 * model for first pivottable
	 * @return
	 * @throws Exception
	 */
	public TabularPivotModel getPivotModel () throws Exception {
		if (_pivotModel == null) {
			_pivotModel = getPivotModel(getData(), getColumns());
			// only first pivottable has MAX calculator on RowOne
			PivotField field = _pivotModel.getField("RowOne");
			_pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
		}
		return _pivotModel;
	}
	/**
	 * model for second pivottable
	 * @return
	 * @throws Exception
	 */
	public TabularPivotModel getPivotModelTwo () throws Exception {
		if (_pivotModelTwo == null) {
			List<List<Object>> rawData = getData();

			// only second pivottable has RowThree - 5
			List<Object> data = new ArrayList<Object>();
			data.add("RowOne - 1");
			data.add("RowTwo - 1");
			data.add("RowThree - 5");
			data.add("ColumnOne - 1");
			data.add("ColumnTwo - 1");
			data.add(5);
			rawData.add(data);

			_pivotModelTwo = getPivotModel(rawData, getColumns());
		}
		return _pivotModelTwo;
	}

	/**
	 * create a pivot model
	 * @param data
	 * @param Columns
	 * @return
	 * @throws Exception
	 */
	public TabularPivotModel getPivotModel (List<List<Object>> data, List<String> columns) throws Exception {
		TabularPivotModel pivotModel = new TabularPivotModel(data, columns);

		// assign rows, the order matches to the level of row node field
		pivotModel.setFieldType("RowOne", PivotField.Type.ROW);
		pivotModel.setFieldType("RowTwo", PivotField.Type.ROW);
		pivotModel.setFieldType("RowThree", PivotField.Type.ROW);

		// assign columns, the order matches to the level of column node field
		pivotModel.setFieldType("ColumnOne", PivotField.Type.COLUMN);
		pivotModel.setFieldType("ColumnTwo", PivotField.Type.COLUMN);

		// assign datas, the order matches to the order of data field
		pivotModel.setFieldType("DataOne", PivotField.Type.DATA);

		PivotField field = pivotModel.getField("RowOne");
		pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM});
		return pivotModel;
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
	@Listen("onClick = #btn")
	public void doCompare () {
		StringBuilder sb = new StringBuilder("");
		for (CellAttr cell : cells) {
			sb.append(cell.getCellInfo());
			Number val = getValue(_pivotModel, cell);
			Number valTwo = getValue(_pivotModelTwo, cell);
			if (val == null) {
				sb.append("First pivottable does not contain this cell");
			} else {
				sb.append("The value of this cell in first pivottable is ")
					.append(val);
			}
			sb.append("\n");
			if (valTwo == null) {
				sb.append("Second pivottable does not contain this cell");
			} else {
				sb.append("The value of this cell in second pivottable is ")
					.append(valTwo);
			}
			sb.append("\n");
			if (val != null
				&& valTwo != null) {
				if (val.doubleValue() > valTwo.doubleValue()) {
					sb.append("The value of this cell in second pivottable is smaller than first pivottable\n");
				} else if (val.doubleValue() < valTwo.doubleValue()) {
					sb.append("The value of this cell in second pivottable is grater than first pivottable\n");
				}
			}
			sb.append("\n\n");
		}
		tbx.setValue(sb.toString());
	}
	/**
	 * get value from a model based on cellAttr
	 * @param model the model to get value
	 * @param cellAttr the attributes represent a specific cell
	 * @return
	 */
	public Number getValue (TabularPivotModel model, CellAttr cellAttr) {
		PivotHeaderNode row = findNode(model.getRowHeaderTree().getRoot(), cellAttr._rowKeys);
		PivotHeaderNode col = findNode(model.getColumnHeaderTree().getRoot(), cellAttr._colKeys);
		int dataIdx = findDataFieldIndex(model, cellAttr._dataFieldName);
		int rowCalIdx = -1;
		int colCalIdx = -1;
		if (row != null) {
			rowCalIdx = findCalculatorIndex(row, cellAttr._rowCalculatorLabelKey);
		}
		if (col != null) {
			colCalIdx = findCalculatorIndex(col, cellAttr._colCalculatorLabelKey);
		}
		if (row == null // row should exists
			|| col == null // col should exists
			|| dataIdx == -2 // data field should exists
			|| rowCalIdx == -2 // row calculator should exists if _rowCalculatorLabelKey is not null
			|| colCalIdx == -2) { // column calculator should exists if _colCalculatorLabelKey is not null
			return null;
		}
		return model.getValue(row, rowCalIdx, col, colCalIdx, dataIdx);
	}
	/**
	 * find the corresponding node in a pivot model
	 * @param root The root of pivot header tree to search the corresponding node
	 * @param keys list of node key
	 * @return PivotHeaderNode the corresponding node, null denotes not found
	 */
	public static PivotHeaderNode findNode (PivotHeaderNode root, List keys) {
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
				null; // not found
	}
	/**
	 * Search the corresponding data field index in a pivot model
	 * @param model the model to search
	 * @param fieldName the name of data field
	 * @return int the corresponding data field index, -2 denotes not found
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
		return -2; // not found
	}
	/**
	 * Search the corresponding calculator index of a pivot field
	 * @param field the field to search
	 * @param labelKey the labelKey of calculator
	 * @return int the corresponding calculator index, -2 denotes not found
	 */
	public static int findCalculatorIndex (PivotHeaderNode node, String labelKey) {
		if (labelKey == null) return -1;
		Calculator[] cals = node.getField().getSubtotals();
		for (int i = 0; i < cals.length; i++) {
			if (cals[i].getLabelKey().equals(labelKey)) {
				return i;
			}
		}
		return -2; // not found
	}
	/** Inner class represent a specific data cell
	 * 
	 * @author benbai123
	 *
	 */
	class CellAttr {
		public List<String> _rowKeys;
		public List<String> _colKeys;
		public String _dataFieldName;
		public String _rowCalculatorLabelKey;
		public String _colCalculatorLabelKey;
		public CellAttr (List<String> rowKeys, List<String> colKeys, String dataFieldName,
				String rowCalculatorLabelKey, String colCalculatorLabelKey) {
			_rowKeys = rowKeys;
			_colKeys = colKeys;
			_dataFieldName = dataFieldName;
			_rowCalculatorLabelKey = rowCalculatorLabelKey;
			_colCalculatorLabelKey = colCalculatorLabelKey;
		}
		public String getCellInfo () {
			StringBuilder sb = new StringBuilder("");
			sb.append("Row: ");
			if (_rowKeys == null || _rowKeys.size() == 0) {
				sb.append("Grand Total");
			} else {
				for (String key : _rowKeys) {
					sb.append(key).append(" - ");
				}
			}
			sb.append("\nColumn: ");
			if (_colKeys == null || _colKeys.size() == 0) {
				sb.append("Grand Total");
			} else {
				for (String key : _colKeys) {
					sb.append(key).append(" - ");
				}
			}
			sb.append("\nData: ")
				.append(_dataFieldName)
				.append("\nRow Calculator: ")
				.append(_rowCalculatorLabelKey == null? "Data" : _rowCalculatorLabelKey)
				.append("\nColumn Calculator: ")
				.append(_colCalculatorLabelKey == null? "Data" : _rowCalculatorLabelKey)
				.append("\n");
			return sb.toString();
		}
	}
}