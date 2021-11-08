package edu.bits.wilp.ir_assignment.utils;

import edu.bits.wilp.ir_assignment.math.SparseVector;
import org.apache.commons.math3.util.Decimal64;

import java.math.RoundingMode;
import java.text.DecimalFormat;

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
     * @param left  Vector 1
     * @param right Vector 2
     * @return double number representing similarity. value is between 0 and 1.
     */
    // cos-sim(l, r) = dot(l,r) / (norm(l) * norm(r))
    public static double cosineSim(SparseVector left, SparseVector right) {
        Decimal64 dot = left.dotProduct(right);
        Decimal64 productOfNorm = left.norm().multiply(right.norm());

        return dot.divide(productOfNorm).doubleValue();
    }
}
