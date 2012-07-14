package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.Pivottable;

import org.zkoss.pivot.event.PivotUIEvent;
import org.zkoss.pivot.impl.SimplePivotHeaderTree;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;

import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

/**
 * Tested with ZK 6.0.1 CE and ZK Pivottable 2.0.0
 *
 */
@SuppressWarnings("rawtypes")
public class TestComposer extends SelectorComposer {
	/**
	 * generated serial version UID
	 */
	private static final long serialVersionUID = -2897873399288955635L;
	private TabularPivotModel _pivotModel;

	@Wire("#pivottable")
	private Pivottable pivottable;

	private int _rowLevelToSort = 1;	// the level of rows to sort
	private int _fieldIndexToSort = 0; // the index of data field under _sortColumn
	private PivotHeaderNode _columnNodeToSort; // the column node with respect to _sortColumn

	/**
	 * Get pivottable's model
	 * @return TabularPivotModel the pivottable's model
	 * @throws Exception
	 */
	public TabularPivotModel getPivotModel () throws Exception {
		if (_pivotModel == null) {
			_pivotModel = new TabularPivotModel(getData(), getColumns());

			// assign rows, the order matches to the level of row node field
			_pivotModel.setFieldType("Row_Level_001", PivotField.Type.ROW);
			_pivotModel.setFieldType("Row_Level_002", PivotField.Type.ROW);
			_pivotModel.setFieldType("Row_Level_003", PivotField.Type.ROW);
			_pivotModel.setFieldType("Row_Level_004", PivotField.Type.ROW);

			// assign columns, the order matches to the level of column node field
			_pivotModel.setFieldType("Column_Level_001", PivotField.Type.COLUMN);
			_pivotModel.setFieldType("Column_Level_002", PivotField.Type.COLUMN);

			// assign datas, the order matches to the order of data field
			_pivotModel.setFieldType("Data_Field_001", PivotField.Type.DATA);
			_pivotModel.setFieldType("Data_Field_002", PivotField.Type.DATA);
			_pivotModel.setFieldType("Data_Field_003", PivotField.Type.DATA);
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

		for (int i = 0; i < 10000; i++) {
			List<Object> data = new ArrayList<Object>();
			data.add("Row_Level_001 - " + (r.nextInt(10) + 1));
			data.add("Row_Level_002 - " + (r.nextInt(10) + 1));
			data.add("Row_Level_003 - " + (r.nextInt(10) + 1));
			data.add("Row_Level_004 - " + (r.nextInt(10) + 1));
			data.add("Column_Level_001 - " + (r.nextInt(10) + 1));
			data.add("Column_Level_002 - " + (r.nextInt(10) + 1));
			data.add(r.nextInt(10000));
			data.add(r.nextDouble() * 10000.0);
			data.add(r.nextInt(100));
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
				"Row_Level_001", "Row_Level_002", "Row_Level_003", "Row_Level_004",
				"Column_Level_001", "Column_Level_002",
				"Data_Field_001", "Data_Field_002", "Data_Field_003"
		});
	}

	/**
	 * ** Added **
	 * sort column ascending
	 */
	@Listen("onClick = #btnSortAsc")
	public void doSortAscending (MouseEvent event) {
		doSort(true);
	}

	/**
	 * ** Added **
	 * sort column descending
	 */
	@Listen("onClick = #btnSortDesc")
	public void doSortDescending (MouseEvent event) {
		doSort(false);
	}
	/**
	 * ** Added **
	 * sort column
	 * @param ascending boolean, true: ascending, false: descending
	 */
	private void doSort (boolean ascending) {
		if (_columnNodeToSort != null) {
			// get the rowHeaderTree
			SimplePivotHeaderTree rowHeaderTree = (SimplePivotHeaderTree)_pivotModel.getRowHeaderTree();
			// create comparator
			ColumnHeaderComparator comparator = new ColumnHeaderComparator(_pivotModel,
					_columnNodeToSort, _fieldIndexToSort, _rowLevelToSort, ascending);
			// do sort
			rowHeaderTree.sort(comparator);
			// reset model to trigger rerender
			pivottable.setModel(_pivotModel);
		}
	}

	/**
	 * ** Added **
	 * Update sort attributes from click event of pivottable
	 */
	@Listen("onPivotPopup = #pivottable")
	public void updateSortAttributes (PivotUIEvent e) {
		PivotField dataField = e.getDataField();

		_rowLevelToSort = e.getRowContext() != null? e.getRowContext().getNode().getDepth() : 0;
		if (dataField != null) {
			_columnNodeToSort = e.getColumnContext().getNode();
			_fieldIndexToSort = getFieldIndexToSort(dataField.getFieldName(), _pivotModel.getDataFields());
		}
	}
	/**
	 * ** Added **
	 * get the index of the field to sort
	 * @param fieldToSort the field to sort
	 * @param fields all fields get from pivot model
	 * @return int the index of the field to sort
	 */
	private int getFieldIndexToSort (String fieldToSort, TabularPivotField[] fields) {
		for (int i = 0; i < fields.length; i++) {
			if (fieldToSort.equals(fields[i].getFieldName()))
				return i;
		}
		return 0;
	}
}
