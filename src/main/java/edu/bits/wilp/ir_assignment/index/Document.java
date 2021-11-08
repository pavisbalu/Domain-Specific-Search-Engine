package edu.bits.wilp.ir_assignment.index;

import java.util.*;

/**
 * Represents a document that would be indexed.
 * <em>indexFields</em> represents a list of key-value pairs that would be indexed and searched upon. While the
 * <em>nonIndexedFields</em> also contain key-value pairs we don't index based on them. These are usually metadata
 * that can be associated with the document but not used for indexing / searching. Eg: title, description would be
 * indexed fields, while latitude, longitude would be non-indexed.
 * <p>
 * For simplicity we're going to assume all the key-value pairs as strings.
 */
public class Document implements Iterable<Field> {
    private final Map<String, String> indexedFields;
    private final Map<String, String> nonIndexedFields;

    private int docId;
    private Map<String, Map<String, Double>> tfPerIndexedField;

    public Document() {
        this.indexedFields = new HashMap<>();
        this.nonIndexedFields = new HashMap<>();
        this.tfPerIndexedField = new HashMap<>();
    }

    public Document(Document another) {
        this(another.indexedFields, another.nonIndexedFields, another.docId, another.tfPerIndexedField);
    }

    private Document(Map<String, String> indexedFields, Map<String, String> nonIndexedFields, int docId, Map<String, Map<String, Double>> tfPerIndexedField) {
        this.indexedFields = indexedFields;
        this.nonIndexedFields = nonIndexedFields;
        this.docId = docId;
        this.tfPerIndexedField = tfPerIndexedField;
    }

    public int getDocId() {
        return docId;
    }

    public Document addIndexField(String key, String value) {
        if (nonIndexedFields.containsKey(key)) {
            throw new RuntimeException("field already exist as non-indexed");
        }
        this.indexedFields.put(key, value);
        return this;
    }

    public Document addNonIndexedField(String key, String value) {
        if (indexedFields.containsKey(key)) {
            throw new RuntimeException("field already exist as indexed");
        }
        this.nonIndexedFields.put(key, value);
        return this;
    }

    public int size() {
        return indexedFields.size() + nonIndexedFields.size();
    }

    public int nrOfIndexedFields() {
        return indexedFields.size();
    }

    @Override
    public Iterator<Field> iterator() {
        return new Iterator<>() {
            final Iterator<Map.Entry<String, String>> indexedIt = indexedFields.entrySet().iterator();
            final Iterator<Map.Entry<String, String>> nonIndexedIt = nonIndexedFields.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return indexedIt.hasNext() || nonIndexedIt.hasNext();
            }

            @Override
            public Field next() {
                if (indexedIt.hasNext()) {
                    Map.Entry<String, String> next = indexedIt.next();
                    return new Field(next.getKey(), next.getValue(), true);
                } else if (nonIndexedIt.hasNext()) {
                    Map.Entry<String, String> next = nonIndexedIt.next();
                    return new Field(next.getKey(), next.getValue(), false);
                }
                throw new NoSuchElementException("Iterator is empty");
            }
        };
    }

    public Set<String> indexedFields() {
        return indexedFields.keySet();
    }

    public String get(String fieldName) {
        return indexedFields.getOrDefault(fieldName, nonIndexedFields.get(fieldName));
    }

    public String getOrDefault(String fieldName, String defaultValue) {
        return indexedFields.getOrDefault(fieldName, nonIndexedFields.getOrDefault(fieldName, defaultValue));
    }

    Document setDocId(int docId) {
        this.docId = docId;
        return this;
    }

    Set<String> tokens(String field) {
        return tfPerIndexedField.get(field).keySet();
    }

    Double tf(String field, String token) {
        return tfPerIndexedField.get(field).getOrDefault(token, 0.0);
    }

    Document setTfPerIndexedField(String field, Map<String, Double> tfPerIndexedField) {
        this.tfPerIndexedField.put(field, tfPerIndexedField);
        return this;
    }
}
