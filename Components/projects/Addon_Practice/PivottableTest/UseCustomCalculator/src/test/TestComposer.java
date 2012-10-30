package test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderContext;
import org.zkoss.pivot.Pivottable;

import org.zkoss.pivot.impl.SimplePivotRenderer;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;
import org.zkoss.pivot.impl.calc.Context;
import org.zkoss.pivot.impl.calc.ContextType;
import org.zkoss.pivot.impl.calc.ContextualCalculator;

import org.zkoss.zk.ui.select.SelectorComposer;

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
			
			// assign columns, the order matches to the level of column node field
			_pivotModel.setFieldType("Column_Level_001", PivotField.Type.COLUMN);
			
			// assign datas, the order matches to the order of data field
			_pivotModel.setFieldType("Data_Field_001", PivotField.Type.DATA);
			_pivotModel.setFieldType("Data_Field_002", PivotField.Type.DATA);
			_pivotModel.setFieldType("Data_Field_003", PivotField.Type.DATA);
			TabularPivotField field = _pivotModel.getField("Data_Field_003");
			_pivotModel.setFieldSummary(field, new PercentageCalculator());
		}
		return _pivotModel;
	}
	/**
	 * Custom pivot renderer,
	 * use custom format for column field "Data_Field_003"
	 * @return
	 */
	public SimplePivotRenderer getPivotRenderer () {
		return new SimplePivotRenderer() {
			public String renderCell(Number data, Pivottable table, 
					PivotHeaderContext rowContext, PivotHeaderContext columnContext,
					PivotField dataField) {
				DecimalFormat pf = new DecimalFormat("##,###.00 %");
				return "Data_Field_003".equals(dataField.getFieldName())?
						pf.format(data.doubleValue()) : super.renderCell(data, table, rowContext, columnContext, dataField);
			}
		};
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

			Double dataOne = r.nextDouble() * 10000.0 + 1.0;
			Double dataTwo = r.nextDouble() * 10000.0 + 1.0;
			data.add("Row_Level_001 - " + (r.nextInt(10) + 1));
			data.add("Column_Level_001 - " + (r.nextInt(10) + 1));

			data.add(dataOne);
			data.add(dataTwo);
			data.add(new DataForPercentage(dataOne, dataTwo));
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
			"Row_Level_001",
			"Column_Level_001",
			"Data_Field_001", "Data_Field_002", "Data_Field_003"
		});
	}

	/**
	 * The context for PercentageCalculator
	 *
	 */
	public static class PercentageContext implements Context<PercentageContext> {

		private Double _sumOfDataOne = 0.0;
		private Double _sumOfDataTwo = 0.0;

		// what to do when iterating over raw data.
		@Override
		public void add(Object item) {
			if (item instanceof DataForPercentage) {
				DataForPercentage _item = (DataForPercentage)item;
				_sumOfDataOne += _item.getDataOne();
				_sumOfDataTwo += _item.getDataTwo();
			} else {
				_sumOfDataOne += (Double)item;
				_sumOfDataTwo += (Double)item;
			}
		}
		// what to do when merging from contexts of a partition of its raw data set.
		@Override
		public void merge(PercentageContext ctx) {
			_sumOfDataOne += ctx.getSumOfDataOne();
			_sumOfDataTwo += ctx.getSumOfDataTwo();
		}
		public Double getSumOfDataOne () {
			return _sumOfDataOne;
		}
		public Double getSumOfDataTwo () {
			return _sumOfDataTwo;
		}

		public Double getPercentage() {
			return getSumOfDataOne() / getSumOfDataTwo();
		}

		public static final ContextType<PercentageContext> CONTEXT_TYPE = 
				new ContextType<PercentageContext>() {
			// ContextType has the responsibility as a Context factory
			@Override
			public PercentageContext create() {
				return new PercentageContext();
			}
		};
	}

	/**
	 * The custom calculator to calculate percentage value
	 * based on the given data
	 *
	 */
	public static class PercentageCalculator implements ContextualCalculator<PercentageContext> {

		public static final PercentageCalculator INSTANCE = new PercentageCalculator();
		private PercentageCalculator() {}

		// specify the context type
		@Override
		public ContextType<PercentageContext> getContextType() {
			return PercentageContext.CONTEXT_TYPE;
		}
		// summarize the end result from the context
		@Override
		public Number getResult(PercentageContext context) {
			return context.getPercentage();
		}
		@Override
		public String getLabel() {
			return "Percentage";
		}
		@Override
		public String getLabelKey() {
			return "percentage";
		}
	}

	/**
	 * Store the data for calculating percentage value
	 *
	 */
	private class DataForPercentage {
		private Double _dataOne;
		private Double _dataTwo;
		public DataForPercentage(Double dataOne, Double dataTwo) {
			_dataOne = dataOne;
			_dataTwo = dataTwo;
		}
		public Double getDataOne () {
			return _dataOne;
		}
		public Double getDataTwo () {
			return _dataTwo;
		}
	}

}