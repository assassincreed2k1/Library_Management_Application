package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CombinedDocument extends LibraryService {
    public CombinedDocument() {
        // Create combined_documents table
        createCombinedDocumentsTable();
    }

    private void createCombinedDocumentsTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS combined_documents AS
                SELECT id, name, genre, 'book' AS document_type FROM book
                UNION ALL
                SELECT id, name, genre, 'magazine' AS document_type FROM magazine
                UNION ALL
                SELECT id, name, genre, 'news' AS document_type FROM news;
                """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table combined_documents created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating combined_documents table: " + e.getMessage());
        }
    }

    public void updateCombinedDocument() {
        // Delete all data and insert updated data
        String deleteDataSQL = "DELETE FROM combined_documents;";
        String insertDataSQL = """
                INSERT INTO combined_documents (id, name, genre, document_type)
                SELECT id, name, genre, 'book' AS document_type FROM book
                UNION ALL
                SELECT id, name, genre, 'magazine' AS document_type FROM magazine
                UNION ALL
                SELECT id, name, genre, 'news' AS document_type FROM news;
                """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Delete existing data
            stmt.execute(deleteDataSQL);
            System.out.println("All data deleted from combined_documents.");

            // Insert new data
            stmt.execute(insertDataSQL);
            System.out.println("New data inserted into combined_documents.");

        } catch (SQLException e) {
            System.err.println("Error updating combined_documents table: " + e.getMessage());
        }
    }
}
