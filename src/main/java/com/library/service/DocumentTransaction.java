package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
// import java.sql.Statement;

import com.library.model.doc.Book;
import com.library.model.doc.Document;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;

public class DocumentTransaction extends LibraryService {

    private CombinedDocument combined_document = new CombinedDocument();
    private LoanManagement loanManagement = new LoanManagement();

    public DocumentTransaction() {
        String sql = "CREATE TABLE IF NOT EXISTS bookTransaction (" +
                     "idTransaction INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "document_id CHAR(255) NOT NULL, " +
                     "edited_by CHAR(7) NOT NULL, " +
                     "membershipId CHAR(7) NOT NULL, " +
                     "borrowDate DATE NOT NULL, " +
                     "dueDate DATE NOT NULL, " +
                     "returnDate DATE, " +
                     "status TEXT, " +
                     "FOREIGN KEY (document_id) REFERENCES combined_documents(id), " +
                     "FOREIGN KEY (edited_by) REFERENCES librarian(employeeID), " +
                     "FOREIGN KEY (membershipId) REFERENCES member(membershipID)" +
                     ");";
        super.createList(sql);
    }

    /**
     * Borrow a document.
     * @param documentId Document ID
     * @param membershipId Member's ID (7 characters)
     * @param editedBy Librarian's employee ID
     * @param borrowDate Borrow date
     * @param dueDate Due date
     * @return Status message
     */
    public String borrowDocument(String documentId, String membershipId, String editedBy, String borrowDate, String dueDate) {
        if (membershipId == null || membershipId.length() != 7) {
            return "Invalid membership ID format.";
        }
        if (editedBy == null || editedBy.length() != 7) {
            return "Invalid librarian's employee ID format.";
        }

        // Fetch the document
        Document document = combined_document.getDocument(documentId);

        if (document == null) {
            return "Can't find this document in database.";
        }

        // Check availability and update document status
        if (document instanceof Book book) {
            if (book.getIsAvailable()) {
                loanManagement.borrowBook(book);
            } else {
                return "Book is not available.";
            }
        } else if (document instanceof Magazine magazine) {
            if (magazine.getIsAvailable()) {
                loanManagement.borrowMagazine(magazine);
            } else {
                return "Magazine is not available.";
            }
        } else if (document instanceof Newspaper newspaper) {
            if (newspaper.getIsAvailable()) {
                loanManagement.borrowNewspaper(newspaper);
            } else {
                return "Newspaper is not available.";
            }
        }

        // Insert transaction into bookTransaction table
        String sql = "INSERT INTO bookTransaction (document_id, membershipId, edited_by, borrowDate, dueDate, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, documentId);
            pstmt.setString(2, membershipId);
            pstmt.setString(3, editedBy);
            pstmt.setString(4, borrowDate);
            pstmt.setString(5, dueDate);
            pstmt.setString(6, "borrowed");
            pstmt.executeUpdate();

            return "Document borrowed successfully.";
        } catch (SQLException e) {
            return "Error borrowing document: " + e.getMessage();
        }
    }

    /**
     * Return a borrowed document.
     * @param documentId Document ID
     * @param membershipId Member's ID (7 characters)
     * @return Status message
     */
    public String returnDocument(String documentId, String membershipId) {
        if (membershipId == null || membershipId.length() != 7) {
            return "Invalid membership ID format.";
        }

        // Fetch the document
        Document document = combined_document.getDocument(documentId);

        if (document == null) {
            return "Can't find this document in database.";
        }

        String sql = "UPDATE bookTransaction SET returnDate = DATE('now'), status = 'returned' " +
                     "WHERE document_id = ? AND membershipId = ? AND returnDate IS NULL AND status = 'borrowed'";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, documentId);
            pstmt.setString(2, membershipId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                if (document instanceof Book book) {
                    if (!book.getIsAvailable()) {
                        loanManagement.returnBook(book);
                    } else {
                        return "Book is already available. It hasn't been borrowed.";
                    }
                } else if (document instanceof Magazine magazine) {
                    if (!magazine.getIsAvailable()) {
                        loanManagement.returnMagazine(magazine);
                    } else {
                        return "Magazine is already available. It hasn't been borrowed.";
                    }
                } else if (document instanceof Newspaper newspaper) {
                    if (!newspaper.getIsAvailable()) {
                        loanManagement.returnNewspaper(newspaper);
                    } else {
                        return "Newspaper is already available. It hasn't been borrowed.";
                    }
                }

                return "Document returned successfully.";
            } else {
                return "No matching transaction found or document already returned.";
            }
        } catch (SQLException e) {
            return "Error returning document: " + e.getMessage();
        }
    }
}
