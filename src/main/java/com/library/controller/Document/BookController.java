package com.library.controller.Document;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.library.controller.ServiceManager;
import com.library.model.doc.Book;
import com.library.service.BookManagement;

public class BookController {

    private BookManagement bookManagement;

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private Task<Void> currentTask;

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

        // Set up the columns to use properties from the Book class
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        publishDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        prevImage.setOnMouseClicked(event -> showSelectedBookDetails());

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

        executor.submit(task);
    }

    private void showSelectedBookDetails() {
        if (currentTask != null && currentTask.isRunning()) {
            System.out.println("Task Cancelled");
            currentTask.cancel();
        }
    
        currentTask = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showSelectedBookDetails()...");
    
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null && !isCancelled()) {
                    Platform.runLater(() -> updateBookDetails(selectedBook));
                }
                return null;
            }
    
            @Override
            protected void succeeded() {
                System.out.println("Succeeded!");
            }
    
            @Override
            protected void cancelled() {
                System.out.println("Task Cancelled");
            }
    
            @Override
            protected void failed() {
                System.out.println("Task Error");
            }
        };
    
        executor.submit(currentTask);
    }

    private void updateBookDetails(Book selectedBook) {
        moreInfoPane.getChildren().clear();
        prevImage.setImage(new Image(selectedBook.getImagePreview()));

        Label idLabel = new Label("ID: " + selectedBook.getID());
        Label titleLabel = new Label("Title: " + selectedBook.getName());
        Label authorLabel = new Label("Author: " + selectedBook.getAuthor());
        Label genreLabel = new Label("Genre: " + selectedBook.getGroup());
        Label publishDateLabel = new Label("Publish Date: " + selectedBook.getPublishDate());
        Label isbnLabel = new Label("ISBN: " + selectedBook.getISBN());
        Label availabilityLabel = new Label("Available: " + (selectedBook.getIsAvailable() ? "Yes" : "No"));

        idLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        authorLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        genreLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        publishDateLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        isbnLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        availabilityLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        moreInfoPane.getChildren().addAll(idLabel, titleLabel, authorLabel, genreLabel, publishDateLabel, isbnLabel,
                availabilityLabel);

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
    }

    private ObservableList<Book> getBookList() {
        return bookManagement.getAllBooks();
    }
}