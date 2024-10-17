package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.library.model.doc.Book;

public class BookManagement extends LibraryService {
    /**
     * Adds a new {@link Book} to the library's book collection.
     * 
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
            pstmt.setBoolean(5, book.getIsAvailable());
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Update Book */
    public void updateDocuments(Book book) {
        String sql_stmt = "UPDATE Books SET "
                + "name = ?, "
                + "group = ?, "
                + "author = ?, "
                + "isAvailable = ?, "
                + "WHERE id = ?";

        String name = book.getName();
        String group = book.getGroup();
        String author = book.getAuthor();
        boolean isAvailable = book.getIsAvailable();

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, name);
            pstmt.setString(2, group);
            pstmt.setString(3, author);
            pstmt.setBoolean(4, isAvailable);
            pstmt.setBoolean(5, isAvailable);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /** Remove Book */
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
