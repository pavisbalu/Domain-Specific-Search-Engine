package edu.bits.wilp.ir_assignment.index;

public class TfIdfKey {
    public int docId;
    public int tokenAsVocabId;

    public TfIdfKey(int docId, int tokenAsVocabId) {
        this.docId = docId;
        this.tokenAsVocabId = tokenAsVocabId;
    }
}
