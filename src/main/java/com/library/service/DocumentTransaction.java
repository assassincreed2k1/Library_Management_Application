package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.sql.Statement;

public class DocumentTransaction extends LibraryService {

    private CombinedDocument combined_document= new CombinedDocument();

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
     * Xử lý mượn tài liệu
     * @param documentId Mã tài liệu
     * @param membershipId ID thành viên mượn tài liệu
     * @param editedBy ID nhân viên xử lý giao dịch
     * @param borrowDate Ngày mượn
     * @param dueDate Ngày đến hạn
     */
    public void borrowDocument(String documentId, int membershipId, int editedBy, String borrowDate, String dueDate) {
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
            System.out.println("Document borrowed successfully.");
        } catch (SQLException e) {
            System.err.println("Error borrowing document: " + e.getMessage());
        }
    }

    /**
     * Xử lý trả tài liệu
     * @param documentId Mã tài liệu
     * @param membershipId ID thành viên trả tài liệu
     * @param editedBy ID nhân viên xử lý giao dịch
     * @param returnDate Ngày trả tài liệu
     */
    public void returnDocument(String documentId, int membershipId, int editedBy) {
        String sql = "UPDATE bookTransaction SET returnDate = DATE('now'), status = 'returned' " +
                     "WHERE document_id = ? AND membershipId = ? AND edited_by = ? AND returnDate IS NULL";           
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set các tham số cho câu lệnh SQL
            pstmt.setString(1, documentId);
            pstmt.setInt(2, membershipId);
            pstmt.setInt(3, editedBy);

            // Thực thi câu lệnh
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Document returned successfully.");
            } else {
                System.out.println("No matching transaction found or document already returned.");
            }
        } catch (SQLException e) {
            System.err.println("Error returning document: " + e.getMessage());
        }
    }
}
