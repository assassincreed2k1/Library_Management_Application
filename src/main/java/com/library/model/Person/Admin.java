package com.library.model.Person;

import com.library.service.AdminManagement;

public class Admin extends Librarian {
    private AdminManagement adminManagament;

    public Admin(Admin admin) {
        super(admin.name, admin.address, admin.gender, admin.dateOfBirth, admin.phoneNumber, admin.position);
    }

    /**
     * Default constructor to initialize the fixed values for System Admin
     */
    public Admin() {
        super("admin", "N/A", "N/A", "N/A", "N/A", "System Admin");
        this.password = "admin";
        this.employeeId = "A000001";
        adminManagament = new AdminManagement();
    }

    public void updateAdmin() {
        adminManagament.updateAdmin(this);
    }

    public Admin getInforFromDatabase() {
        Admin adminFromDB = adminManagament.getAdminInfo();

        if (adminFromDB == null) {
            System.out.println("Can't get Admin from database.");
            return null;
        } 

        return adminFromDB;
    }
}
