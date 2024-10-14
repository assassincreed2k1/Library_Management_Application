package com.library.document;

/**
 * News
 * 
 * @param source Source of News: Eg: Cong An Nhan Dan Newspapers
 * @param category Category of News: Eg: 
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

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCategory(String category) {
        return this.category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}
