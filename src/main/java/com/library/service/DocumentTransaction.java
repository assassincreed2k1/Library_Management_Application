package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.sql.Statement;

import com.library.model.doc.Book;
import com.library.model.doc.Document;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;

public class DocumentTransaction extends LibraryService {

    private CombinedDocument combined_document= new CombinedDocument();
    private LoanManagement loanManagement = new LoanManagement();

    public DocumentTransaction() {
        String sql = "CREATE TABLE IF NOT EXISTS bookTransaction (" +
                    "idTransaction INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "document_id CHAR(255) NOT NULL, " +
                    "edited_by INTEGER NOT NULL, " +
                    "membershipId INTEGER NOT NULL, " +
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
     * 
     * @param documentId
     * @param membershipId
     * @param editedBy
     * @param borrowDate
     * @param dueDate
     * @return
     */
    public String borrowDocument(String documentId, int membershipId, int editedBy, String borrowDate, String dueDate) {
        //thiết lập cho phần document
        Document document = combined_document.getDocument(documentId);
    
        if (document == null) {
            return "Can't find this document in database";
        }
    
        //check xem tài liệu đã được mượn chưa trong cơ sở dữ liệu, nếu chưa thì set thành mượn rồi
        if (document instanceof Book) {
            Book book = (Book) document;  // Ép kiểu document thành Book
            if (book.getIsAvailable()) {  // Kiểm tra nếu sách có sẵn
                loanManagement.borrowBook(book);
            } else {
                return "Book is not available.";
            }
        } else if (document instanceof Magazine) {
            Magazine magazine = (Magazine) document;  // Ép kiểu document thành Magazine
            if (magazine.getIsAvailable()) {  // Kiểm tra nếu tạp chí có sẵn
                loanManagement.borrowMagazine(magazine);
            } else {
                return "Magazine is not available.";
            }
        } else if (document instanceof Newspaper) {
            Newspaper newspaper = (Newspaper) document;  // Ép kiểu document thành Newspaper
            if (newspaper.getIsAvailable()) {  // Kiểm tra nếu báo có sẵn
                loanManagement.borrowNewspaper(newspaper);
            } else {
                return "Newspaper is not available.";
            }
        }
        
        //thiết lập cho phần bookTransaction
        //merging all documents before check
        combined_document.updateCombinedDocument();
        String sql = "INSERT INTO bookTransaction (document_id, membershipId, edited_by, borrowDate, dueDate, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set các tham số cho câu lệnh SQL
            pstmt.setString(1, documentId);
            pstmt.setInt(2, membershipId);
            pstmt.setInt(3, editedBy);
            pstmt.setString(4, borrowDate);
            pstmt.setString(5, dueDate);
            pstmt.setString(6, "borrowed");  // Trạng thái khi mượn tài liệu
    
            // Thực thi câu lệnh
            pstmt.executeUpdate();
            return "Document borrowed successfully.";
        } catch (SQLException e) {
            return "Error borrowing document: " + e.getMessage();
        }
    }
    
    /**
     * 
     * @param documentId
     * @param membershipId
     * @return
     */
    public String returnDocument(String documentId, int membershipId) {
        //thiết lập cho phần document
        Document document = combined_document.getDocument(documentId);

        if (document == null) {
            return "Can't find this document in database";
        }
    
        String sql = "UPDATE bookTransaction SET returnDate = DATE('now'), status = 'returned' " +
                     "WHERE document_id = ? AND membershipId = ? AND returnDate IS NULL AND status = 'borrowed'";           
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set các tham số cho câu lệnh SQL
            pstmt.setString(1, documentId);
            pstmt.setInt(2, membershipId);
    
            // Thực thi câu lệnh
            int rowsAffected = pstmt.executeUpdate();
    
            if (rowsAffected > 0) {
                if (document instanceof Book) {
                    Book book = (Book) document;  // Ép kiểu document thành Book
                    if (!book.getIsAvailable()) {  // Kiểm tra xem đúng là mượn hay chưa
                        loanManagement.returnBook(book);
                    } else {
                        return "Book is available. It haven't borrowed before.";
                    }
                } else if (document instanceof Magazine) {
                    Magazine magazine = (Magazine) document;  // Ép kiểu document thành Magazine
                    if (!magazine.getIsAvailable()) {  // Kiểm tra xem đúng là mượn hay chưa
                        loanManagement.returnMagazine(magazine);
                    } else {
                        return "Magazine is available. It haven't borrowed before.";
                    }
                } else if (document instanceof Newspaper) {
                    Newspaper newspaper = (Newspaper) document;  // Ép kiểu document thành Newspaper
                    if (!newspaper.getIsAvailable()) {  // Kiểm tra xem đúng là mượn hay chưa
                        loanManagement.returnNewspaper(newspaper);
                    } else {
                        return "Newspaper is available. It haven't borrowed before.";
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
