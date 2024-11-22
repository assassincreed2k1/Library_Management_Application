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
// import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
// import javafx.stage.Stage;

public class UpdateDocumentController {

    private Object callerController;
    private Object selectedDocument;

    @FXML
    private VBox mainBox;

    @FXML
    public void initialize() {
        mainBox.setPadding(new Insets(10));
        //mainBox.setAlignment(Pos.CENTER);
    }

    public void setCallerController(Object callerController) {
        System.out.println("Setting callerController: " + callerController);
        this.callerController = callerController;
    }

    public void setSelectedDocument(Object selectedDocument) {
        System.out.println("Setting selectedDocument: " + selectedDocument);
        this.selectedDocument = selectedDocument;
    }

    public void setup() {
        if (callerController == null || selectedDocument == null) {
            System.out.println("CallerController or SelectedDocument is not set yet.");
            return;
        }

        if (callerController instanceof BookController) {
            bookSetup();
            System.out.print("Running " + callerController.getClass().getSimpleName() + " setup");
        } else if (callerController instanceof MagazineController) {
            magazineSetup();
            System.out.print("Running " + callerController.getClass().getSimpleName() + " setup");
        } else if (callerController instanceof NewspaperController) {
            newspaperSetup();
            System.out.println("Running newspaperSetup setup");
        } else {
            System.out.println("Invalid caller controller");
        }
    }

    private void bookSetup() {
        if (mainBox == null) {
            System.out.println("mainBox = null");
            return;
        }

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

        mainBox.getChildren().addAll(
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

        mainBox.getChildren().addAll(
                magazineIdLabel,
                new Label("Title:"), titleField,
                new Label("Genre:"), genreField,
                new Label("Publisher:"), publisherField,
                availabilityCheckBox,
                updateMagazineButton,
                statusLabel);
    }

    private void newspaperSetup() {
        Newspaper selectedNewspaper = (Newspaper) selectedDocument;

        Label newspaperIdLabel = new Label("ID: " + selectedNewspaper.getID());
        newspaperIdLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextField nameField = new TextField(selectedNewspaper.getName());
        TextField groupField = new TextField(selectedNewspaper.getGroup());
        TextField sourceField = new TextField(selectedNewspaper.getSource());
        TextField regionField = new TextField(selectedNewspaper.getRegion());
        CheckBox availabilityCheckBox = new CheckBox("Available");
        availabilityCheckBox.setSelected(selectedNewspaper.getIsAvailable());

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        Button updateNewspaperButton = new Button("Update This Newspaper");

        updateNewspaperButton.setOnAction(e -> {
            selectedNewspaper.setName(nameField.getText());
            selectedNewspaper.setGroup(groupField.getText());
            selectedNewspaper.setSource(sourceField.getText());
            selectedNewspaper.setRegion(regionField.getText());
            selectedNewspaper.setIsAvailable(availabilityCheckBox.isSelected());

            try {
                ServiceManager.getNewsPaperManagement().updateDocuments(selectedNewspaper);
                statusLabel.setText("Newspaper updated successfully!");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
            } catch (Exception ex) {
                statusLabel.setText("Failed to update newspaper.");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            }
        });

        mainBox.getChildren().addAll(
                newspaperIdLabel,
                new Label("Name:"), nameField,
                new Label("Group:"), groupField,
                new Label("Source:"), sourceField,
                new Label("Region:"), regionField,
                availabilityCheckBox,
                updateNewspaperButton,
                statusLabel);
    }

}
