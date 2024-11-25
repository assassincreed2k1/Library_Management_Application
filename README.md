# Library Management System

## Available Languages
- 🇬🇧 [English](README.md)
- 🇻🇳 [Tiếng Việt](README.vi.md)

## Table of Contents
1. [Introduction](#introduction)
2. [Features](#features)
3. [System Requirements](#system-requirements)
4. [How to Use](#how-to-use)
5. [Folder Structure](#folder-structure)
6. [Authors](#authors)

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
- **Development Tools:** Visual Studio Code
- **Libraries/Dependencies:** 
  - JavaFX for the user interface.
  - JUnit for testing.
  - Other dependencies are declared in `module-info.java`.

## How to Use

1. **Download the application:**
   - Visit the [Releases](https://github.com/Nezuko1909/Library_Management_Application/releases) section of the GitHub repository.
   - Download the latest `.jar` file (`Library-management-system.jar`).

2. **Run the application:**
   - Ensure you have Java 17 or higher installed on your system. Verify the version by running:
     ```bash
     java -version
     ```
   - Navigate to the folder containing the `.jar` file and execute the following command:
     ```bash
     java -jar library-management-system.jar
     ```

3. **Interact with the application:**
   - The application will open with a graphical user interface (GUI).
   - Use its features to manage documents, users, and borrowing/return transactions.

4. **Optional: Configure runtime options (if needed):**
   - If you face issues with JavaFX, ensure the required runtime arguments are added:
     ```bash
     java --module-path <path-to-javafx> --add-modules javafx.controls,javafx.fxml -jar library-management-system.jar
     ```
     Replace `<path-to-javafx>` with the directory where JavaFX is installed on your system.

5. **Feedback and Issues:**
   - If you encounter bugs or have suggestions, report them in the [Issues](https://github.com/Nezuko1909/Library_Management_Application/issues) section of the repository.

## Folder Structure

The project is organized as follows:
```markdown
library-management-system/ 
├── src/ 
│ ├── main/ 
│ │ ├── java/ 
│ │ │ ├── com.library/ 
│ │ │ │ ├── App.java # Main application entry point 
│ │ │ │ ├── controller/ # Handles user interactions and business logic 
│ │ │ │ ├── model/ # Contains models for documents, users, and transactions 
│ │ │ │ └── service/ # Core business logic and services 
│ │ ├── resources/ 
│ │ │ ├── css/ # CSS files for styling the GUI 
│ │ │ ├── fxml/ # FXML files defining the GUI layout 
│ │ │ └── img/ # Images used in the application 
│ ├── test/ 
│ │ ├── java/ 
│ │ │ ├── com.library/ # Test cases for different components 
│ │ │ └── service/ # Unit tests for services 
├── module-info.java # Java module configuration 
└── README.md # Project documentation
```

### Key Directories
- `src/main/java/com.library`:
  - **`controller/`**: Contains controllers managing the application's user interface and logic.
  - **`model/`**: Defines models for documents (e.g., books, newspapers), users, and transactions.
  - **`service/`**: Implements business logic like document management, user handling, and transactions.
- `src/main/resources`:
  - **`css/`**: Custom CSS files for enhancing the application's look and feel.
  - **`fxml/`**: GUI layout definitions using JavaFX FXML files.
  - **`img/`**: Icons and images used across the application.
- `src/test/java`:
  - Contains JUnit test files for testing controllers and services.

### Notable Files
- `App.java`: The main entry point for running the application.
- `module-info.java`: Declares Java modules and their dependencies.

## Authors

This project is developed and maintained by:

- **thanhbt25**
- **Nghia**
- **Tv.Quyen** 
- **ChauKhanhLy**

For feedback or contributions, please contact us via [GitHub Issues](https://github.com/Nezuko1909/Library_Management_Application/issues).
