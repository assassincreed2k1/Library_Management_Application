package com.library;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * This class provides methods to create and manage a SQLite database
 * for a warehouse inventory system. It supports operations such as
 * creating the database, creating a table, inserting, updating,
 * selecting, and deleting data.
 */
public class myDatabase {
    final private String url = "jdbc:sqlite:db/library.db";

    /**
     * Creates a new SQLite database if it does not already exist.
     * Prints the name of the database driver and confirms the creation of the database.
     */
    public void createDB() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Creates a new table named 'warehouse' in the database if it does not exist.
     * The table includes columns for id, name, and capacity.
     */
    public void createTable() {
        String sql_statement = "CREATE TABLE IF NOT EXISTS warehouse ("
                            + " id INTEGER PRIMARY KEY, "
                            + " name text NOT NULL, "
                            + " capacity REAL"
                            + ");";
        
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
     * Inserts sample data into the 'warehouse' table.
     * This method adds three entries for different types of goods with their capacities.
     */
    public void insertData() {
        String[] names = new String[] {"Raw Materials", "Semifinished Goods", "Finished Goods"};
        int[] capacities = new int[] {3000, 4000, 5000};

        String sql_stmt = "INSERT INTO warehouse(name,capacity) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql_stmt);

            for (int i = 0; i < 3; i++) {
                pstmt.setString(1, names[i]);
                pstmt.setDouble(2, capacities[i]);
                pstmt.executeUpdate();
            } 
        } catch (SQLException e) {
                System.err.println(e.getMessage());
        }
    }

    /**
     * Updates the details of an entry in the 'warehouse' table.
     * Specifically, it updates the name and capacity for the entry with the specified id.
     */
    public void updateData() {
        String sql_stmt = "UPDATE warehouse SET name = ? ,"
                        + "capacity = ? "
                        + "WHERE id = ?";

        String name = "Finished Products";
        int capacity = 5500;
        int id = 3;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.setInt(3, id);
            // Update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Selects and displays data from the 'warehouse' table
     * where the capacity is greater than the specified value.
     * 
     * @param capacity The capacity threshold for filtering records.
     */
    public void selectData() {
        String sql_statement = "SELECT id, name, capacity FROM warehouse where capacity > ?";
        double capacity = 3600;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement prepared_statememt = conn.prepareStatement(sql_statement)) {
            prepared_statememt.setDouble(1, capacity);
            ResultSet rs = prepared_statememt.executeQuery();

            while (rs.next()) {
                System.out.printf("%-5s%-25s%-10s%n",
                                 rs.getInt("id"),
                                 rs.getString("name"),
                                 rs.getDouble("capacity")
                                 );
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Deletes an entry from the 'warehouse' table based on the specified id.
     * 
     * @param id The id of the entry to be deleted.
     */
    public void deleteData() {
        String sql_statement = "DELETE FROM warehouse WHERE id = ?";
        int id = 3;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
