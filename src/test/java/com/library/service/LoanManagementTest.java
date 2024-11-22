package com.library.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.*;

import com.library.model.doc.Book;
import com.library.model.doc.Newspaper;
import com.library.model.doc.Magazine;

class LoanManagementTest {
    private static LoanManagement loanManagement;
    private static Connection connection;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Tạo cơ sở dữ liệu SQLite tạm
        connection = DriverManager.getConnection(LoanManagement.getUrl());
        try (Statement stmt = connection.createStatement()) {
            // Tạo bảng Books
            stmt.execute("CREATE TABLE IF NOT EXISTS Books (id TEXT PRIMARY KEY, isAvailable BOOLEAN)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Newspaper (id TEXT PRIMARY KEY, isAvailable BOOLEAN)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Magazines (id TEXT PRIMARY KEY, isAvailable BOOLEAN)");

            // Thêm dữ liệu mẫu
            stmt.execute("INSERT INTO Books (id, isAvailable) VALUES ('book1', 1)");
            stmt.execute("INSERT INTO Newspaper (id, isAvailable) VALUES ('newspaper1', 1)");
            stmt.execute("INSERT INTO Magazines (id, isAvailable) VALUES ('magazine1', 1)");
        }
        loanManagement = new LoanManagement();
    }

    @AfterAll
    static void cleanupDatabase() throws SQLException {
        // Đóng kết nối
        connection.close();
    }

    @Test
    void testBorrowBook_Success() {
        // Arrange
        Book book = new Book("book1", true);

        // Act
        loanManagement.borrowBook(book);

        // Assert
        boolean isAvailable = isBookAvailable("book1");
        assertFalse(isAvailable, "The book should be marked as not available.");
    }

    @Test
    void testReturnBook_Success() {
        // Arrange
        Book book = new Book("book1", false);

        // Act
        loanManagement.returnBook(book);

        // Assert
        boolean isAvailable = isBookAvailable("book1");
        assertTrue(isAvailable, "The book should be marked as available.");
    }

    @Test
    void testBorrowNewspaper_Success() {
        // Arrange
        Newspaper newspaper = new Newspaper("newspaper1", true);

        // Act
        loanManagement.borrowNewspaper(newspaper);

        // Assert
        boolean isAvailable = isNewspaperAvailable("newspaper1");
        assertFalse(isAvailable, "The newspaper should be marked as not available.");
    }

    @Test
    void testReturnNewspaper_Success() {
        // Arrange
        Newspaper newspaper = new Newspaper("newspaper1", false);

        // Act
        loanManagement.returnNewspaper(newspaper);

        // Assert
        boolean isAvailable = isNewspaperAvailable("newspaper1");
        assertTrue(isAvailable, "The newspaper should be marked as available.");
    }

    @Test
    void testBorrowMagazine_Success() {
        // Arrange
        Magazine magazine = new Magazine("magazine1", true);

        // Act
        loanManagement.borrowMagazine(magazine);

        // Assert
        boolean isAvailable = isMagazineAvailable("magazine1");
        assertFalse(isAvailable, "The magazine should be marked as not available.");
    }

    @Test
    void testReturnMagazine_Success() {
        // Arrange
        Magazine magazine = new Magazine("magazine1", false);

        // Act
        loanManagement.returnMagazine(magazine);

        // Assert
        boolean isAvailable = isMagazineAvailable("magazine1");
        assertTrue(isAvailable, "The magazine should be marked as available.");
    }

    // Helper methods to check availability in the database
    private boolean isBookAvailable(String id) {
        return checkAvailability("Books", id);
    }

    private boolean isNewspaperAvailable(String id) {
        return checkAvailability("Newspaper", id);
    }

    private boolean isMagazineAvailable(String id) {
        return checkAvailability("Magazines", id);
    }

    private boolean checkAvailability(String table, String id) {
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT isAvailable FROM " + table + " WHERE id = '" + id + "'");
            if (rs.next()) {
                return rs.getBoolean("isAvailable");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
