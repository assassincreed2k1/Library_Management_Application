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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RemoveDocumentController {

    private Object callerController;
    private Object selectedDocument;

    @FXML
    private VBox mainBox;

    @FXML
    public void initialize() {
        mainBox.setPadding(new Insets(10));
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
            removeBookSetup();
        } else if (callerController instanceof MagazineController) {
            removeMagazineSetup();
        } else if (callerController instanceof NewspaperController) {
            removeNewspaperSetup();
        } else {
            System.out.println("Invalid caller controller");
        }
    }

    private void removeBookSetup() {
        Book selectedBook = (Book) selectedDocument;
        BookManagement bookManagement = ServiceManager.getBookManagement();

        double anchorWidth = 400;
        double fieldWidth = anchorWidth * 0.8;
        double startX = (anchorWidth - fieldWidth) / 2;
        double startY = 20;

        TextField idField = new TextField();
        idField.setPromptText("ID");
        idField.setLayoutX(startX);
        idField.setLayoutY(startY);
        idField.setPrefWidth(fieldWidth);

        Button checkButton = new Button("Check");
        Button removeButton = new Button("Remove");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        statusLabel.setWrapText(true);

        checkButton.setLayoutX(startX);
        checkButton.setLayoutY(startY + 40);
        removeButton.setLayoutX(startX + fieldWidth + 10);
        removeButton.setLayoutY(startY + 40);

        checkButton.setOnAction(event -> {
            String id = idField.getText();
            if (id.isEmpty()) {
                statusLabel.setText("Please enter an ID.");
                statusLabel.setStyle("-fx-text-fill: red;");
            } else {
                Book foundBook = bookManagement.getDocument(id);
                if (foundBook != null) {
                    statusLabel.setText("Book found:\nName: " + foundBook.getName()
                            + "\nAuthor: " + foundBook.getAuthor()
                            + "\nGenre: " + foundBook.getGroup()
                            + "\nPublish Date: " + foundBook.getPublishDate()
                            + "\nISBN: " + foundBook.getISBN());
                    statusLabel.setStyle("-fx-text-fill: green;");
                } else {
                    statusLabel.setText("No book found with ID: " + id);
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });

        removeButton.setOnAction(event -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                statusLabel.setText("Please enter an ID to remove.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Removal");
            alert.setHeaderText("Are you sure you want to remove this book?");
            alert.setContentText("Book ID: " + id);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        bookManagement.removeDocument(bookManagement.getDocument(id));
                        statusLabel.setText("Book with ID " + id + " removed successfully.");
                        statusLabel.setStyle("-fx-text-fill: green;");
                        idField.clear();
                    } catch (Exception e) {
                        statusLabel.setText("Failed to remove book.");
                        statusLabel.setStyle("-fx-text-fill: red;");
                        e.printStackTrace();
                    }
                } else {
                    statusLabel.setText("Removal canceled.");
                    statusLabel.setStyle("-fx-text-fill: orange;");
                }
            });
        });

        if (selectedDocument != null) {
            idField.setText(selectedBook.getID());
            checkButton.fire();
        }

        mainBox.getChildren().addAll(idField, checkButton, removeButton, statusLabel);
    }

    private void removeMagazineSetup() {
        Magazine selectedMagazine = (Magazine) selectedDocument;
        MagazineManagement magazineManagement = ServiceManager.getMagazineManagement();

        Label confirmationLabel = new Label(
                "Are you sure you want to delete the magazine: " + selectedMagazine.getName() + "?");
        confirmationLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        confirmButton.setOnAction(e -> {
            try {
                magazineManagement.removeDocument(selectedMagazine);
                statusLabel.setText("Magazine deleted successfully!");
                statusLabel.setStyle("-fx-text-fill: green;");
            } catch (Exception ex) {
                statusLabel.setText("Failed to delete magazine.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        cancelButton.setOnAction(e -> closeWindow());

        mainBox.getChildren().addAll(confirmationLabel, confirmButton, cancelButton, statusLabel);
    }

    private void removeNewspaperSetup() {
        Newspaper selectedNewspaper = (Newspaper) selectedDocument;

        Label confirmationLabel = new Label(
                "Are you sure you want to delete the newspaper: " + selectedNewspaper.getName() + "?");
        confirmationLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        confirmButton.setOnAction(e -> {
            try {
                ServiceManager.getNewsPaperManagament().removeDocument(selectedNewspaper);
                statusLabel.setText("Newspaper deleted successfully!");
                statusLabel.setStyle("-fx-text-fill: green;");
            } catch (Exception ex) {
                statusLabel.setText("Failed to delete newspaper.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        cancelButton.setOnAction(e -> closeWindow());

        mainBox.getChildren().addAll(confirmationLabel, confirmButton, cancelButton, statusLabel);
    }

    private void closeWindow() {
        Stage stage = (Stage) mainBox.getScene().getWindow();
        stage.close();
    }
}
