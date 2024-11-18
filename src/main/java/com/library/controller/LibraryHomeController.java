package com.library.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.library.service.APIService;
import com.library.service.BookManagement;
import com.library.model.doc.Book;
import com.library.service.LibraryService;
import com.library.service.ServiceManager;
import com.library.controller.tools.SearchBookController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * Controller class for managing the library's home page.
 * It handles interactions with the UI components and manages library data.
 */
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
    private TextField searchTextField;

    @FXML
    private Button searchButton;

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
    private Hyperlink moreButton;

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
    private Label oldestName1, oldestName2, oldestName3, oldestName4;

    @FXML
    private Label latestAuthor1, latestAuthor2, latestAuthor3, latestAuthor4;

    @FXML
    private Label oldestAuthor1, oldestAuthor2, oldestAuthor3, oldestAuthor4;

    @FXML
    private Label latestGenre1, latestGenre2, latestGenre3, latestGenre4;

    @FXML
    private Label oldestGenre1, oldestGenre2, oldestGenre3, oldestGenre4;

    @FXML
    private Label latestAvailable1, latestAvailable2, latestAvailable3, latestAvailable4;

    @FXML
    private Label oldestAvailable1, oldestAvailable2, oldestAvailable3, oldestAvailable4;

    @FXML
    private Label[] latestNames;
    private Label[] latestAuthors;
    private Label[] latestGenres;
    private Label[] latestAvailables;

    private Label[] oldestNames;
    private Label[] oldestAuthors;
    private Label[] oldestGenres;
    private Label[] oldestAvailables;

    // Main Content
    @FXML
    private ImageView mainImageView;

    /**
     * Initializes the controller and sets up the necessary components.
     * This method is called automatically when the controller is loaded.
     */
    @FXML
    public void initialize() {
        this.latestNames = new Label[] { latestName1, latestName2, latestName3, latestName4 };
        this.latestAuthors = new Label[] { latestAuthor1, latestAuthor2, latestAuthor3, latestAuthor4 };
        this.latestGenres = new Label[] { latestGenre1, latestGenre2, latestGenre3, latestGenre4 };
        this.latestAvailables = new Label[] { latestAvailable1, latestAvailable2, latestAvailable3, latestAvailable4 };

        this.oldestNames = new Label[] { oldestName1, oldestName2, oldestName3, oldestName4 };
        this.oldestAuthors = new Label[] { oldestAuthor1, oldestAuthor2, oldestAuthor3, oldestAuthor4 };
        this.oldestGenres = new Label[] { oldestGenre1, oldestGenre2, oldestGenre3, oldestGenre4 };
        this.oldestAvailables = new Label[] { oldestAvailable1, oldestAvailable2, oldestAvailable3, oldestAvailable4 };

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

        searchButton.setOnAction(event-> handleSearchDocuments());
        addDocumentButton.setOnAction(event -> handleAddDocument());
        removeDocumentButton.setOnAction(event -> handleRemoveDocument());
        updateDocumentButton.setOnAction(event -> handleUpdateDocument());
        checkAvailabilityButton.setOnAction(event -> handleCheckAvailability());
        documentAddressButton.setOnAction(event -> handleDocumentAddress());
        showAllButton.setOnAction(event -> handleShowAll());
        moreButton.setOnAction(event -> {
            try {
                switchTo("/fxml/Documents/Books.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void handleSearchDocuments() {
        String keyword = searchTextField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            throw new IllegalArgumentException("Search field cannot be empty");
        }

        SearchBookController.setKeyWord(keyword);
        try {
            openNewWindow("/fxml/Library/Tools/SearchBook.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle Add Document action --Need Fix
    private void handleAddDocument() {
        try {
            openNewWindow("/fxml/Library/Tools/AddDocument.fxml");
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
        showLatestDocs();
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == latestBooks) {
                showLatestDocs();
            } else if (newTab == oldestBooks) {
                showOldestDocs();
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

    private void showLatestDocs() {
        Book latestBook = bookManagement.getDocument(libraryService.getCurrentID());
        if (latestBook == null) {
            System.out.println("Latest Book is Empty");
            return;
        }
        String[] bookIDs = new String[4];
        bookIDs[0] = latestBook.getID();

        for (int i = 1; i < 4; i++) {
            int id = Integer.parseInt(latestBook.getID()) - i;
            bookIDs[i] = id > 0 ? String.format("%09d", id) : "";
        }

        ImageView[] imageViews = { latestDoc1, latestDoc2, latestDoc3, latestDoc4 };

        for (int i = 0; i < bookIDs.length; i++) {
            String currentID = bookIDs[i];
            if (currentID != "") {
                Book book = bookManagement.getDocument(currentID);
                this.latestNames[i].setText(book.getName());
                this.latestAuthors[i].setText(book.getAuthor());
                this.latestGenres[i].setText(book.getGroup());
                if (book.getIsAvailable()) {
                    this.latestAvailables[i].setText("Yes");
                } else {
                    this.latestAvailables[i].setText("No");
                }

                String isbn = libraryService.getBookISBN(currentID);
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
            } else {
                System.out.println("Current Book is Empty");
            }
        }
    }

    private void showOldestDocs() {
        Book latestBook = bookManagement.getDocument(libraryService.getCurrentID());
        if (latestBook == null) {
            System.out.println("BookList is Empty");
            return;
        }
        int latestBookID = Integer.parseInt(latestBook.getID());
        String[] bookIDs = new String[4];
        bookIDs[0] = "000000001";

        for (int i = 1; i < 4; i++) {
            int id = 1 + i;
            bookIDs[i] = id <= latestBookID ? String.format("%09d", id) : "";
        }

        ImageView[] imageViews = { oldestDoc1, oldestDoc2, oldestDoc3, oldestDoc4};

        for (int i = 0; i < bookIDs.length; i++) {
            String currentID = bookIDs[i];
            if (currentID != "") {
                Book book = bookManagement.getDocument(currentID);
                this.oldestNames[i].setText(book.getName());
                this.oldestAuthors[i].setText(book.getAuthor());
                this.oldestGenres[i].setText(book.getGroup());
                if (book.getIsAvailable()) {
                    this.oldestAvailables[i].setText("Yes");
                } else {
                    this.oldestAvailables[i].setText("No");
                }

                String isbn = libraryService.getBookISBN(currentID);
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
            } else {
                System.out.println("Current Book is Empty");
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

    private void openNewWindow(String name) {
        try {
            //Load file FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(name));
            Scene scene = new Scene(fxmlLoader.load());

            //Create new Window
            Stage newStage = new Stage();
            newStage.setTitle("New Window");
            newStage.setScene(scene);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
