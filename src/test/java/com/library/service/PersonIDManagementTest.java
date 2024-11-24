package com.library.service;

import org.junit.jupiter.api.*;

import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class PersonIDManagementTest {

    private static PersonIDManagement personIDManagement;
    private static final String TEST_DB = "test_library.db";
    private static final String TEST_TABLE = "PersonIDTable";

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Copy the database library.db to make test files
        Files.copy(Paths.get("library.db"), Paths.get(TEST_DB), StandardCopyOption.REPLACE_EXISTING);

        // Initialize Personidmanagement object with test database file
        personIDManagement = new PersonIDManagement(TEST_TABLE);
        personIDManagement.setUrl("jdbc:sqlite:" + TEST_DB);
    }

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + TEST_DB);
             Statement stmt = conn.createStatement()) {
            // delete and recreated the test table
            stmt.execute("DROP TABLE IF EXISTS " + TEST_TABLE);
            stmt.execute("CREATE TABLE " + TEST_TABLE + " (id INT PRIMARY KEY)");
        }
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        // delete database test after completion
        Files.deleteIfExists(Paths.get(TEST_DB));
    }

    @Test
    void testGetID_InitialID() {
        // When the table has no ID, the initialized ID must be 1
        int initialID = personIDManagement.getID();
        assertEquals(1, initialID);
    }

    @Test
    void testGetID_AfterIncrease() {
        // increase ID and check new value
        personIDManagement.getID(); //CreateInitialId
        personIDManagement.increaseID();
        int currentID = personIDManagement.getID();
        assertEquals(2, currentID);
    }

    @Test
    void testIncreaseID_MultipleTimes() {
        // increase ID many times and check
        personIDManagement.getID(); // Create the original ID
        personIDManagement.increaseID();
        personIDManagement.increaseID();
        personIDManagement.increaseID();

        int currentID = personIDManagement.getID();
        assertEquals(4, currentID);
    }

    @Test
    void testGetID_Persistence() {
        // Check the value of ID stored in database
        personIDManagement.getID(); // Create the original ID
        personIDManagement.increaseID();

        // Create new objects and check whether the ID is kept the same
        PersonIDManagement newInstance = new PersonIDManagement(TEST_TABLE);
        newInstance.setUrl("jdbc:sqlite:" + TEST_DB);
        int currentID = newInstance.getID();
        assertEquals(2, currentID);
    }
}
