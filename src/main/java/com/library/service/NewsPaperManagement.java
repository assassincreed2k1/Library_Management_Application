package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.library.model.doc.Newspaper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NewsPaperManagement extends LibraryService {

    public NewsPaperManagement() {
        // Create listNewspaper
        createList("CREATE TABLE IF NOT EXISTS Newspaper ("
                + "id VARCHAR(255) PRIMARY KEY, "
                + "name VARCHAR(255), "
                + "newsGroup VARCHAR(50), "
                + "source VARCHAR(255), "
                // + "category VARCHAR(255), "
                + "region VARCHAR(255), "
                + "isAvailable BOOLEAN)");
    }

    public void addDocuments(Newspaper newspaper) {
        String sql_statement = "INSERT INTO Newspaper "
                + "(id, name, newsGroup, source, region, isAvailable) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, newspaper.getID());
            pstmt.setString(2, newspaper.getName());
            pstmt.setString(3, newspaper.getGroup());
            pstmt.setString(4, newspaper.getSource());
            pstmt.setString(5, newspaper.getRegion());
            pstmt.setBoolean(6, newspaper.getIsAvailable());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateDocuments(Newspaper newspaper) {
        String sql_stmt = "UPDATE Newspaper SET "
                + "name = ?, "
                + "newsGroup = ?, "
                + "source = ?, "
                + "region = ?, "
                + "isAvailable = ? "
                + "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, newspaper.getName());
            pstmt.setString(2, newspaper.getGroup());
            pstmt.setString(3, newspaper.getSource());
            pstmt.setString(4, newspaper.getRegion());
            pstmt.setBoolean(5, newspaper.getIsAvailable());
            pstmt.setString(6, newspaper.getID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeDocument(Newspaper newspaper) {
        String sql_stmt = "DELETE FROM Newspaper WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, newspaper.getID());
            pstmt.executeUpdate();
            System.out.println("Data deleted successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves all newspapers from the database and returns them as an
     * ObservableList.
     * This method executes a SQL query to fetch all records from the "Newspapers"
     * table,
     * creates Newspaper objects, and populates them with data from the database.
     * The resulting list of Newspaper objects is then returned.
     *
     * @return An ObservableList containing all the newspapers from the database.
     */
    public ObservableList<Newspaper> getAllNewspapers() {
        ObservableList<Newspaper> newspaperList = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM Newspaper"; // SQL query to fetch all records from the "Newspapers" table

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Newspaper newspaper = new Newspaper();
                newspaper.setID(rs.getString("id"));
                newspaper.setName(rs.getString("name"));
                newspaper.setGroup(rs.getString("newsGroup"));
                newspaper.setSource(rs.getString("source"));
                newspaper.setRegion(rs.getString("region"));
                newspaper.setImagePreview("");
                newspaper.setIsAvailable(rs.getBoolean("isAvailable"));
                newspaperList.add(newspaper);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return newspaperList;
    }

    /**
     * Retrieves a specific newspaper by its ID from the database.
     *
     * @param id The ID of the newspaper to retrieve.
     * @return The Newspaper object with the specified ID, or null if not found.
     */
    public Newspaper getDocument(String id) {
        String sql_statement = "SELECT * FROM Newspaper WHERE id = ?";
        Newspaper newspaper = null;

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                newspaper = new Newspaper();
                newspaper.setID(rs.getString("id"));
                newspaper.setName(rs.getString("name"));
                newspaper.setGroup(rs.getString("newsGroup"));
                newspaper.setSource(rs.getString("source"));
                newspaper.setRegion(rs.getString("region"));
                newspaper.setImagePreview("");
                newspaper.setIsAvailable(rs.getBoolean("isAvailable"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return newspaper;
    }

    public void setUrl(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUrl'");
    }
}
