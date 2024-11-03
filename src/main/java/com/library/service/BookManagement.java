package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.library.model.doc.Book;

/**
 * The {@code BookManagement} class provides services for managing a library's
 * book collection,
 * including adding, updating, removing, retrieving, and displaying available
 * books.
 * It interacts with an SQLite database.
 */
public class BookManagement extends LibraryService {

    /**
     * Constructs a {@code BookManagement} object.
     * It creates the Books table if it doesn't already exist.
     */
    public BookManagement() {
        super.createList("CREATE TABLE IF NOT EXISTS Books ("
                + "id VARCHAR(255) PRIMARY KEY, "
                + "name VARCHAR(255), "
                + "bookGroup VARCHAR(50), "
                + "author VARCHAR(255), "
                + "publishDate VARCHAR(50), "
                + "ISBN VARCHAR(50), " // Thêm cột ISBN
                + "isAvailable BOOLEAN)");
    }

    /**
     * Adds a new {@link Book} to the library's book collection.
     * 
     * @param book The new book to add to the collection.
     */
    public void addDocuments(Book book) {
        String sql_statement = "INSERT INTO Books "
                + "(id, name, bookGroup, author, publishDate, ISBN, isAvailable) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, book.getID());
            pstmt.setString(2, book.getName());
            pstmt.setString(3, book.getGroup());
            pstmt.setString(4, book.getAuthor());
            pstmt.setString(5, book.getPublishDate()); 
            pstmt.setString(6, book.getISBN()); 
            pstmt.setBoolean(7, book.getIsAvailable());
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates an existing {@link Book} in the library's collection.
     * 
     * @param book The book with updated information.
     */
    public void updateDocuments(Book book) {
        String sql_stmt = "UPDATE Books SET "
                + "name = ?, "
                + "bookGroup = ?, "
                + "author = ?, "
                + "publishDate = ?, "
                + "ISBN = ?, " 
                + "isAvailable = ? "
                + "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getGroup());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublishDate()); 
            pstmt.setString(5, book.getISBN()); 
            pstmt.setBoolean(6, book.getIsAvailable());
            pstmt.setString(7, book.getID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Removes a {@link Book} from the library's collection.
     * 
     * @param book The book to be removed.
     */
    public void removeDocument(Book book) {
        String sql_statement = "DELETE FROM Books WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, book.getID());
            pstmt.executeUpdate();
            System.out.println("Data deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves a {@link Book} from the library's collection using its ID.
     * 
     * @param id The ID of the book to retrieve.
     * @return The {@link Book} object if found, otherwise {@code null}.
     */
    public Book getDocument(String id) {
        String sql_statement = "SELECT * FROM Books WHERE id = ?";
        Book book = null;

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String bookId = rs.getString("id");
                String name = rs.getString("name");
                String group = rs.getString("bookGroup");
                String author = rs.getString("author");
                String publishDate = rs.getString("publishDate"); 
                String isbn = rs.getString("ISBN"); 
                boolean isAvailable = rs.getBoolean("isAvailable");

                book = new Book();
                book.setID(bookId);
                book.setName(name);
                book.setGroup(group);
                book.setAuthor(author);
                book.setPublishDate(publishDate); 
                book.setISBN(isbn); 
                book.setIsAvailable(isAvailable);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return book;
    }

    /**
     * Displays all books in the library that are currently available.
     * 
     * @param type The type of the document (currently unused in this method, but
     *             can be expanded for future use).
     */
    public void displayAvailableDocuments(String type) {
        String sql_statement = "SELECT * FROM Books WHERE isAvailable = true";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement);
                ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Available Books:");
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String group = rs.getString("bookGroup");
                String author = rs.getString("author");
                String publishDate = rs.getString("publishDate"); 
                String isbn = rs.getString("ISBN"); 
                boolean isAvailable = rs.getBoolean("isAvailable");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Group: " + group);
                System.out.println("Author: " + author);
                System.out.println("Publish Date: " + publishDate); 
                System.out.println("ISBN: " + isbn); 
                System.out.println("Available: " + isAvailable);
                System.out.println("------------------------");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
