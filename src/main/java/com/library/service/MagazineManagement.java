package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.library.model.doc.Magazine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

        try (Connection conn = DriverManager.getConnection(url);
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

    /**
     * Retrieves all magazines from the database and returns them as an
     * ObservableList.
     * This method executes a SQL query to fetch all records from the "Magazines"
     * table,
     * creates Magazine objects, and populates them with data from the database.
     * The resulting list of Magazine objects is then returned.
     *
     * @return An ObservableList containing all the magazines from the database.
     */
    public ObservableList<Magazine> getAllMagazines() {
        ObservableList<Magazine> magazineList = FXCollections.observableArrayList();
        String sql_statement = "SELECT * FROM Magazines";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Magazine magazine = new Magazine();
                magazine.setID(rs.getString("id"));
                magazine.setName(rs.getString("name"));
                magazine.setGroup(rs.getString("magazineGroup"));
                magazine.setPublisher(rs.getString("publisher"));
                magazine.setImagePreview("");
                magazine.setIsAvailable(rs.getBoolean("isAvailable"));
                magazineList.add(magazine);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return magazineList;
    }

    /**
     * Retrieves a specific magazine by its ID from the database.
     *
     * @param id The ID of the magazine to retrieve.
     * @return The Magazine object with the specified ID, or null if not found.
     */
    public Magazine getDocument(String id) {
        String sql_statement = "SELECT * FROM Magazines WHERE id = ?";
        Magazine magazine = null;

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                magazine = new Magazine();
                magazine.setID(rs.getString("id"));
                magazine.setName(rs.getString("name"));
                magazine.setGroup(rs.getString("magazineGroup"));
                magazine.setPublisher(rs.getString("publisher"));
                magazine.setImagePreview("");
                magazine.setIsAvailable(rs.getBoolean("isAvailable"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return magazine;
    }

    public void setUrl(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUrl'");
    }

}
