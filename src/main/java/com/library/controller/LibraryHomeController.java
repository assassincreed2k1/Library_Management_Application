package com.library.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;

public class LibraryHomeController {
    
    // Taskbar Components
    @FXML
    private ImageView iconImageView;
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label subtitleLabel;
    
    @FXML
    private Button logOutButton;
    
    @FXML
    private Label usernameLabel;

    // Mini Boxes
    @FXML
    private AnchorPane documentsPane;
    
    @FXML
    private ComboBox<String> genreComboBox;
    
    @FXML
    private ComboBox<String> booksComboBox;
    
    @FXML
    private ComboBox<String> magazinesComboBox;
    
    @FXML
    private ComboBox<String> newspapersComboBox;
    
    @FXML
    private ComboBox<String> videosComboBox;
    
    @FXML
    private ComboBox<String> albumsComboBox;
    
    @FXML
    private AnchorPane toolsPane;
    
    @FXML
    private Button addDocumentButton;
    
    @FXML
    private Button removeDocumentButton;
    
    @FXML
    private Button updateDocumentButton;
    
    @FXML
    private Button checkAvailabilityButton;
    
    @FXML
    private Button documentAddressButton;
    
    @FXML
    private Button showAllButton;

    // Main Content
    @FXML
    private ImageView mainImageView;
    
    // Initialization method
    @FXML
    public void initialize() {
        setupComboBoxes();
        setupButtons();
    }

    private void setupComboBoxes() {
        genreComboBox.getItems().addAll("Fiction", "Non-Fiction", "Science", "Biography");
        booksComboBox.getItems().addAll("Book 1", "Book 2", "Book 3");
        magazinesComboBox.getItems().addAll("Magazine 1", "Magazine 2");
        newspapersComboBox.getItems().addAll("Newspaper 1", "Newspaper 2");
        videosComboBox.getItems().addAll("Video 1", "Video 2");
        albumsComboBox.getItems().addAll("Album 1", "Album 2");
    }

    private void setupButtons() {
        logOutButton.setOnAction(event -> handleLogOut());
        addDocumentButton.setOnAction(event -> handleAddDocument());
        removeDocumentButton.setOnAction(event -> handleRemoveDocument());
        updateDocumentButton.setOnAction(event -> handleUpdateDocument());
        checkAvailabilityButton.setOnAction(event -> handleCheckAvailability());
        documentAddressButton.setOnAction(event -> handleDocumentAddress());
        showAllButton.setOnAction(event -> handleShowAll());
    }

    // Handle LogOut action
    private void handleLogOut() {
        System.out.println("Logging out...");
        try { 
            showAlert("Log Out", "Are you sure you want to log out?");
            switchTo("/fxml/Login/SignIn.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle Add Document action
    private void handleAddDocument() {
        System.out.println("Adding a document...");
    }

    // Handle Remove Document action
    private void handleRemoveDocument() {
        System.out.println("Removing a document...");
    }

    // Handle Update Document action
    private void handleUpdateDocument() {
        System.out.println("Updating a document...");
    }

    // Handle Check Availability action
    private void handleCheckAvailability() {
        System.out.println("Checking document availability...");
    }

    // Handle Document Address action
    private void handleDocumentAddress() {
        System.out.println("Getting document's address...");
    }

    // Handle Show All action
    private void handleShowAll() {
        System.out.println("Showing all documents...");
        // try {
        //     switchTo("/fxml/Documents/Documents.fxml");
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    // Switch to another page
    private void switchTo(String pagePath) throws IOException {
        Parent libraryPage = FXMLLoader.load(getClass().getResource(pagePath));
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        Scene scene = new Scene(libraryPage);
        stage.setScene(scene);
        stage.show();
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
