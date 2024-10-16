package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.library.model.doc.Book;

public class BookManagement extends LibraryService {
    /**
     * Adds a new {@link Book} to the library's book collection.
     * @param book The new book to add.
     */
    public void addDocuments(Book book) {
        String sql_statement = "INSERT INTO Books " 
                               + "(id, name, group, author, isAvailable) " 
                               + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url); 
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, book.getID());
            pstmt.setString(2, book.getName());
            pstmt.setString(3, book.getGroup());
            pstmt.setString(4, book.getAuthor());
            pstmt.setBoolean(5, book.getAvailable());
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**Update Book */
    public void updateDocuments(Book book) {

    }

        /**Remove Book */
        public void removeDocument(Book book) {
            String sql_statement = "DELETE FROM Books WHERE id = ?";
    
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
                pstmt.setString(1, book.getID());
                pstmt.executeUpdate();
                System.out.println("Data deleted successfully");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


}
