package com.library.model.document;

class Document {
    private static long idCounter = 0; // Static long integer generate id
    private String ID; 
    private String Name;
    private String Group;
    private boolean Available;

    public Document() {
        this.ID = generateID();
        this.Name = "No name";
        this.Group = "Default";
        this.Available = false;
    }

    public Document(String name, String group, int quantity) {
        this.ID = generateID(); 
        this.Name = name;
        this.Group = group;
        this.Available = true;
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
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        this.Group = group;
    }

    public void setAvailable(boolean isAvailable) {
        this.Available = isAvailable;
    }

    public boolean getAvailable() {
        return Available;
    }
}
