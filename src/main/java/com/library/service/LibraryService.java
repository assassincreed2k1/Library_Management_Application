package com.library.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LibraryService {
    final protected String url = "jdbc:sqlite:db/library.db";
    private int idCounter;

    public LibraryService() {
        createDataBase();
        createIDCounterTable();
        idCounter = loadIDCounterFromDB();
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

    /**
     * delete table from database.
     * 
     * @param table name of table that want to delete
     */
    public void deleteTable(String table) {
        String sql_statement = "DROP TABLE " + table + ";";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql_statement);
            System.out.println("drop table successfully\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createIDCounterTable() {
        String sql_statement = "CREATE TABLE IF NOT EXISTS IDCounter ("
                + "idCounter INTEGER)";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql_statement);

            String initData = "INSERT INTO IDCounter (idCounter) SELECT 0 WHERE NOT EXISTS (SELECT 1 FROM IDCounter)";
            stmt.execute(initData);
            System.out.println("IDCounter table created or already exists with initial value");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private int loadIDCounterFromDB() {
        String sql_query = "SELECT idCounter FROM IDCounter";
        int counterValue = 0;

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql_query)) {
            if (rs.next()) {
                counterValue = rs.getInt("idCounter");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return counterValue;
    }

    /**
     * Generates a unique ID for the document in the format of a zero-padded
     * nine-digit string.
     * 
     * @return A unique ID as a String.
     */
    public String generateID() {
        idCounter++;

        String sql_update = "UPDATE IDCounter SET idCounter = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_update)) {

            pstmt.setInt(1, idCounter);

            pstmt.executeUpdate();
            System.out.println("ID counter updated in database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return String.format("%09d", idCounter);
    }

}
