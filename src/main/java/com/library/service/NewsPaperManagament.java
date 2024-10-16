package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.library.model.doc.Newspaper;

public class NewsPaperManagament extends LibraryService {
    public void addDocuments(Newspaper newspaper) {
        String sql_statement = "INSERT INTO Magazines " 
                + "(id, name, group, source, region, isAvailable) "
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

    }

    public void removeDocument(Newspaper newspaper) {

    }

}
