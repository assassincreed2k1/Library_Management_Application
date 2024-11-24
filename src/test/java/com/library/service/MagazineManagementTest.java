package com.library.service;

import static org.junit.jupiter.api.Assertions.*;

import com.library.model.doc.Magazine;
import org.junit.jupiter.api.*;

import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class MagazineManagementTest {

    private static MagazineManagement magazineManagement;
    private static final String TEST_DB = "test_library.db";

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Sao chép database library.db để làm file thử nghiệm
        Files.copy(Paths.get("library.db"), Paths.get(TEST_DB), StandardCopyOption.REPLACE_EXISTING);

        // Khởi tạo đối tượng MagazineManagement với file database thử nghiệm
        magazineManagement = new MagazineManagement();
        magazineManagement.setUrl("jdbc:sqlite:" + TEST_DB);
    }

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + TEST_DB);
             Statement stmt = conn.createStatement()) {
            // Xóa và tạo lại bảng Magazines để đảm bảo mỗi bài test có dữ liệu sạch
            stmt.execute("DROP TABLE IF EXISTS Magazines");
            stmt.execute("CREATE TABLE Magazines ("
                    + "id VARCHAR(255) PRIMARY KEY, "
                    + "name VARCHAR(255), "
                    + "magazineGroup VARCHAR(50), "
                    + "publisher VARCHAR(255), "
                    + "isAvailable BOOLEAN)");
        }
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        // Xóa database thử nghiệm sau khi hoàn tất
        Files.deleteIfExists(Paths.get(TEST_DB));
    }

    @Test
    void testAddDocuments() {
        Magazine magazine = new Magazine();
        magazine.setID("001");
        magazine.setName("Tech Today");
        magazine.setGroup("Technology");
        magazine.setPublisher("TechPub");
        magazine.setIsAvailable(true);

        magazineManagement.addDocuments(magazine);

        Magazine retrievedMagazine = magazineManagement.getDocument("001");
        assertNotNull(retrievedMagazine);
        assertEquals("Tech Today", retrievedMagazine.getName());
    }

    @Test
    void testUpdateDocuments() {
        Magazine magazine = new Magazine();
        magazine.setID("002");
        magazine.setName("Health Weekly");
        magazine.setGroup("Health");
        magazine.setPublisher("HealthPub");
        magazine.setIsAvailable(true);

        magazineManagement.addDocuments(magazine);

        magazine.setName("Health Monthly");
        magazineManagement.updateDocuments(magazine);

        Magazine updatedMagazine = magazineManagement.getDocument("002");
        assertNotNull(updatedMagazine);
        assertEquals("Health Monthly", updatedMagazine.getName());
    }

    @Test
    void testRemoveDocument() {
        Magazine magazine = new Magazine();
        magazine.setID("003");
        magazine.setName("Science Daily");
        magazine.setGroup("Science");
        magazine.setPublisher("SciencePub");
        magazine.setIsAvailable(true);

        magazineManagement.addDocuments(magazine);

        magazineManagement.removeDocument(magazine);

        Magazine removedMagazine = magazineManagement.getDocument("003");
        assertNull(removedMagazine);
    }

    @Test
    void testGetAllMagazines() {
        Magazine magazine1 = new Magazine();
        magazine1.setID("004");
        magazine1.setName("Art Monthly");
        magazine1.setGroup("Art");
        magazine1.setPublisher("ArtPub");
        magazine1.setIsAvailable(true);

        Magazine magazine2 = new Magazine();
        magazine2.setID("005");
        magazine2.setName("Nature Journal");
        magazine2.setGroup("Nature");
        magazine2.setPublisher("NaturePub");
        magazine2.setIsAvailable(false);

        magazineManagement.addDocuments(magazine1);
        magazineManagement.addDocuments(magazine2);

        var allMagazines = magazineManagement.getAllMagazines();
        assertEquals(2, allMagazines.size());
    }

    @Test
    void testGetDocument() {
        Magazine magazine = new Magazine();
        magazine.setID("006");
        magazine.setName("History Digest");
        magazine.setGroup("History");
        magazine.setPublisher("HistoryPub");
        magazine.setIsAvailable(true);

        magazineManagement.addDocuments(magazine);

        Magazine retrievedMagazine = magazineManagement.getDocument("006");
        assertNotNull(retrievedMagazine);
        assertEquals("History Digest", retrievedMagazine.getName());
    }
}

