package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.impl.TabularPivotModel;

public class PivotModelProvider {
	/** pivot model with the 'whole' raw data */
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
	 * the order of column names
	 * @return
	 * @throws Exception
	 */
	public List<List<Object>> getData() {
		List<List<Object>> result = new ArrayList<List<Object>>();
		Random r = new Random();

		for (int i = 0; i < 100; i++) {
			List<Object> data = new ArrayList<Object>();
			data.add("Row_Level_001 - " + (r.nextInt(5) + 1));
			data.add("Row_Level_002 - " + (r.nextInt(5) + 1));
			data.add("Row_Level_003 - " + (r.nextInt(5) + 1));
			data.add("Row_Level_004 - " + (r.nextInt(5) + 1));
			data.add("Column_Level_001 - " + (r.nextInt(5) + 1));
			data.add("Column_Level_002 - " + (r.nextInt(5) + 1));
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
}
