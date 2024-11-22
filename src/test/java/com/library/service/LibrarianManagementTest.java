package com.library.service;

import com.library.model.Person.Librarian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LibrarianManagementTest {
    private LibrarianManagement librarianManagement;

    @BeforeEach
    public void setUp() {
        librarianManagement = new LibrarianManagement();
    }

    @Test
    public void testAddLibrarian() {
        Librarian librarian = new Librarian();
        librarian.setEmployeeId("E001");
        librarian.setName("Nguyen Van A");
        librarian.setAddress("123 Xuan Thuy");
        librarian.setDateOfBirth("1985-04-01");
        librarian.setPhoneNumber("0123456789");
        librarian.setGender("male");
        librarian.setPosition("Head Librarian");
        librarian.setPassword("password123");

        librarianManagement.addLibrarian(librarian);

        // Verify that the librarian was added correctly
        Librarian retrievedLibrarian = librarianManagement.getLibrarianInfo("E001");
        assertNotNull(retrievedLibrarian);
        assertEquals("Nguyen Van A", retrievedLibrarian.getName());
        assertEquals("123 Xuan Thuy", retrievedLibrarian.getAddress());
        assertEquals("1985-04-01", retrievedLibrarian.getDateOfBirth());
        assertEquals("0123456789", retrievedLibrarian.getPhoneNumber());
        assertEquals("male", retrievedLibrarian.getGender());
        assertEquals("Head Librarian", retrievedLibrarian.getPosition());
        assertEquals("password123", retrievedLibrarian.getPassword());
    }

    @Test
    public void testUpdateLibrarian() {
        Librarian librarian = new Librarian("Tran Thi B", "456 Xuan Thuy", "female", "1990-05-05", "0987654321", "E002", "Assistant Librarian", "password456");
        librarianManagement.addLibrarian(librarian);

        // Update the librarian's details
        librarian.setAddress("789 Pham Van Dong");
        librarian.setPosition("Senior Assistant Librarian");
        librarianManagement.updateLibrarian(librarian);

        // Verify that the librarian's information has been updated
        Librarian updatedLibrarian = librarianManagement.getLibrarianInfo("E002");
        assertNotNull(updatedLibrarian);
        assertEquals("789 Pham Van Dong", updatedLibrarian.getAddress());
        assertEquals("Senior Assistant Librarian", updatedLibrarian.getPosition());
    }

    @Test
    public void testRemoveLibrarian() {
        Librarian librarian = new Librarian("Ha Van C", "321 Hoa Lac", "male", "1980-11-11", "1234567890", "E003", "Librarian", "password789");
        librarianManagement.addLibrarian(librarian);

        // Remove the librarian
        librarianManagement.removeLibrarian("E003");
        
        // Verify that the librarian was removed
        Librarian removedLibrarian = librarianManagement.getLibrarianInfo("E003");
        assertNull(removedLibrarian);
    }
}
