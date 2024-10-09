package com.library.document;

/**Class Book
 * @param Name book name (title, Example: )
 * @param Group Group (Example: "TextBook", "Novel", "PictureBook", ...)
 * @param ID ID (Example: "000000001")
 * @param Quantity Quantity (Example: 10)
 * @param Author
 */
public class Book extends Document {
    private String IBSN;
    private String Author;

    // Getter setter for IBSN and Author
    public String getIBSN() {
        return IBSN;
    }

    public void setIBSN(String ibsn) {
        this.IBSN = ibsn;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        this.Author = author;
    }

    /**Default Book:
     * @param Name "No name"
     * @param Group "Default"
     * @param ID "Invalid"
     * @param IBSN ""
     * @param Author "None"
     */
    public Book() {
        // Call constructor Document()
        super();
        this.IBSN = "";
        this.Author = "None";
    }

    /**Book:
     * @param Name
     * @param Group 
     * @param ID 
     * @param IBSN 
     * @param Author
     */
    public Book(String name, String group, int quantity, String ibsn, String author) {
        super(name, group, quantity);
        this.IBSN = ibsn;
        this.Author = author;
    }
    
    /**Get Book info
     * @return Name + Group + ID + IBSN + Author
    */
    public String getInfo() {
        return "<" + super.getName() + "><" + super.getGroup() + "><" 
        + super.getID() + "><" + IBSN + "><" + Author + ">";
    }

}