package com.library.model.Person;

public class Admin extends Librarian {
    //instance => only one object
    public static final Admin INSTANCE = new Admin("admin", "Vietnam", "Male", 
                                                   "2024-20-11", "1234567890", 
                                                   "A000001", "System Admin");

    //private in order to prevent create modify admin infor
    private Admin(String name, String address, String gender, String dateOfBirth, 
                  String phoneNumber, String employeeId, String position) {
        super(name, address, gender, dateOfBirth, phoneNumber, employeeId, position);
    }
}
