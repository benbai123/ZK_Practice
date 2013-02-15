package test;

/**
 * attributes used to denote a cell in current view of pivottable
 * and the index points to old value while
 * loop through values and build update script as needed
 * @author benbai123
 *
 */
public class Position {
	// index of current row in the view
	private int _rowIdx = 0;
	// index of current column in the view
	private int _colIdx = 0;
	// index to get stored value
	private int _ptr = 0;
	// constructor
	public Position (int rowIdx, int colIdx) {
		_rowIdx = rowIdx;
		_colIdx = colIdx;
	}
	// move to next row
	public void toNextRow () {
		increaseRow();
		resetCol();
	}
	// control
	public void increaseRow () {
		_rowIdx++;
	}
	public void increaseCol () {
		_colIdx++;
	}
	public void resetCol () {
		_colIdx = 0;
	}
	// getters
	public int getRowIdx () {
		return _rowIdx;
	}
	public int getColIdx () {
		return _colIdx;
	}
	public int getPtr () {
		_ptr ++;
		return _ptr - 1;
	}
}