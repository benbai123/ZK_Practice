package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;
/**
 * utilities for pivottable
 * 
 * Tested with ZK 6.0.2 EE and ZK Pivottable 2.0.0
 *
 * @author benbai123
 */
public class PVTUtils {
	/**
	 * Create a new pivot model based on
	 * current pivot model and new data 
	 * @param model
	 * @param newData
	 * @return
	 */
	public static TabularPivotModel cloneModelWithData (TabularPivotModel model, Iterable<List<Object>>newData) {
		TabularPivotField[] fields = model.getFields();

		// get columns from old model
		List<String> columns = new ArrayList<String>();
		// set field
		for (TabularPivotField tpf : fields) {
			columns.add(tpf.getFieldName());
		}

		TabularPivotModel newModel = new TabularPivotModel(newData, columns);
		PVTUtils.syncModelStructure(model, newModel);
		return newModel;
	}
	/**
	 * get the index in raw data of a field
	 * @param columns columns used in pivot model
	 * @param fieldName name of the field to search index
	 * @return int the found index
	 */
	public static int getFieldIndex (List<String> columns, String fieldName) {
		int index = -1;
		// search field name in columns
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).equals(fieldName)) {
				index = i;
				break;
			}
		}
		return index;
	}
	/**
	 * get all different values of a field
	 * @param rawData the raw data to get different values from
	 * @param columns all columns in pivot model
	 * @param index the index to get value from list
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getDistinctValues (Iterable<List<Object>> rawData, List<String> columns, int index) {
		// set used to hold distinct values
		Set s = new HashSet();
		// result list
		List result = new ArrayList();
		
		if (index == -1) return result;
		// add all value to set directly
		for (List<Object> data : rawData) {
			s.add(data.get(index));
		}
		// copy to list then sort the list
		for (Object o : s) {
			result.add(o);
		}
		Collections.sort(result);
		return result;
	}
	/**
	 * sync the structure of pivot model
	 * 
	 * @param model the base pivot model
	 * @param modelTwo the pivot model to adjust its structure
	 */
	public static void syncModelStructure (TabularPivotModel model, TabularPivotModel modelTwo) {
		syncFields(model.getRowFields(), modelTwo);
		syncFields(model.getColumnFields(), modelTwo);
		syncFields(model.getDataFields(), modelTwo);
		syncFields(model.getFields(PivotField.Type.UNUSED), modelTwo);
		syncOpenStatus(model.getRowHeaderTree().getRoot(), modelTwo.getRowHeaderTree().getRoot(), false);
		syncOpenStatus(model.getColumnHeaderTree().getRoot(), modelTwo.getColumnHeaderTree().getRoot(), false);
	}
	/**
	 * sync pivot fields of pivot model
	 * @param fields the base fields
	 * @param model the pivot model to adjust its fields
	 */
	private static void syncFields (TabularPivotField[] fields, TabularPivotModel model) {
		for (TabularPivotField f : fields) {
			model.setFieldType(f.getFieldName(), f.getType());

			PivotField field = model.getField(f.getFieldName());
			model.setFieldSubtotals(field, f.getSubtotals());
			model.setFieldKeyComparator(field, f.getComparator());
		}
	}
	/**
	 * Synchronize the open status of two pivot header trees
	 * 
	 * @param root the root of the base pivot header tree (or its sub trees)
	 * @param rootTwo the root of the pivot header tree (or its sub trees) to sync
	 * @param checkAll whether sync whole tree, <br>
	 * true: sync whole tree, put every node of base pivot header tree into open list to sync<br>
	 * false: sync only current view, only put the displayed node into open list to sync
	 */
	private static void syncOpenStatus (PivotHeaderNode root, PivotHeaderNode rootTwo, boolean checkAll) {
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