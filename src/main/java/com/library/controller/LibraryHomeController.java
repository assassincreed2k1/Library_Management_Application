package com.library.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    private TextField searchTextField;
    
    @FXML
    private ComboBox<String> genreComboBox;
    
    @FXML
    private Label usernameLabel;

    // Mini Boxes
    @FXML
    private AnchorPane documentsPane;
    
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
    
    @FXML
    private TabPane tabPane;

    // Initialization method
    @FXML
    public void initialize() {
        setupComboBoxes();
        setupButtons();
    }

    private void setupComboBoxes() {
        
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

    @FXML
    private void handleSearch() {
        String query = searchTextField.getText();
        System.out.println("Searching for: " + query);

    }

    private void handleLogOut() {
        System.out.println("Logging out...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login/Login.fxml"));
            Parent loginRoot= loader.load();

            Stage curStage = (Stage) logOutButton.getScene().getWindow();

            curStage.setScene(new Scene(loginRoot));
            curStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Login.fxml.");
        }
    }

    private void handleAddDocument() {
        System.out.println("Adding a document...");
        
    }

    private void handleRemoveDocument() {
        System.out.println("Removing a document...");

    }

    private void handleUpdateDocument() {
        System.out.println("Updating a document...");

    }

    private void handleCheckAvailability() {
        System.out.println("Checking document availability...");

    }

    private void handleDocumentAddress() {
        System.out.println("Getting document's address...");

    }

    private void handleShowAll() {
        System.out.println("Showing all documents...");

    }
    
    private void switchToAddBookPage() {
        
    }
}
