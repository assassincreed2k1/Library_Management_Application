package com.library.model.doc;

import com.library.service.MagazineManagement;

/**
 * The {@code Magazine} class represents a type of {@link Document}.
 * 
 * @param publisher The publisher of the magazine (e.g., Nhi Dong publisher,
 *                  TechWorld publisher).
 */
public class Magazine extends Document {
    private String publisher;
    private MagazineManagement magazineManagement = new MagazineManagement();

    /**
     * Default constructor for the {@code Magazine} class.
     * Initializes the publisher field with an empty value.
     */
    public Magazine() {
        super();
        this.publisher = "";
    }

    /**
     * Constructs a {@code Magazine} with the specified ID and availability status.
     * Initializes other fields with default values.
     * 
     * @param id          The ID of the magazine.
     * @param isAvailable The availability status of the magazine.
     */
    public Magazine(String id, boolean isAvailable) {
        super(id, "Unknown", "Unknown", isAvailable); // Gọi constructor của Document với giá trị mặc định cho name và group
        this.publisher = "Unknown";
    }

    /**
     * Constructs a {@code Magazine} with the specified name, group, quantity,
     * and publisher.
     * 
     * @param name      The name of the magazine.
     * @param group     The group or category the magazine belongs to.
     * @param publisher The publisher of the magazine.
     */
    public Magazine(String id, String name, String group, String publisher) {
        super(id, name, group);
        this.publisher = publisher;
    }

    /**
     * Construcs a code Magazine with the spwcified id, name, group, available, publisher.
     * 
     * @param id ID of the magazine.
     * @param name the name of the magazine.
     * @param group the group or categogy the magazine belongs to.
     * @param isAvailable the available of the magazine.
     * @param publisher the publisher of the magazine.
     */
    public Magazine(String id, String name, String group, Boolean isAvailable, String publisher) {
        super(id, name, group, isAvailable);
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

    @Override
    public String getDetails() {
        return String.format("%s\n Publisher: %s\n",
                            super.getDetails(), this.publisher);
    }

    public Magazine getInforFromDatabase(String id) {
        Magazine magazineFromDB = magazineManagement.getDocument(id);

        if (magazineFromDB == null) {
            System.out.println("Can't find this book in database");
        } 
        return magazineFromDB;
    }
}
