package com.library.model.doc;

/**
 * The {@code Document} class represents a document with an ID, name, group,
 * availability status, and an image preview.
 */
class Document {
    private String id;
    private String name;
    private String group;
    private boolean isAvailable;
    private String imagePreview;

    /**
     * Default constructor for the {@code Document} class.
     * Initializes the document with an empty ID, default name ("No name"),
     * default group ("Default"), sets availability to false, and initializes
     * an empty image preview.
     */
    public Document() {
        this.id = "";
        this.name = "No name";
        this.group = "Default";
        this.isAvailable = false;
        this.imagePreview = "";
    }

    /**
     * Constructs a {@code Document} with the specified name and group,
     * and sets the availability status to true. The ID is initialized as empty,
     * and the image preview is not set.
     *
     * @param name  The name of the document.
     * @param group The group or category the document belongs to.
     */
    public Document(String name, String group) {
        this.id = "";
        this.name = name;
        this.group = group;
        this.isAvailable = true;
    }

    /**
     * Constructs a {@code Document} with the specified ID, name, and group,
     * and sets the availability status to true. The image preview is not set.
     *
     * @param id    The unique identifier of the document.
     * @param name  The name of the document.
     * @param group The group or category the document belongs to.
     */
    public Document(String id, String name, String group) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.isAvailable = true;
    }

    /**
     * Sets the ID of this document.
     *
     * @param id The ID to set for this document.
     */
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
     * @param name The new name to set for this document.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the group of this document.
     *
     * @return The group or category of the document.
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the group of this document.
     *
     * @param group The new group to set for this document.
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Checks if the document is available.
     *
     * @return {@code true} if the document is available, {@code false} otherwise.
     */
    public boolean getIsAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of this document.
     *
     * @param isAvailable {@code true} if the document is available, {@code false} otherwise.
     */
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * Gets the image preview path or URL for this document.
     *
     * @return The image preview path or URL.
     */
    public String getImagePreview() {
        return imagePreview;
    }

    /**
     * Sets the image preview path or URL for this document.
     *
     * @param imagePreview The new image preview path or URL to set.
     */
    public void setImagePreview(String imagePreview) {
        this.imagePreview = imagePreview;
    }
}
