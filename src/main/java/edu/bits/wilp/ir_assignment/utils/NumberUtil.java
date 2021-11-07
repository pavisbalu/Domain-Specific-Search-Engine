package edu.bits.wilp.ir_assignment.utils;

import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.util.Decimal64;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

public class NumberUtil {
    /**
     * Round a double value to specified number of places.
     *
     * @param value  Double number
     * @param places number of decimal places to retun
     * @return double value that's set to required number of places
     */
    public static double roundDouble(double value, int places) {
        DecimalFormat df = new DecimalFormat("#." + "#".repeat(places));
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(value));
    }

    /**
     * Compute the Cosine Similarity between two vectors.
     * <pre>
     *     cos-sim(l, r) = dot(l,r) / (norm(l) * norm(r))
     * </pre>
     *
     * @return double number representing similarity. value is between 0 and 1.
     */
    // cos-sim(l, r) = dot(l,r) / (norm(l) * norm(r))
    public static double cosineSim(FieldVector<Decimal64> left, FieldVector<Decimal64> right) {
        Decimal64 dot = left.dotProduct(right);
        Decimal64 productOfNorm = norm(left).multiply(norm(right));

        return dot.divide(productOfNorm).doubleValue();
    }

    /**
     * Computes the <a href="https://mathworld.wolfram.com/FrobeniusNorm.html">Frobenius norm or Euclidean norm</a>
     * of a given vector.
     *
     * @return {@link Decimal64} value representing the norm.
     */
    public static Decimal64 norm(FieldVector<Decimal64> vector) {
        FieldVector<Decimal64> squares = vector.ebeMultiply(vector);
        Decimal64[] fieldElements = squares.toArray();
        Decimal64 sum = Arrays.stream(fieldElements).reduce(Decimal64.ZERO, Decimal64::add);
        return sum.sqrt();
    }

}
