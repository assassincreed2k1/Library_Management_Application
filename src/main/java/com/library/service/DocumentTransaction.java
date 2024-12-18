package com.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.library.model.Person.Member;
import com.library.model.doc.Book;
import com.library.model.doc.Document;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;

public class DocumentTransaction extends LibraryService {

    private CombinedDocument combined_document = new CombinedDocument();
    private LoanManagement loanManagement = new LoanManagement();

    public void setCombinedDocument(CombinedDocument combined_document) {
        this.combined_document = combined_document;
    }

    public void setLoanManagement(LoanManagement loanManagement) {
        this.loanManagement = loanManagement;
    }

    public DocumentTransaction() {
        String sql = """
            CREATE TABLE IF NOT EXISTS bookTransaction (
                idTransaction INTEGER PRIMARY KEY AUTOINCREMENT,
                document_id CHAR(255) NOT NULL,
                edited_by CHAR(7) NOT NULL,
                membershipId CHAR(7) NOT NULL,
                borrowDate DATE NOT NULL,
                dueDate DATE NOT NULL,
                returnDate DATE,
                status TEXT,
                score INTEGER DEFAULT NULL,
                comment TEXT DEFAULT NULL,
                FOREIGN KEY (document_id) REFERENCES combined_documents(id),
                FOREIGN KEY (edited_by) REFERENCES librarian(employeeID),
                FOREIGN KEY (membershipId) REFERENCES member(membershipID)
            );
        """;
        super.createList(sql);
    }

    public String borrowDocument(String documentId, String membershipId, String editedBy, String borrowDate, String dueDate) {
        if (membershipId == null || membershipId.length() != 7) {
            return "Invalid membership ID format.";
        }
        if (editedBy == null || editedBy.length() != 7) {
            return "Invalid librarian's employee ID format.";
        }

        Document document = combined_document.getDocument(documentId);

        if (document == null) {
            return "Can't find this document in database.";
        }

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

    public String returnDocument(String documentId, String membershipId) {
        if (membershipId == null || membershipId.length() != 7) {
            return "Invalid membership ID format.";
        }

        Document document = combined_document.getDocument(documentId);

        if (document == null) {
            return "Can't find this document in database.";
        }

        String sql = """
            UPDATE bookTransaction
            SET returnDate = DATE('now'), status = 'returned'
            WHERE document_id = ? AND membershipId = ? AND returnDate IS NULL AND status = 'borrowed'
        """;

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

    public String reviewDocument(String documentId, String membershipId, int score, String comment) {
        if (membershipId == null || membershipId.length() != 7) {
            return "Invalid membership ID format.";
        }

        if (score < 1 || score > 5) {
            return "Score must be between 1 and 5.";
        }

        String sql = """
            UPDATE bookTransaction
            SET score = ?, comment = ?
            WHERE document_id = ? AND membershipId = ? AND status = 'returned' AND score is null
        """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, score);
            pstmt.setString(2, comment);
            pstmt.setString(3, documentId);
            pstmt.setString(4, membershipId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Review submitted successfully.";
            } else {
                return "No matching transaction found for review.";
            }
        } catch (SQLException e) {
            return "Error submitting review: " + e.getMessage() + ". Please check!";
        }
    }

    public double getAverageScore(String isbn) {
        String sql = """
                SELECT b.isbn, AVG(t.score) AS average_score
                FROM Books b
                JOIN bookTransaction t ON t.document_id = b.id
                WHERE b.isbn = ?
                GROUP BY b.isbn;
                """;

        double averageScore = 0.0;

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    averageScore = rs.getDouble("average_score");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return averageScore;
    }

    public ArrayList<String> getComment(String isbn) {
        String sql = """
                SELECT t.membershipId, t.comment 
                FROM Books b
                JOIN bookTransaction t ON t.document_id = b.id
                WHERE b.isbn = ?
                """;
    
        ArrayList<String> comments = new ArrayList<>();
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, isbn);
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String membershipId = rs.getString("membershipId");
                    String comment = rs.getString("comment");
    
                    // Lấy thông tin thành viên từ database
                    Member member =new Member();
                    member = member.getInforFromDatabase(membershipId);
    
                    // Kiểm tra nếu member hoặc comment là null
                    if (member != null && comment != null) {
                        comments.add(member.getName() + " said: \n\n" + comment);
                    } else if (comment != null) {
                        comments.add("Unknown Member said: \n\n" + comment);
                    }
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return comments;
    }
}    
