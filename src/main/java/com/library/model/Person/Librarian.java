package com.library.model.Person;

import com.library.service.LibrarianManagement;

public class Librarian extends Person {
    private String employeeId; 
    private String position;  
    private String password; 

    private LibrarianManagement libManagement = new LibrarianManagement();

    /**
     * Default constructor for Librarian with no initial values
     */
    public Librarian() {
        this.employeeId = ""; 
        this.position = "";   
        this.password = "";   
    }

    /**
     * Constructor for Librarian with all information
     * 
     * @param name        The name of the librarian
     * @param address     The address of the librarian
     * @param gender      The gender of the librarian
     * @param dateOfBirth The date of birth of the librarian
     * @param phoneNumber The phone number of the librarian
     * @param email       The email of the librarian
     * @param employeeId  The unique ID for the librarian
     * @param position    The position of the librarian
     * @param password    The password for the librarian's account
     */
    public Librarian(String name, String address, String gender, String dateOfBirth, 
                    String phoneNumber, String employeeId, String position, String password) {
        super(name, address, gender, dateOfBirth, phoneNumber);
        this.employeeId = employeeId; 
        this.position = position;       
        this.password = password;       
    }

    public String getEmployeeId() {
        return employeeId; 
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId; 
    }

    public String getPosition() {
        return position; 
    }

    public void setPosition(String position) {
        this.position = position; 
    }

    public String getPassword() {
        return password; 
    }

    public void setPassword(String password) {
        this.password = password; 
    }

    /**
     * Override getDetails to include librarian-specific info
     * 
     * @return String containing all details about the librarian
     */
    @Override
    public String getDetails() {
        return String.format("%sEmployee ID: %s\nPosition: %s\n",
                super.getDetails(), employeeId, position);
    }

    /**
     * add librarian to database.
     */
    public void addLibrarian() {
        libManagement.addLibrarian(this);
    }

    /**
     * delete this librarian from dababase.
     */
    public void deleteLibrarian() {
        libManagement.removeLibrarian(this.employeeId);
    }

    /**
     * update new things in librarian in database.
     */
    public void updateLibrarian() {
        libManagement.updateLibrarian(this);
    }
}
