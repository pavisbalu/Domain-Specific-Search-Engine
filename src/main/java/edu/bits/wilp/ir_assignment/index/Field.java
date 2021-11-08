package edu.bits.wilp.ir_assignment.index;

public class Field {
    private final String key;
    private final String value;
    private final boolean isIndexed;

    // for kryo
    private Field(boolean isIndexed) {
        this("", "", false);
    }

    public Field(String key, String value, boolean isIndexed) {
        this.key = key;
        this.value = value;
        this.isIndexed = isIndexed;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
