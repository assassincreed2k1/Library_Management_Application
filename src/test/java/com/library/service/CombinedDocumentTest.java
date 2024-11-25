package com.library.service;

import static org.junit.jupiter.api.Assertions.*;

import com.library.model.doc.Book;
import com.library.model.doc.Document;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import org.junit.jupiter.api.*;

import java.sql.*;

class CombinedDocumentTest {

    private static final String TEST_DB_URL = "jdbc:sqlite:library_test.db";
    private CombinedDocument combinedDocument;

    @BeforeAll
    static void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement()) {

            // Create Books, Magazines, Newspaper
            stmt.execute("CREATE TABLE IF NOT EXISTS Books (id TEXT PRIMARY KEY, name TEXT);");
            stmt.execute("CREATE TABLE IF NOT EXISTS Magazines (id TEXT PRIMARY KEY, name TEXT);");
            stmt.execute("CREATE TABLE IF NOT EXISTS Newspaper (id TEXT PRIMARY KEY, name TEXT);");

            // Add sample data
            stmt.execute("INSERT INTO Books (id, name) VALUES ('b1', 'Book One');");
            stmt.execute("INSERT INTO Magazines (id, name) VALUES ('m1', 'Magazine One');");
            stmt.execute("INSERT INTO Newspaper (id, name) VALUES ('n1', 'News One');");
        } catch (SQLException e) {
            fail("Failed to set up test database: " + e.getMessage());
        }
    }

    @AfterAll
    static void cleanupDatabase() {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
            Statement stmt = conn.createStatement()) {
            // delete the tables after checking
            stmt.execute("DROP TABLE IF EXISTS Books;");
            stmt.execute("DROP TABLE IF EXISTS Magazines;");
            stmt.execute("DROP TABLE IF EXISTS Newspaper;");
            stmt.execute("DROP TABLE IF EXISTS combined_documents;");

        } catch (SQLException e) {
            fail("Failed to clean up test database: " + e.getMessage());
        }
    }

    @BeforeEach
    void setup() {
        combinedDocument = new CombinedDocument(TEST_DB_URL);
    }

    @Test
    void testCreateCombinedDocumentsTable() {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='combined_documents';");
            assertTrue(rs.next(), "combined_documents table should be created.");

        } catch (SQLException e) {
            fail("Failed to verify combined_documents table: " + e.getMessage());
        }
    }

    @Test
    void testUpdateCombinedDocument() {
        combinedDocument.updateCombinedDocument();

        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM combined_documents;");

            // Check with enough data from 3 pounds
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            assertEquals(3, rowCount, "combined_documents should contain data from Books, Magazines, and Newspaper.");

        } catch (SQLException e) {
            fail("Failed to verify combined_documents data: " + e.getMessage());
        }
    }

    @Test
    void testGetDocument() {
        combinedDocument.updateCombinedDocument();

        // check data for each type of document
        Document book = combinedDocument.getDocument("b1");
        assertNotNull(book);
        assertTrue(book instanceof Book, "Document with id 'b1' should be a Book.");

        Document magazine = combinedDocument.getDocument("m1");
        assertNotNull(magazine);
        assertTrue(magazine instanceof Magazine, "Document with id 'm1' should be a Magazine.");

        Document newspaper = combinedDocument.getDocument("n1");
        assertNotNull(newspaper);
        assertTrue(newspaper instanceof Newspaper, "Document with id 'n1' should be a Newspaper.");

        // check the document does not exist
        Document nonExistent = combinedDocument.getDocument("unknown");
        assertNull(nonExistent, "Non-existent document should return null.");
    }
}
