package edu.bits.wilp.ir_assignment.search;

/**
 * POJO representing the final output that's returned from {@link Searcher} instance.
 */
public class OutputRank {
    private final int docId;
    private final double cosineSim;
    private final String document;

    public OutputRank(int docId, double cosineSim, String document) {
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
        return "OutputRank{" +
                "docId=" + docId +
                ", cosineSim=" + cosineSim +
                ", document='" + document + '\'' +
                '}';
    }
}
