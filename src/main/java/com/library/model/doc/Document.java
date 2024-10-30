package com.library.model.doc;

/**
 * The {@code Document} class represents a document with an ID, name, group,
 * and availability status.
 */
class Document {
    private String id;
    private String name;
    private String group;
    private boolean isAvailable;

    /**
     * Default constructor for the {@code Document} class. Initializes the
     * document with a generated ID, default name, default group, and sets
     * it as not available.
     */
    public Document() {
        this.id = "";
        this.name = "No name";
        this.group = "Default";
        this.isAvailable = false;
    }

    /**
     * Constructs a {@code Document} with the specified name, group, and
     * sets the availability status to true.
     * 
     * @param name     The name of the document.
     * @param group    The group or category the document belongs to.
     * @param quantity The number of copies of the document (not used in this
     *                 context).
     */
    public Document(String name, String group) {
        this.id = "";
        this.name = name;
        this.group = group;
        this.isAvailable = true;
    }
    
    // for find or get doc
    public Document(String id, String name, String group) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.isAvailable = true;
    }

    public void setID(String id) {
        this.id = id;
    }
    /**
     * Gets the ID of this document.
     * 
     * @return The ID of the document.
     */
    public String getID() {
        return id;
    }

    /**
     * Gets the name of this document.
     * 
     * @return The name of the document.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this document.
     * 
     * @param name The new name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the group of this document.
     * 
     * @return The group of the document.
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the group of this document.
     * 
     * @param group The new group to set.
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Sets the availability status of this document.
     * 
     * @param isAvailable The new availability status to set.
     */
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * Gets the availability status of this document.
     * 
     * @return {@code true} if the document is available, {@code false} otherwise.
     */
    public boolean getIsAvailable() {
        return isAvailable;
    }
}
