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
    }

    /** Create new database */
    private void createDataBase() {
        try (Connection cn = DriverManager.getConnection(url)) {
                DatabaseMetaData meta = cn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void createList(String sql_statement) {
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql_statement);
            System.out.println("Table created or already created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
