package edu.bits.wilp.ir_assignment.search;

public class OutputRank {
    private final int docId;
    private final double cosineSim;

    public OutputRank(int docId, double cosineSim) {
        this.docId = docId;
        this.cosineSim = cosineSim;
    }

    public int getDocId() {
        return docId;
    }

    public double getCosineSim() {
        return cosineSim;
    }

    @Override
    public String toString() {
        return "OutputRank{" +
                "docId=" + docId +
                ", cosineSim=" + cosineSim +
                '}';
    }
}
