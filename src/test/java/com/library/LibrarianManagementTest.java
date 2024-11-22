// package com.library;

// import com.library.model.Person.Librarian;
// import com.library.service.LibrarianManagement;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import static org.junit.jupiter.api.Assertions.*;

// public class LibrarianManagementTest {
//     private LibrarianManagement librarianManagement;

//     @BeforeEach
//     public void setUp() {
//         librarianManagement = new LibrarianManagement();
//     }

//     @Test
//     public void testAddLibrarian() {
//         Librarian librarian = new Librarian();
//         librarian.setEmployeeId("E001");
//         librarian.setName("John Doe");
//         librarian.setAddress("123 Main St");
//         librarian.setDateOfBirth("1985-04-01");
//         librarian.setPhoneNumber("0123456789");
//         librarian.setGender("male");
//         librarian.setPosition("Head Librarian");
//         librarian.setPassword("password123");

//         librarianManagement.addLibrarian(librarian);

//         // Verify that the librarian was added correctly
//         Librarian retrievedLibrarian = librarianManagement.getLibrarianInfo("E001");
//         assertNotNull(retrievedLibrarian);
//         assertEquals("John Doe", retrievedLibrarian.getName());
//         assertEquals("123 Main St", retrievedLibrarian.getAddress());
//         assertEquals("1985-04-01", retrievedLibrarian.getDateOfBirth());
//         assertEquals("0123456789", retrievedLibrarian.getPhoneNumber());
//         assertEquals("male", retrievedLibrarian.getGender());
//         assertEquals("Head Librarian", retrievedLibrarian.getPosition());
//         assertEquals("password123", retrievedLibrarian.getPassword());
//     }

//     @Test
//     public void testUpdateLibrarian() {
//         Librarian librarian = new Librarian("Jane Doe", "456 Elm St", "female", "1990-05-05", "0987654321", "E002", "Assistant Librarian", "password456");
//         librarianManagement.addLibrarian(librarian);

//         // Update the librarian's details
//         librarian.setAddress("789 Oak St");
//         librarian.setPosition("Senior Assistant Librarian");
//         librarianManagement.updateLibrarian(librarian);

//         // Verify that the librarian's information has been updated
//         Librarian updatedLibrarian = librarianManagement.getLibrarianInfo("E002");
//         assertNotNull(updatedLibrarian);
//         assertEquals("789 Oak St", updatedLibrarian.getAddress());
//         assertEquals("Senior Assistant Librarian", updatedLibrarian.getPosition());
//     }

//     @Test
//     public void testRemoveLibrarian() {
//         Librarian librarian = new Librarian("Jim Beam", "321 Pine St", "male", "1980-11-11", "1234567890", "E003", "Librarian", "password789");
//         librarianManagement.addLibrarian(librarian);

//         // Remove the librarian
//         librarianManagement.removeLibrarian("E003");
        
//         // Verify that the librarian was removed
//         Librarian removedLibrarian = librarianManagement.getLibrarianInfo("E003");
//         assertNull(removedLibrarian);
//     }
// }
