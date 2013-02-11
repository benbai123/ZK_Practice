package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
	private Pivottable pivottable;
	@Wire
	private Pivottable pivottableTwo;

	private TabularPivotModel _pivotModel;
	private TabularPivotModel _pivotModelTwo;

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
	public TabularPivotModel getPivotModelTwo () throws Exception {
		if (_pivotModelTwo == null) {
			_pivotModelTwo = new TabularPivotModel(getData(), getColumns());

			// assign rows, the order matches to the level of row node field
			_pivotModelTwo.setFieldType("RowOne", PivotField.Type.ROW);
			_pivotModelTwo.setFieldType("RowTwo", PivotField.Type.ROW);
			_pivotModelTwo.setFieldType("RowThree", PivotField.Type.ROW);

			// assign columns, the order matches to the level of column node field
			_pivotModelTwo.setFieldType("ColumnOne", PivotField.Type.COLUMN);
			_pivotModelTwo.setFieldType("ColumnTwo", PivotField.Type.COLUMN);

			// assign datas, the order matches to the order of data field
			_pivotModelTwo.setFieldType("DataOne", PivotField.Type.DATA);

			PivotField field = _pivotModelTwo.getField("RowOne");
			_pivotModelTwo.setFieldSubtotals(field, new Calculator[] {StandardCalculator.SUM, StandardCalculator.MAX});
		}
		return _pivotModelTwo;
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
		List<PivotHeaderNode> originalOpenList = new ArrayList<PivotHeaderNode>();
		List<PivotHeaderNode> newOpenList = new ArrayList<PivotHeaderNode>();

		// sync displayed node only if not checkAll
		// so do not need to scan whole header tree
		for (PivotHeaderNode node : root.getChildren()) {
			// checkAll: sync each node
			// !checkAll: sync displayed node
			if (checkAll
				|| (node.getDepth() == 1 || node.getParent().isOpen())) {
				originalOpenList.add(node);
			}
		}
		// for each node in base open list
		for (PivotHeaderNode node : originalOpenList) {
			boolean found = false;
			for (PivotHeaderNode newNode : rootTwo.getChildren()) {
				if (node.getKey().equals(newNode.getKey())) {
					found = true;
					newNode.setOpen(node.isOpen());
					newOpenList.add(newNode);
				}
			}
			// add null to new open list if not found
			// so the size of base open list and
			// the size of new open list will be the same
			if (!found) {
				newOpenList.add(null);
			}
		}

		// recursively sync sub trees
		for (int i = 0; i < originalOpenList.size(); i++) {
			if (newOpenList.get(i) != null) {
				syncOpenStatus(originalOpenList.get(i), newOpenList.get(i), checkAll);
			}
		}
	}
}