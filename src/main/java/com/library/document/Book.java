package com.library.document;

class Book extends Document {
    private String IBSN;
    private String Author;

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

    public Book(String name, String group, String ID, String ibsn, String author) {
        
    }
}