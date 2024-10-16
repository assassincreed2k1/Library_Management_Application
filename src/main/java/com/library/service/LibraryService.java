package com.library.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class LibraryService {
    final protected String url = "jdbc:sqlite:db/library.db";

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
                DatabaseMetaData meta = cn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void createList(String sql_statement) {
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql_statement);
            System.out.println("Table created or already created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
