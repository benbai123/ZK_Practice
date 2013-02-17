package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.impl.StandardCalculator;
import org.zkoss.pivot.impl.TabularPivotModel;

/**
 * Provide pivot model, create new data
 * @author benbai123
 *
 */
public class PVTModelProvider {
	/**
	 * Get pivottable's model, also make a snapshot of it
	 * @return TabularPivotModel the pivottable's model
	 * @throws Exception
	 */
	public static TabularPivotModel getPivotModel () throws Exception {
		List<List<Object>> rawData = getData();
		TabularPivotModel pivotModel;
		pivotModel = new TabularPivotModel(rawData, getColumns());

		// assign rows, the order matches to the level of row node field
		pivotModel.setFieldType("RowOne", PivotField.Type.ROW);
		pivotModel.setFieldType("RowTwo", PivotField.Type.ROW);
		pivotModel.setFieldType("RowThree", PivotField.Type.ROW);

		// assign columns, the order matches to the level of column node field
		pivotModel.setFieldType("ColumnOne", PivotField.Type.COLUMN);
		pivotModel.setFieldType("ColumnTwo", PivotField.Type.COLUMN);
		pivotModel.setFieldType("ColumnThree", PivotField.Type.COLUMN);

		// assign datas, the order matches to the order of data field
		pivotModel.setFieldType("DataOne", PivotField.Type.DATA);
		pivotModel.setFieldType("DateTwo", PivotField.Type.DATA);

		PivotField field = pivotModel.getField("RowOne");
		pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
		field = pivotModel.getField("RowTwo");
		pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
		field = pivotModel.getField("ColumnOne");
		pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
		field = pivotModel.getField("ColumnTwo");
		pivotModel.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});

		return pivotModel;
	}

	/**
	 * prepare the data for pivottable's model
	 * The order of object put into data list should match
	 * the order of column name's
	 * @return
	 * @throws Exception
	 */
	public static List<List<Object>> getData() throws Exception {
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
	 * prepare columns name for pivottable's model
	 * @return
	 */
	public static List<String> getColumns() {
		return Arrays.asList(new String[]{
				"RowOne", "RowTwo", "RowThree",
				"ColumnOne", "ColumnTwo", "ColumnThree",
				"DataOne", "DateTwo"
		});
	}
	/**
	 * Generate some random data
	 * @return
	 */
	public static List<List<Object>> getNewDatas(StringBuilder sb) {
		List<List<Object>> result = new ArrayList<List<Object>>();
		Random r = new Random();
		int amount = 1;

		for (int i = 0; i < amount; i++) {
			List<Object> data = new ArrayList<Object>();
			Object o;
			o = "RowOne - " + (r.nextInt(5) + 1);
			sb.append("add: ")
				.append(o)
				.append("\t");
			data.add(o);
			o = "RowTwo - " + (r.nextInt(5) + 1);
			sb.append(o)
				.append("\t");
			data.add(o);
			o = "RowThree - " + (r.nextInt(5) + 1);
			sb.append(o)
				.append("\t");
			data.add(o);
			o = "ColumnOne - " + (r.nextInt(5) + 1);
			sb.append(o)
				.append("\t");
			data.add(o);
			o = "ColumnTwo - " + (r.nextInt(5) + 1);
			sb.append(o)
				.append("\t");
			data.add(o);
			o = "ColumnThree - " + (r.nextInt(5) + 1);
			sb.append(o)
				.append("\t");
			data.add(o);
			o = -5 + r.nextInt(11); // -5 ~ 5
			sb.append(o)
				.append("\t");
			data.add(o);
			o = -5 + r.nextInt(11);
			sb.append(o)
				.append("\t\n");
			data.add(o);
			result.add(data);
		}
		return result;
	}
}