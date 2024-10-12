package com.library.document;

/**Magazine extends Document
 * 
 */
public class Magazine extends Document {
    private String publisher;

    
    // Getter/setter for Author
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Magazine() {
        super();
        this.publisher = "";
    }

    /**Constructor for Magazine */
    public Magazine(String name, String group, int quantity, String publisher) {
        super(name, group, quantity);
        this.publisher = publisher;
    }

}
