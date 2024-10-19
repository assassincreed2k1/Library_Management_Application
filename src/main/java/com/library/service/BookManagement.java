package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.library.model.doc.Book;

public class BookManagement extends LibraryService {
    public BookManagement() {
        super.createList("CREATE TABLE IF NOT EXISTS Books ("
                        + "id VARCHAR(255) PRIMARY KEY, "
                        + "name VARCHAR(255), "
                        + "bookGroup VARCHAR(50), "
                        + "author VARCHAR(255), "
                        + "isAvailable BOOLEAN)");
    }

    /**
     * Adds a new {@link Book} to the library's book collection.
     * 
     * @param book The new book to add.
     */
    public void addDocuments(Book book) {
        String sql_statement = "INSERT INTO Books "
                + "(id, name, bookGroup, author, isAvailable) "
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
                + "bookGroup = ?, "
                + "author = ?, "
                + "isAvailable = ? "
                + "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getGroup());
            pstmt.setString(3, book.getAuthor());
            pstmt.setBoolean(4, book.getIsAvailable());
            pstmt.setString(5, book.getID());
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

    /**Get Document */
    public Book getDocument(String id) {
        String sql_statement = "SELECT * FROM Books WHERE id = ?";
        Book book = null;

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String bookId = rs.getString("id");
                String name = rs.getString("name");
                String group = rs.getString("bookGroup");
                String author = rs.getString("author");
                boolean isAvailable = rs.getBoolean("isAvailable");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return book;
    }
}
