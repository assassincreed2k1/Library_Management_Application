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
    /**
     * Creates a table for librarians. If it already exists, it will not create a new one.
     */
    public LibrarianManagement() {
        super.createList("CREATE TABLE IF NOT EXISTS Librarian ("
                        + "employeeId INT PRIMARY KEY AUTO_INCREMENT, "
                        + "name VARCHAR(255), "
                        + "address varchar(255), "
                        + "dateOfBirth date,  "
                        + "phoneNumber varchar(11),  "
                        + "gender varchar(6),  "
                        + "position varchar(255), "
                        + "password VARCHAR(255) "
                        + ");  ");
    }

    /**
     * Adds a librarian to the database.
     * 
     * @param lib Librarian
     */
    public void addLibrarian(Librarian lib) {
        String sql_statement = "INSERT INTO Librarian "
                + "(employeeId, name, address, dateOfBirth, phoneNumber, gender, position, password) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Date birth = null;
        if (lib.getDateOfBirth() != null) {
            birth = DateString.toSqlDate(lib.getDateOfBirth());
        }

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, lib.getEmployeeId());
            pstmt.setString(2, lib.getName());
            pstmt.setString(3, lib.getAddress());
            pstmt.setDate(4, birth);
            pstmt.setString(5, lib.getPhoneNumber());
            pstmt.setString(6, lib.getGender());
            pstmt.setString(7, lib.getPosition());
            pstmt.setString(8, lib.getPassword());
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates librarian information in the database.
     * 
     * @param lib Librarian
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
                        + "WHERE employeeId = ?;";

        Date birth = null;
        if (lib.getDateOfBirth() != null) {
            birth = DateString.toSqlDate(lib.getDateOfBirth());
        }

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, lib.getName());
            pstmt.setString(2, lib.getAddress());
            pstmt.setDate(3, birth);
            pstmt.setString(4, lib.getPhoneNumber());
            pstmt.setString(5, lib.getGender());
            pstmt.setString(6, lib.getPosition());
            pstmt.setString(7, lib.getPassword());
            pstmt.setString(8, lib.getEmployeeId());
            pstmt.executeUpdate();
            System.out.println("Librarian updated successfully");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Removes a librarian from the database.
     * 
     * @param employeeId String
     */
    public void removeLibrarian(String employeeId) {
        String sql_statement = "DELETE FROM Librarian WHERE employeeId = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, employeeId);
            pstmt.executeUpdate();
            System.out.println("Librarian deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves librarian information from the database.
     * 
     * @param employeeId employee ID string
     * @return Librarian
     */
    public Librarian getLibrarianInfo(String employeeId) {
        String sql_statement = "SELECT * FROM Librarian WHERE employeeId = ?";
        Librarian lib = null;

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                lib = new Librarian();
                lib.setEmployeeId(rs.getString("employeeId"));
                lib.setName(rs.getString("name"));
                lib.setAddress(rs.getString("address"));
                if (rs.getDate("dateOfBirth") != null) {
                    lib.setDateOfBirth(rs.getDate("dateOfBirth").toString());
                }
                lib.setPhoneNumber(rs.getString("phoneNumber"));
                lib.setGender(rs.getString("gender"));
                lib.setPosition(rs.getString("position"));
                lib.setPassword(rs.getString("password")); // Retrieve the password
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return lib;
    }
}
