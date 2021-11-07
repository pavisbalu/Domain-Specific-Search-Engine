package edu.bits.wilp.ir_assignment.index;

import java.util.ArrayList;
import java.util.List;

public class Documents {
    private final List<Document> documents;

    // for kryo
    private Documents() {
        this(new ArrayList<>());
    }

    public Documents(List<Document> documents) {
        this.documents = documents;
    }

    public List<Document> getDocuments() {
        return documents;
    }
}
