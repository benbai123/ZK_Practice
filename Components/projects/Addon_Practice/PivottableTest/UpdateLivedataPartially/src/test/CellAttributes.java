package test;

import java.util.List;

public class CellAttributes {
	private List<Object> _rowKeys;
	private List<Object> _colKeys;
	private String _dataFieldName;
	private String _rowCalculatorLabelKey;
	private String _colCalculatorLabelKey;

	public CellAttributes (List<Object> rowKeys, List<Object> colKeys, String dataFieldName,
			String rowCalculatorLabelKey, String colCalculatorLabelKey) {
		_rowKeys = rowKeys;
		_colKeys = colKeys;
		_dataFieldName = dataFieldName;
		_rowCalculatorLabelKey = rowCalculatorLabelKey;
		_colCalculatorLabelKey = colCalculatorLabelKey;
	}
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
	public String getCellInfo () {
		StringBuilder sb = new StringBuilder("");
		sb.append("Row: ");
		if (_rowKeys == null || _rowKeys.size() == 0) {
			sb.append("Grand Total");
		} else {
			for (Object key : _rowKeys) {
				sb.append(key).append(" - ");
			}
		}
		sb.append("\nColumn: ");
		if (_colKeys == null || _colKeys.size() == 0) {
			sb.append("Grand Total");
		} else {
			for (Object key : _colKeys) {
				sb.append(key).append(" - ");
			}
		}
		sb.append("\nData: ")
			.append(_dataFieldName)
			.append("\nRow Calculator: ")
			.append(_rowCalculatorLabelKey == null? "Data" : _rowCalculatorLabelKey)
			.append("\nColumn Calculator: ")
			.append(_colCalculatorLabelKey == null? "Data" : _rowCalculatorLabelKey)
			.append("\n");
		return sb.toString();
	}
}