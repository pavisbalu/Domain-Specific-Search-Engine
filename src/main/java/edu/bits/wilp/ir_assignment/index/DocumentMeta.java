package edu.bits.wilp.ir_assignment.index;

import java.util.Map;
import java.util.Set;

public class DocumentMeta {
    private final Map<String, Double> tf;
    public final int docId;
    public final String text;

    public DocumentMeta(Map<String, Double> tf, int docId, String text) {
        this.tf = tf;
        this.docId = docId;
        this.text = text;
    }

    public Set<String> tokens() {
        return tf.keySet();
    }

    public Double tf(String token) {
        return tf.getOrDefault(token, 0.0);
    }
}
