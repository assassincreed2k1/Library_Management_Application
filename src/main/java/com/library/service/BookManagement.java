package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.library.model.doc.Book;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
                + "id VARCHAR(255), "
                + "name VARCHAR(255), "
                + "bookGroup VARCHAR(50), "
                + "author VARCHAR(255), "
                + "publishDate VARCHAR(50), "
                + "ISBN VARCHAR(50), "
                + "isAvailable BOOLEAN, "
                + "imagePreview VARCHAR(255))");
    }

    /**
     * Adds a new {@link Book} to the library's book collection.
     * 
     * @param book The new book to add to the collection.
     */
    public void addDocuments(Book book) {
        String sql_statement = "INSERT INTO Books "
                + "(id, name, bookGroup, author, publishDate, ISBN, isAvailable, imagePreview) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, book.getID());
            pstmt.setString(2, book.getName());
            pstmt.setString(3, book.getGroup());
            pstmt.setString(4, book.getAuthor());
            pstmt.setString(5, book.getPublishDate());
            pstmt.setString(6, book.getISBN());
            pstmt.setBoolean(7, book.getIsAvailable());
            pstmt.setString(8, book.getImagePreview());
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
                + "isAvailable = ?, "
                + "imagePreview = ? "
                + "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getGroup());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublishDate());
            pstmt.setString(5, book.getISBN());
            pstmt.setBoolean(6, book.getIsAvailable());
            pstmt.setString(7, book.getImagePreview());
            pstmt.setString(8, book.getID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Updates all books in the library's collection that match the given ISBN.
     * 
     * @param book The book containing the updated information. All books with the
     *             same ISBN will be updated with this data.
     */
    public void updateDocumentMatchingISBN(Book book) {
        String sql_stmt = "UPDATE Books SET "
                + "name = ?, "
                + "bookGroup = ?, "
                + "author = ?, "
                + "publishDate = ?, "
                + "isAvailable = ?, "
                + "imagePreview = ? "
                + "WHERE ISBN = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getGroup());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublishDate());
            pstmt.setBoolean(5, book.getIsAvailable());
            pstmt.setString(6, book.getImagePreview());
            pstmt.setString(7, book.getISBN());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating books with matching ISBN: " + e.getMessage());
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
                String imagePreview = rs.getString("imagePreview");

                book = new Book();
                book.setID(bookId);
                book.setName(name);
                book.setGroup(group);
                book.setAuthor(author);
                book.setPublishDate(publishDate);
                book.setISBN(isbn);
                book.setIsAvailable(isAvailable);
                book.setImagePreview(imagePreview);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return book;
    }

    /**
     * Retrieves all books from the database and returns them as an ObservableList.
     * This method executes a SQL query to fetch all records from the "Books" table,
     * creates Book objects, and populates them with data from the database.
     * The resulting list of Book objects is then returned.
     *
     * @return An ObservableList containing all the books from the database.
     */
    public ObservableList<Book> getAllBooks() {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        String sql_statement = "SELECT * FROM Books";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book();
                book.setID(rs.getString("id"));
                book.setName(rs.getString("name"));
                book.setGroup(rs.getString("bookGroup"));
                book.setAuthor(rs.getString("author"));
                book.setPublishDate(rs.getString("publishDate"));
                book.setISBN(rs.getString("ISBN"));
                book.setIsAvailable(rs.getBoolean("isAvailable"));
                book.setImagePreview(rs.getString("imagePreview"));
                bookList.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bookList;
    }

    public ObservableList<Book> getAllBooksFilter(String keyword) {
        ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
        String sql_statement = "SELECT * FROM Books WHERE id LIKE ? OR name LIKE ? OR ISBN LIKE ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {

            // Using LIKE to search Books with Name or ISBN
            pstmt.setString(1, "%" + keyword + "%"); // Search with Name
            pstmt.setString(2, "%" + keyword + "%"); // Search with ISBN

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setID(rs.getString("id"));
                    book.setName(rs.getString("name"));
                    book.setGroup(rs.getString("bookGroup"));
                    book.setAuthor(rs.getString("author"));
                    book.setPublishDate(rs.getString("publishDate"));
                    book.setISBN(rs.getString("ISBN"));
                    book.setIsAvailable(rs.getBoolean("isAvailable"));
                    book.setImagePreview(rs.getString("imagePreview"));
                    filteredBooks.add(book);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return filteredBooks;
    }

    public ObservableList<Book> getPopularBooks() {
        ObservableList<Book> popularBooks = FXCollections.observableArrayList();
        String sql_statement = """
                SELECT b.*, COUNT(*) AS borrowed_total
                FROM Books b
                JOIN bookTransaction t ON t.document_id = b.id
                GROUP BY b.id
                HAVING COUNT(*) > 0
                ORDER BY borrowed_total DESC
                LIMIT 10;
                """;

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book();
                book.setID(rs.getString("id"));
                book.setName(rs.getString("name"));
                book.setGroup(rs.getString("bookGroup"));
                book.setAuthor(rs.getString("author"));
                book.setPublishDate(rs.getString("publishDate"));
                book.setISBN(rs.getString("ISBN"));
                book.setIsAvailable(rs.getBoolean("isAvailable"));
                book.setImagePreview(rs.getString("imagePreview"));

                popularBooks.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return popularBooks;
    }

    public Book getDocumentViaIsbn(String isbn) {
        String sql_statement = "SELECT * FROM Books WHERE isbn = ?";
        Book book = null;

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String bookId = rs.getString("id");
                String name = rs.getString("name");
                String group = rs.getString("bookGroup");
                String author = rs.getString("author");
                String publishDate = rs.getString("publishDate");
                String isbn_ = rs.getString("ISBN");
                boolean isAvailable = rs.getBoolean("isAvailable");
                String imagePreview = rs.getString("imagePreview");

                book = new Book();
                book.setID(bookId);
                book.setName(name);
                book.setGroup(group);
                book.setAuthor(author);
                book.setPublishDate(publishDate);
                book.setISBN(isbn_);
                book.setIsAvailable(isAvailable);
                book.setImagePreview(imagePreview);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return book;
    }

}
