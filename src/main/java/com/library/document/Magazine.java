package com.library.document;

/**
 * The {@code Magazine} class represents a type of {@link Document}.
 * @param publisher The publisher of the magazine (e.g., Nhi Dong publisher,
 *                  TechWorld publisher).
 * @param genre     The genre of the magazine (e.g., science, entertainment,
 *                  fashion).
 */
public class Magazine extends Document {
    private String publisher;
    private String genre;

    /**
     * Default constructor for the {@code Magazine} class.
     * Initializes the publisher and genre fields with empty values.
     */
    public Magazine() {
        super();
        this.publisher = "";
        this.genre = "";
    }

    /**
     * Constructs a {@code Magazine} with the specified name, group, quantity,
     * publisher, and genre.
     * 
     * @param name      The name of the magazine.
     * @param group     The group or category the magazine belongs to.
     * @param quantity  The number of copies of the magazine.
     * @param publisher The publisher of the magazine.
     * @param genre     The genre of the magazine.
     */
    public Magazine(String name, String group, int quantity, String publisher, String genre) {
        super(name, group, quantity);
        this.publisher = publisher;
        this.genre = genre;
    }

    /**
     * Gets the publisher of this magazine.
     * 
     * @return The publisher of the magazine.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher of this magazine.
     * 
     * @param publisher The new publisher to set.
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets the genre of this magazine.
     * 
     * @return The genre of the magazine.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of this magazine.
     * 
     * @param genre The new genre to set.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }
}
