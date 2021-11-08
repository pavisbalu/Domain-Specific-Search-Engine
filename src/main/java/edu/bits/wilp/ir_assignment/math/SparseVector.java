package edu.bits.wilp.ir_assignment.math;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Decimal64;
import org.apache.commons.math3.util.OpenIntToFieldHashMap;

public class SparseVector {
    private final OpenIntToFieldHashMap<Decimal64> entries;
    /**
     * Dimension of the vector.
     */
    private final int virtualSize;

    public SparseVector(int dimension) {
        this.virtualSize = dimension;
        this.entries = new OpenIntToFieldHashMap<>(SparseMatrix.FIELD);
    }

    public Decimal64 dotProduct(SparseVector another) {
        Decimal64 res = SparseMatrix.FIELD.getZero();
        OpenIntToFieldHashMap<Decimal64>.Iterator iter = entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res = res.add(another.getEntry(iter.key()).multiply(iter.value()));
        }
        return res;
    }

    public SparseVector squared() {
        SparseVector squared = new SparseVector(virtualSize);
        OpenIntToFieldHashMap<Decimal64>.Iterator iter = entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            Decimal64 value = iter.value();
            squared.setEntry(key, value.multiply(getEntry(key)));
        }
        return squared;
    }

    public Decimal64 norm() {
        SparseVector squared = squared();
        Decimal64 sum = Decimal64.ZERO;
        OpenIntToFieldHashMap<Decimal64>.Iterator iter = squared.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            sum = sum.add(iter.value());
        }
        return sum.sqrt();
    }

    public Decimal64 getEntry(int pos) {
        checkDimensions(pos);
        return this.entries.get(pos);
    }

    public void setEntry(int pos, Decimal64 value) {
        checkDimensions(pos);
        if (value.equals(SparseMatrix.FIELD.getZero())) {
            this.entries.remove(pos);
        } else {
            this.entries.put(pos, value);
        }
    }

    private void checkDimensions(int index) {
        if (index < 0 || index >= dimension()) {
            throw new OutOfRangeException(LocalizedFormats.COLUMN_INDEX,
                    index, 0, dimension() - 1);
        }
    }

    private int dimension() {
        return virtualSize;
    }
}
