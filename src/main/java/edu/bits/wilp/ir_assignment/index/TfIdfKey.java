package edu.bits.wilp.ir_assignment.index;

import java.util.Objects;

/**
 * Represents the Tuple in {@link TfIdf}
 */
public class TfIdfKey {
    private final int docId;
    private final int tokenAsVocabId;

    // for kryo
    private TfIdfKey() {
        this(0, 0);
    }

    public TfIdfKey(int docId, int tokenAsVocabId) {
        this.docId = docId;
        this.tokenAsVocabId = tokenAsVocabId;
    }

    public int getDocId() {
        return docId;
    }

    public int getTokenAsVocabId() {
        return tokenAsVocabId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TfIdfKey tfIdfKey = (TfIdfKey) o;
        return docId == tfIdfKey.docId && tokenAsVocabId == tfIdfKey.tokenAsVocabId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(docId, tokenAsVocabId);
    }
}
