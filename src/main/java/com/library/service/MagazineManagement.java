package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.library.model.document.Magazine;

public class MagazineManagement extends Library2 {
    public void addDocuments(Magazine mgz) {
        String sql_statement = "INSERT INTO Magazines "
                               + "(id, name, group, publisher, genre, isAvailable) " 
                               + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(super.url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
                pstmt.setString(1, mgz.getID());
                pstmt.setString(2, mgz.getName());
                pstmt.setString(3, mgz.getGroup());
                pstmt.setString(4, mgz.getPublisher());
                pstmt.setString(5, mgz.getGenre());
                pstmt.setBoolean(6, mgz.getAvailable());
                pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

        /**Update Document
     * 
     */
    public void updateDocuments(Magazine mgz) {
        
    }

    public void removeDocument(Magazine mgz) {
        
    }
    
}
