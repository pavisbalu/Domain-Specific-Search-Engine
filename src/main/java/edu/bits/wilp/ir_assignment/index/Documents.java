package edu.bits.wilp.ir_assignment.index;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for List&lt;{@link Document}&gt; for Kryo
 */
public class Documents {
    private final List<Document> documentOlds;

    // for kryo
    private Documents() {
        this(new ArrayList<>());
    }

    public Documents(List<Document> documentOlds) {
        this.documentOlds = documentOlds;
    }

    public List<Document> getDocuments() {
        return documentOlds;
    }
}
