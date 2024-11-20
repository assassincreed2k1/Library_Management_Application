package com.library.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
// import javafx.scene.control.ComboBox;
// import javafx.scene.control.Label;

import java.io.IOException;

// import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
// import javafx.scene.image.ImageView;

public class DocumentController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnBack;

    public void initialize() {
        setupButtons();
    }

    private void setupButtons() {
        btnAdd.setOnAction(event -> handleAddBook());
        btnEdit.setOnAction(event -> handleEditBook());
        btnDelete.setOnAction(event -> handleDeleteBook());
        btnBack.setOnAction(event -> handleBack());
    }

    // Handler for Add Book button
    @FXML
    private void handleAddBook() {
        // Example action for Add Book
        showAlert("Add Book", "Add Book functionality not implemented yet.");
    }

    // Handler for Edit Book button
    @FXML
    private void handleEditBook() {
        // Example action for Edit Book
        showAlert("Edit Book", "Edit Book functionality not implemented yet.");
    }

    // Handler for Delete Book button
    @FXML
    private void handleDeleteBook() {
        // Example action for Delete Book
        showAlert("Delete Book", "Delete Book functionality not implemented yet.");
    }

    // Handler for Log Out button
    @FXML
    private void handleBack() {
        // Example action for Log Out
        // showAlert("Log Out", "Log Out functionality not implemented yet.");
        try {
            switchTo("/fxml/Library/LibraryHome.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

        // Switch to another page
        private void switchTo(String pagePath) throws IOException {
            Parent libraryPage = FXMLLoader.load(getClass().getResource(pagePath));
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            Scene scene = new Scene(libraryPage);
            stage.setScene(scene);
            stage.show();
        }
}
