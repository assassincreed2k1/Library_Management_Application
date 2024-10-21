package com.library.model.doc;

/**
 * Class Book
 * 
 * @param Name     Name of book (title, Example: )
 * @param Group    Group (Example: "TextBook", "Novel", "PictureBook", ...)
 * @param ID       ID (Example: "000000001")
 * @param author
 */
public class Book extends Document {
    private String ibsn;
    private String author;

    /**
     * Default Book:
     * 
     * @param Name   "No name"
     * @param Group  "Default"
     * @param ID     "Invalid"
     * @param ibsn   ""
     * @param author "None"
     */
    public Book() {
        // Call constructor Document()
        super();
        this.ibsn = "";
        this.author = "None";
    }

    /**
     * Book:
     * 
     * @param Name
     * @param Group
     * @param ID
     * @param IBSN
     * @param author
     */
    public Book(String name, String group, String ibsn, String author) {
        super(name, group);
        this.ibsn = ibsn;
        this.author = author;
    }

    public Book(String id, String name, String group, String ibsn, String author) {
        super(id, name, group);
        this.ibsn = ibsn;
        this.author = author;
    }

    // Getter setter for IBSN and author
    public String getIBSN() {
        return ibsn;
    }

    public void setIBSN(String ibsn) {
        this.ibsn = ibsn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
}