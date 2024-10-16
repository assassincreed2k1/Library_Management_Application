package com.library.model.doc;

class Document {
    private static long idCounter = 0; // Static long integer generate id
    private String id; 
    private String name;
    private String group;
    private boolean isAvailable;

    public Document() {
        this.ID = generateID();
        this.name = "No name";
        this.group = "Default";
        this.isAvailable = false;
    }

    public Document(String name, String group, int quantity) {
        this.ID = generateID(); 
        this.name = name;
        this.group = group;
        this.isAvailable = true;
    }

    // ID Auto generate, Example: 000000001, 012345678,...
    private String generateID() {
        idCounter++;
        return String.format("%09d", idCounter);
    }

    // Getter / setter
    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }
}
