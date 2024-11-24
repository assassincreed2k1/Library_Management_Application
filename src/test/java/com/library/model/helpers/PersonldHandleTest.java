package com.library.model.helpers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.library.model.Person.Admin;
import com.library.model.Person.Librarian;
import com.library.model.Person.Member;
import com.library.model.Person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonIdHandleTest {
    private Member mockMember;
    private Librarian mockLibrarian;
    private Admin mockAdmin;

    @BeforeEach
    void setUp() {
        mockMember = mock(Member.class);
        mockLibrarian = mock(Librarian.class);
        mockAdmin = mock(Admin.class);
    }

    @Test
    void testGetPersonWithValidMemberId() throws Exception {
        // Arrange
        String validMemberId = "M000001";
        when(mockMember.getInforFromDatabase(validMemberId)).thenReturn(mockMember);

        // Act
        Person result = PersonIdHandle.getPerson(validMemberId);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Member);
        verify(mockMember).getInforFromDatabase(validMemberId);
    }

    @Test
    void testGetPersonWithValidLibrarianId() throws Exception {
        // Arrange
        String validLibrarianId = "L000001";
        when(mockLibrarian.getInforFromDatabase(validLibrarianId)).thenReturn(mockLibrarian);

        // Act
        Person result = PersonIdHandle.getPerson(validLibrarianId);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Librarian);
        verify(mockLibrarian).getInforFromDatabase(validLibrarianId);
    }

    @Test
    void testGetPersonWithValidAdminId() throws Exception {
        // Arrange
        String validAdminId = "A000001";
        when(mockAdmin.getInforFromDatabase()).thenReturn(mockAdmin);

        // Act
        Person result = PersonIdHandle.getPerson(validAdminId);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Admin);
        verify(mockAdmin).getInforFromDatabase();
    }

    @Test
    void testGetPersonWithInvalidIdFormat() {
        // Arrange
        String invalidId = "X123456";

        // Act
        Person result = PersonIdHandle.getPerson(invalidId);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetPersonWithNullId() {
        // Act
        Person result = PersonIdHandle.getPerson(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetPersonWhenMemberDatabaseError() throws Exception {
        // Arrange
        String validMemberId = "M000002";
        when(mockMember.getInforFromDatabase(validMemberId)).thenThrow(new RuntimeException("Database error"));

        // Act
        Person result = PersonIdHandle.getPerson(validMemberId);

        // Assert
        assertNull(result);
        verify(mockMember).getInforFromDatabase(validMemberId);
    }

    @Test
    void testGetPersonWhenLibrarianDatabaseError() throws Exception {
        // Arrange
        String validLibrarianId = "L000002";
        when(mockLibrarian.getInforFromDatabase(validLibrarianId)).thenThrow(new RuntimeException("Database error"));

        // Act
        Person result = PersonIdHandle.getPerson(validLibrarianId);

        // Assert
        assertNull(result);
        verify(mockLibrarian).getInforFromDatabase(validLibrarianId);
    }

    @Test
    void testGetPersonWhenAdminDatabaseError() throws Exception {
        // Arrange
        String validAdminId = "A000002";
        when(mockAdmin.getInforFromDatabase()).thenThrow(new RuntimeException("Database error"));

        // Act
        Person result = PersonIdHandle.getPerson(validAdminId);

        // Assert
        assertNull(result);
        verify(mockAdmin).getInforFromDatabase();
    }
}
