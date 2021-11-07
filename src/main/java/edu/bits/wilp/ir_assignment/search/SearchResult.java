package edu.bits.wilp.ir_assignment.search;

import java.util.List;

/**
 * POJO representing the final output that's returned from {@link Searcher} instance.
 */
public class SearchResult {
    private final List<OutputRecord> records;
    private final double precision;
    private final double recall;

    public SearchResult(List<OutputRecord> records, double precision, double recall) {
        this.records = records;
        this.precision = precision;
        this.recall = recall;
    }

    public List<OutputRecord> getRecords() {
        return records;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }
}
