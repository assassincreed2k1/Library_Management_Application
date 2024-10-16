package com.library.model.doc;

/**
 * The {@code Newspaper} class represents a type of {@link Document}.
 * 
 * @param source The source of the newspaper (e.g., Cong An Nhan Dan
 *               Newspaper).
 * @param region The region of publication for the newspaper (e.g., National,
 *               International).
 */
public class Newspaper extends Document {
    private String source;
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
     * source, and region.
     * 
     * @param name     The name of the newspaper.
     * @param group    The group or category the newspaper belongs to.
     * @param quantity The number of copies of the newspaper.
     * @param source   The source of the newspaper.
     * @param region   The region where the newspaper is published.
     */
    public Newspaper(String name, String group, String source, String region) {
        super(name, group);
        this.source = source;
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
