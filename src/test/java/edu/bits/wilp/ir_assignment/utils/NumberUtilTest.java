package edu.bits.wilp.ir_assignment.utils;

import edu.bits.wilp.ir_assignment.math.SparseVector;
import org.apache.commons.math3.util.Decimal64;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NumberUtilTest {
    @Test
    public void testRoundTo4Places() {
        assertThat(NumberUtil.roundDouble(0.31231354312341234, 1), is(0.4));
        assertThat(NumberUtil.roundDouble(0.31231354312341234, 2), is(0.32));
        assertThat(NumberUtil.roundDouble(0.31231354312341234, 3), is(0.313));
        assertThat(NumberUtil.roundDouble(0.31231354312341234, 4), is(0.3124));
    }

    @Test
    public void testCosineSim() {
        SparseVector left = new SparseVector(5);
        SparseVector right = new SparseVector(5);

        left.setEntry(0, new Decimal64(3.0));
        right.setEntry(1, new Decimal64(12.0));
        double cosineSim = NumberUtil.cosineSim(left, right);
        assertThat(cosineSim, is(0.0));
    }

    @Test
    public void testNorm() {
        SparseVector left = new SparseVector(5);
        left.setEntry(0, new Decimal64(2.0));
        left.setEntry(1, new Decimal64(3.0));
        double norm = left.norm().doubleValue();
        assertThat(norm, is(3.605551275463989));
    }
}