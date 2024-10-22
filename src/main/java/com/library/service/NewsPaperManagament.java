package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.library.model.doc.Newspaper;

public class NewsPaperManagament extends LibraryService {

    public NewsPaperManagament() {
        // Create listNewspaper
        createList("CREATE TABLE IF NOT EXISTS Newspaper ("
        + "id VARCHAR(255) PRIMARY KEY, "
        + "name VARCHAR(255), "
        + "newsGroup VARCHAR(50), "
        + "source VARCHAR(255), "
        //+ "category VARCHAR(255), "
        + "region VARCHAR(255), "
        + "isAvailable BOOLEAN)");
    }


    public void addDocuments(Newspaper newspaper) {
        String sql_statement = "INSERT INTO Newspaper " 
                + "(id, name, newsGroup, source, region, isAvailable) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(super.url);
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
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeDocument(Newspaper newspaper) {
        String sql_stmt = "DELETE FROM Newspaper WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url); 
                PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, newspaper.getID());
            pstmt.executeUpdate();
            addDeletedID(newspaper.getID());
            System.out.println("Data deleted successfully!");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
