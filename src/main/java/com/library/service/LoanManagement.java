package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.library.model.doc.Book;
import com.library.model.doc.Newspaper;
import com.library.model.doc.Magazine;

public class LoanManagement extends LibraryService {

    /**
     * Borrow a book from the library.
     * This method checks if the book is available and, if so, updates its availability status to false in the database.
     *
     * @param book The book object to be borrowed.
     */
    public void borrowBook(Book book) {
        if (!book.getIsAvailable()) {
            System.out.println("Book is already borrowed.");
            return;
        }

        String sql = "UPDATE Books SET isAvailable = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, false); // Set availability to false
            pstmt.setString(2, book.getID());
            pstmt.executeUpdate();
            System.out.println("Book borrowed successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Return a borrowed book to the library.
     * This method updates the availability status of the book to true in the database.
     *
     * @param book The book object to be returned.
     */
    public void returnBook(Book book) {
        String sql = "UPDATE Books SET isAvailable = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, true); // Set availability to true
            pstmt.setString(2, book.getID());
            pstmt.executeUpdate();
            System.out.println("Book returned successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

     /**
     * Borrow a newspaper from the library.
     * This method checks if the newspaper is available and, if so, updates its availability status to false in the database.
     *
     * @param newspaper The newspaper object to be borrowed.
     */
    public void borrowNewspaper(Newspaper newspaper) {
        if (!newspaper.getIsAvailable()) {
            System.out.println("Newspaper is already borrowed.");
            return;
        }

        String sql = "UPDATE Newspaper SET isAvailable = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, false); // Set availability to false
            pstmt.setString(2, newspaper.getID());
            pstmt.executeUpdate();
            System.out.println("Newspaper borrowed successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Return a borrowed newspaper to the library.
     * This method updates the availability status of the newspaper to true in the database.
     *
     * @param newspaper The newspaper object to be returned.
     */
    public void returnNewspaper(Newspaper newspaper) {
        String url = "jdbc:sqlite:library.db"; 
        String sql = "UPDATE Newspaper SET isAvailable = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, true); // Set availability to true
            pstmt.setString(2, newspaper.getID());
            pstmt.executeUpdate();
            System.out.println("Newspaper returned successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Borrow a magazine from the library.
     * This method checks if the magazine is available and, if so, updates its availability status to false in the database.
     *
     * @param magazine The magazine object to be borrowed.
     */
    public void borrowMagazine(Magazine magazine) {
        if (!magazine.getIsAvailable()) {
            System.out.println("Magazine is already borrowed.");
            return;
        }

        String sql = "UPDATE Magazines SET isAvailable = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, false); // Set availability to false
            pstmt.setString(2, magazine.getID());
            pstmt.executeUpdate();
            System.out.println("Magazine borrowed successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

     /**
     * Return a borrowed magazine to the library.
     * This method updates the availability status of the magazine to true in the database.
     *
     * @param magazine The magazine object to be returned.
     */
    public void returnMagazine(Magazine magazine) {
        String sql = "UPDATE Magazines SET isAvailable = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, true); // Set availability to true
            pstmt.setString(2, magazine.getID());
            pstmt.executeUpdate();
            System.out.println("Magazine returned successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
