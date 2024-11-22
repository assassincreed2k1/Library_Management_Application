package com.library.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.library.model.doc.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DocumentTransactionTest {

    private DocumentTransaction documentTransaction;
    private CombinedDocument mockCombinedDocument;
    private LoanManagement mockLoanManagement;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockCombinedDocument = mock(CombinedDocument.class);
        mockLoanManagement = mock(LoanManagement.class);

        // Inject mocks
        documentTransaction = new DocumentTransaction();
        documentTransaction.setCombinedDocument(mockCombinedDocument);
        documentTransaction.setLoanManagement(mockLoanManagement);
    }

    @Test
    void testBorrowDocument_SuccessfulBorrow() throws SQLException {
        // Mock document
        Book mockBook = mock(Book.class);
        when(mockBook.getIsAvailable()).thenReturn(true);
        when(mockCombinedDocument.getDocument("DOC123")).thenReturn(mockBook);

        // Test borrowing a document
        String result = documentTransaction.borrowDocument("DOC123", "MEM1234", "LIB4567", "2024-11-20", "2024-12-20");

        // Verify the correct calls were made
        verify(mockLoanManagement).borrowBook(mockBook);
        assertEquals("Document borrowed successfully.", result);
    }

    @Test
    void testBorrowDocument_DocumentNotAvailable() {
        // Mock document
        Book mockBook = mock(Book.class);
        when(mockBook.getIsAvailable()).thenReturn(false);
        when(mockCombinedDocument.getDocument("DOC123")).thenReturn(mockBook);

        // Test borrowing a document
        String result = documentTransaction.borrowDocument("DOC123", "MEM1234", "LIB4567", "2024-11-20", "2024-12-20");

        // Verify no interaction with loan management
        verifyNoInteractions(mockLoanManagement);
        assertEquals("Book is not available.", result);
    }

    @Test
    void testBorrowDocument_InvalidMembershipId() {
        // Test with invalid membership ID
        String result = documentTransaction.borrowDocument("DOC123", "MEM1", "LIB4567", "2024-11-20", "2024-12-20");

        assertEquals("Invalid membership ID format.", result);
    }

    @Test
    void testReturnDocument_SuccessfulReturn() throws SQLException {
        // Mock document
        Book mockBook = mock(Book.class);
        when(mockBook.getIsAvailable()).thenReturn(false);
        when(mockCombinedDocument.getDocument("DOC123")).thenReturn(mockBook);

        // Test returning a document
        String result = documentTransaction.returnDocument("DOC123", "MEM1234");

        // Verify the correct calls were made
        verify(mockLoanManagement).returnBook(mockBook);
        assertEquals("Document returned successfully.", result);
    }

    @Test
    void testReturnDocument_NoMatchingTransaction() {
        // Mock document
        Book mockBook = mock(Book.class);
        when(mockBook.getIsAvailable()).thenReturn(false);
        when(mockCombinedDocument.getDocument("DOC123")).thenReturn(mockBook);

        // Simulate no matching transaction found
        String result = documentTransaction.returnDocument("DOC999", "MEM1234");

        // Verify no interaction with loan management
        verifyNoInteractions(mockLoanManagement);
        assertEquals("No matching transaction found or document already returned.", result);
    }

    @Test
    void testReturnDocument_InvalidMembershipId() {
        // Test with invalid membership ID
        String result = documentTransaction.returnDocument("DOC123", "MEM1");

        assertEquals("Invalid membership ID format.", result);
    }
}

