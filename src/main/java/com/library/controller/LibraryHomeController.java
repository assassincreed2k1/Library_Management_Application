package com.library.controller;

import java.io.IOException;

import com.library.service.APIService;
import com.library.service.BookManagement;
import com.library.service.LibraryService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.EventHandler;

public class LibraryHomeController {

    private LibraryService libraryService;

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
    private ComboBox<String> typeComboBox;
    
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

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab latestBooks, oldestBooks;

    @FXML
    private ImageView latestDoc1, latestDoc2, latestDoc3, latestDoc4;

    @FXML
    private ImageView oldestDoc1, oldestDoc2, oldestDoc3, oldestDoc4;

    // Main Content
    @FXML
    private ImageView mainImageView;
    
    // Initialization method
    @FXML
    public void initialize() {
        this.libraryService = ServiceManager.getLibraryService();
        setupComboBoxes();
        setupButtons();
        setUpTabPane();
    }

    private void setupComboBoxes() {
        typeComboBox.getItems().addAll("Books", "Magazines", "Newspapers");
        booksComboBox.getItems().addAll("All Books");
        magazinesComboBox.getItems().addAll("All Magazines");
        newspapersComboBox.getItems().addAll("All Newspapers");
        videosComboBox.getItems().addAll("All Videos");
        albumsComboBox.getItems().addAll("All Albums");
    }

    private void setupButtons() {
        logOutButton.setOnAction(event -> handleLogOut());

        setComboBoxHandler(booksComboBox);
        setComboBoxHandler(magazinesComboBox);
        setComboBoxHandler(newspapersComboBox);

        addDocumentButton.setOnAction(event -> handleAddDocument());
        removeDocumentButton.setOnAction(event -> handleRemoveDocument());
        updateDocumentButton.setOnAction(event -> handleUpdateDocument());
        checkAvailabilityButton.setOnAction(event -> handleCheckAvailability());
        documentAddressButton.setOnAction(event -> handleDocumentAddress());
        showAllButton.setOnAction(event -> handleShowAll());


    }

    // Handle LogOut action  -- Need Fix
    private void handleLogOut() {
        System.out.println("Logging out...");
        try {
            showAlert("Log Out", "Are you sure you want to log out?");
            switchTo("/fxml/Login/SignIn.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setComboBoxHandler(ComboBox<String> comboBox) {
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectedPage = comboBox.getValue();
                switchToPage(selectedPage);
            }
        });
    }
    
    //Handle Switch Page
    private void switchToPage(String pageName) {
        String fxmlFile = "";
        switch (pageName) {
            case "All Books":
                fxmlFile = "/fxml/Documents/Books.fxml";
                break;
            case "All Magazines":
                fxmlFile = "/fxml/Documents/Magazines.fxml";
                break;
            case "All Newspapers":
                fxmlFile = "/fxml/Documents/Newspapers.fxml";
                break;
            default:
                break;
        }

        try {
            switchTo(fxmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle Add Document action    --Need Fix
    private void handleAddDocument() {
        try {
            switchTo("/fxml/Library/Tools/AddDocument.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle Remove Document action   --Need Fix
    private void handleRemoveDocument() {
        try {
            switchTo("/fxml/Library/Tools/RemoveDocument.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // Handle Show All action   -- Need Fix
    private void handleShowAll() {
        System.out.println("Showing all documents...");
        try {
            switchTo("/fxml/Documents/Documents.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void setUpTabPane() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == latestBooks) {
                showLatestDocs();
            } else if (newTab == oldestBooks) {
                showOldestDocs();
            }
        });
    }

    // Show Cover of Latest Documents     --Need Fix: Add database
    private void showLatestDocs() {
        String lastBookID = libraryService.getCurrentID();
        int lastBookIdInt = Integer.parseInt(lastBookID);

        String firstBookID = (lastBookIdInt < 1000000000) ? String.format("%09d", lastBookIdInt)
                : String.valueOf(lastBookIdInt);
        String secondBookID = (lastBookIdInt < 1000000000) ? String.format("%09d", lastBookIdInt - 1)
                : String.valueOf(lastBookIdInt - 1);
        String thirdBookID = (lastBookIdInt < 1000000000) ? String.format("%09d", lastBookIdInt - 2)
                : String.valueOf(lastBookIdInt - 2);
        String forthBookID = (lastBookIdInt < 1000000000) ? String.format("%09d", lastBookIdInt - 3)
                : String.valueOf(lastBookIdInt - 3);

        String firstBookISBN = libraryService.getBookISBN(firstBookID);
        String secondBookISBN = libraryService.getBookISBN(secondBookID);
        String thirdBookISBN = libraryService.getBookISBN(thirdBookID);
        String forthBookISBN = libraryService.getBookISBN(forthBookID);

        if (firstBookISBN != null) {
            latestDoc1.setImage(new Image(APIService.getCoverBookURL(firstBookISBN)));
        }
        if (secondBookISBN != null) {
            latestDoc2.setImage(new Image(APIService.getCoverBookURL(secondBookISBN)));
        }
        if (thirdBookISBN != null) {
            latestDoc3.setImage(new Image(APIService.getCoverBookURL(thirdBookISBN)));
        }
        if (forthBookISBN != null) {
            latestDoc4.setImage(new Image(APIService.getCoverBookURL(forthBookISBN)));
        }
    }
    
    // // Show Cover of Oldest Documents --Need Fix: Add database
    private void showOldestDocs() {
        // oldestDoc1.setImage(new Image(""));
        // oldestDoc2.setImage(new Image(""));
        // oldestDoc3.setImage(new Image(""));
        // oldestDoc4.setImage(new Image(""));
    }
}
