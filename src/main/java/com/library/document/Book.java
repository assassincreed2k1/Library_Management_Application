package com.library.document;

class Book extends Document {
    private String IBSN;
    private String Author;

    // Getter setter for IBSN and Author
    String getIBSN() {
        return IBSN;
    }

    void setIBSN(String ibsn) {
        this.IBSN = ibsn;
    }

    String getAuthor() {
        return Author;
    }

    void setAuthor(String author) {
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

    public Book(String name, String group, String ID, int quantity, String ibsn, String author) {
        super(name, group, ID, quantity);
        this.IBSN = ibsn;
        this.Author = author;
    }
    
}