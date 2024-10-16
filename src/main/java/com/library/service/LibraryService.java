package com.library.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;

import java.sql.ResultSet;

public class LibraryService {
    final private String url = "jdbc:sqlite:db/library.db";

    public LibraryService() {
        createDataBase();
        // Create listBooks
        createList("CREATE TABLE IF NOT EXISTS Books ("
                + "ID VARCHAR(255) PRIMARY KEY, "
                + "Name VARCHAR(255), "
                + "Group VARCHAR(50), "
                + "Author VARCHAR(255), "
                + "isAvailable BOOLEAN)");

        // Create listMagazines
        createList("CREATE TABLE IF NOT EXISTS Magazines ("
                + "ID VARCHAR(255) PRIMARY KEY, "
                + "Name VARCHAR(255), "
                + "Group VARCHAR(50), "
                + "Publisher VARCHAR(255), "
                + "Genre VARCHAR(255), "
                + "isAvailable BOOLEAN)");

        // Create listNewspaper
        createList("CREATE TABLE IF NOT EXISTS Newspaper ("
                + "ID VARCHAR(255) PRIMARY KEY, "
                + "Name VARCHAR(255), "
                + "Group VARCHAR(50), "
                + "Source VARCHAR(255), "
                + "Category VARCHAR(255), "
                + "Region VARCHAR(255), "
                + "isAvailable BOOLEAN)");
    }

    /** Create new database */
    public void createDataBase() {
        try (Connection cn = DriverManager.getConnection(url)) {
            if (cn == null) {
                DatabaseMetaData meta = cn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void createList(String sql_statement) {
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // Create a new table
            stmt.execute(sql_statement);
            System.out.println("Table created or already created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a new {@link Book} to the library's book collection.
     * 
     * @param book The new book to add.
     */
    public void addDocuments(Book book) {
        String sql_statement = "INSERT INTO Books (ID, Name, Group, Author) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, book.getID());
            pstmt.setString(2, book.getName());
            pstmt.setString(3, book.getGroup());
            pstmt.setString(4, book.getAuthor());
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDocuments(Magazine mgz) {

    }

    public void addDocuments(Newspaper news) {

    }

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

    public void removeDocument(Magazine mgz) {

    }

    public void removeDocument(Newspaper news) {

    }

}
