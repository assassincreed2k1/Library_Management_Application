package com.library.model.Person;

public class Librarian extends Person {
    private String employeeId;
    private String position;

    /**
     * COnstructor for Librarian with all information
     * @param name
     * @param address
     * @param gender
     * @param dateOfBirth
     * @param phoneNumber
     * @param email
     * @param employeeId ID cua nguoi thu thu
     * @param position chuc vu cua thu thu
     */

    public Librarian(){
        this.employeeId = "";
        this.position = "";
    }

    public Librarian(String name, String address, String gender, String dateOfBirth, String phoneNumber, String email, String employeeId, String position){
        super(name, address, gender, dateOfBirth, phoneNumber, email);
        this.employeeId = employeeId;
        this.position = position;
    }

    public String getEmployeeId(){
        return employeeId;
    }

    public void setEmployeeId(String employeeId){
        this.employeeId = employeeId;
    }

    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position = position;
    }

    /**
     * Override getDetails to include librarian-specific info
     * @return String with all details
     */
    @Override
    public String getDetails() {
        return String.format("%sEmployee ID: %s\nPosition: %s\n",
        super.getDetails(), employeeId, position);
    }
}

