package com.library.document;

/**Magazine extends Document
 * 
 */
public class Magazine extends Document {
    private String Author;
    
    // Getter/setter for Author
    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        this.Author = author;
    }

    public Magazine() {
        super();
        this.Author = "";
    }

    /**Constructor for Magazine */
    public Magazine(String name, String group, int quantity, String author) {
        super(name, group, quantity);
        this.Author = author;
    }

}
