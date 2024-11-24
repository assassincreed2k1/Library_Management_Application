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
        // Sao chép database library.db để làm file thử nghiệm
        Files.copy(Paths.get("library.db"), Paths.get(TEST_DB), StandardCopyOption.REPLACE_EXISTING);

        // Khởi tạo đối tượng PersonIDManagement với file database thử nghiệm
        personIDManagement = new PersonIDManagement(TEST_TABLE);
        personIDManagement.setUrl("jdbc:sqlite:" + TEST_DB);
    }

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + TEST_DB);
             Statement stmt = conn.createStatement()) {
            // Xóa và tạo lại bảng thử nghiệm
            stmt.execute("DROP TABLE IF EXISTS " + TEST_TABLE);
            stmt.execute("CREATE TABLE " + TEST_TABLE + " (id INT PRIMARY KEY)");
        }
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        // Xóa database thử nghiệm sau khi hoàn tất
        Files.deleteIfExists(Paths.get(TEST_DB));
    }

    @Test
    void testGetID_InitialID() {
        // Khi bảng chưa có ID nào, ID khởi tạo phải là 1
        int initialID = personIDManagement.getID();
        assertEquals(1, initialID);
    }

    @Test
    void testGetID_AfterIncrease() {
        // Tăng ID và kiểm tra giá trị mới
        personIDManagement.getID(); // Tạo ID ban đầu
        personIDManagement.increaseID();
        int currentID = personIDManagement.getID();
        assertEquals(2, currentID);
    }

    @Test
    void testIncreaseID_MultipleTimes() {
        // Tăng ID nhiều lần và kiểm tra
        personIDManagement.getID(); // Tạo ID ban đầu
        personIDManagement.increaseID();
        personIDManagement.increaseID();
        personIDManagement.increaseID();

        int currentID = personIDManagement.getID();
        assertEquals(4, currentID);
    }

    @Test
    void testGetID_Persistence() {
        // Kiểm tra giá trị ID được lưu trữ trong database
        personIDManagement.getID(); // Tạo ID ban đầu
        personIDManagement.increaseID();

        // Tạo đối tượng mới và kiểm tra ID có được giữ nguyên không
        PersonIDManagement newInstance = new PersonIDManagement(TEST_TABLE);
        newInstance.setUrl("jdbc:sqlite:" + TEST_DB);
        int currentID = newInstance.getID();
        assertEquals(2, currentID);
    }
}
