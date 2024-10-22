package com.library.service;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * The LibraryService class provides services for managing a library database, including 
 * generating unique IDs, creating necessary tables, and managing an ID counter and deleted IDs.
 */
public class LibraryService {
    final protected String url = "jdbc:sqlite:db/library.db";
    private int idCounter;
    protected ArrayList<String> deletedId;

    /**
     * Constructor for the LibraryService class. It initializes the database connection,
     * creates necessary tables, loads the ID counter, and loads deleted IDs from the database.
     */
    public LibraryService() {
        createDataBase();
        createIDCounterTable();
        createDeletedIDTable();
        idCounter = loadIDCounterFromDB();
        deletedId = loadDeletedIDFromDB();
    }

    /**
     * Creates a new database connection if it doesn't already exist.
     * Displays the driver name and confirmation message if the database is created.
     */
    private void createDataBase() {
        try (Connection cn = DriverManager.getConnection(url)) {
            DatabaseMetaData meta = cn.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A new database has been created");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Creates a table in the database using the provided SQL statement.
     * 
     * @param sql_statement The SQL statement used to create the table.
     */
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
     * Creates the IDCounter table in the database if it does not already exist.
     * Initializes the table with a default value of 0 if it is newly created.
     */
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

    /**
     * Creates the deletedID table in the database if it does not already exist.
     */
    private void createDeletedIDTable() {
        String sql_statement = "CREATE TABLE IF NOT EXISTS deletedID ("
                            + "data VARCHAR(20))";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql_statement);
            System.out.println("deletedID table created or already exists");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Loads the current value of the ID counter from the database.
     * 
     * @return The current value of the ID counter.
     */
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
     * Loads the list of deleted IDs from the database.
     * 
     * @return An ArrayList of deleted IDs as Strings.
     */
    private ArrayList<String> loadDeletedIDFromDB() {
        String sql_query = "SELECT data FROM deletedID";
        ArrayList<String> deletedIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql_query)) {
            while (rs.next()) {
                deletedIds.add(rs.getString("data"));  
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return deletedIds;
    }


        /**
     * Adds a deleted ID to the ArrayList and updates the deletedID table in the database.
     * 
     * @param id The deleted ID to be added (stored as TEXT).
     */
    protected void addDeletedID(String id) {
        deletedId.add(id);
        String sql_insert = "INSERT INTO deletedID (data) VALUES (?)";  // data is now TEXT

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_insert)) {
            pstmt.setString(1, id);  
            pstmt.executeUpdate();
            System.out.println("Deleted ID added to the database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Generates a unique ID for the document. If there are deleted IDs in the list,
     * it reuses the first deleted ID and removes it from both the list and the database.
     * If no deleted IDs are available, it increments the ID counter and generates
     * a new unique ID, updating the database.
     * 
     * @return A unique nine-digit ID as a String.
     */
    public String generateID() {

        if (!deletedId.isEmpty()) {
            String id = deletedId.get(0); 
            deletedId.remove(0);  

            String sql_delete = "DELETE FROM deletedID WHERE data = ?";

            try (Connection conn = DriverManager.getConnection(url);
                    PreparedStatement pstmt = conn.prepareStatement(sql_delete)) {
                pstmt.setString(1, id);
                pstmt.executeUpdate();
                System.out.println("Deleted ID removed from the database");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return id;
        }

        idCounter++;
        String sql_update = "UPDATE IDCounter SET idCounter = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_update)) {

            pstmt.setInt(1, idCounter);
            pstmt.executeUpdate();
            System.out.println("ID counter updated in the database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return String.format("%09d", idCounter); 
    }

}
