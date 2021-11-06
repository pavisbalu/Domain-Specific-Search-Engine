package edu.bits.wilp.ir_assignment.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtil {
    public static double roundDouble(double value, int places) {
        DecimalFormat df = new DecimalFormat("#." + "#".repeat(places));
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(value));
    }

    // Ref - http://www.java2s.com/example/java-utility-method/cosine-similarity/cosinesim-double-a-double-b-e9dbc.html
    public static double cosineSim(double[] a, double[] b) {
        if (a == null || b == null || a.length < 1 || b.length < 1 || a.length != b.length)
            return Double.NaN;

        double sum = 0.0, sum_a = 0, sum_b = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
            sum_a += a[i] * a[i];
            sum_b += b[i] * b[i];
        }

        double val = Math.sqrt(sum_a) * Math.sqrt(sum_b);

        return sum / val;
    }

}
