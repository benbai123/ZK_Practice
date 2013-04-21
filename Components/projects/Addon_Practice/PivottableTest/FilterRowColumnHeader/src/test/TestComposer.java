package test;

import java.util.List;
import org.zkoss.pivot.Pivottable;

import org.zkoss.pivot.event.PivotUIEvent;
import org.zkoss.pivot.impl.TabularPivotField;
import org.zkoss.pivot.impl.TabularPivotModel;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 * Tested with ZK 6.0.2 EE and ZK Pivottable 2.0.0
 *
 * @author benbai123
 */
@SuppressWarnings("rawtypes")
public class TestComposer extends SelectorComposer {
	
	private static final long serialVersionUID = -8249566421884806620L;
	@Wire
	Pivottable pivottable;
	@Wire
	Div filter; // div that will contain filter list
	@Wire
	Label lb; // filter info
	// pivot model with the 'whole' raw data
	private TabularPivotModel _pivotModel;
	// model provider, provide the columns, raw data and pivot model
	private PivotModelProvider _modelProvider = new PivotModelProvider();

	// handler to do the works of filter
	// NOTE: Renew it if changed to a complete different model
	private FilterHandler _filterHandler = new FilterHandler();

	/**
	 * Get pivottable's model
	 * @return TabularPivotModel the pivottable's model
	 * @throws Exception
	 */
	public TabularPivotModel getPivotModel () throws Exception {
		if (_pivotModel == null) {
			_pivotModel = _modelProvider.getPivotModel();
		}
		return PVTUtils.cloneModelWithData(_pivotModel, getFilteredData());
	}

	/**
	 * update the selected field for filter
	 * @param e
	 */
	@Listen("onPivotPopup = #pivottable")
	public void updateFilterIndex (PivotUIEvent e) {
		if (e.getRowContext() != null
			&& e.getColumnContext() == null) {
			// clicked on row field
			updateFilter(_pivotModel.getRowFields()[e.getRowContext().getNode().getDepth()-1]);
		} else if (e.getRowContext() == null
				&& e.getColumnContext() != null) {
			// clicked on column field
			updateFilter(_pivotModel.getColumnFields()[e.getColumnContext().getNode().getDepth()-1]);
		}
	}
	/**
	 * called while filter is changed
	 * update filter value of selected field
	 * @param event
	 */
	@Listen("onFilterChanged = #pivottable")
	public void updateLimit (FilterChangedEvent event) {
		_filterHandler.updateLimit(event.getFieldName(), event.getValue(), event.isChecked());
		updatePivottable();
	}
	/**
	 * update the filter list
	 * @param field the field to update filter list
	 */
	private void updateFilter (TabularPivotField field) {
		_filterHandler.updateFilter(pivottable,
				field,
				_modelProvider.getColumns(),
				getRawData(),
				filter);
		// update field info label
		lb.setValue("field type: " + field.getType() + ", field name: " + field.getFieldName());
	}
	/**
	 * update pivottable with filtered pivot model
	 */
	private void updatePivottable () {
		// store current structure at first
		PVTUtils.syncModelStructure((TabularPivotModel)pivottable.getModel(), _pivotModel);
		TabularPivotModel filteredModel = PVTUtils.cloneModelWithData(_pivotModel, getFilteredData());
		pivottable.setModel(filteredModel);
	}
	/**
	 * get the filtered data
	 * @return
	 */
	private Iterable<List<Object>> getFilteredData () {
		return _filterHandler.filterData(_pivotModel, _modelProvider.getColumns(), getRawData());
	}
	/**
	 * get complete raw data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Iterable<List<Object>> getRawData () {
		return (Iterable<List<Object>>)_pivotModel.getRawData();
	}
}