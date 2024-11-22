package com.library.model.Person;

public class Person {
    private String name;
    private String address;
    private String gender;
    private String dateOfBirth;
    private String phoneNumber;
    private String password;

    /**
     * Constructor Person no param
     */
    public Person() {
        name = "";
        address = "";
        gender = "";
        dateOfBirth = "";
        phoneNumber = "";
        password = "123456"; //default
    }

    /**
     * Constructor for person who has all information.
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
        this.password = "123456";
    }

    /**
     * Constructor for person who has all information.
     * @param name is string
     * @param address is string
     * @param gender is string
     * @param dateOfBirth is string
     * @param phoneNumber is string
     * @param password is string
     */
    public Person(String name, String address, String gender, String dateOfBirth, String phoneNumber, String password) {
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return String with all information of Person (excluding password for security).
     * @return is String
     */
    public String getDetails() {
        return String.format("Name: %s\nGender: %s\nAddress: %s\nPhone Number: %s\n",
                            name, gender, address, phoneNumber);
    }
}
