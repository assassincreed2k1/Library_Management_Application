package com.library.Person;

public class Person {
    private String name;
    private String address;
    private String gender;
    private String dateOfBirth;
    private String phoneNumber;
    private String email;

    /**
     * constructor for person who has all information.
     * @param name is String
     * @param address is String
     * @param gender is String
     * @param dateOfBirth is String
     * @param phoneNumber is String
     * @param email is String
     */
    public Person(String name, String address, String gender, String dateOfBirth, String phoneNumber, String email) { 
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * constructor for person who just has phoneNumber or email.
     * @param name
     * @param address
     * @param gender
     * @param dateOfBirth
     * @param phoneNumber
     */
    public Person(String name, String address, String gender, String dateOfBirth, String phoneNum_email) { 
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        if (phoneNum_email.charAt(0) >= '0' && phoneNum_email.charAt(0) <= '9') {
            this.phoneNumber = phoneNum_email;
        } else {
            this.email = phoneNum_email;
        }
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * return String will all information of Person.
     * @return is String
     */
    public String getDetails() {
        return String.format("Name: %s\nGender: %s\nAddress: %s\nPhone Number: %s\nEmail: %s\n",
                            name, gender, address, phoneNumber, email);
    }


}
