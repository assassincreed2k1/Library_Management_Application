package com.library.controller.tools;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;

import com.google.zxing.WriterException;
import com.library.API.QRCodeGenerator;
import com.library.model.doc.Book;
import com.library.service.BackgroundService;
import com.library.service.BookManagement;
import com.library.service.ServiceManager;

/**
 * Controller for managing the search book interface.
 * It handles interactions between the user and the application's book
 * management system.
 */
public class SearchBookController {
    /**
     * The {@code TextFieldUpdate} class is used to encapsulate the
     * {@link TextField}
     * objects for updating book details such as title, author, genre, publish date,
     * and ISBN.
     */
    public static String keyword;

    private BookManagement bookManagement;
    private BackgroundService executor;

    @FXML
    private AnchorPane taskBar;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> idColumn;

    @FXML
    private TableColumn<Book, Boolean> isAvailableColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, String> publishDateColumn;

    @FXML
    private TableColumn<Book, String> isbnColumn;

    @FXML
    private ImageView prevImage;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private AnchorPane moreInfoPane;

    private Task<Void> showBookTask;
    private Task<Void> showPrevTask;
    private final Image defaultImagePrv = new Image(getClass().getResource("/img/prv.png").toExternalForm());
    // private final Image defaultErrImagePrv = new
    // Image(getClass().getResource("/img/prve.png").toExternalForm());
    private final Image defaultNoImagePrv = new Image(getClass().getResource("/img/Noprev.png").toExternalForm());

    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    private void initialize() {
        createQRCodeDirectory();
        this.bookManagement = ServiceManager.getBookManagement();
        this.executor = ServiceManager.getBackgroundService();

        // Set up the columns to use properties from the Book class
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        publishDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        isAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));

        isAvailableColumn.setCellFactory(column -> new TableCell<Book, Boolean>() {
            @Override
            protected void updateItem(Boolean isAvailable, boolean empty) {
                super.updateItem(isAvailable, empty);

                if (empty || isAvailable == null) {
                    setText(null);
                } else {
                    setText(isAvailable ? "Yes" : "No");
                }
            }
        });

        bookTable.setOnMouseClicked(event -> runShowBookTask());
        bookTable.setOnKeyPressed(event -> runShowBookTask());

        prevImage.setOnMouseClicked(event -> showPreview());

        showBookTask = reShowBookTask();
        showPrevTask = reShowPrevTask();

        loadBookListAsync();
    }

    private Task<Void> reShowBookTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showBookTask()...");
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                Platform.runLater(() -> updateBookDetails(selectedBook));
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("showBookTask(): Succeeded!");
            }

            @Override
            protected void cancelled() {
                System.out.println("showBookTask(): Task Cancelled");
            }

            @Override
            protected void failed() {
                System.out.println("showBookTask(): Task Error");
            }
        };
    }

    private Task<Void> reShowPrevTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showPrevTask()...");
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    Image img = new Image(selectedBook.getImagePreview(), true);
                    if (!img.isError()) {
                        Platform.runLater(() -> prevImage.setImage(img));
                    } else {
                        Platform.runLater(() -> prevImage.setImage(defaultNoImagePrv));
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("showPrevTask(): Succeeded!");
            }

            @Override
            protected void cancelled() {
                System.out.println("showPrevTask(): Task Cancelled");
            }

            @Override
            protected void failed() {
                System.out.println("showPrevTask(): Task Error");
                Platform.runLater(() -> prevImage.setImage(defaultNoImagePrv));
            }
        };
    }

    private void loadBookListAsync() {
        Task<ObservableList<Book>> loadBookListAsyncTask = new Task<>() {
            @Override
            protected ObservableList<Book> call() {
                System.out.println("Running loadBookListAsync()...");
                if (keyword != null) {
                    return bookManagement.getAllBooksFilter(keyword);
                } else {
                    return bookManagement.getAllBooks();
                }
            }
        };

        loadBookListAsyncTask.setOnSucceeded(event -> {
            System.out.println("Succeeded: loadBookListAsync()");
            bookTable.setItems(loadBookListAsyncTask.getValue());
        });

        loadBookListAsyncTask.setOnFailed(event -> {
            System.out.println("Failed to load book list.");
        });

        executor.startNewThread(loadBookListAsyncTask);
    }

    private void runShowBookTask() {
        if (showBookTask != null && showBookTask.isRunning()) {
            System.out.println("Task Cancelled");
            showBookTask.cancel();
        }

        showBookTask = reShowBookTask();

        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            System.out.println("No book selected.");
            return;
        }

        executor.startNewThread(showBookTask);
    }

    private void showPreview() {
        showPrevTask = reShowPrevTask();
        executor.startNewThread(showPrevTask);
    }

    private void updateBookDetails(Book selectedBook) {
        moreInfoPane.getChildren().clear();

        Label idLabel = createStyledLabel("ID: " + selectedBook.getID(), 5, 0);
        Label titleLabel = createStyledLabel("Title: " + selectedBook.getName(), 5, 20);
        Label authorLabel = createStyledLabel("Author: " + selectedBook.getAuthor(), 5, 40);
        Label genreLabel = createStyledLabel("Genre: " + selectedBook.getGroup(), 5, 60);
        Label publishDateLabel = createStyledLabel("Publish Date: "
                + selectedBook.getPublishDate(), 5, 80);
        Label isbnLabel = createStyledLabel("ISBN: "
                + selectedBook.getISBN(), 5, 100);
        Label availabilityLabel = createStyledLabel("Available: "
                + (selectedBook.getIsAvailable() ? "Yes" : "No"), 5, 120);

        Button editButton = createStyledButton("Edit", 5, 160, event -> openEditPage(selectedBook));
        Button deleteButton = createStyledButton("Delete", 200, 160, event -> openDeletePage(selectedBook));

        moreInfoPane.getChildren().addAll(idLabel, titleLabel, authorLabel, genreLabel,
                publishDateLabel, isbnLabel, availabilityLabel, editButton, deleteButton);

        prevImage.setImage(defaultImagePrv);
        prevImage.setOnMouseClicked(event -> showPreview());
    }

    private Label createStyledLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        label.setLayoutX(x);
        label.setLayoutY(y);
        return label;
    }

    private Button createStyledButton(String text, double x, double y, EventHandler<ActionEvent> eventHandler) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setOnAction(eventHandler);
        return button;
    }

    private void openEditPage(Book selectedBook) {
        try {
            FXMLLoader editPage = new FXMLLoader(getClass().getResource("/fxml/Library/Tools/updateDocument.fxml"));
            Parent root = editPage.load();

            UpdateDocumentController upController = editPage.getController();
            upController.setId(selectedBook.getID());

            Stage stage = new Stage();
            stage.setTitle("Update Document");
            stage.setScene(new Scene(root));

            stage.setOnCloseRequest(event -> bookTable.setItems(bookManagement.getAllBooks()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDeletePage(Book selectedBook) {
        try {
            FXMLLoader delPage = new FXMLLoader(getClass().getResource("/fxml/Library/Tools/removeDocument.fxml"));
            Parent root = delPage.load();

            RemoveDocumentController rmController = delPage.getController();
            rmController.setId(selectedBook.getID());

            Stage stage = new Stage();
            stage.setTitle("Remove Document");
            stage.setScene(new Scene(root));

            stage.setOnCloseRequest(event -> bookTable.setItems(bookManagement.getAllBooks()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setKeyWord(String keyString) {
        SearchBookController.keyword = keyString;
    }

    @FXML
    private void generateQRCode() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(Alert.AlertType.ERROR, "Generate QR Code", "Please select a book to generate QR code.");
            return;
        }

        Task<Void> generateQRCodeTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String qrData = String.format(
                        "{ \"bookID\": %s, \"title\": \"%s\", \"author\": \"%s\", \"genre\": \"%s\", \"publishedDate\": \"%s\" }",
                        selectedBook.getID(),
                        selectedBook.getName(),
                        selectedBook.getAuthor(),
                        selectedBook.getGroup(),
                        selectedBook.getPublishDate()
                );

                String filePath = "src/main/resources/img/qr_codes/book_" + selectedBook.getID() + ".png";

                try {
                    QRCodeGenerator.generateQRCodeImage(qrData, 200, 200, filePath);
                    Image qrImage = new Image(new File(filePath).toURI().toString());
                    Platform.runLater(() -> qrCodeImageView.setImage(qrImage));
                } catch (WriterException | IOException e) {
                    e.printStackTrace();
                    Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Generate QR Code", "Error generating QR Code: " + e.getMessage()));
                }

                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "QR Code Generated", "QR Code saved successfully."));
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Generate QR Code", "Error generating QR Code."));
            }
        };

        Thread qrCodeThread = new Thread(generateQRCodeTask);
        qrCodeThread.setDaemon(true);
        qrCodeThread.start();
    }

    private void createQRCodeDirectory() {
        File directory = new File("src/main/resources/img/qr_codes");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}