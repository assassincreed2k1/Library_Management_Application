package com.library.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.library.service.APIService;
import com.library.service.BookManagement;
import com.library.model.doc.Book;
import com.library.service.LibraryService;
import com.library.service.ServiceManager;

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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.event.EventHandler;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class LibraryHomeController {

    private LibraryService libraryService;
    private BookManagement bookManagement;
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

    @FXML
    private Label latestName1, latestName2, latestName3, latestName4;

    @FXML
    private Label latestAuthor1, latestAuthor2, latestAuthor3, latestAuthor4;

    @FXML
    private Label latestGenre1, latestGenre2, latestGenre3, latestGenre4;

    @FXML
    private Label latestAvailable1, latestAvailable2, latestAvailable3, latestAvailable4;

    @FXML
    private Label[] latestNames;
    private Label[] latestAuthors;
    private Label[] latestGenres;
    private Label[] latestAvailables;

    // Main Content
    @FXML
    private ImageView mainImageView;

    // Initialization method
    @FXML
    public void initialize() {
        this.latestNames = new Label[] { latestName1, latestName2, latestName3, latestName4 };
        this.latestAuthors = new Label[] { latestAuthor1, latestAuthor2, latestAuthor3, latestAuthor4 };
        this.latestGenres = new Label[] { latestGenre1, latestGenre2, latestGenre3, latestGenre4 };
        this.latestAvailables = new Label[] { latestAvailable1, latestAvailable2, latestAvailable3, latestAvailable4 };
        this.libraryService = ServiceManager.getLibraryService();
        this.bookManagement = ServiceManager.getBookManagement();

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

    // Handle LogOut action -- Need Fix
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

    // Handle Switch Page
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

    // Handle Add Document action --Need Fix
    private void handleAddDocument() {
        try {
            switchTo("/fxml/Library/Tools/AddDocument.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle Remove Document action --Need Fix
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

    // Handle Show All action -- Need Fix
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
                showLatestDocsImg();
            } else if (newTab == oldestBooks) {
                // show Oldest Documents
            }
        });
    }

    // Show Cover of Latest Documents --Need Fix: Add database
    private final Map<String, Image> imageCache = new HashMap<>();

    private Image getCachedImage(String imageUrl) {
        if (imageCache.containsKey(imageUrl)) {
            return imageCache.get(imageUrl);
        }
        Image image = new Image(imageUrl, true);
        imageCache.put(imageUrl, image);
        return image;
    }

    private void showLatestDocsImg() {
        Book latestBook = bookManagement.getDocument(libraryService.getCurrentID());
        String[] bookIDs = {
                latestBook.getID(),
                String.format("%09d", Integer.parseInt(latestBook.getID()) - 1),
                String.format("%09d", Integer.parseInt(latestBook.getID()) - 2),
                String.format("%09d", Integer.parseInt(latestBook.getID()) - 3)
        };

        ImageView[] imageViews = { latestDoc1, latestDoc2, latestDoc3, latestDoc4 };

        for (int i = 0; i < bookIDs.length; i++) {
            Book book = bookManagement.getDocument(bookIDs[i]);
            this.latestNames[i].setText(book.getName());
            this.latestAuthors[i].setText(book.getAuthor());
            this.latestGenres[i].setText(book.getGroup());
            if (book.getIsAvailable()) {
                this.latestAvailables[i].setText("Yes");
            } else {
                this.latestAvailables[i].setText("No");
            }

            String isbn = libraryService.getBookISBN(bookIDs[i]);
            if (isbn != null) {
                String imageUrl = APIService.getCoverBookURL(isbn);

                // Use cache first, then load async if necessary
                Image cachedImage = getCachedImage(imageUrl);
                if (cachedImage.getProgress() < 1.0) {
                    loadImageAsync(imageUrl, imageViews[i]);
                } else {
                    imageViews[i].setImage(cachedImage);
                }
            }
        }
    }

    private void loadImageAsync(String imageUrl, ImageView imageView) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                return new Image(imageUrl, true);
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            imageCache.put(imageUrl, image); // Cache the loaded image
            imageView.setImage(image);
        });
        loadImageTask.setOnFailed(event -> System.out.println("Failed to load image: " + imageUrl));

        new Thread(loadImageTask).start();
    }

}
