package com.library.model.Person;

import com.library.service.LibrarianManagement;

public class Librarian extends Person {
    protected String employeeId; 
    protected String position;  

    private LibrarianManagement libManagement = new LibrarianManagement();

    /**
     * Default constructor for Librarian with no initial values
     */
    public Librarian() {
        this.employeeId = ""; 
        this.position = "";   
    }


    /**
     * Constructor for Librarian with all information except password.
     * 
     * @param name        The name of the librarian
     * @param address     The address of the librarian
     * @param gender      The gender of the librarian
     * @param dateOfBirth The date of birth of the librarian
     * @param phoneNumber The phone number of the librarian
     * @param employeeId  The unique ID for the librarian
     * @param position    The position of the librarian
     */
    public Librarian(String name, String address, String gender, String dateOfBirth, 
                    String phoneNumber, String employeeId, String position) {
        super(name, address, gender, dateOfBirth, phoneNumber);
        this.employeeId = employeeId; 
        this.position = position;       
    }

    /**
     * Constructor for Librarian 
     * @param name
     * @param address
     * @param gender
     * @param dateOfBirth
     * @param phoneNumber
     * @param employeeId
     * @param password
     * @param position
     */
    public Librarian(String name, String address, String gender, String dateOfBirth, 
                    String phoneNumber, String employeeId, String password, String position) {
        super(name, address, gender, dateOfBirth, phoneNumber, password);
        this.employeeId = employeeId; 
        this.position = position;       
    }

    /**
     * Constructor for Librarian when adding.
     * 
     * @param name        The name of the librarian
     * @param address     The address of the librarian
     * @param gender      The gender of the librarian
     * @param dateOfBirth The date of birth of the librarian
     * @param phoneNumber The phone number of the librarian
     * @param position    The position of the librarian
     */
    public Librarian(String name, String address, String gender, String dateOfBirth, 
                    String phoneNumber, String position) {
        super(name, address, gender, dateOfBirth, phoneNumber);
        this.position = position;    
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
    public boolean addLibrarian() {
        return libManagement.addLibrarian(this);
    }

    /**
     * delete this librarian from database.
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

    /**
     * Retrieves librarian information from the database by employee ID.
     * 
     * @param employeeId ID of the librarian to retrieve.
     * @return Librarian retrieved from the database or null if not found.
     */
    public Librarian getInforFromDatabase(String employeeId) {
        Librarian librarianFromDB = libManagement.getLibrarianInfo(employeeId);

        if (librarianFromDB == null) {
            System.out.println("No librarian found with ID : " + employeeId);
            return null;
        } 

        return librarianFromDB;
    }
}
