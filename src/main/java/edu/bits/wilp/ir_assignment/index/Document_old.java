package edu.bits.wilp.ir_assignment.index;

/**
 * Model that's persisted to disk. We store this file so while searching using the {@link TfIdf} model,
 * we can also retrieve the actual documents and present it to the users.
 */
public class Document_old {
    private final int docId;
    private final String text;

    // for kryo
    private Document_old() {
        this(0, "");
    }

    public Document_old(int docId, String text) {
        this.docId = docId;
        this.text = text;
    }

    public int getDocId() {
        return docId;
    }

    public String getText() {
        return text;
    }
}
