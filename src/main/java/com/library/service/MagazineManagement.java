package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.library.model.doc.Magazine;

public class MagazineManagement extends LibraryService {

    public MagazineManagement() {
        // Create listMagazines
        super.createList("CREATE TABLE IF NOT EXISTS Magazines ("
                        + "id VARCHAR(255) PRIMARY KEY, "
                        + "name VARCHAR(255), "
                        + "magazineGroup VARCHAR(50), "
                        + "publisher VARCHAR(255), "
                        + "isAvailable BOOLEAN)");
    }


    /**
     * Adds a new {@link Magazine} to the library's book collection.
     * 
     * @param mgz The new magazine to add.
     */
    public void addDocuments(Magazine mgz) {
        String sql_statement = "INSERT INTO Magazines "
                + "(id, name, magazineGroup, publisher, isAvailable) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(super.url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, mgz.getID());
            pstmt.setString(2, mgz.getName());
            pstmt.setString(3, mgz.getGroup());
            pstmt.setString(4, mgz.getPublisher());
            pstmt.setBoolean(5, mgz.getIsAvailable());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateDocuments(Magazine magazine) {
        String sql_stmt = "UPDATE Magazines SET "
                + "name = ?, "
                + "magazineGroup = ?, "
                + "publisher = ?, "
                + "isAvailable = ? "
                + "WHERE id = ?";

        String group = magazine.getGroup();
        String publisher = magazine.getPublisher();
        boolean isAvailable = magazine.getIsAvailable();

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, magazine.getName());
            pstmt.setString(2, group);
            pstmt.setString(3, publisher);
            pstmt.setBoolean(4, isAvailable);
            pstmt.setString(5, magazine.getID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }   

    public void removeDocument(Magazine magazine) {
        String sql_statement = "DELETE FROM Magazines WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, magazine.getID());
            pstmt.executeUpdate();
            System.out.println("Data deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
