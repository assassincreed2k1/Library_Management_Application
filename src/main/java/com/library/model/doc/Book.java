package com.library.model.doc;

/**
 * Class Book
 * 
 * @param Name     Name of book (title, Example: )
 * @param Group    Group (Example: "TextBook", "Novel", "PictureBook", ...)
 * @param ID       ID (Example: "000000001")
 * @param author   Author of the book
 * @param publishDate  Publish date of the book
 */
public class Book extends Document {
    private String isbn;
    private String author;
    private String publishDate; 

    /**
     * Default Book:
     * 
     * @param Name       "No name"
     * @param Group      "Default" (genre)
     * @param ID         "Invalid"
     * @param isbn       ""
     * @param author     "None"
     * @param publishDate "Unknown Date"
     */
    public Book() {
        // Call constructor Document()
        super();
        this.isbn = "";
        this.author = "None";
        this.publishDate = "Unknown Date"; 
    }

    /**
     * Book:
     * 
     * @param Name       Name of the book
     * @param Group      Group of the book
     * @param ID         ID of the book
     * @param isbn       ISBN of the book
     * @param author     Author of the book
     * @param publishDate Publish date of the book
     */
    public Book(String name, String group, String isbn, String author, String publishDate) {
        super(name, group);
        this.isbn = isbn;
        this.author = author;
        this.publishDate = publishDate; 
    }

    public Book(String id, String name, String group, String isbn, String author, String publishDate) {
        super(id, name, group);
        this.isbn = isbn;
        this.author = author;
        this.publishDate = publishDate;
    }

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
    
    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
