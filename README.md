# Library Management System

## Introduction
The **Library Management System** is a Java-based application designed to assist with library management, including managing documents (books, magazines, newspapers), users (members, librarians), and borrowing/return transactions.  

## Features
- **Document Management:**
  - Add, edit, and delete documents (books, magazines, newspapers).
  - Search and view document details.
- **User Management:**
  - Register and log in to accounts.
  - Manage member and librarian information.
  - Renew membership cards.
- **Transactions:**
  - Borrow and return documents.
  - View transaction history.
- **Others:**
  - Review and rate books.
  - User-friendly interface with CSS and FXML support.

## System Requirements
- **Language:** Java 17 or higher.
- **Development Tools:** IntelliJ IDEA or Eclipse.
- **Libraries/Dependencies:** 
  - JavaFX for the user interface.
  - JUnit for testing.
  - Other dependencies are declared in `module-info.java`.

## How to Use
1. **Clone the repository:**
   - Open a terminal and run:
     ```bash
     git clone <repository-url>
     cd library-management-system
     ```

2. **Set up the development environment:**
   - Open the project in your preferred IDE (IntelliJ IDEA or Eclipse).
   - Ensure that Java 17 or later is installed on your system.
   - Configure JavaFX by setting the `--module-path` and `--add-modules` arguments in your IDE's runtime options.
   - Make sure all dependencies specified in `module-info.java` are resolved.

3. **Run the application:**
   - Locate the `App.java` file in `src/main/java/com/library`.
   - Run the file to start the application.

4. **Execute tests:**
   - Navigate to `src/test/java` in your IDE.
   - Run test files using JUnit to verify the application’s functionality.

5. **Explore the application:**
   - Use the provided UI to manage documents, users, and transactions.
   - Features such as book reviews, borrowing history, and user management are accessible via the application interface.

