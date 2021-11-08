package edu.bits.wilp.ir_assignment.math;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Decimal64;
import org.apache.commons.math3.util.Decimal64Field;

import java.util.HashMap;
import java.util.Map;

public class SparseMatrix {
    static final Decimal64Field FIELD = Decimal64Field.getInstance();
    private int rows;
    private int columns;
    private Map<Integer, SparseVector> rowEntries;

    public SparseMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.rowEntries = new HashMap<>();
    }

    public void setEntry(int row, int column, Decimal64 value) {
        checkRowIndex(row);
        checkColumnIndex(column);
        SparseVector columns = getRow(row);
        columns.setEntry(column, value);
        this.rowEntries.put(row, columns);
    }

    public int getColumnDimension() {
        return columns;
    }

    public int getRowDimension() {
        return rows;
    }

    public Decimal64 get(int row, int column) {
        checkRowIndex(row);
        checkColumnIndex(column);
        return getRow(row).getEntry(column);
    }

    public SparseVector getRow(int row) {
        checkRowIndex(row);
        return rowEntries.getOrDefault(row, new SparseVector(getColumnDimension()));
    }

    /**
     * Check if a row index is valid.
     *
     * @param row Row index to check.
     * @throws OutOfRangeException if {@code index} is not valid.
     */
    protected void checkRowIndex(final int row) throws OutOfRangeException {
        if (row < 0 || row >= getRowDimension()) {
            throw new OutOfRangeException(LocalizedFormats.ROW_INDEX,
                    row, 0, getRowDimension() - 1);
        }
    }

    /**
     * Check if a column index is valid.
     *
     * @param column Column index to check.
     * @throws OutOfRangeException if {@code index} is not valid.
     */
    protected void checkColumnIndex(final int column)
            throws OutOfRangeException {
        if (column < 0 || column >= getColumnDimension()) {
            throw new OutOfRangeException(LocalizedFormats.COLUMN_INDEX,
                    column, 0, getColumnDimension() - 1);
        }
    }
}
