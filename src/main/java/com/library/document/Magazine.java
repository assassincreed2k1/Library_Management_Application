package com.library.document;

/**Magazine extends Document
 * 
 */
public class Magazine extends Document {
    private String Author;
    
    // Getter/setter for Author
    String getAuthor() {
        return Author;
    }

    void setAuthor(String author) {
        this.Author = author;
    }

    public Magazine() {
        super();
        this.Author = "";
    }

    public Magazine(String name, String group, String ID , int quantity, String author) {
        super(name, group, ID, quantity);
        this.Author = author;
    }



}
