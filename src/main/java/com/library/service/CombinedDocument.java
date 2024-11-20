package com.library.service;

import java.sql.Connection;
// import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.library.model.doc.Book;
import com.library.model.doc.Document;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;

public class CombinedDocument extends LibraryService {
    public CombinedDocument() {
        // Create combined_documents table
        createCombinedDocumentsTable();
    }

    private void createCombinedDocumentsTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS combined_documents AS
                SELECT id, name, 'book' AS document_type FROM Books
                UNION ALL
                SELECT id, name, 'magazine' AS document_type FROM Magazines
                UNION ALL
                SELECT id, name, 'news' AS document_type FROM Newspaper;
                """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table combined_documents created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating combined_documents table: " + e.getMessage());
        }
    }

    public void updateCombinedDocument() {
        // Delete all data and insert updated data
        String deleteDataSQL = "DELETE FROM combined_documents;";
        String insertDataSQL = """
                INSERT INTO combined_documents (id, name, document_type)
                SELECT id, name, 'book' AS document_type FROM Books
                UNION ALL
                SELECT id, name, 'magazine' AS document_type FROM Magazines
                UNION ALL
                SELECT id, name, 'news' AS document_type FROM Newspaper;
                """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Delete existing data
            stmt.execute(deleteDataSQL);
            System.out.println("All data deleted from combined_documents.");

            // Insert new data
            stmt.execute(insertDataSQL);
            System.out.println("New data inserted into combined_documents.");

        } catch (SQLException e) {
            System.err.println("Error updating combined_documents table: " + e.getMessage());
        }
    }

    public Document getDocument(String documentId) {
        String sql_statement = "SELECT document_type FROM combined_documents WHERE id = ?";
        Document document = null; // Khởi tạo tài liệu mặc định là null
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql_statement)) {
            
            // Thiết lập tham số cho câu truy vấn
            stmt.setString(1, documentId);
            
            // Thực hiện truy vấn và lấy kết quả
            ResultSet rs = stmt.executeQuery();
            
            // Kiểm tra kết quả truy vấn
            if (rs.next()) {
                String type = rs.getString("document_type");
                
                // Dựa vào document_type, tạo đối tượng Document phù hợp
                if ("book".equals(type)) {
                    document = new Book();
                } else if ("magazine".equals(type)) {
                    document = new Magazine(); 
                } else if ("news".equals(type)) {
                    document = new Newspaper(); // Giả sử bạn có lớp News
                }
                // Nếu `document` đã được khởi tạo, lấy thông tin từ database
                if (document != null) {
                    System.out.println(document.getClass().getName()); //test ok
                    document = document.getInforFromDatabase(documentId);
                } else {
                    throw new IllegalArgumentException("Invalid document type: " + type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return document; // Trả về đối tượng Document tương ứng, có thể là null nếu không tìm thấy
    }
}
