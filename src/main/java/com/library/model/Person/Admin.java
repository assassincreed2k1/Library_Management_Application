package com.library.model.Person;

import com.library.service.AdminManagement;

/**
 * Represents an admin user in the library system, extending from the Librarian class.
 * The Admin class is responsible for managing administrative tasks such as updating
 * admin information and retrieving admin details from the database.
 */
public class Admin extends Librarian {
    private AdminManagement adminManagament;

    /**
     * Constructor to create a copy of an existing Admin object.
     * 
     * @param admin The Admin object to copy information from.
     */
    public Admin(Admin admin) {
        super(admin.name, admin.address, admin.gender, admin.dateOfBirth, admin.phoneNumber, admin.position);
    }

    /**
     * Default constructor to initialize the fixed values for the System Admin.
     * Sets the name, address, gender, date of birth, phone number, position, password, 
     * and employee ID for the system admin.
     */
    public Admin() {
        super("admin", "N/A", "N/A", "N/A", "N/A", "System Admin");
        this.password = "admin";
        this.employeeId = "A000001";
        adminManagament = new AdminManagement();
    }

    /**
     * Updates the admin information in the system using the AdminManagement service.
     */
    public void updateAdmin() {
        adminManagament.updateAdmin(this);
    }

    /**
     * Retrieves the admin information from the database.
     * 
     * @return An Admin object containing the retrieved admin details, or null if no admin is found.
     */
    public Admin getInforFromDatabase() {
        Admin adminFromDB = adminManagament.getAdminInfo();

        if (adminFromDB == null) {
            System.out.println("Can't get Admin from database.");
            return null;
        }

        return adminFromDB;
    }
}
