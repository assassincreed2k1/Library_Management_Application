package com.library.controller.Library;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.library.controller.tools.DocumentDisplayManager;
import com.library.controller.tools.SearchBookController;
import com.library.model.Person.User;
import com.library.service.BookManagement;
import com.library.service.LibraryService;
import com.library.service.ServiceManager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LibraryForBorrowerController {
    private LibraryService libraryService;
    private BookManagement bookManagement;
    private DocumentDisplayManager latestDocsManager;
    private DocumentDisplayManager oldestDocsManager;

    public static Map<String, Image> imageCache = new HashMap<>();

    @FXML
    private Label usernameLabel;

    @FXML
    private AnchorPane documentsPane;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<String> menuComboBox;

    @FXML
    private ComboBox<String> booksComboBox;

    @FXML
    private ComboBox<String> magazinesComboBox;

    @FXML
    private ComboBox<String> newspapersComboBox;

    @FXML
    private AnchorPane toolsPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Button showAllButton;

    @FXML
    private Button borrowedBooksButton;

    @FXML
    private Hyperlink moreBooks1, moreBooks2;

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
    private ImageView[] latestImageViews = { latestDoc1, latestDoc2, latestDoc3, latestDoc4 };

    private Label[] oldestNames;
    private Label[] oldestAuthors;
    private Label[] oldestGenres;
    private Label[] oldestAvailables;
    private ImageView[] oldestImageViews = { oldestDoc1, oldestDoc2, oldestDoc3, oldestDoc4 };


    @FXML
    private Hyperlink[] moreListBooks;

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
        this.latestImageViews = new ImageView[] { latestDoc1, latestDoc2, latestDoc3, latestDoc4 };

        this.oldestNames = new Label[] { oldestName1, oldestName2, oldestName3, oldestName4 };
        this.oldestAuthors = new Label[] { oldestAuthor1, oldestAuthor2, oldestAuthor3, oldestAuthor4 };
        this.oldestGenres = new Label[] { oldestGenre1, oldestGenre2, oldestGenre3, oldestGenre4 };
        this.oldestAvailables = new Label[] { oldestAvailable1, oldestAvailable2, oldestAvailable3, oldestAvailable4 };
        this.oldestImageViews = new ImageView[] { oldestDoc1, oldestDoc2, oldestDoc3, oldestDoc4 };

        this.moreListBooks = new Hyperlink[] { moreBooks1, moreBooks2 };

        this.libraryService = ServiceManager.getLibraryService();
        this.bookManagement = ServiceManager.getBookManagement();

        this.latestDocsManager = new DocumentDisplayManager(bookManagement, libraryService,
                latestImageViews, latestNames, latestAuthors, latestGenres, latestAvailables);

        this.oldestDocsManager = new DocumentDisplayManager(bookManagement, libraryService,
                oldestImageViews, oldestNames, oldestAuthors, oldestGenres, oldestAvailables);

        this.usernameLabel.setText("Welcome " + User.getLastName() + " !");

        setupComboBoxes();
        setupButtons();
        setUpTabPane();
    }

    private void setupComboBoxes() {
        menuComboBox.getItems().addAll("Update Infor", "Log Out");
        typeComboBox.getItems().addAll("Books", "Magazines", "Newspapers");
        booksComboBox.getItems().addAll("All Books");
        magazinesComboBox.getItems().addAll("All Magazines");
        newspapersComboBox.getItems().addAll("All Newspapers");
        
    }

    private void setupButtons() {
        setComboBoxHandler(booksComboBox);
        setComboBoxHandler(magazinesComboBox);
        setComboBoxHandler(newspapersComboBox);
        setComboBoxHandler(menuComboBox);
        searchButton.setOnAction(event -> handleSearchDocuments());
        showAllButton.setOnAction(event -> handleShowAll());
        borrowedBooksButton.setOnAction(event -> handleBorrowBookButton());

        for (int i = 0; i < 2; i++) {
            moreListBooks[i].setOnAction(event -> {
                try {
                    libraryService.switchTo("/fxml/Documents/Books.fxml", (Stage) usernameLabel.getScene().getWindow());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }
    
    private void setComboBoxHandler(ComboBox<String> comboBox) {
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectedPage = comboBox.getValue();
                if (selectedPage == "Log Out") {
                    System.out.println("Logging out...");
                    try {
                        showAlert("Log Out", "Logging out!");
                        User.clearUser(); // xoá thông tin User trước khi ra khỏi
                        libraryService.switchTo("/fxml/Login/SignIn.fxml",
                                (Stage) usernameLabel.getScene().getWindow());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    switchToPage(selectedPage);
                }
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
            libraryService.switchTo(fxmlFile, (Stage) usernameLabel.getScene().getWindow());
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

    // Handle Show All action -- Need Fix
    private void handleShowAll() {
        System.out.println("Showing all documents...");
        try {
            libraryService.switchTo("/fxml/Documents/Documents.fxml", (Stage) usernameLabel.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Borrow Books
    private void handleBorrowBookButton() {
        try {
            libraryService.switchTo("/fxml/Library/Tools/BorrowingHistoryForMember.fxml", (Stage) usernameLabel.getScene().getWindow());
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

    private void setUpTabPane() {
        latestDocsManager.showDocuments(Integer.parseInt(libraryService.getCurrentID()), true);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == latestBooks) {
                latestDocsManager.showDocuments(Integer.parseInt(libraryService.getCurrentID()), true);
            } else if (newTab == oldestBooks) {
                oldestDocsManager.showDocuments(1, false);
            }
        });
    }

    private void openNewWindow(String name) {
        try {
            // Load file FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(name));
            Scene scene = new Scene(fxmlLoader.load());

            // Create new Window
            Stage newStage = new Stage();
            newStage.setTitle("New Window");
            newStage.setScene(scene);
            newStage.centerOnScreen();
            newStage.setResizable(false);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
