package edu.bits.wilp.ir_assignment.crawler;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

public class Data {
    private final String url;
    private final String title;
    private final Map<String, String> fields;
    private final String description;

    public Data(String url, String title, Map<String, String> fields, String description) {
        this.url = url;
        this.title = title;
        this.fields = fields;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("url", url)
                .append("title", title)
                .append("fields", fields)
                .append("description", description)
                .toString();
    }
}
