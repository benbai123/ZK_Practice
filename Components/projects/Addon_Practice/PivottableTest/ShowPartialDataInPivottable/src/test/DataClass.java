package test;

public class DataClass {
	String _rowOne;
	String _rowTwo;
	String _rowThree;

	String _columnOne;
	String _columnTwo;
	String _columnThree;

	int _dataOne;
	int _dataTwo;
	int _dataThree;

	public DataClass (String rowOne, String rowTwo, String rowThree,
					String columnOne, String columnTwo, String columnThree,
					int dataOne, int dataTwo, int dataThree) {
		_rowOne = rowOne;
		_rowTwo = rowTwo;
		_rowThree = rowThree;
		_columnOne = columnOne;
		_columnTwo = columnTwo;
		_columnThree = columnThree;
		_dataOne = dataOne;
		_dataTwo = dataTwo;
		_dataThree = dataThree;
	}
	public String getRowOne() {
		return _rowOne;
	}
	public void setRowOne(String _rowOne) {
		this._rowOne = _rowOne;
	}
	public String getRowTwo() {
		return _rowTwo;
	}
	public void setRowTwo(String _rowTwo) {
		this._rowTwo = _rowTwo;
	}
	public String getRowThree() {
		return _rowThree;
	}
	public void setRowThree(String _rowThree) {
		this._rowThree = _rowThree;
	}
	public String getColumnOne() {
		return _columnOne;
	}
	public void setColumnOne(String _columnOne) {
		this._columnOne = _columnOne;
	}
	public String getColumnTwo() {
		return _columnTwo;
	}
	public void setColumnTwo(String _columnTwo) {
		this._columnTwo = _columnTwo;
	}
	public String getColumnThree() {
		return _columnThree;
	}
	public void setColumnThree(String _columnThree) {
		this._columnThree = _columnThree;
	}
	public int getDataOne() {
		return _dataOne;
	}
	public void setDataOne(int _dataOne) {
		this._dataOne = _dataOne;
	}
	public int getDataTwo() {
		return _dataTwo;
	}
	public void setDataTwo(int _dataTwo) {
		this._dataTwo = _dataTwo;
	}
	public int getDataThree() {
		return _dataThree;
	}
	public void setDataThree(int _dataThree) {
		this._dataThree = _dataThree;
	}
}