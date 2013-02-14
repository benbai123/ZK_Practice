package test;

public class Position {
	private int _rowIdx = 0;
	private int _colIdx = 0;
	public Position (int rowIdx, int colIdx) {
		_rowIdx = rowIdx;
		_colIdx = colIdx;
	}
	public void increaseRow () {
		_rowIdx++;
	}
	public void increaseCol () {
		_colIdx++;
	}
	public void resetCol () {
		_colIdx = 0;
	}
	public int getRowIdx () {
		return _rowIdx;
	}
	public int getColIdx () {
		return _colIdx;
	}
}