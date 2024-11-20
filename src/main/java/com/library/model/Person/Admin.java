package com.library.model.Person;

public class Admin extends Librarian {
    // Fixed fields for System Admin's ID and password
    final private String id;
    final private String password;

    /**
     * Default constructor to initialize the fixed values for System Admin
     */
    public Admin() {
        // Set fixed ID and password
        super("admin", "Vietnam", "Male", "2024-20-11", 
        "1234567890", "A000001", "System Admin");
        this.id = "A000001";
        this.password = "admin";
    }

    // Getters for id and password (no setters, since these are fixed)
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
