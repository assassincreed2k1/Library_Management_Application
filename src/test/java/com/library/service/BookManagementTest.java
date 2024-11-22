package com.library.service;

import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import com.library.model.doc.Book;
import javafx.collections.ObservableList;

public class BookManagementTest {

    private static BookManagement bookManagement;

    @BeforeAll
    public static void setup() {
        // Initialize BookManagement
        bookManagement = new BookManagement();
        createTestDatabase();
    }

    @AfterAll
    public static void cleanup() {
        // Drop the Books table after tests
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db")) {
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE IF EXISTS Books;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTestDatabase() {
        // Creating the test database and table if not already present
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db")) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS Books (" +
                    "id VARCHAR(255), " +
                    "name VARCHAR(255), " +
                    "bookGroup VARCHAR(50), " +
                    "author VARCHAR(255), " +
                    "publishDate VARCHAR(50), " +
                    "ISBN VARCHAR(50), " +
                    "isAvailable BOOLEAN, " +
                    "imagePreview VARCHAR(255));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddDocument() {
        Book book = new Book();
        book.setID("1");
        book.setName("Test Book");
        book.setGroup("Fiction");
        book.setAuthor("Test Author");
        book.setPublishDate("2024-11-22");
        book.setISBN("123456789");
        book.setIsAvailable(true);
        book.setImagePreview("image.jpg");

        bookManagement.addDocuments(book);

        Book retrievedBook = bookManagement.getDocument("1");

        assertNotNull(retrievedBook);
        assertEquals("Test Book", retrievedBook.getName());
        assertEquals("Fiction", retrievedBook.getGroup());
        assertEquals("Test Author", retrievedBook.getAuthor());
        assertTrue(retrievedBook.getIsAvailable());
    }

    @Test
    public void testUpdateDocument() {
        Book book = new Book();
        book.setID("2");
        book.setName("Update Book");
        book.setGroup("Science");
        book.setAuthor("Update Author");
        book.setPublishDate("2024-11-23");
        book.setISBN("987654321");
        book.setIsAvailable(true);
        book.setImagePreview("update_image.jpg");

        bookManagement.addDocuments(book);

        book.setName("Updated Book");
        book.setAuthor("Updated Author");

        bookManagement.updateDocuments(book);

        Book updatedBook = bookManagement.getDocument("2");

        assertNotNull(updatedBook);
        assertEquals("Updated Book", updatedBook.getName());
        assertEquals("Updated Author", updatedBook.getAuthor());
    }

    @Test
    public void testRemoveDocument() {
        Book book = new Book();
        book.setID("3");
        book.setName("Book to Delete");
        book.setGroup("History");
        book.setAuthor("Delete Author");
        book.setPublishDate("2024-11-22");
        book.setISBN("112233445");
        book.setIsAvailable(false);
        book.setImagePreview("delete_image.jpg");

        bookManagement.addDocuments(book);

        bookManagement.removeDocument(book);

        Book deletedBook = bookManagement.getDocument("3");

        assertNull(deletedBook);
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book();
        book1.setID("4");
        book1.setName("Book 1");
        book1.setGroup("Drama");
        book1.setAuthor("Author 1");
        book1.setPublishDate("2024-11-25");
        book1.setISBN("234567890");
        book1.setIsAvailable(true);
        book1.setImagePreview("book1.jpg");
        bookManagement.addDocuments(book1);

        Book book2 = new Book();
        book2.setID("5");
        book2.setName("Book 2");
        book2.setGroup("Poetry");
        book2.setAuthor("Author 2");
        book2.setPublishDate("2024-11-26");
        book2.setISBN("345678901");
        book2.setIsAvailable(false);
        book2.setImagePreview("book2.jpg");
        bookManagement.addDocuments(book2);

        ObservableList<Book> allBooks = bookManagement.getAllBooks();

        assertNotNull(allBooks);
        assertEquals(2, allBooks.size());
    }

    @Test
    public void testGetAllBooksFilter() {
        Book book = new Book();
        book.setID("6");
        book.setName("Filtered Book");
        book.setGroup("Fiction");
        book.setAuthor("Filter Author");
        book.setPublishDate("2024-11-27");
        book.setISBN("456789012");
        book.setIsAvailable(true);
        book.setImagePreview("filtered_image.jpg");

        bookManagement.addDocuments(book);

        ObservableList<Book> filteredBooks = bookManagement.getAllBooksFilter("Filtered");

        assertNotNull(filteredBooks);
        assertEquals(1, filteredBooks.size());
        assertEquals("Filtered Book", filteredBooks.get(0).getName());
    }
}
