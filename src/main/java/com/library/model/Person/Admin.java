package com.library.model.Person;

public class Admin extends Librarian {
    final private String adminId = "A0000000001";
    final private String password = "01010101";
    
    public Admin() {
        super();
    }

    public Admin(String name, String address, String gender, String dateOfBirth, 
                 String phoneNumber, int employeeId, String position, String password) {
        super(name, address, gender, dateOfBirth, phoneNumber, employeeId, position, password);
    }
}
