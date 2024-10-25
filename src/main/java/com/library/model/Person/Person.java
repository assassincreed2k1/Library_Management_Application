package com.library.model.Person;

public class Person {
    private String name;
    private String address;
    private String gender;
    private String dateOfBirth;
    private String phoneNumber;

    /**
     * constructor Person no param
     */
    public Person() {
        name = "";
        address = "";
        gender = "";
        dateOfBirth = "";
        phoneNumber = "";
    }

    /**
     * constructor for person who has all information.
     * @param name is String
     * @param address is String
     * @param gender is String
     * @param dateOfBirth is String
     * @param phoneNumber is String
     */
    public Person(String name, String address, String gender, String dateOfBirth, String phoneNumber) { 
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * return String will all information of Person.
     * @return is String
     */
    public String getDetails() {
        return String.format("Name: %s\nGender: %s\nAddress: %s\nPhone Number: %s\n",
                            name, gender, address, phoneNumber);
    }


}
