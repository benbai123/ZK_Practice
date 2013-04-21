package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.zkoss.pivot.Pivottable;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;
import org.zkoss.pivot.util.PivotModels;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

/**
 * Class to handle tasks with respect to filter
 *
 * NOTE: A FilterHandler is rely on a specific set of fields and
 * should be renew after the fields of pivot model are changed 
 * 
 * Tested with ZK 6.0.2 EE and ZK Pivottable 2.0.0
 *
 * @author benbai123
 */
public class FilterHandler {
	/**
	 * map that contains all Limit object
	 * use field name as the key
	 * 
	 */
	private Map<String, Limit> _fieldsLimitsMap = new HashMap<String, Limit>();
	/**
	 * map that contains all field index
	 * use field name as the key
	 */
	private Map<String, Integer> _indexMap = new HashMap<String, Integer>();;

	@SuppressWarnings("rawtypes")
	public void updateFilter (Pivottable pivottable, TabularPivotField field, List<String> columns, Iterable<List<Object>> rawData, Div filter) {
		List distinctValues = PVTUtils.getDistinctValues(rawData, columns, getFieldIndex(columns, field.getFieldName()));
		updateFilterList(pivottable, distinctValues, field, filter);
	}
	/**
	 * update the limited values of a field
	 * @param fieldName the name of the field to update
	 * @param value the value to update
	 * @param accept whether accept the value above
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateLimit (String fieldName, Object value, boolean accept) {
		// try find Limit object
		Limit limit = _fieldsLimitsMap.get(fieldName);
		// create a new one if not found
		if (limit == null) {
			limit = new Limit(fieldName, new HashSet());
			_fieldsLimitsMap.put(fieldName, limit);
		}
		// remove value from limited values if the value is accepted
		// add value to limited values if the value is not accepted
		if (accept) {
			limit.getLimitedValues().remove(value);
		} else {
			limit.getLimitedValues().add(value);
		}
	}
	/**
	 * filter data by limits
	 * @param model pivot model, get all row/column fields from it
	 * @param rawData raw data, to filter it
	 * @return the filtered raw data
	 */
	public Iterable<List<Object>> filterData (TabularPivotModel model, final List<String> columns, Iterable<List<Object>> rawData) {
		// field name of row/column fields
		final List<String> rcColumns = new ArrayList<String>();
		// keep a final object of limit map so can be used in inner class
		final Map<String, Limit> limits = _fieldsLimitsMap;
		// add all row/column field names
		for (TabularPivotField tpf : model.getRowFields()) {
			rcColumns.add(tpf.getFieldName());
		}
		for (TabularPivotField tpf : model.getColumnFields()) {
			rcColumns.add(tpf.getFieldName());
		}
		return PivotModels.filter(rawData, new PivotModels.Filter<List<Object>>() {
			public boolean keep(List<Object> row) {
				// for each row/column (field names)
				for (String s : rcColumns) {
					// find Limit object
					Limit l = limits.get(s);
					if (l != null) {
						// find value index
						int index = getFieldIndex(columns, s);
						// get value
						Object value = row.get(index);
						// do not keep the value if the value is in limit
						if (l.getLimitedValues().contains(value)) {
							return false;
						}
					}
				}
				return true;
			}
		});
	}
	/**
	 * create the filter list
	 * @param pivottable pivottable, to fire onFilterChanged event as needed
	 * @param distinctValues all different values, used to construct the filter list
	 * @param field pivot field, used to get fieldName
	 * @param filter the div component specified in index.zul
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateFilterList (final Pivottable pivottable, List distinctValues, final TabularPivotField field, Div filter) {
		// clear old children
		filter.getChildren().clear();

		Listbox lb = new Listbox();
		lb.setWidth("200px");
		final String fieldName = field.getFieldName();
		Limit limit = _fieldsLimitsMap.get(fieldName);

		// for each value of this field
		for (final Object value : distinctValues) {
			Listitem li = new Listitem();
			Listcell lc = new Listcell();
			Checkbox cb = new Checkbox(value.toString());

			// update checked status of checkbox according to
			// whether this value is a limited value
			if (limit != null && limit.getLimitedValues().contains(value)) {
				cb.setChecked(false);
			} else {
				cb.setChecked(true);
			}
			cb.setParent(lc);
			lc.setParent(li);
			li.setParent(lb);

			// add onCheck event listener to checkbox
			cb.addEventListener("onCheck", new EventListener() {
				public void onEvent (Event event) {
					// fire event to pivottable with
					// the information of changed filter attributes
					Events.postEvent(new FilterChangedEvent(pivottable, fieldName, value, ((CheckEvent)event).isChecked() ));
				}
			});
		}
		// add listbox to div
		lb.setParent(filter);
	}
	/**
	 * get the index in raw data of a field
	 * @param columns columns used in pivot model
	 * @param fieldName name of the field to search index
	 * @return int the found index
	 */
	private int getFieldIndex (List<String> columns, String fieldName) {
		// search it from index map at first
		if (!_indexMap.containsKey(fieldName)) {
			int index = PVTUtils.getFieldIndex(columns, fieldName);
			// store it to index map
			_indexMap.put(fieldName, index);
		}
		return _indexMap.get(fieldName);
	}
}