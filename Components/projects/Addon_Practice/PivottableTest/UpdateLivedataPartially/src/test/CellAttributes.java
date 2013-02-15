package test;

import java.util.List;

/**
 * attributes used to denotes a specific cell
 * @author benbai123
 *
 */
public class CellAttributes {
	// keys to denote a row node
	private List<Object> _rowKeys;
	// keys to denote a column node
	private List<Object> _colKeys;
	// name to denotes a data field
	private String _dataFieldName;
	// labelKey to denote a row calculator
	private String _rowCalculatorLabelKey;
	// labelKey to denote a column calculator
	private String _colCalculatorLabelKey;

	// constructor
	public CellAttributes (List<Object> rowKeys, List<Object> colKeys, String dataFieldName,
			String rowCalculatorLabelKey, String colCalculatorLabelKey) {
		_rowKeys = rowKeys;
		_colKeys = colKeys;
		_dataFieldName = dataFieldName;
		_rowCalculatorLabelKey = rowCalculatorLabelKey;
		_colCalculatorLabelKey = colCalculatorLabelKey;
	}
	/**
	 * used to reset all attributes
	 * @param rowKeys
	 * @param colKeys
	 * @param dataFieldName
	 * @param rowCalculatorLabelKey
	 * @param colCalculatorLabelKey
	 */
	public void updateAttributes (List<Object> rowKeys, List<Object> colKeys, String dataFieldName,
			String rowCalculatorLabelKey, String colCalculatorLabelKey) {
		_rowKeys = rowKeys;
		_colKeys = colKeys;
		_dataFieldName = dataFieldName;
		_rowCalculatorLabelKey = rowCalculatorLabelKey;
		_colCalculatorLabelKey = colCalculatorLabelKey;
	}
	// setters/getters
	public void setRowKeys (List<Object> rowKeys) {
		_rowKeys = rowKeys;
	}
	public List<Object> getRowKeys () {
		return _rowKeys;
	}
	public void setColKeys (List<Object> colKeys) {
		_colKeys = colKeys;
	}
	public List<Object> getColKeys () {
		return _colKeys;
	}
	public void setDataFieldName (String dataFieldName) {
		_dataFieldName = dataFieldName;
	}
	public String getDataFieldName () {
		return _dataFieldName;
	}
	public void setRowCalculatorLabelKey (String rowCalculatorLabelKey) {
		_rowCalculatorLabelKey = rowCalculatorLabelKey;
	}
	public String getRowCalculatorLabelKey () {
		return _rowCalculatorLabelKey;
	}
	public void setColCalculatorLabelKey (String colCalculatorLabelKey) {
		_colCalculatorLabelKey = colCalculatorLabelKey;
	}
	public String getColCalculatorLabelKey () {
		return _colCalculatorLabelKey;
	}
}