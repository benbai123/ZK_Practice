package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.Pivottable;
import org.zkoss.pivot.impl.TabularPivotModel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Timer;

/**
 * The scenario is to reduce memory consumption, get only required
 * data from data base each time and purge it after the data for pivot
 * model are ready
 * 
 * @author ben
 *
 */
public class PartialRenderComposer extends GenericForwardComposer {
	private Pivottable pivottable; // pitottable
	private Intbox paging; // custom paging control
	private Intbox info;
	private Intbox nodes;
	private Intbox memInfo;
	private Chosenbox rowFieldChosen;
	private Chosenbox columnFieldChosen;
	private Chosenbox dataFieldChosen;
	private Timer checkTimer;

	private TabularPivotModel _pivotModel;

	private Runtime rt = Runtime.getRuntime();

	// fake test data, render only once
	private static List<DataClass> _testData; // data that simulate a db resource
	private List<String> _rowFieldList;
	private List<String> _columnFieldList;
	private List<String> _dataFieldList;
	private List<String> _unusedFieldList;
	private int _size = 0;
	private int _currentPage = 1; // page of custom paging
	private int _pageNodesLimit = 10; // number of first level node of a page
	private int _testDataSize = 200000;

	// ------------------------------- //
	// flow control                    //
	// ------------------------------- //
	@SuppressWarnings("unchecked")
	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
		info.setValue(_size);

		updateFieldLists();

		nodes.setValue(_pageNodesLimit);
		// show all data in one page
		pivottable.setPageSize(_size);
	}

	// ------------------------------- //
	// Event handling                  //
	// ------------------------------- //
	/**
	 * update page number then update pivot model
	 */
	public void onChanging$paging (InputEvent e) {
		_currentPage = Integer.parseInt(e.getValue());
		if ((_currentPage-1) * _pageNodesLimit >= 10) {
			// return to first page if overflow
			_currentPage = 1;
			paging.setValue(_currentPage);
		}
		updatePivotModel();
	}
	/**
	 * update first-level-nodes per page then update pivot model
	 * @param e
	 */
	public void onChange$nodes (InputEvent e) {
		_pageNodesLimit = Integer.parseInt(e.getValue());
		updatePivotModel();
	}

	/**
	 * update field list and data then update models
	 * @param e
	 */
	public void onSelect$rowFieldChosen (SelectEvent e) {
		moveItem(e, _rowFieldList, _unusedFieldList);
		updatePivotModel();
		updateFieldLists();
	}
	/**
	 * update field list and data then update models
	 * @param e
	 */
	public void onSelect$columnFieldChosen (SelectEvent e) {
		moveItem(e, _columnFieldList, _unusedFieldList);
		updatePivotModel();
		updateFieldLists();
	}
	/**
	 * update field list and data then update models
	 * @param e
	 */
	public void onSelect$dataFieldChosen (SelectEvent e) {
		moveItem(e, _dataFieldList, _unusedFieldList);
		updatePivotModel();
		updateFieldLists();
	}
	/**
	 * require gc and update memory info
	 */
	public void onTimer$checkTimer () {
		System.gc();
		memInfo.setValue(new Long((rt.totalMemory()-rt.freeMemory())/1024/1024).intValue());
	}

	// ------------------------------- //
	// data management                 //
	// ------------------------------- //
	public TabularPivotModel getPivotModel () throws Exception {
		List<List<Object>> rawData = getPivotData();
		_pivotModel = new TabularPivotModel(rawData, getColumns());
		List<String> l;
		int i;

		for (i = 0, l = getRowFieldList(); i < l.size(); i++) {
			_pivotModel.setFieldType(l.get(i), PivotField.Type.ROW);
		}
		for (i = 0, l = getColumnFieldList(); i < l.size(); i++) {
			_pivotModel.setFieldType(l.get(i), PivotField.Type.COLUMN);
		}
		for (i = 0, l = getDataFieldList(); i < l.size(); i++) {
			_pivotModel.setFieldType(l.get(i), PivotField.Type.DATA);
		}
		return _pivotModel;
	}
	/**
	 * update pivot model
	 */
	private void updatePivotModel() {
		try {
			_pivotModel = getPivotModel();
			pivottable.setModel(_pivotModel);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * update chosenbox field list
	 */
	private void updateFieldLists () {
		ListModelList model = getChosenModel(getRowFieldList(), getUnusedFieldList());
		rowFieldChosen.setModel(model);
		model.setSelection(_rowFieldList);

		model = getChosenModel(getColumnFieldList(), getUnusedFieldList());
		columnFieldChosen.setModel(model);
		model.setSelection(_columnFieldList);

		model = getChosenModel(getDataFieldList(), getUnusedFieldList());
		dataFieldChosen.setModel(model);
		model.setSelection(_dataFieldList);
	}

	/**
	 * Get partial data for pivot model
	 * @return
	 * @throws Exception
	 */
	private List<List<Object>> getPivotData() throws Exception {
		List<List<Object>> l = new ArrayList<List<Object>>();
		l.addAll(getData());

		// update size info
		_size = l.size();
		if (info != null) {
			info.setValue(_size);
		}
		if (pivottable != null) {
			// show all data in one page
			pivottable.setPageSize(_size);
		}

		_testData.clear();
		_testData = null;
		return l;
	}
	/**
	 * Move field from specific field list to unused field list ((removed))
	 * or vice versa (added)
	 * @param e
	 * @param origin
	 * @param unuse
	 */
	private void moveItem (SelectEvent e, List origin, List unuse) {
		Object target = null;
		List objs = new ArrayList();
		boolean add = false;
		
		objs.addAll(e.getSelectedObjects());
		// check whether field removed
		for (Object obj : origin) {
			if (!objs.contains(obj)) {
				target = obj;
				break;
			}
		}
		// check whether field added
		if (target == null) {
			add = true;
			for (Object obj : unuse) {
				if (objs.contains(obj)) {
					target = obj;
					break;
				}
			}
		}
		if (add) {
			// field added, move it to self list
			origin.add((String)target);
			unuse.remove(target);
		} else {
			// field removed, move it to unused
			unuse.add((String)target);
			origin.remove(target);
		}
	}
	/**
	 * Get the data list from fake db and take only required data,
	 * for pivot model, based on the 'used' fields and data range
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<List<Object>> getData() throws Exception {
		List<List<Object>> rawData = new ArrayList<List<Object>>();
		_rowFieldList = getRowFieldList(); // current row fields
		_columnFieldList = getColumnFieldList(); // current column fields
		_dataFieldList = getDataFieldList(); // current data fields
		// size for inner object array
		int arraySize = _rowFieldList.size() + _columnFieldList.size() + _dataFieldList.size();
		int index = 0;
		// data range
		int first = (_currentPage - 1)*_pageNodesLimit + 1;
		int last = (_currentPage - 1)*_pageNodesLimit + _pageNodesLimit;

		prepareTestData(); // prepare the test data
		for (DataClass d : _testData) {
			Object[] objs = new Object[arraySize];

			// check whether is in range
			String rowHead = (String)getValue(d, _rowFieldList.get(0));
			int nodeNo = Integer.parseInt((rowHead).substring(rowHead.indexOf("__") + 2, rowHead.length()));
			// Only load rows are in page range
			if (nodeNo >= first && nodeNo <= last) {
				// add values to array
				for (int i = 0; i < _rowFieldList.size(); i++) {
					objs[index] = getValue(d, _rowFieldList.get(i));
					index++;
				}
				for (int i = 0; i < _columnFieldList.size(); i++) {
					objs[index] = getValue(d, _columnFieldList.get(i));
					index++;
				}
				for (int i = 0; i < _dataFieldList.size(); i++) {
					objs[index] = getValue(d, _dataFieldList.get(i));
					index++;
				}
				index = 0; // reset index
				// add array to rawData
				rawData.add(Arrays.asList(objs));
			}
		}

		return rawData;
	}
	/**
	 * Get value from DataClass with respect to the field name
	 * @param d DataClass object
	 * @param name field name
	 * @return
	 */
	private Object getValue (DataClass d, String name) {
		return "Row_One".equals(name)? d.getRowOne()
				: "Row_Two".equals(name)? d.getRowTwo()
				: "Row_Three".equals(name)? d.getRowThree()
				: "Column_One".equals(name)? d.getColumnOne()
				: "Column_Two".equals(name)? d.getColumnTwo()
				: "Column_Three".equals(name)? d.getColumnThree()
				: "Data_One".equals(name)? d.getDataOne()
				: "Data_Two".equals(name)? d.getDataTwo() : d.getDataThree();
	}
	/**
	 * Fake db's data,
	 * will be cleared after the data for PivotModel are ready
	 */
	private void prepareTestData () {
		if (_testData == null) { // generate random data
			_testData = new ArrayList<DataClass>();
			Random r = new Random();
			for (int i = 0; i < _testDataSize; i++) {
				_testData.add(new DataClass(
						"Row_One__" + (r.nextInt(10) + 1), "Row_Two__" + (r.nextInt(10) + 1), "Row_Three__" + (r.nextInt(10) + 1),
						"Column_One__" + (r.nextInt(3) + 1), "Column_Two__" + (r.nextInt(3) + 1), "Column_Three__" + (r.nextInt(3) + 1),
						r.nextInt(1000) + 1, r.nextInt(1000) + 1, r.nextInt(1000) + 1));
			}
		}
	}
	/**
	 * Get the field names for pivottable
	 * @return
	 */
	private List<String> getColumns() {
		List<String> l = new ArrayList<String>();
		l.addAll(getRowFieldList());
		l.addAll(getColumnFieldList());
		l.addAll(getDataFieldList());
		return l;
	}
	/**
	 * Get all row field names
	 * @return
	 */
	private List<String> getRowFieldList () {
		if (_rowFieldList == null) { // init
			_rowFieldList = new ArrayList<String>();
			_rowFieldList.add("Row_One");
			_rowFieldList.add("Row_Two");
			_rowFieldList.add("Row_Three");
		}
		return _rowFieldList;
	}
	/**
	 * Get all column field names
	 * @return
	 */
	private List<String> getColumnFieldList () {
		if (_columnFieldList == null) { // init
			_columnFieldList = new ArrayList<String>();
			_columnFieldList.add("Column_One");
			_columnFieldList.add("Column_Two");
			_columnFieldList.add("Column_Three");
		}
		return _columnFieldList;
	}
	/**
	 * Get all data field names
	 * @return
	 */
	private List<String> getDataFieldList () {
		if (_dataFieldList == null) { // init
			_dataFieldList = new ArrayList<String>();
			_dataFieldList.add("Data_One");
			_dataFieldList.add("Data_Two");
			_dataFieldList.add("Data_Three");
		}
		return _dataFieldList;
	}
	/**
	 * Get all unused field names,
	 * for chosenbox model only
	 * @return
	 */
	private List<String> getUnusedFieldList () {
		if (_unusedFieldList == null) { // init
			_unusedFieldList = new ArrayList<String>();
		}
		return _unusedFieldList;
	}
	/**
	 * Get the list for chosenbox model by specific list (row/column/data)
	 * plus unusedFieldList
	 * @param fields The specific list and unusedFieldList
	 * @return
	 */
	private ListModelList<String> getChosenModel (List<String>... fields) {
		List<String> l = new ArrayList<String>(); 
		for (List<String> field : fields) {
			l.addAll(field);
		}
		return new ListModelList<String>(l);
	}
}
