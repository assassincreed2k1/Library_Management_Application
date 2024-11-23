package com.library.controller.Document;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
// import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
// import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.io.IOException;

import com.library.controller.tools.RemoveDocumentController;
import com.library.controller.tools.UpdateDocumentController;
import com.library.model.doc.Book;
import com.library.service.BackgroundService;
import com.library.service.BookManagement;
import com.library.service.ServiceManager;
import com.library.service.LibraryService;

public class BookController {

    private LibraryService libraryService = new LibraryService();

    private BookManagement bookManagement;

    private BackgroundService executor;

    @FXML
    private AnchorPane taskBar;

    @FXML
    Button exitButton;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> idColumn;

    @FXML
    private TableColumn<Book, Boolean> isAvailableColumn;

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

    private Task<Void> showBookTask;
    private Task<Void> showPrevTask;
    private final Image defaultImagePrv = new Image(getClass().getResource("/img/prv.png").toExternalForm());
    //private final Image defaultErrImagePrv = new Image(getClass().getResource("/img/prve.png").toExternalForm());
    private final Image defaultNoImagePrv = new Image(getClass().getResource("/img/noprv.png").toExternalForm());

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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        isAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));

        isAvailableColumn.setCellFactory(column -> new TableCell<Book, Boolean>() {
            @Override
            protected void updateItem(Boolean isAvailable, boolean empty) {
                super.updateItem(isAvailable, empty);

                if (empty || isAvailable == null) {
                    setText(null);
                } else {
                    setText(isAvailable ? "Yes" : "No");
                }
            }
        });

        bookTable.setOnMouseClicked(event -> runShowBookTask());
        bookTable.setOnKeyPressed(event -> runShowBookTask());

        prevImage.setOnMouseClicked(event -> showPreview());

        exitButton.setOnAction(event -> {
            try {
                libraryService.switchTo("/fxml/Library/LibraryHome.fxml",
                        (Stage) exitButton.getScene().getWindow());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        showBookTask = reShowBookTask();
        showPrevTask = reShowPrevTask();

        loadBookListAsync();
    }

    private Task<Void> reShowBookTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showBookTask()...");
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                Platform.runLater(() -> updateBookDetails(selectedBook));
                return null;
            }
    
            @Override
            protected void succeeded() {
                System.out.println("showBookTask(): Succeeded!");
            }
    
            @Override
            protected void cancelled() {
                System.out.println("showBookTask(): Task Cancelled");
            }
    
            @Override
            protected void failed() {
                System.out.println("showBookTask(): Task Error");
            }
        };
    }

    private Task<Void> reShowPrevTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showPrevTask()...");
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    Image img = new Image(selectedBook.getImagePreview(), true);
                    if (!img.isError()) {
                        Platform.runLater(() -> prevImage.setImage(img));
                    } else {
                        Platform.runLater(() -> prevImage.setImage(defaultNoImagePrv));
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
                Platform.runLater(() -> prevImage.setImage(defaultNoImagePrv));
            }
        };
    }

    private void loadBookListAsync() {
        Task<ObservableList<Book>> loadBookListAsyncTask = new Task<>() {
            @Override
            protected ObservableList<Book> call() {
                System.out.println("Running loadBookListAsync()...");
                return bookManagement.getAllBooks();
            }
        };

        loadBookListAsyncTask.setOnSucceeded(event -> {
            System.out.println("Succeeded: loadBookListAsync()");
            bookTable.setItems(loadBookListAsyncTask.getValue());
        });

        loadBookListAsyncTask.setOnFailed(event -> {
            System.out.println("Failed to load book list.");
        });

        executor.startNewThread(loadBookListAsyncTask);
    }

    private void runShowBookTask() {
        if (showBookTask != null && showBookTask.isRunning()) {
            System.out.println("Task Cancelled");
            showBookTask.cancel();
        }

        showBookTask = reShowBookTask();

        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            System.out.println("No book selected.");
            return;
        }

        executor.startNewThread(showBookTask);
    }

    private void showPreview() {
        showPrevTask = reShowPrevTask();
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
        Button deleteButton = new Button("Delete");

        idLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        authorLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        genreLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        publishDateLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        isbnLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        availabilityLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        editButton.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        deleteButton.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        editButton.setOnAction(event -> {
            try {
                FXMLLoader editPage = new FXMLLoader(getClass().getResource("/fxml/Library/Tools/updateDocument.fxml"));
                Parent root = editPage.load();

                UpdateDocumentController upController = editPage.getController();
                upController.setId(selectedBook.getID());

                Stage stage = new Stage();
                stage.setTitle("Update Document");
                stage.setScene(new Scene(root));

                stage.setOnCloseRequest(event2 -> {
                    bookTable.setItems(bookManagement.getAllBooks());
                });

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteButton.setOnAction(event -> {
            try {
                FXMLLoader delPage = new FXMLLoader(getClass().getResource("/fxml/Library/Tools/removeDocument.fxml"));
                Parent root = delPage.load();

                RemoveDocumentController rmController = delPage.getController();
                rmController.setId(selectedBook.getID());

                Stage stage = new Stage();
                stage.setTitle("Remove Document");
                stage.setScene(new Scene(root));

                stage.setOnCloseRequest(event2 -> {
                    bookTable.setItems(bookManagement.getAllBooks());
                });

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        moreInfoPane.getChildren().addAll(idLabel, titleLabel, authorLabel, genreLabel, publishDateLabel, isbnLabel,
                availabilityLabel, editButton, deleteButton);

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

        deleteButton.setLayoutX(200);
        deleteButton.setLayoutY(160);

        prevImage.setImage(defaultImagePrv);
        prevImage.setOnMouseClicked(event -> showPreview());
    }
}