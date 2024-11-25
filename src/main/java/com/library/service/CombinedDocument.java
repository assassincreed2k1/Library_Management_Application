package com.library.service;

import java.sql.Connection;
// import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.library.model.doc.Book;
import com.library.model.doc.Document;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;

/**
 * This class manages the combined documents (books, magazines, and newspapers) in the library.
 * It provides functionality for creating, updating, and retrieving documents from a combined table in the database.
 */
public class CombinedDocument extends LibraryService {
    
    /**
     * Default constructor that creates the combined_documents table.
     */
    public CombinedDocument() {
        createCombinedDocumentsTable();
    }

    /**
     * Constructor that accepts a database URL and creates the combined_documents table.
     *
     * @param url The database URL used to connect to the library database.
     */
    public CombinedDocument(String url) {
        createCombinedDocumentsTable();
    }

    /**
     * Creates the combined_documents table by combining data from Books, Magazines, and Newspapers tables.
     * The table will include the document's ID, name, and type (book, magazine, or news).
     */
    private void createCombinedDocumentsTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS combined_documents AS
                SELECT id, name, 'book' AS document_type FROM Books
                UNION ALL
                SELECT id, name, 'magazine' AS document_type FROM Magazines
                UNION ALL
                SELECT id, name, 'news' AS document_type FROM Newspaper;
                """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table combined_documents created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating combined_documents table: " + e.getMessage());
        }
    }

    /**
     * Updates the combined_documents table by first deleting all existing data
     * and then reinserting data from the Books, Magazines, and Newspapers tables.
     */
    public void updateCombinedDocument() {
        // Delete all data and insert updated data
        String deleteDataSQL = "DELETE FROM combined_documents;";
        String insertDataSQL = """
                INSERT INTO combined_documents (id, name, document_type)
                SELECT id, name, 'book' AS document_type FROM Books
                UNION ALL
                SELECT id, name, 'magazine' AS document_type FROM Magazines
                UNION ALL
                SELECT id, name, 'news' AS document_type FROM Newspaper;
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

    /**
     * Retrieves a document (book, magazine, or newspaper) based on its ID.
     *
     * @param documentId The ID of the document to retrieve.
     * @return The corresponding Document object (Book, Magazine, or Newspaper), or null if not found.
     */
    public Document getDocument(String documentId) {
        String sql_statement = "SELECT document_type FROM combined_documents WHERE id = ?";
        Document document = null; // Initialize the default document is null
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql_statement)) {
            
            // Set parameters for queries
            stmt.setString(1, documentId);
            
           // perform query and get results
            ResultSet rs = stmt.executeQuery();
            
            // check the query results
            if (rs.next()) {
                String type = rs.getString("document_type");
                
                // Based on document_type, create the appropriate document object
                if ("book".equals(type)) {
                    document = new Book();
                } else if ("magazine".equals(type)) {
                    document = new Magazine(); 
                } else if ("news".equals(type)) {
                    document = new Newspaper(); //Suppose you have News class
                }
                // If the document` has been initialized, taking information from database
                if (document != null) {
                    System.out.println(document.getClass().getName()); //test ok
                    document = document.getInforFromDatabase(documentId);
                } else {
                    throw new IllegalArgumentException("Invalid document type: " + type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return document; // Returns the corresponding document object, may be null if not found
    }
}
