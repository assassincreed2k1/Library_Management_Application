package com.library.model.doc;

/**
 * Represents a document in the library system.
 * Each document has a unique ID, a name, a group, a quantity, and availability
 * status.
 */
class Document {
    private static long idCounter = 0; // Static long integer to generate ID
    private String ID;
    private String Name;
    private String Group;
    private int Quantity;
    private boolean Available;

    /**
     * Default constructor that initializes the document with default values.
     * The ID is auto-generated, and the default values are set for name, group, and
     * quantity.
     */
    public Document() {
        this.ID = generateID();
        this.Name = "No name";
        this.Group = "Default";
        this.Quantity = 0;
        this.Available = false;
    }

    /**
     * Constructs a Document with the specified name, group, and quantity.
     *
     * @param name     the name of the document
     * @param group    the group of the document
     * @param quantity the quantity of the document
     */
    public Document(String name, String group, int quantity) {
        this.ID = generateID();
        this.Name = name;
        this.Group = group;
        this.Quantity = quantity;
        this.Available = quantity > 0;
    }

    /**
     * Generates a unique ID for the document in the format of 9 digits (e.g.,
     * 000000001).
     *
     * @return a string representing the unique ID
     */
    private String generateID() {
        idCounter++;
        return String.format("%09d", idCounter);
    }

    /**
     * Returns the unique ID of the document.
     *
     * @return the document's ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Returns the name of the document.
     *
     * @return the document's name
     */
    public String getName() {
        return Name;
    }

    /**
     * Sets the name of the document.
     *
     * @param name the new name of the document
     */
    public void setName(String name) {
        this.Name = name;
    }

    /**
     * Returns the group of the document.
     *
     * @return the document's group
     */
    public String getGroup() {
        return Group;
    }

    /**
     * Sets the group of the document.
     *
     * @param group the new group of the document
     */
    public void setGroup(String group) {
        this.Group = group;
    }

    /**
     * Returns the quantity of the document.
     *
     * @return the document's quantity
     */
    public int getQuantity() {
        return Quantity;
    }

    /**
     * Sets the quantity of the document.
     *
     * @param quantity the new quantity of the document
     */
    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    /**
     * Returns the availability status of the document.
     *
     * @return true if the document is available, false otherwise
     */
    public boolean isAvailable() {
        return Available;
    }
}
