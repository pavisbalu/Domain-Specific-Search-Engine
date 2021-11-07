package edu.bits.wilp.ir_assignment.search;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * POJO Representing the Individual Documents in the Output
 */
public class OutputRecord {
    private final int docId;
    private final double cosineSim;
    private final String document;

    public OutputRecord(int docId, double cosineSim, String document) {
        this.docId = docId;
        this.cosineSim = cosineSim;
        this.document = document;
    }

    public int getDocId() {
        return docId;
    }

    public double getCosineSim() {
        return cosineSim;
    }

    public String getDocument() {
        return document;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("docId", docId)
                .append("cosineSim", cosineSim)
                .append("document", document)
                .toString();
    }
}
