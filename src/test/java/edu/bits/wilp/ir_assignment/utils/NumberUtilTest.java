package edu.bits.wilp.ir_assignment.utils;

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
}