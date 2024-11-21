package com.library.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.library.model.Person.Admin;
import com.library.model.helpers.DateString;

public class AdminManagement extends LibraryService {

    /**
     * Constructor for AdminManagement class.
     */
    public AdminManagement() {
        super.createList("CREATE TABLE IF NOT EXISTS Admin ("
                + "employeeId CHAR(7) PRIMARY KEY, "
                + "name VARCHAR(255), "
                + "address VARCHAR(255), "
                + "dateOfBirth DATE, "
                + "phoneNumber VARCHAR(11), "
                + "gender VARCHAR(6), "
                + "position VARCHAR(255), "
                + "password VARCHAR(255)"
                + ");");
    }

    /**
     * Retrieves admin information from the database.
     * 
     * @return The Admin object containing the admin's information, or null if not found.
     */
    public Admin getAdminInfo() {
        //check xem có bản ghi nào bên trong CSDL không
        if (!hasAdminRecords()) {
            defaultAdd();;
        }
        String sql_statement = "SELECT * FROM Admin LIMIT 1";

        Admin admin = null;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                admin = new Admin();
                admin.setEmployeeId(rs.getString("employeeId"));
                admin.setName(rs.getString("name"));
                admin.setAddress(rs.getString("address"));
                admin.setDateOfBirth(rs.getDate("dateOfBirth") != null ? rs.getDate("dateOfBirth").toString() : null);
                admin.setPhoneNumber(rs.getString("phoneNumber"));
                admin.setGender(rs.getString("gender"));
                admin.setPosition(rs.getString("position"));
                admin.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching admin info: " + e.getMessage());
            e.printStackTrace();
        }

        return admin;
    }

    /**
     * Updates the admin's information in the database.
     * 
     * @param admin The Admin object containing the updated details.
     */
    public void updateAdmin(Admin admin) {
        if (admin == null || admin.getEmployeeId() == null) {
            System.err.println("Invalid admin object or employee ID.");
            return;
        }

        //check xem có bản ghi nào bên trong CSDL không
        if (!hasAdminRecords()) {
            defaultAdd();
        }

        String sql_stmt = "UPDATE Admin SET "
                + "name = ?, "
                + "address = ?, "
                + "dateOfBirth = ?, "
                + "phoneNumber = ?, "
                + "gender = ?, "
                + "position = ?, "
                + "password = ? "
                + "WHERE employeeId = ?";

        Date birth = admin.getDateOfBirth() != null ? DateString.toSqlDate(admin.getDateOfBirth()) : null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {

            pstmt.setString(1, admin.getName());
            pstmt.setString(2, admin.getAddress());
            pstmt.setDate(3, birth);
            pstmt.setString(4, admin.getPhoneNumber());
            pstmt.setString(5, admin.getGender());
            pstmt.setString(6, admin.getPosition());
            pstmt.setString(7, admin.getPassword());
            pstmt.setString(8, admin.getEmployeeId());
            pstmt.executeUpdate();

            System.out.println("Admin updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating admin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // viet method default insert vao neu chua co gia tri nao ben trong

    /**
     * Inserts a default admin into the database if no records exist.
     */
    private void defaultAdd() {
        Admin admin = new Admin();
        String sql_stmt = "INSERT INTO Admin (employeeId, name, address, dateOfBirth, phoneNumber, gender, position, password) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {

            pstmt.setString(1, admin.getEmployeeId());
            pstmt.setString(2, admin.getName());
            pstmt.setString(3, admin.getAddress());
            pstmt.setDate(4, DateString.toSqlDate(admin.getDateOfBirth()));
            pstmt.setString(5, admin.getPhoneNumber());
            pstmt.setString(6, admin.getGender());
            pstmt.setString(7, admin.getPosition());
            pstmt.setString(8, admin.getPassword());

            pstmt.executeUpdate();
            System.out.println("Default admin added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding default admin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks if the Admin table has any records.
     * 
     * @return true if the table contains at least one record, false otherwise.
     */
    public boolean hasAdminRecords() {
        String sql_stmt = "SELECT COUNT(*) FROM Admin";
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql_stmt);
            ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking admin records: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


}
