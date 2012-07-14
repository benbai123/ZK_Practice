package test;

import java.math.BigDecimal;
import java.util.Comparator;

import org.zkoss.pivot.PivotHeaderNode;
import org.zkoss.pivot.impl.TabularPivotModel;

/**
 * ** Added **
 *  the comparator for sort row header node
 *  Tested with ZK 6.0.1 CE and ZK Pivottable 2.0.0
 */
public class ColumnHeaderComparator implements Comparator<PivotHeaderNode> {
	private TabularPivotModel _pivotModel; // the data model
	private PivotHeaderNode _columnNodeToSort; // the column node to sort
	private int _fieldIndexToSort; // the field index of data fields to sort
	private int _rowLevelToSort; // the row level to sort
	private boolean _ascending; // sort direction

	/**
	 * Constructor
	 * @param pivotModel TabularPivotModel, The pivottable's model
	 * @param columnNodeToSort PivotHeaderNode, The column to sort
	 * @param fieldIndexToSort int, The index of the field under the columnNodeToSort to sort
	 * @param rowLevelToSort int, The level of row header node to sort
	 * @param ascending boolean, true: sort ascending, false: sort descending
	 */
	public ColumnHeaderComparator (TabularPivotModel pivotModel, PivotHeaderNode columnNodeToSort,
			int fieldIndexToSort, int rowLevelToSort, boolean ascending) {
		_pivotModel = pivotModel;
		_columnNodeToSort = columnNodeToSort;
		_fieldIndexToSort = fieldIndexToSort;
		_rowLevelToSort = rowLevelToSort;
		_ascending = ascending;
	}
	/**
	 * compare two node
	 */
	public int compare (PivotHeaderNode n1, PivotHeaderNode n2) {
		int result = 0;
		// get the level of two node
		int l1 = n1.getDepth();
		int l2 = n2.getDepth();

		// only compare if node is at sort level
		if (l1 == _rowLevelToSort
			&& l2 == _rowLevelToSort) {
			// get the values and compare
			Number v1 = _pivotModel.getValue(n1, -1, _columnNodeToSort, -1, _fieldIndexToSort);
			Number v2 = _pivotModel.getValue(n2, -1, _columnNodeToSort, -1, _fieldIndexToSort);
			result = (_ascending? 1 : -1 ) * (new BigDecimal(v1.toString()).compareTo(new BigDecimal(v2.toString())));
		}

		return result;
	}
}
