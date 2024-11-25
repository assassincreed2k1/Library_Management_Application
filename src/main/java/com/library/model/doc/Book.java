package com.library.model.doc;

import com.library.service.BookManagement;

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
    private BookManagement bookManagement = new BookManagement();

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
     * Default Book with ID and avalid.
     * @param id ID
     * @param isAvailable avalid.
     */
    public Book(String id, boolean isAvailable) {
        super(id, "Unknown", "Unknown", isAvailable); // Gọi constructor của Document với giá trị mặc định cho name và group
        this.isbn = "";
        this.author = "Unknown";
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

    /**
     * Book.
     * 
     * @param id ID of the book.
     * @param name name of the book.
     * @param group group of book.
     * @param isAvailable Available of book.
     * @param isbn ISBN of the book.
     * @param author Author of the book.
     * @param publishDate PublishDate of the book.
     */
    public Book(String id, String name, String group, boolean isAvailable, String isbn, String author, String publishDate) {
        super(id, name, group, isAvailable); // Gọi constructor của Document
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

    @Override
    public String getDetails() {
        return String.format("%s\nISBN: %s\nAuthor: %s\nPublish date: %s\n",
                                    super.getDetails(), this.isbn, this.author, this.publishDate);
    }

    public String getDetailsReview() {
        return String.format("Information of Book:\nName: %s\nISBN: %s\nAuthor: %s\nGenre: %s\n"
                            + "Publish date: %s\n",
                            this.name, this.isbn, this.author, this.group, this.publishDate);
    }

    public Book getInforFromDatabase(String id) {
        System.out.println(id);
        Book bookFromDB = bookManagement.getDocument(id);

        if (bookFromDB == null) {
            System.out.println("Can't find this book in database");
        } 
        return bookFromDB;
    }
}
