package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.pivot.event.PivotUIEvent;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

/**
 * Tested with ZK 6.0.2 EE and ZK Pivottable 2.0.0
 *
 * @author benbai123
 */
@SuppressWarnings("rawtypes")
public class TestComposer extends SelectorComposer {
	
	private static final long serialVersionUID = -8249566421884806620L;

	@Wire
	Label lb; /** label, contains type and name of clicked row or column */
	@Wire
	Listbox lbx; /** listbox, contains distinct values of clicked row or column field */
	/** pivot model with the 'whole' raw data */
	private TabularPivotModel _pivotModel;
	/** model provider, provide the columns, raw data and pivot model */
	private PivotModelProvider _modelProvider = new PivotModelProvider();

	/**
	 * map that contains all field index
	 * use field name as the keys
	 */
	private Map<String, Integer> _indexMap = new HashMap<String, Integer>();

	/**
	 * Get pivottable's model
	 * @return TabularPivotModel the pivottable's model
	 * @throws Exception
	 */
	public TabularPivotModel getPivotModel () throws Exception {
		if (_pivotModel == null) {
			_pivotModel = _modelProvider.getPivotModel();
		}
		return _pivotModel;
	}

	/**
	 * update the value list and label if clicked on row or column field
	 * @param e
	 * @throws Exception 
	 */
	@Listen("onPivotPopup = #pivottable")
	public void updateValueList (PivotUIEvent e) throws Exception {
		TabularPivotField field = getClickedField(e);
		if (field != null) {
			updateValueList(field);
		} else {
			lb.setValue("");
			lbx.getChildren().clear();
		}
	}
	/**
	 * get the clicked row or column field
	 * @param e
	 */
	private TabularPivotField getClickedField (PivotUIEvent e) {
		TabularPivotField field = null;
		if (e != null
			&& (e.getRowContext() == null
				|| e.getColumnContext() == null)) {
			// not clicked on data field

			if (e.getRowContext() != null) {
				// clicked on row field
				field = _pivotModel.getRowFields()[e.getRowContext().getNode().getDepth() - 1];
			} else {
				// clicked on column field
				field = _pivotModel.getColumnFields()[e.getColumnContext().getNode().getDepth() - 1];
			}
		}
		return field;
	}
	/**
	 * create the value list of clicked row/column field
	 * @param field pivot field, used to get fieldName
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked" })
	private void updateValueList (TabularPivotField field) throws Exception {
		// clear old children
		lbx.getChildren().clear();

		List<String> columns = _modelProvider.getColumns();
		// index of field name in columns
		int index = getFieldIndex(columns, field.getFieldName());
		// raw data of pivot model
		Iterable<List<Object>> rawData = (Iterable<List<Object>>)getPivotModel().getRawData();
		// distinct values of the given field
		List<Object> distinctValues = getDistinctValues(rawData, columns, index);

		// create listitem for each value
		for (final Object value : distinctValues) {
			Listitem li = new Listitem();
			Listcell lc = new Listcell(value.toString());

			lc.setParent(li);
			li.setParent(lbx);
		}
		// update field info label
		lb.setValue("field type: " + field.getType() + ", field name: " + field.getFieldName());
	}
	/**
	 * get all different values of a field
	 * @param rawData the raw data to get different values from
	 * @param columns all columns in pivot model
	 * @param index the index to get value from list
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	private static List getDistinctValues (Iterable<List<Object>> rawData, List<String> columns, int index) {
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
	 * get the index in raw data of a field
	 * @param columns columns used in pivot model
	 * @param fieldName name of the field to search index
	 * @return int the found index
	 */
	private int getFieldIndex (List<String> columns, String fieldName) {
		// search it from index map at first
		if (_indexMap.containsKey(fieldName)) {
			return _indexMap.get(fieldName);
		} else { // not found
			int index = -1;
			// search field name in columns
			for (int i = 0; i < columns.size(); i++) {
				if (columns.get(i).equals(fieldName)) {
					index = i;
					break;
				}
			}
			// store it to index map
			_indexMap.put(fieldName, index);
			return index;
		}
	}
}