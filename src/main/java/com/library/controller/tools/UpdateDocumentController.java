package com.library.controller.tools;

import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.service.BookManagement;
import com.library.service.MagazineManagement;
import com.library.service.ServiceManager;
import com.library.controller.Document.BookController;
import com.library.controller.Document.MagazineController;
import com.library.controller.Document.NewspaperController;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        fieldContainer = new VBox(10);
        fieldContainer.setPadding(new Insets(10));
        fieldContainer.setAlignment(Pos.CENTER);

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
            bookSetup();
        } else if (callerController instanceof MagazineController) {
            magazineSetup();
        } else if (callerController instanceof NewspaperController) {
            newspaperSetup();
        } else {
            System.out.println("Invalid caller controller");
        }
    }

    private void bookSetup() {
        Book selectedBook = (Book) selectedDocument;
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
        Button updateMatchingIsbnButton = new Button("Update All by ISBN");
    
        updateThisBookButton.setOnAction(e -> {
            selectedBook.setName(titleField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setGroup(genreField.getText());
            selectedBook.setPublishDate(publishDateField.getText());
            selectedBook.setISBN(isbnField.getText());
            selectedBook.setIsAvailable(availabilityCheckBox.isSelected());
    
            try {
                bookManagement.updateDocuments(selectedBook);
                statusLabel.setText("Book updated successfully!");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
            } catch (Exception ex) {
                statusLabel.setText("Failed to update book.");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            }
        });
    
        updateMatchingIsbnButton.setOnAction(e -> {
            selectedBook.setName(titleField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setGroup(genreField.getText());
            selectedBook.setPublishDate(publishDateField.getText());
            selectedBook.setISBN(isbnField.getText());
            selectedBook.setIsAvailable(availabilityCheckBox.isSelected());
    
            try {
                bookManagement.updateDocumentMatchingISBN(selectedBook);
                statusLabel.setText("All books with matching ISBN updated successfully!");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
            } catch (Exception ex) {
                statusLabel.setText("Failed to update books by ISBN.");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            }
        });
    
        VBox.setMargin(bookIdLabel, new Insets(10, 0, 5, 0));
        VBox.setMargin(titleField, new Insets(0, 0, 5, 0));
        VBox.setMargin(authorField, new Insets(0, 0, 5, 0));
        VBox.setMargin(genreField, new Insets(0, 0, 5, 0));
        VBox.setMargin(publishDateField, new Insets(0, 0, 5, 0));
        VBox.setMargin(isbnField, new Insets(0, 0, 5, 0));
        VBox.setMargin(availabilityCheckBox, new Insets(10, 0, 10, 0));
        VBox.setMargin(updateThisBookButton, new Insets(20, 0, 5, 0));
        VBox.setMargin(updateMatchingIsbnButton, new Insets(10, 0, 10, 0));
        VBox.setMargin(statusLabel, new Insets(10, 0, 0, 0));
    
        fieldContainer.setAlignment(Pos.CENTER);
        fieldContainer.setSpacing(10);
        fieldContainer.setPadding(new Insets(10));
    
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
    

    private void magazineSetup() {
        Magazine selectedMagazine = (Magazine) selectedDocument;
        MagazineManagement magazineManagement = ServiceManager.getMagazineManagement();

        Label magazineIdLabel = new Label("ID: " + selectedMagazine.getID());
        magazineIdLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextField titleField = new TextField(selectedMagazine.getName());
        TextField genreField = new TextField(selectedMagazine.getGroup());
        TextField publisherField = new TextField(selectedMagazine.getPublisher());
        CheckBox availabilityCheckBox = new CheckBox("Available");
        availabilityCheckBox.setSelected(selectedMagazine.getIsAvailable());

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        Button updateMagazineButton = new Button("Update This Magazine");

        updateMagazineButton.setOnAction(e -> {
            selectedMagazine.setName(titleField.getText());
            selectedMagazine.setGroup(genreField.getText());
            selectedMagazine.setPublisher(publisherField.getText());
            selectedMagazine.setIsAvailable(availabilityCheckBox.isSelected());

            try {
                magazineManagement.updateDocuments(selectedMagazine);
                statusLabel.setText("Magazine updated successfully!");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
            } catch (Exception ex) {
                statusLabel.setText("Failed to update magazine.");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            }
        });

        fieldContainer.getChildren().addAll(
                magazineIdLabel,
                new Label("Title:"), titleField,
                new Label("Genre:"), genreField,
                new Label("Publisher:"), publisherField,
                availabilityCheckBox,
                updateMagazineButton,
                statusLabel);
    }

    private void newspaperSetup() {

    }

}
