// package com.library;

// import static org.mockito.Mockito.*;

// import com.library.model.doc.Book;
// import com.library.model.doc.Newspaper;
// import com.library.model.doc.Magazine;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.MockedStatic;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;

// public class LoanManagementTest {
//     private LoanManagement loanManagement;

//     @BeforeEach
//     void setUp() {
//         loanManagement = new LoanManagement();
//     }

//     @Test
//     void testBorrowBook_Success() throws SQLException {
//         Book book = mock(Book.class);
//         when(book.getIsAvailable()).thenReturn(true);
//         when(book.getID()).thenReturn("123");

//         try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
//             Connection mockConnection = mock(Connection.class);
//             PreparedStatement mockStatement = mock(PreparedStatement.class);

//             driverManagerMock.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);
//             when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

//             loanManagement.borrowBook(book);

//             verify(mockStatement).setBoolean(1, false);
//             verify(mockStatement).setString(2, "123");
//             verify(mockStatement).executeUpdate();
//         }
//     }

//     @Test
//     void testBorrowBook_AlreadyBorrowed() {
//         Book book = mock(Book.class);
//         when(book.getIsAvailable()).thenReturn(false);

//         loanManagement.borrowBook(book);

//         // No interaction with database
//         verifyNoInteractions(DriverManager.class);
//     }

//     @Test
//     void testReturnBook_Success() throws SQLException {
//         Book book = mock(Book.class);
//         when(book.getID()).thenReturn("123");

//         try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
//             Connection mockConnection = mock(Connection.class);
//             PreparedStatement mockStatement = mock(PreparedStatement.class);

//             driverManagerMock.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);
//             when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

//             loanManagement.returnBook(book);

//             verify(mockStatement).setBoolean(1, true);
//             verify(mockStatement).setString(2, "123");
//             verify(mockStatement).executeUpdate();
//         }
//     }

//     @Test
//     void testBorrowNewspaper_Success() throws SQLException {
//         Newspaper newspaper = mock(Newspaper.class);
//         when(newspaper.getIsAvailable()).thenReturn(true);
//         when(newspaper.getID()).thenReturn("456");

//         try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
//             Connection mockConnection = mock(Connection.class);
//             PreparedStatement mockStatement = mock(PreparedStatement.class);

//             driverManagerMock.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);
//             when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

//             loanManagement.borrowNewspaper(newspaper);

//             verify(mockStatement).setBoolean(1, false);
//             verify(mockStatement).setString(2, "456");
//             verify(mockStatement).executeUpdate();
//         }
//     }

//     @Test
//     void testBorrowMagazine_Success() throws SQLException {
//         Magazine magazine = mock(Magazine.class);
//         when(magazine.getIsAvailable()).thenReturn(true);
//         when(magazine.getID()).thenReturn("789");

//         try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
//             Connection mockConnection = mock(Connection.class);
//             PreparedStatement mockStatement = mock(PreparedStatement.class);

//             driverManagerMock.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);
//             when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

//             loanManagement.borrowMagazine(magazine);

//             verify(mockStatement).setBoolean(1, false);
//             verify(mockStatement).setString(2, "789");
//             verify(mockStatement).executeUpdate();
//         }
//     }
// }
