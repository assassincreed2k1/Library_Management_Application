package com.library.model.Person;

public class Admin extends Librarian {
    public Admin() {
        super();
    }

    public Admin(String name, String address, String gender, String dateOfBirth, 
                 String phoneNumber, int employeeId, String position, String password) {
        super(name, address, gender, dateOfBirth, phoneNumber, employeeId, position, password);
    }
}
