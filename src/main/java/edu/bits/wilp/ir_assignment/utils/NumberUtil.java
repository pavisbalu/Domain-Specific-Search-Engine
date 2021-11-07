package edu.bits.wilp.ir_assignment.utils;

import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.util.Decimal64;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

public class NumberUtil {
    public static double roundDouble(double value, int places) {
        DecimalFormat df = new DecimalFormat("#." + "#".repeat(places));
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(value));
    }

    // cos-sim(l, r) = dot(l,r) / (norm(l) * norm(r))
    public static double cosineSim(FieldVector<Decimal64> left, FieldVector<Decimal64> right) {
        Decimal64 dot = left.dotProduct(right);
        Decimal64 productOfNorm = norm(left).multiply(norm(right));

        return dot.divide(productOfNorm).doubleValue();
    }

    public static Decimal64 norm(FieldVector<Decimal64> row) {
        FieldVector<Decimal64> squares = row.ebeMultiply(row);
        Decimal64[] fieldElements = squares.toArray();
        Decimal64 sum = Arrays.stream(fieldElements).reduce(Decimal64.ZERO, Decimal64::add);
        return sum.sqrt();
    }

}
