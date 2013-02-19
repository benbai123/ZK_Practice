package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.Pivottable;
import org.zkoss.pivot.impl.StandardCalculator;
import org.zkoss.pivot.impl.TabularPivotModel;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

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
	private Pivottable pivottableTwo;

	private TabularPivotModel _pivotModel;
	private TabularPivotModel _pivotModelTwo;

	public TabularPivotModel getPivotModel () throws Exception {
		if (_pivotModel == null) {
			_pivotModel = getPivotModel(getData(), getColumns());
		}
		return _pivotModel;
	}
	public TabularPivotModel getPivotModelTwo () throws Exception {
		if (_pivotModelTwo == null) {
			_pivotModelTwo = getPivotModel(getData(), getColumns());
		}
		return _pivotModelTwo;
	}

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
	@Listen ("onPivotNodeOpen = #pivottable")
	public void syncOpenStatus () {
		syncOpenStatus(_pivotModel.getRowHeaderTree().getRoot(), _pivotModelTwo.getRowHeaderTree().getRoot(), false);
		syncOpenStatus(_pivotModel.getColumnHeaderTree().getRoot(), _pivotModelTwo.getColumnHeaderTree().getRoot(), false);

		// reset model
		pivottableTwo.setModel(null);
		pivottableTwo.setModel(_pivotModelTwo);
	}
	/**
	 * Synchronize the open status of two pivot header trees
	 * 
	 * @param root the root of the base pivot header tree (or its sub trees)
	 * @param rootTwo the root of the pivot header tree to sync (or its sub trees)
	 * @param checkAll whether sync whole tree, <br>
	 * true: sync whole tree, put every node of base pivot header tree into open list to sync<br>
	 * false: sync only current view, only put the displayed node into open list to sync
	 */
	private void syncOpenStatus (PivotHeaderNode root, PivotHeaderNode rootTwo, boolean checkAll) {
		Map<Object, PivotHeaderNode> originalOpenMap = new HashMap<Object, PivotHeaderNode>();

		// sync displayed node only if not checkAll
		// so do not need to scan whole header tree
		for (PivotHeaderNode node : root.getChildren()) {
			// checkAll: sync each node
			// !checkAll: sync displayed node
			if (checkAll
				|| (node.getDepth() == 1 || node.getParent().isOpen())) {
				originalOpenMap.put(node.getKey(), node);
			}
		}
		// for each node in children of rootTwo
		for (PivotHeaderNode newNode : rootTwo.getChildren()) {
			PivotHeaderNode node = originalOpenMap.get(newNode.getKey());
			if (node != null) {
				newNode.setOpen(node.isOpen());
				// recursively sync sub trees
				syncOpenStatus(node, newNode, checkAll);
			}
		}
	}
}