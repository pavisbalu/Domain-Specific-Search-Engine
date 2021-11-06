package edu.bits.wilp.ir_assignment.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// We'll persist the instance of this class as the index file (aka) model
public class TfIdf {
    private final int N;
    private final List<String> vocab;
    private final Map<TfIdfKey, Double> tfIdfVectors;
    private final Map<String, Double> DF;

    public TfIdf(int totalDocuments, Map<String, Double> DF) {
        N = totalDocuments;
        vocab = new ArrayList<>();
        tfIdfVectors = new HashMap<>();
        this.DF = DF;
    }

    public void add(int docId, String token, double tfIdf) {
        int tokenAsVocabId = vocab.indexOf(token);
        if (tokenAsVocabId == -1) {
            vocab.add(token);
            tokenAsVocabId = vocab.indexOf(token);
        }
        tfIdfVectors.put(new TfIdfKey(docId, tokenAsVocabId), tfIdf);
    }

    public int indexOf(String token) {
        return vocab.indexOf(token);
    }

    public int N() {
        return N;
    }

    public int size() {
        return tfIdfVectors.size();
    }

    public double DF(String token) {
        return this.DF.getOrDefault(token, 0.0);
    }
}
