package com.library.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.library.model.doc.*;

import java.sql.ResultSet;

public class LibraryService {
    final protected String url = "jdbc:sqlite:db/library.db";

    public LibraryService() {
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

    public void addDocuments() {

    }

}
