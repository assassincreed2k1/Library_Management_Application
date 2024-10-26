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
    private String isbn;
    private String author;

    /**
     * Default Book:
     * 
     * @param Name   "No name"
     * @param Group  "Default"
     * @param ID     "Invalid"
     * @param isbn   ""
     * @param author "None"
     */
    public Book() {
        // Call constructor Document()
        super();
        this.isbn = "";
        this.author = "None";
    }

    /**
     * Book:
     * 
     * @param Name
     * @param Group
     * @param ID
     * @param ISBN
     * @param author
     */
    public Book(String name, String group, String isbn, String author) {
        super(name, group);
        this.isbn = isbn;
        this.author = author;
    }

    public Book(String id, String name, String group, String isbn, String author) {
        super(id, name, group);
        this.isbn = isbn;
        this.author = author;
    }

    // Getter setter for ISBN and author
    public String getISBN() {
        return isbn;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
}