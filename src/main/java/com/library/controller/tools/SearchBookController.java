package com.library.controller.tools;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.library.model.doc.Book;
import com.library.service.BackgroundService;
import com.library.service.BookManagement;
import com.library.service.ServiceManager;

/**
 * Controller for managing the search book interface.
 * It handles interactions between the user and the application's book
 * management system.
 */
public class SearchBookController {
    /**
     * The {@code TextFieldUpdate} class is used to encapsulate the
     * {@link TextField}
     * objects for updating book details such as title, author, genre, publish date,
     * and ISBN.
     */
    public class TextFieldUpdate {
        public TextField titleField;
        public TextField authorField;
        public TextField genreField;
        public TextField publishDateField;
        public TextField isbnField;

        public TextFieldUpdate(){};

        public TextFieldUpdate(TextField titleField, TextField authorField, TextField genreField,
                TextField publishDateField, TextField isbnField) {
            this.titleField = titleField;
            this.authorField = authorField;
            this.genreField = genreField;
            this.publishDateField = publishDateField;
            this.isbnField = isbnField;
        }
        
    }

    private BookManagement bookManagement;
    private BackgroundService executor;
    public static String keyword;
    private TextFieldUpdate textFieldUpdate;

    @FXML
    private AnchorPane taskBar;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> idColumn;

    @FXML
    private TableColumn<Book, String> isAvailColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, String> publishDateColumn;

    @FXML
    private TableColumn<Book, String> isbnColumn;

    @FXML
    private ImageView prevImage;

    @FXML
    private AnchorPane moreInfoPane;

    /**
     * Initializes the controller after the FXML has been loaded.
     * Sets up table columns, event handlers, and loads the book list
     * asynchronously.
     */
    @FXML
    private void initialize() {
        this.bookManagement = ServiceManager.getBookManagement();
        this.executor = ServiceManager.getBackgroundService();
        this.textFieldUpdate = new TextFieldUpdate();

        // Set up the columns to use properties from the Book class
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        isAvailColumn.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        publishDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        bookTable.setOnMouseClicked(event -> showSelectedBookDetails());
        bookTable.setOnKeyPressed(event -> showSelectedBookDetails());

        prevImage.setOnMouseClicked(event -> showPreview());

        loadBookListAsync();
    }

    /**
     * Sets the keyword to be used for searching books.
     * 
     * @param keyword the keyword to search for
     */
    public static void setKeyWord(String keyword) {
        SearchBookController.keyword = keyword;
    }

    /**
     * Loads the book list asynchronously using a background thread.
     */
    private void loadBookListAsync() {
        Task<ObservableList<Book>> task = new Task<>() {
            @Override
            protected ObservableList<Book> call() {
                System.out.println("Running loadBookListAsync()...");
                return getBookList(keyword);
            }
        };

        task.setOnSucceeded(event -> {
            System.out.println("Succeeded: loadBookListAsync()");
            bookTable.setItems(task.getValue());
        });

        task.setOnFailed(event -> {
            System.out.println("Failed to load book list.");
        });

        executor.startNewThread(task);
    }

    private Task<Void> showBookTask;
    private Task<Void> showPrevTask;

    /**
     * Displays the details of the selected book in the UI.
     * Cancels any ongoing task before starting a new one.
     */
    private void showSelectedBookDetails() {
        if (showBookTask != null && showBookTask.isRunning()) {
            System.out.println("Task Cancelled");
            showBookTask.cancel();
        }

        showBookTask = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new updateBookDetails()...");

                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    Platform.runLater(() -> updateBookDetails(selectedBook));
                }
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("updateBookDetails(): Succeeded!");
            }

            @Override
            protected void cancelled() {
                System.out.println("updateBookDetails(): Task Cancelled");
            }

            @Override
            protected void failed() {
                System.out.println("updateBookDetails(): Task Error");
            }
        };

        showPrevTask = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showPrevTask()...");
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    Platform.runLater(() -> prevImage.setImage(new Image(selectedBook.getImagePreview())));
                }
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("showPrevTask(): Succeeded!");
            }

            @Override
            protected void cancelled() {
                System.out.println("showPrevTask(): Task Cancelled");
            }

            @Override
            protected void failed() {
                System.out.println("showPrevTask(): Task Error");
            }
        };

        executor.startNewThread(showBookTask);
    }

    /**
     * Displays a preview image for the currently selected book.
     * If the book does not have a preview image, a default image is displayed
     * instead.
     */
    private void showPreview() {
        showPrevTask = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showPrevTask()...");
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    if (selectedBook.getImagePreview() != null || !selectedBook.getImagePreview().isEmpty()) {
                        Platform.runLater(() -> prevImage.setImage(new Image(selectedBook.getImagePreview())));
                    } else {
                        prevImage.setImage(new Image(getClass().getResource("/img/prve.png").toExternalForm()));
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("showPrevTask(): Succeeded!");
            }

            @Override
            protected void cancelled() {
                System.out.println("showPrevTask(): Task Cancelled");
            }

            @Override
            protected void failed() {
                System.out.println("showPrevTask(): Task Error");
            }
        };
        executor.startNewThread(showPrevTask);
    }

    /**
     * Updates the details section with information about the selected book.
     *
     * @param selectedBook the book whose details are to be displayed
     */
    private void updateBookDetails(Book selectedBook) {
        moreInfoPane.getChildren().clear();

        Label idLabel = new Label("ID: " + selectedBook.getID());
        Label titleLabel = new Label("Title: " + selectedBook.getName());
        Label authorLabel = new Label("Author: " + selectedBook.getAuthor());
        Label genreLabel = new Label("Genre: " + selectedBook.getGroup());
        Label publishDateLabel = new Label("Publish Date: " + selectedBook.getPublishDate());
        Label isbnLabel = new Label("ISBN: " + selectedBook.getISBN());
        Label availabilityLabel = new Label("Available: " + (selectedBook.getIsAvailable() ? "Yes" : "No"));
        Button editButton = new Button("Edit");

        idLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        authorLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        genreLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        publishDateLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        isbnLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        availabilityLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        editButton.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        editButton.setOnAction(event -> openEditDialog(selectedBook));

        moreInfoPane.getChildren().addAll(idLabel, titleLabel, authorLabel, genreLabel, publishDateLabel, isbnLabel,
                availabilityLabel, editButton);

        idLabel.setLayoutX(5);
        idLabel.setLayoutY(0);

        titleLabel.setLayoutX(5);
        titleLabel.setLayoutY(20);

        authorLabel.setLayoutX(5);
        authorLabel.setLayoutY(40);

        genreLabel.setLayoutX(5);
        genreLabel.setLayoutY(60);

        publishDateLabel.setLayoutX(5);
        publishDateLabel.setLayoutY(80);

        isbnLabel.setLayoutX(5);
        isbnLabel.setLayoutY(100);

        availabilityLabel.setLayoutX(5);
        availabilityLabel.setLayoutY(120);

        editButton.setLayoutX(5);
        editButton.setLayoutY(160);

        prevImage.setImage(new Image(getClass().getResource("/img/prv.png").toExternalForm()));
    }

    /**
     * Retrieves a filtered list of books based on the provided keyword.
     *
     * @param keyword the search keyword used to filter the books
     * @return an ObservableList of books matching the keyword
     */
    private ObservableList<Book> getBookList(String keyword) {
        return bookManagement.getAllBooksFilter(keyword);
    }

    /**
     * Retrieves a complete list of books from the database.
     *
     * @return an ObservableList containing all books
     */
    private ObservableList<Book> getBookList() {
        return bookManagement.getAllBooks();
    }

    /**
     * Opens a dialog window for editing the details of the selected book.
     *
     * @param selectedBook the book to be edited
     */
    private void openEditDialog(Book selectedBook) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Book Information");

        VBox editPane = new VBox(10);
        editPane.setPadding(new Insets(10));

        Label bookIdLabel = new Label("ID: " + selectedBook.getID());
        bookIdLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        textFieldUpdate.titleField = new TextField(selectedBook.getName());
        textFieldUpdate.authorField = new TextField(selectedBook.getAuthor());
        textFieldUpdate.genreField = new TextField(selectedBook.getGroup());
        textFieldUpdate.publishDateField = new TextField(selectedBook.getPublishDate());
        textFieldUpdate.isbnField = new TextField(selectedBook.getISBN());
        CheckBox availabilityCheckBox = new CheckBox("Available");
        availabilityCheckBox.setSelected(selectedBook.getIsAvailable());

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        Button updateThisBookButton = new Button("Update This Book");
        updateThisBookButton.setOnAction(e -> {
            updateBook(selectedBook, textFieldUpdate,
                    availabilityCheckBox, statusLabel, false);
        });

        Button updateMatchingIsbnButton = new Button("Update All by ISBN");
        updateMatchingIsbnButton.setOnAction(e -> {
            updateBook(selectedBook, textFieldUpdate,availabilityCheckBox, statusLabel, true);
        });

        editPane.getChildren().addAll(bookIdLabel, new Label("Title:"), textFieldUpdate.titleField,
                new Label("Author:"), textFieldUpdate.authorField,
                new Label("Genre:"), textFieldUpdate.genreField,
                new Label("Publish Date:"), textFieldUpdate.publishDateField,
                new Label("ISBN:"), textFieldUpdate.isbnField,
                availabilityCheckBox, updateThisBookButton, updateMatchingIsbnButton, statusLabel);

        Scene editScene = new Scene(editPane, 400, 500);
        editStage.setScene(editScene);

        editStage.show();
    }

    /**
     * Updates the book details in the database.
     *
     * @param selectedBook         the book being updated
     * @param textFieldUpdate      all the text field to update book
     * @param availabilityCheckBox the availability checkbox
     * @param statusLabel          the status label to display feedback
     * @param updateByIsbn         whether to update all books with matching ISBN
     */
    private void updateBook(Book selectedBook, TextFieldUpdate textFieldUpdate, CheckBox availabilityCheckBox, Label statusLabel,
            boolean updateByIsbn) {
        selectedBook.setName(textFieldUpdate.titleField.getText());
        selectedBook.setAuthor(textFieldUpdate.authorField.getText());
        selectedBook.setGroup(textFieldUpdate.genreField.getText());
        selectedBook.setPublishDate(textFieldUpdate.publishDateField.getText());
        selectedBook.setISBN(textFieldUpdate.isbnField.getText());
        selectedBook.setIsAvailable(availabilityCheckBox.isSelected());

        try {
            if (updateByIsbn) {
                this.bookManagement.updateDocumentMatchingISBN(selectedBook);
                statusLabel.setText("All books with matching ISBN updated successfully!");
            } else {
                this.bookManagement.updateDocuments(selectedBook);
                statusLabel.setText("Book updated successfully!");
            }
            bookTable.setItems(getBookList());
            statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
        } catch (Exception ex) {
            statusLabel.setText("Failed to update book.");
            statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        }
    }
}