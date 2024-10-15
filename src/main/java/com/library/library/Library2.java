package com.library.library;

import com.library.document.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Library2 {
    final private String url = "jdbc:sqlite:db/library.db";

    public Library2() {
        createDataBase();
        createListBook();
        createListMagazine();
        createListNewspaper();
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

    private void createListBook() {
        String sql_statement = "CREATE TABLE IF NOT EXISTS Books ("
                            + "ID VARCHAR(255) PRIMARY KEY, "
                            + "Name VARCHAR(255), "
                            + "Group VARCHAR(50), "
                            + "Author VARCHAR(255), "
                            + "Quantity INT)";  
        
        try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement()) {
            // Create a new table
            stmt.execute(sql_statement);
            System.out.println("Table created or already created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void createListMagazine() {
        String sql_statement = "CREATE TABLE IF NOT EXISTS Magazines ("
                            + "ID VARCHAR(255) PRIMARY KEY, "
                            + "Name VARCHAR(255), "
                            + "Group VARCHAR(50), "
                            + "Publisher VARCHAR(255), "
                            + "Genre VARCHAR(255), "
                            + "Quantity INT)";  
    
        try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement()) {
            // Create a new table
            stmt.execute(sql_statement);
            System.out.println("Table created or already created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void createListNewspaper() {
        String sql_statement = "CREATE TABLE IF NOT EXISTS Newspaper ("
                            + "ID VARCHAR(255) PRIMARY KEY, "
                            + "Name VARCHAR(255), "
                            + "Group VARCHAR(50), "
                            + "Source VARCHAR(255), "
                            + "Category VARCHAR(255), "
                            + "Region VARCHAR(255), "
                            + "Quantity INT)";  
    
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
     * If the book already exists, its quantity is updated.
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

}
