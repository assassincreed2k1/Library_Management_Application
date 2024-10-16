package com.library.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.library.model.document.*;

import java.sql.ResultSet;

public class Library2 {
    final private String url = "jdbc:sqlite:db/library.db";

    public Library2() {
        createDataBase();
        // Create listBooks
        createList("CREATE TABLE IF NOT EXISTS Books ("
                    + "id VARCHAR(255) PRIMARY KEY, "
                    + "name VARCHAR(255), "
                    + "group VARCHAR(50), "
                    + "author VARCHAR(255), "
                    + "isAvailable BOOLEAN)");

        // Create listMagazines
        createList("CREATE TABLE IF NOT EXISTS Magazines ("
                    + "id VARCHAR(255) PRIMARY KEY, "
                    + "name VARCHAR(255), "
                    + "group VARCHAR(50), "
                    + "publisher VARCHAR(255), "
                    + "genre VARCHAR(255), "
                    + "isAvailable BOOLEAN)");

        // Create listNewspaper
        createList("CREATE TABLE IF NOT EXISTS Newspaper ("
                            + "id VARCHAR(255) PRIMARY KEY, "
                            + "name VARCHAR(255), "
                            + "group VARCHAR(50), "
                            + "source VARCHAR(255), "
                            + "category VARCHAR(255), "
                            + "region VARCHAR(255), "
                            + "isAvailable BOOLEAN)");
    }

    /**Create new database */
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

    public void addDocuments(Magazine mgz) {
        String sql_statement = "INSERT INTO Magazines "
                               + "(id, name, group, publisher, genre, isAvailable) " 
                               + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
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

    public void addDocuments(Newspaper news) {

    }

    /**Update Document
     * 
     */
    public void updateDocuments(Book book) {

    }

    public void updateDocuments(Magazine mgz) {
        
    }

    public void updateDocuments(Newspaper news) {
        
    }


    /**Remove Document */
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
