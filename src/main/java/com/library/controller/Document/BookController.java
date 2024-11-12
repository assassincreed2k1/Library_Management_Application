package com.library.controller.Document;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import com.library.controller.ServiceManager;
import com.library.model.doc.Book;
import com.library.service.BookManagement;

public class BookController {

    private BookManagement bookManagement;

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
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("bookGroup"));
        publishDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        bookTable.setItems(getBookList());

        if (!getBookList().isEmpty()) {
            prevImage.setImage(new Image(getBookList().getFirst().getImagePreview()));
        }

    }

    @FXML
    private void showSelectedBookDetails() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
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

            moreInfoPane.getChildren().addAll(titleLabel, authorLabel, genreLabel, publishDateLabel, isbnLabel,
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
    }

    private ObservableList<Book> getBookList() {
        return bookManagement.getAllBooks();
    }
}