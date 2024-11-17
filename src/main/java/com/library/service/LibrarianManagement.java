package com.library.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.library.model.Person.Librarian;
import com.library.model.helpMethod.DateString;

public class LibrarianManagement extends LibraryService {
    private PersonIDManagement librarianIdManagement = new PersonIDManagement("LibrarianID");

    /**
     * Constructor for LibrarianManagement class. It initializes the Librarian table
     * in the database
     * if it doesn't already exist.
     */
    public LibrarianManagement() {
        super.createList("CREATE TABLE IF NOT EXISTS Librarian ("
                + "employeeId INTEGER PRIMARY KEY, "
                + "name VARCHAR(255), "
                + "address VARCHAR(255), "
                + "dateOfBirth DATE,  "
                + "phoneNumber VARCHAR(11),  "
                + "gender VARCHAR(6),  "
                + "position VARCHAR(255), "
                + "password VARCHAR(255) "
                + ");");
    }

    /**
     * Adds a new librarian to the database.
     * 
     * @param lib The Librarian object containing the librarian's details to be
     *            added.
     */
    public void addLibrarian(Librarian lib) {
        int newId = librarianIdManagement.getID(); // Lấy ID hiện tại từ PersonIDManagement
        String sql_statement = "INSERT INTO Librarian "
                + "(employeeId, name, address, dateOfBirth, phoneNumber, gender, position, password) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Date birth = lib.getDateOfBirth() != null ? DateString.toSqlDate(lib.getDateOfBirth()) : null;

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, newId); // Sử dụng ID từ PersonIDManagement
            pstmt.setString(2, lib.getName());
            pstmt.setString(3, lib.getAddress());
            pstmt.setDate(4, birth);
            pstmt.setString(5, lib.getPhoneNumber());
            pstmt.setString(6, lib.getGender());
            pstmt.setString(7, lib.getPosition());
            pstmt.setString(8, lib.getPassword());
            pstmt.executeUpdate();

            librarianIdManagement.increaseID(); // Tăng ID cho lần sử dụng tiếp theo
            System.out.println("Data inserted successfully with ID: " + newId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the details of an existing librarian in the database.
     * 
     * @param lib The Librarian object containing the updated details.
     */
    public void updateLibrarian(Librarian lib) {
        String sql_stmt = "UPDATE Librarian SET "
                + "name = ?, "
                + "address = ?, "
                + "dateOfBirth = ?, "
                + "phoneNumber = ?, "
                + "gender = ?, "
                + "position = ?, "
                + "password = ? "
                + "WHERE employeeId = ?";

        Date birth = lib.getDateOfBirth() != null ? DateString.toSqlDate(lib.getDateOfBirth()) : null;

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, lib.getName());
            pstmt.setString(2, lib.getAddress());
            pstmt.setDate(3, birth);
            pstmt.setString(4, lib.getPhoneNumber());
            pstmt.setString(5, lib.getGender());
            pstmt.setString(6, lib.getPosition());
            pstmt.setString(7, lib.getPassword());
            pstmt.setInt(8, lib.getEmployeeId());
            pstmt.executeUpdate();
            System.out.println("Librarian updated successfully");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Removes a librarian from the database by employee ID.
     * 
     * @param employeeId The ID of the librarian to be removed.
     */
    public void removeLibrarian(int employeeId) {
        String sql_statement = "DELETE FROM Librarian WHERE employeeId = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
            System.out.println("Librarian deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves librarian information based on employee ID from the database.
     * 
     * @param employeeId The ID of the librarian whose information is to be
     *                   retrieved.
     * @return A Librarian object containing the librarian's information, or null if
     *         not found.
     */
    public Librarian getLibrarianInfo(int employeeId) {
        String sql_statement = "SELECT * FROM Librarian WHERE employeeId = ?";
        Librarian lib = null;

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                lib = new Librarian();
                lib.setEmployeeId(rs.getInt("employeeId"));
                lib.setName(rs.getString("name"));
                lib.setAddress(rs.getString("address"));
                lib.setDateOfBirth(rs.getDate("dateOfBirth") != null ? rs.getDate("dateOfBirth").toString() : null);
                lib.setPhoneNumber(rs.getString("phoneNumber"));
                lib.setGender(rs.getString("gender"));
                lib.setPosition(rs.getString("position"));
                lib.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return lib;
    }

    /**
     * Checks if a librarian exists in the database with the provided ID and
     * password.
     * 
     * @param id       The employee ID of the librarian.
     * @param password The password of the librarian.
     * @return true if a librarian with the given ID and password exists, false
     *         otherwise.
     */
    public boolean checkLibrarian(int id, String password) {
        String sql_statement = "SELECT * FROM Librarian WHERE employeeId = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
