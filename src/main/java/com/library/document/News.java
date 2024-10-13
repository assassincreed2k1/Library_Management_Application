package com.library.document;

/**
 * News
 * 
 * @param
 */
public class News extends Document {
    private String source;
    private String category;
    private String region;

    public News() {
        super();
    }

    public News(String name, String group, int quantity, String source, String category, String region) {
        super(name, group, quantity);
        this.source = source;
        this.category = category;
        this.region = region;
    }

}
