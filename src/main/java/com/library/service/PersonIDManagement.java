package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonIDManagement extends LibraryService {
    private String table;

    public PersonIDManagement(String table) {
        this.table = table;
        super.createList("CREATE TABLE IF NOT EXISTS " + table + " ("
                + "id INT PRIMARY KEY"
                + ");");
    }

    /**
     * increase id, use after adding, start from 1.
     * 
     */
    public void increaseID() {
        try (Connection conn = DriverManager.getConnection(url)) {
            int currentID = getID();
            int newID = currentID + 1;

            String updateSQL = "UPDATE " + table + " SET id = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setInt(1, newID);
                pstmt.setInt(2, currentID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * get person id in a table that manage id.
     * @return int ID
     */
    public int getID() {
        int id = 0;
        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "SELECT id FROM " + table;
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                    ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("id");
                } else {
                    // Nếu không có ID nào trong bảng, khởi tạo ID là 1
                    id = 1;
                    String insertSQL = "INSERT INTO " + table + " (id) VALUES (?)";
                    try (PreparedStatement insertPstmt = conn.prepareStatement(insertSQL)) {
                        insertPstmt.setInt(1, id);
                        insertPstmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void setUrl(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUrl'");
    }
}
