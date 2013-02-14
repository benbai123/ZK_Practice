package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.impl.StandardCalculator;
import org.zkoss.pivot.impl.TabularPivotModel;

import org.zkoss.zk.ui.select.SelectorComposer;


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

	private TabularPivotModel _pivotModel;

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
}