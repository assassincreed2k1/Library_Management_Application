package com.library.model.doc;

import com.library.service.NewsPaperManagement;

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

    private NewsPaperManagement newsManagament = new NewsPaperManagement();

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
     * @param source   The source of the newspaper.
     * @param region   The region where the newspaper is published.
     */
    public Newspaper(String id, String name, String group, String source, String region) {
        super(id, name, group);
        this.source = source;
        this.region = region;
    }

    /**
     * Constructs a {@code Newspaper} with the specified name, group, quantity,
     * available source, and region.
     * @param id ID of the newspaper.
     * @param name The name of the newpaper.
     * @param group The group of the newpaper.
     * @param isAvailable The available of the newpaper.
     * @param source The source of the newpaper.
     * @param region The region of the newpaper.
     */
    public Newspaper(String id, String name, String group, boolean isAvailable, String source, String region) {
        super(id, name, group, isAvailable);
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

    @Override
    public String getDetails() {
        return String.format("%s\nSource: %s\nRegion: 5s\n", 
                            super.getDetails(), this.source, this.region);
    }

    public Newspaper getInforFromDatabase(String id) {
        Newspaper newsFromDB = newsManagament.getDocument(id);

        if (newsFromDB == null) {
            System.out.println("Can't find this book in database");
        } 
        return newsFromDB;
    }
}
