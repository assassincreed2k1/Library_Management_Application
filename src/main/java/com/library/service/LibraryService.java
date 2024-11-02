package com.library.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * The LibraryService class provides services for managing a library database,
 * including
 * generating unique IDs, creating necessary tables, and managing an ID counter
 * and deleted IDs.
 */
public class LibraryService {
    final protected String url = "jdbc:sqlite:db/library.db";

    /**
     * Constructor for the LibraryService class. It initializes the database
     * connection,
     * creates necessary tables, loads the ID counter, and loads deleted IDs from
     * the database.
     */
    public LibraryService() {
        createDataBase();
        createIDGeneratorTable();
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

    /**
     * Creates the 'id_generator' table in the database if it does not already
     * exist.
     * The table consists of a single column 'id' which is an integer.
     * 
     * This method checks if the table exists, and if not, creates it.
     * It also checks if there are any existing records in the table.
     * If the table is empty, it inserts an initial ID value of 1 into the table.
     * 
     * @throws SQLException if a database access error occurs or the SQL statement
     *                      is invalid.
     */
    private void createIDGeneratorTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS id_generator ("
                + "id INTEGER)";
        String selectCurrentIdSql = "SELECT id FROM id_generator";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSql);
            System.out.println("Table id_generator created or already exists");

            try (ResultSet rs = stmt.executeQuery(selectCurrentIdSql)) {
                if (!rs.next()) {
                    String insertInitialIdSql = "INSERT INTO id_generator (id) VALUES (0)";
                    stmt.executeUpdate(insertInitialIdSql);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Generates a unique ID by updating the current ID in the "id_generator" table
     * and returning the new ID. If the ID is less than or equal to 9 digits, it is
     * formatted as a 9-digit zero-padded string. If the ID exceeds 9 digits, it
     * returns the ID as is. If no valid current ID is found, the method returns
     * null.
     *
     * @return A String representation of the generated ID, zero-padded to 9 digits
     *         if applicable. Returns null if an SQLException occurs or if no valid
     *         current ID is found.
     *
     *         Example:
     * 
     *         <pre>
     *         generateID(); // returns "000000001", "000000002", etc., for IDs
     *         // less than 1 billion. For example, it will return "1000000000"
     *         // for an ID of 1 billion.
     *         </pre>
     */
    public String generateID() {
        String generatedID = null;

        String selectCurrentIdSql = "SELECT id FROM id_generator";
        String updateIdSql = "UPDATE id_generator SET id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

            int currentId = -1;

            try (ResultSet rs = stmt.executeQuery(selectCurrentIdSql)) {
                if (rs.next()) {
                    currentId = rs.getInt("id");
                }
            }

            if (currentId >= 0) {
                currentId++;

                try (PreparedStatement pstmt = conn.prepareStatement(updateIdSql)) {
                    pstmt.setInt(1, currentId);
                    pstmt.executeUpdate();
                }

                // Check if the current ID is less than or equal to 9 digits
                if (currentId < 1000000000) {
                    generatedID = String.format("%09d", currentId);
                } else {
                    generatedID = String.valueOf(currentId);
                }
            } else {
                System.out.println("No valid current ID found.");
                generatedID = null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            generatedID = null;
        }

        return generatedID;
    }

}
