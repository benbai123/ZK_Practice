package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.impl.TabularPivotModel;

public class PivotModelProvider {
	// pivot model with the 'whole' raw data
	private TabularPivotModel _pivotModel;
	/**
	 * Get pivottable's model
	 * @return TabularPivotModel the pivottable's model
	 * @throws Exception
	 */
	public TabularPivotModel getPivotModel () {
		if (_pivotModel == null) {
			_pivotModel = new TabularPivotModel(getData(), getColumns());

			// assign rows, the order matches to the level of row node field
			_pivotModel.setFieldType("Row_01", PivotField.Type.ROW);
			_pivotModel.setFieldType("Row_02", PivotField.Type.ROW);
			_pivotModel.setFieldType("Row_03", PivotField.Type.ROW);

			// assign columns, the order matches to the level of column node field
			_pivotModel.setFieldType("Column_01", PivotField.Type.COLUMN);
			_pivotModel.setFieldType("Column_02", PivotField.Type.COLUMN);

			// assign datas, the order matches to the order of data field
			_pivotModel.setFieldType("Data_01", PivotField.Type.DATA);
			_pivotModel.setFieldType("Data_02", PivotField.Type.DATA);
			_pivotModel.setFieldType("Data_03", PivotField.Type.DATA);
		}
		return _pivotModel;
	}
	/**
	 * prepare columns name for pivottable's model
	 * @return
	 */
	public List<String> getColumns() {
		return Arrays.asList(new String[]{
				"Row_01", "Row_02", "Row_03",
				"Column_01", "Column_02",
				"Data_01", "Data_02", "Data_03"
		});
	}
	/**
	 * prepare the data for pivottable's model
	 * The order of object put into data list matches
	 * the order of column name's order
	 * @return
	 * @throws Exception
	 */
	private List<List<Object>> getData() {
		List<List<Object>> result = new ArrayList<List<Object>>();
		Random r = new Random();

		for (int i = 0; i < 100; i++) {
			List<Object> data = new ArrayList<Object>();
			data.add("Row_01 - " + (r.nextInt(5) + 1));
			data.add("Row_02 - " + (r.nextInt(5) + 1));
			data.add("Row_03 - " + (r.nextInt(5) + 1));
			data.add("Column_01 - " + (r.nextInt(5) + 1));
			data.add("Column_02 - " + (r.nextInt(5) + 1));
			data.add(r.nextInt(10000));
			data.add(r.nextDouble() * 10000.0);
			data.add(r.nextInt(100));
			result.add(data);
		}
		return result;
	}
}