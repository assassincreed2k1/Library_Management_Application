package com.library.model.doc;

/**
 * The {@code Newspaper} class represents a type of {@link Document}.
 * 
 * @param source   The source of the newspaper (e.g., Cong An Nhan Dan
 *                 Newspaper).
 * @param category The category of the newspaper (e.g., politics, sports,
 *                 entertainment).
 * @param region   The region of publication for the newspaper (e.g., National,
 *                 International).
 */
public class Newspaper extends Document {
    private String source;
    private String category;
    private String region;

    /**
     * Default constructor for the {@code Newspaper} class.
     * Initializes the {@code Newspaper} with default values.
     */
    public Newspaper() {
        super();
    }

    /**
     * Constructs a {@code Newspaper} with the specified name, group, quantity,
     * source, category, and region.
     * 
     * @param name     The name of the newspaper.
     * @param group    The group or category the newspaper belongs to.
     * @param quantity The number of copies of the newspaper.
     * @param source   The source of the newspaper.
     * @param category The category of the newspaper.
     * @param region   The region where the newspaper is published.
     */
    public Newspaper(String name, String group, int quantity, String source, String category, String region) {
        super(name, group, quantity);
        this.source = source;
        this.category = category;
        this.region = region;
    }

    /**
     * Gets the source of this newspaper.
     * 
     * @return The source of the newspaper.
     */
    public String getSource() {
        return this.source;
    }

    /**
     * Sets the source of this newspaper.
     * 
     * @param source The new source to set.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the category of this newspaper.
     * 
     * @param category The category of the newspaper.
     * @return The category of the newspaper.
     */
    public String getCategory(String category) {
        return this.category;
    }

    /**
     * Sets the category of this newspaper.
     * 
     * @param category The new category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the region where this newspaper is published.
     * 
     * @return The region of publication.
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * Sets the region where this newspaper is published.
     * 
     * @param region The new region to set.
     */
    public void setRegion(String region) {
        this.region = region;
    }
}
