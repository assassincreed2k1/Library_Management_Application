package com.library.model.doc;

/**
 * The {@code Magazine} class represents a type of {@link Document}.
 * 
 * @param publisher The publisher of the magazine (e.g., Nhi Dong publisher,
 *                  TechWorld publisher).
 */
public class Magazine extends Document {
    private String publisher;

    /**
     * Default constructor for the {@code Magazine} class.
     * Initializes the publisher field with an empty value.
     */
    public Magazine() {
        super();
        this.publisher = "";
    }

    /**
     * Constructs a {@code Magazine} with the specified name, group, quantity,
     * and publisher.
     * 
     * @param name      The name of the magazine.
     * @param group     The group or category the magazine belongs to.
     * @param quantity  The number of copies of the magazine.
     * @param publisher The publisher of the magazine.
     */
    public Magazine(String id, String name, String group, String publisher) {
        super(id, name, group);
        this.publisher = publisher;
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
}
