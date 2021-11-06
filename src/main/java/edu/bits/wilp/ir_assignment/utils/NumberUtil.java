package edu.bits.wilp.ir_assignment.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtil {
    public static double roundDouble(double value, int places) {
        DecimalFormat df = new DecimalFormat("#." + "#".repeat(places));
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(value));
    }
}
