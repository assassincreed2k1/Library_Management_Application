package com.library.model.Person;

import com.library.service.LibrarianManagement;

public class Librarian extends Person {
    private int employeeId; 
    private String position;  
    private String password; 

    private LibrarianManagement libManagement = new LibrarianManagement();

    /**
     * Default constructor for Librarian with no initial values
     */
    public Librarian() {
        this.employeeId = 0; 
        this.position = "";   
        this.password = "";   
    }

    /**
     * Constructor for Librarian with all information.
     * 
     * @param name        The name of the librarian
     * @param address     The address of the librarian
     * @param gender      The gender of the librarian
     * @param dateOfBirth The date of birth of the librarian
     * @param phoneNumber The phone number of the librarian
     * @param employeeId  The unique ID for the librarian
     * @param position    The position of the librarian
     * @param password    The password for the librarian's account
     */
    public Librarian(String name, String address, String gender, String dateOfBirth, 
                    String phoneNumber, int employeeId, String position, String password) {
        super(name, address, gender, dateOfBirth, phoneNumber);
        this.employeeId = employeeId; 
        this.position = position;       
        this.password = password;       
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
        this.password = "00001111"; //default password, u can change if u like       
    }

    public int getEmployeeId() {
        return employeeId; 
    }

    public void setEmployeeId(int employeeId) {
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
        return String.format("%sEmployee ID: L%09d\nPosition: %s\n",
                super.getDetails(), employeeId, position);
    }

    /**
     * add librarian to database.
     */
    public void addLibrarian() {
        libManagement.addLibrarian(this);
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
    public Librarian getInforFromDatabase(int employeeId) {
        Librarian librarianFromDB = libManagement.getLibrarianInfo(employeeId);

        if (librarianFromDB == null) {
            System.out.println("No librarian found with ID : " + employeeId);
            return null;
        } 

        return librarianFromDB;
    }
}
