package com.library.document;


/**Class Document
 * @param name Name
 * @param group Group
 * @param id ID
 */
class Document {
    private String Name;
    private String Group;
    private String ID;
    private int Quantity;
    private boolean Available;
    
    /**Getter/Setter for 
     * @param Name
     * @return Document.Name */
    public String getName() { 
        return Name;
    }

    /**Getter/Setter for 
     * @param Name */
    public void setName(String name) {
        this.Name = name;
    }
    
    /**Getter/Setter for 
     * @param Group
     * @return Document.Group */
    public String getGroup() {
        return Group;
    }

    /**Getter/Setter for 
     * @param Group */
    public void setGroup(String group) {
        this.Group = group;
    }

    /**Getter/Setter for 
     * @param ID
     * @return Document.ID */
    public String getID() {
        return ID;
    }
    
    /**Getter/Setter for 
     * @param ID */
    public void setID(String id) {
        this.ID = id;
    }

    /**Getter/Setter for 
     * @param Quantity */
    public int getQuantity() {
        return Quantity;
    }

    /**Getter/Setter for 
     * @param Quantity */
    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    /**Default Document:
     * @param Name "No name"
     * @param Group "Default"
     * @param ID "Invalid"
     */
    public Document() {
        this.Name = "No name";
        this.Group = "Default";
        this.ID = "Invalid";
        this.Quantity = 0;
        this.Available = false;
    }

    /**Document:
     * @param Name String
     * @param Group String
     * @param ID String 
     * @param Quantity int
     */
    public Document(String name, String group, String id, int Quantity) {
        this.Name = name;
        this.Group = group;
        this.ID = id;
        this.Quantity = Quantity;
        if (this.Quantity > 0) {
            this.Available = true;
        } else {
            this.Available = false;
        }
    }

    public boolean isAvailable() {
        return Available;
    }

};