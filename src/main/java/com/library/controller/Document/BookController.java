package com.library.controller.Document;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;

import com.library.controller.tools.UpdateDocumentController;
import com.library.model.doc.Book;
import com.library.service.BackgroundService;
import com.library.service.BookManagement;
import com.library.service.ServiceManager;

public class BookController {

    private BookManagement bookManagement;

    private BackgroundService executor;

    @FXML
    private AnchorPane taskBar;

    @FXML
    Button exitButton;

    @FXML
    private TableView<Book> bookTable;

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

    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    private void initialize() {
        this.bookManagement = ServiceManager.getBookManagement();
        this.executor = ServiceManager.getBackgroundService();

        // Set up the columns to use properties from the Book class
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        publishDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        bookTable.setOnMouseClicked(event -> showSelectedBookDetails());
        bookTable.setOnKeyPressed(event -> showSelectedBookDetails());

        prevImage.setOnMouseClicked(event -> showPreview());

        exitButton.setOnAction(event -> {
            try {
                FXMLLoader editPage = new FXMLLoader(getClass().getResource("/fxml/Library/LibraryHome.fxml"));
                Parent root = editPage.load();

                Stage stage = (Stage) exitButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loadBookListAsync();
    }

    private void loadBookListAsync() {
        Task<ObservableList<Book>> task = new Task<>() {
            @Override
            protected ObservableList<Book> call() {
                System.out.println("Running loadBookListAsync()...");
                return getBookList();
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

        /* 
        editButton.setOnAction(event -> {
            Stage editStage = new Stage();
            editStage.setTitle("Edit Book Information");

            VBox editPane = new VBox(10);
            editPane.setPadding(new Insets(10));

            Label bookIdLabel = new Label("ID: " + selectedBook.getID());
            bookIdLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            TextField titleField = new TextField(selectedBook.getName());
            TextField authorField = new TextField(selectedBook.getAuthor());
            TextField genreField = new TextField(selectedBook.getGroup());
            TextField publishDateField = new TextField(selectedBook.getPublishDate());
            TextField isbnField = new TextField(selectedBook.getISBN());
            CheckBox availabilityCheckBox = new CheckBox("Available");
            availabilityCheckBox.setSelected(selectedBook.getIsAvailable());

            Label statusLabel = new Label();
            statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

            Button updateThisBookButton = new Button("Update This Book");
            updateThisBookButton.setOnAction(e -> {
                selectedBook.setName(titleField.getText());
                selectedBook.setAuthor(authorField.getText());
                selectedBook.setGroup(genreField.getText());
                selectedBook.setPublishDate(publishDateField.getText());
                selectedBook.setISBN(isbnField.getText());
                selectedBook.setIsAvailable(availabilityCheckBox.isSelected());

                try {
                    this.bookManagement.updateDocuments(selectedBook);
                    bookTable.setItems(getBookList());
                    statusLabel.setText("Book updated successfully!");
                    statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
                } catch (Exception ex) {
                    statusLabel.setText("Failed to update book.");
                    statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
                }
            });

            Button updateMatchingIsbnButton = new Button("Update All by ISBN");
            updateMatchingIsbnButton.setOnAction(e -> {
                selectedBook.setName(titleField.getText());
                selectedBook.setAuthor(authorField.getText());
                selectedBook.setGroup(genreField.getText());
                selectedBook.setPublishDate(publishDateField.getText());
                selectedBook.setISBN(isbnField.getText());
                selectedBook.setIsAvailable(availabilityCheckBox.isSelected());

                try {
                    this.bookManagement.updateDocumentMatchingISBN(selectedBook);
                    bookTable.setItems(getBookList());
                    statusLabel.setText("All books with matching ISBN updated successfully!");
                    statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
                } catch (Exception ex) {
                    statusLabel.setText("Failed to update books by ISBN.");
                    statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
                }
            });

            editPane.getChildren().addAll(
                    bookIdLabel,
                    new Label("Title:"), titleField,
                    new Label("Author:"), authorField,
                    new Label("Genre:"), genreField,
                    new Label("Publish Date:"), publishDateField,
                    new Label("ISBN:"), isbnField,
                    availabilityCheckBox,
                    updateThisBookButton,
                    updateMatchingIsbnButton,
                    statusLabel);

            Scene editScene = new Scene(editPane, 400, 500);
            editStage.setScene(editScene);

            editStage.show();
        });
        */

        editButton.setOnAction(event -> {
            try {
                FXMLLoader editPage = new FXMLLoader(getClass().getResource("/fxml/Library/Tools/updateDocument.fxml"));
                Parent root = editPage.load();

                UpdateDocumentController upController = editPage.getController();
                upController.setCallerController(this);
                upController.setSelectedDocument(selectedBook);
                upController.setup();

                Stage stage = new Stage();
                stage.setTitle("Update Document");
                stage.setScene(new Scene(root));

                stage.setOnCloseRequest(event2 -> {
                    bookTable.setItems(getBookList());
                });

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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

    private ObservableList<Book> getBookList() {
        return bookManagement.getAllBooks();
    }
}