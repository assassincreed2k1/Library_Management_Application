package com.library.document;

/**
 * Magazine extends Document
 * 
 * @param publisher Publisher (Example: Nhi Dong publisher,...)
 * @param genre     Genre (Example: science, entertainment, fashion,...)
 */
public class Magazine extends Document {
    private String publisher;
    private String genre;

<<<<<<< HEAD
    // Getter/setter for Publisher
=======
    
    // Getter/setter for Author
>>>>>>> 7daad81e065f6645a05ab0fabd1d5570ae378510
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    // Getter/setter for Genre
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Magazine() {
        super();
        this.publisher = "";
        this.genre = "";
    }

    /**Constructor for Magazine */
    public Magazine(String name, String group, int quantity, String publisher, String genre) {
        super(name, group, quantity);
        this.publisher = publisher;
        this.genre = genre;
    }

}
