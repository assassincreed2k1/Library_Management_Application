package com.library.service;

import static org.junit.jupiter.api.Assertions.*;

import com.library.model.doc.Newspaper;
import org.junit.jupiter.api.*;

import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class NewsPaperManagementTest {

    private static NewsPaperManagement newsPaperManagement;
    private static final String TEST_DB = "test_library.db";

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Copy the database library.db to make test files
        Files.copy(Paths.get("library.db"), Paths.get(TEST_DB), StandardCopyOption.REPLACE_EXISTING);

        // Initialize NewspaperMangent with test database file
        newsPaperManagement = new NewsPaperManagement();
        newsPaperManagement.setUrl("jdbc:sqlite:" + TEST_DB);
    }

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + TEST_DB);
             Statement stmt = conn.createStatement()) {
            // delete and recreate the Newspaper table to make sure each test has clean data
            stmt.execute("DROP TABLE IF EXISTS Newspaper");
            stmt.execute("CREATE TABLE Newspaper ("
                    + "id VARCHAR(255) PRIMARY KEY, "
                    + "name VARCHAR(255), "
                    + "newsGroup VARCHAR(50), "
                    + "source VARCHAR(255), "
                    + "region VARCHAR(255), "
                    + "isAvailable BOOLEAN)");
        }
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        // delete database test after completion
        Files.deleteIfExists(Paths.get(TEST_DB));
    }

    @Test
    void testAddDocuments() {
        Newspaper newspaper = new Newspaper();
        newspaper.setID("N001");
        newspaper.setName("Daily News");
        newspaper.setGroup("General");
        newspaper.setSource("NewsCorp");
        newspaper.setRegion("National");
        newspaper.setIsAvailable(true);

        newsPaperManagement.addDocuments(newspaper);

        Newspaper retrievedNewspaper = newsPaperManagement.getDocument("N001");
        assertNotNull(retrievedNewspaper);
        assertEquals("Daily News", retrievedNewspaper.getName());
    }

    @Test
    void testUpdateDocuments() {
        Newspaper newspaper = new Newspaper();
        newspaper.setID("N002");
        newspaper.setName("Weekly Times");
        newspaper.setGroup("Weekly");
        newspaper.setSource("TimesCorp");
        newspaper.setRegion("Regional");
        newspaper.setIsAvailable(true);

        newsPaperManagement.addDocuments(newspaper);

        newspaper.setName("Updated Weekly Times");
        newsPaperManagement.updateDocuments(newspaper);

        Newspaper updatedNewspaper = newsPaperManagement.getDocument("N002");
        assertNotNull(updatedNewspaper);
        assertEquals("Updated Weekly Times", updatedNewspaper.getName());
    }

    @Test
    void testRemoveDocument() {
        Newspaper newspaper = new Newspaper();
        newspaper.setID("N003");
        newspaper.setName("Local News");
        newspaper.setGroup("Local");
        newspaper.setSource("LocalCorp");
        newspaper.setRegion("City");
        newspaper.setIsAvailable(true);

        newsPaperManagement.addDocuments(newspaper);

        newsPaperManagement.removeDocument(newspaper);

        Newspaper removedNewspaper = newsPaperManagement.getDocument("N003");
        assertNull(removedNewspaper);
    }

    @Test
    void testGetAllNewspapers() {
        Newspaper newspaper1 = new Newspaper();
        newspaper1.setID("N004");
        newspaper1.setName("Sports Weekly");
        newspaper1.setGroup("Sports");
        newspaper1.setSource("SportsCorp");
        newspaper1.setRegion("National");
        newspaper1.setIsAvailable(true);

        Newspaper newspaper2 = new Newspaper();
        newspaper2.setID("N005");
        newspaper2.setName("Tech Daily");
        newspaper2.setGroup("Technology");
        newspaper2.setSource("TechCorp");
        newspaper2.setRegion("Global");
        newspaper2.setIsAvailable(false);

        newsPaperManagement.addDocuments(newspaper1);
        newsPaperManagement.addDocuments(newspaper2);

        var allNewspapers = newsPaperManagement.getAllNewspapers();
        assertEquals(2, allNewspapers.size());
    }

    @Test
    void testGetDocument() {
        Newspaper newspaper = new Newspaper();
        newspaper.setID("N006");
        newspaper.setName("History Monthly");
        newspaper.setGroup("History");
        newspaper.setSource("HistoryCorp");
        newspaper.setRegion("Global");
        newspaper.setIsAvailable(true);

        newsPaperManagement.addDocuments(newspaper);

        Newspaper retrievedNewspaper = newsPaperManagement.getDocument("N006");
        assertNotNull(retrievedNewspaper);
        assertEquals("History Monthly", retrievedNewspaper.getName());
    }
}
