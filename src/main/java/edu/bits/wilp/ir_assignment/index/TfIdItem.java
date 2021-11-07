package edu.bits.wilp.ir_assignment.index;

/**
 * Represents a POJO that is used when iterating over {@link TfIdf}. This is more readable than a
 * {@link java.util.Map.Entry} instance. Also, JDK doesn't have Tuple-n classes for easy manipulation.
 */
public class TfIdItem {
    private final String token;
    private final int docId;
    private final double tfId;

    public TfIdItem(String token, int docId, double tfId) {
        this.token = token;
        this.docId = docId;
        this.tfId = tfId;
    }

    public String getToken() {
        return token;
    }

    public int getDocId() {
        return docId;
    }

    public double getTfId() {
        return tfId;
    }
}
