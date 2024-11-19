package com.library.controller.tools;

import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.service.BookManagement;
import com.library.service.ServiceManager;
import com.library.controller.Document.BookController;
import com.library.controller.Document.MagazineController;
import com.library.controller.Document.NewspaperController;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateDocumentController {

    private Object callerController; 
    private Object selectedDocument; 

    @FXML
    private VBox fieldContainer;

    @FXML
    public void initialize() {
        setup();
    }

    public void setCallerController(Object callerController) {
        this.callerController = callerController;
    }

    public void setSelectedDocument(Object selectedDocument) {
        this.selectedDocument = selectedDocument;
    }

    private void setup() {
        if (callerController instanceof BookController) {
            Book selectedBook = (Book)selectedDocument;
            BookManagement bookManagement = ServiceManager.getBookManagement();

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
                    bookManagement.updateDocuments(selectedBook);
                    //bookTable.setItems(getBookList());
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
                    bookManagement.updateDocumentMatchingISBN(selectedBook);
                    //bookTable.setItems(getBookList());
                    statusLabel.setText("All books with matching ISBN updated successfully!");
                    statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
                } catch (Exception ex) {
                    statusLabel.setText("Failed to update books by ISBN.");
                    statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
                }
            });

            fieldContainer.getChildren().addAll(
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
        }
    }

}
