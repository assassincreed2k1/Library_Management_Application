package com.library.controller.tools;

import java.lang.Exception;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.service.APIService;
import com.library.service.BookManagement;
import com.library.service.MagazineManagement;
import com.library.service.NewsPaperManagement;
import com.library.service.ServiceManager;
import com.library.service.LibraryService;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.concurrent.Task;
import javafx.application.Platform;
import javafx.concurrent.Service;

public class AddDocumentController {

    private BookManagement bookManagement;
    private MagazineManagement magazineManagement;
    private NewsPaperManagement newsPaperManagament;
    private LibraryService libraryService;

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView iconImageView;

    @FXML
    private Button addButton;

    @FXML
    private ComboBox<String> documentTypeComboBox;

    @FXML
    private Text documentTypeText;

    @FXML
    private AnchorPane getDocumentInfoPane;

    @FXML
    private ImageView docImagePreview;

    private final Image defaultDocImgPrev = new Image(getClass().getResource("/img/Add.png").toExternalForm());

    private AnchorPane inputPane;
    private double anchorWidth = 400;
    private double fieldWidth = anchorWidth * 0.8;
    private double startX = (anchorWidth - fieldWidth) / 2;
    private double startY = 20;
    private double spacing = 35;
    Label errorLabel;

    @FXML
    public void initialize() {
        this.bookManagement = ServiceManager.getBookManagement();
        this.magazineManagement = ServiceManager.getMagazineManagement();
        this.newsPaperManagament = ServiceManager.getNewsPaperManagement();
        this.libraryService = ServiceManager.getLibraryService();

        inputPane = new AnchorPane();
        inputPane.setId("inputPane");

        documentTypeComboBox.getItems().addAll("Book", "Magazine", "Newspaper");

        documentTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            createInputFields(newValue);
        });
    }

    private void setDocImagePreview() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set Image Preview");
        dialog.setHeaderText("Enter the image URL below:");
        dialog.setContentText("Image URL:");

        dialog.showAndWait().ifPresent(url -> {
            if (url != null && !url.isEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
                if (url.matches("https?://.*\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                    Image img = new Image(url);
                    if (img.isError()) {
                        docImagePreview.setImage(defaultDocImgPrev);
                        errorLabel.setText("Error loading image. Please check the URL.");
                        errorLabel.setTextFill(Color.RED);
                        docImagePreview.setOnMouseClicked(event -> setDocImagePreview());
                    } else {
                        docImagePreview.setImage(img);
                        errorLabel.setText("Image set successfully.");
                        errorLabel.setTextFill(Color.GREEN);
                        docImagePreview.setOnMouseClicked(null);
                    }
                } else {
                    docImagePreview.setImage(defaultDocImgPrev);
                    errorLabel.setText("Invalid image URL format.");
                    errorLabel.setTextFill(Color.RED);
                    docImagePreview.setOnMouseClicked(event -> setDocImagePreview());
                }
            }
        });
    }

    private void createInputFields(String documentType) {
        getDocumentInfoPane.getChildren().clear();
        inputPane.getChildren().clear();

        if (documentType.equals("Book")) {
            this.createForBook();
        } else if (documentType.equals("Magazine")) {
            this.createForMagazine();
        } else if (documentType.equals("Newspaper")) {
            this.createForNewspaper();
        }

        getDocumentInfoPane.getChildren().add(inputPane);
    }

    private Label createErrorLabel(double layoutX, double layoutY) {
        Label errorLabel = new Label();
        errorLabel.setLayoutX(layoutX);
        errorLabel.setLayoutY(layoutY);
        errorLabel.setTextFill(javafx.scene.paint.Color.RED);
        return errorLabel;
    }

    private TextField createTextField(String promptText, double layoutX,
            double layoutY, double prefWidth) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setLayoutX(layoutX);
        textField.setLayoutY(layoutY);
        textField.setPrefWidth(prefWidth);
        return textField;
    }

    private void createForBook() {
        TextField isbnField = createTextField("Enter ISBN",
                startX, startY, fieldWidth);
        TextField titleField = createTextField("Enter Title",
                startX, startY + spacing, fieldWidth);
        TextField authorField = createTextField("Enter Author",
                startX, startY + spacing * 2, fieldWidth);
        TextField genreField = createTextField("Enter Genre",
                startX, startY + spacing * 3, fieldWidth);
        TextField publishDateField = createTextField("Enter Publish Date",
                startX, startY + spacing * 4, fieldWidth);
        TextField quantityField = createTextField("Enter Quantity",
                startX, startY + spacing * 5, fieldWidth);

        errorLabel = createErrorLabel(startX, startY + spacing * 6);

        inputPane.getChildren().addAll(isbnField, titleField,
                authorField, genreField,
                publishDateField, quantityField, errorLabel);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Service<JSONObject> bookInfoService = new Service<>() {
            @Override
            protected Task<JSONObject> createTask() {
                return new Task<>() {
                    @Override
                    protected JSONObject call() throws Exception {
                        return APIService.getBookInfoByISBN(isbnField.getText());
                    }
                };
            }
        };

        scheduler.schedule(() -> {
            if (bookInfoService.isRunning()) {
                bookInfoService.cancel(); 
                Platform.runLater(() -> {
                    errorLabel.setText("Service timed out.");
                    errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                });
            }
        }, 5, TimeUnit.SECONDS);

        bookInfoService.setOnRunning(event -> {
            errorLabel.setText("Loading book from API...");
            errorLabel.setTextFill(javafx.scene.paint.Color.BLUE);
        });

        bookInfoService.setOnSucceeded(event -> {
            JSONObject getAPIBook = bookInfoService.getValue();
            if (getAPIBook != null && getAPIBook.has("ISBN:" + isbnField.getText())) {
                JSONObject bookData = getAPIBook.getJSONObject("ISBN:" + isbnField.getText());

                titleField.setText(bookData.optString("title", "Unknown Title"));

                JSONArray authorsArray = bookData.getJSONArray("authors");
                String athString = "";
                if (authorsArray.length() > 0) {
                    for (int i = 0; i < authorsArray.length(); i++) {
                        athString += authorsArray.getJSONObject(i).optString("name",
                                "") + ", ";
                    }
                    if (!athString.isEmpty()) {
                        athString = athString.substring(0, athString.length() - 2);
                    }
                    authorField.setText(athString);
                }

                publishDateField.setText(bookData.optString("publish_date",
                        publishDateField.getText()));

                if (bookData.has("subjects")) {
                    JSONArray subjectsArray = bookData.getJSONArray("subjects");
                    genreField.setText(subjectsArray.length() > 0
                            ? subjectsArray.getJSONObject(0).optString("name", genreField.getText())
                            : "");
                }

                if (bookData.has("cover")) {
                    JSONObject cover = bookData.getJSONObject("cover");
                    String prevImgUrl = cover.optString("medium", "");
                    if (!prevImgUrl.isEmpty()) {
                        docImagePreview.setImage(new Image(prevImgUrl));
                        docImagePreview.setPreserveRatio(true);
                        docImagePreview.setOnMouseClicked(null);
                    } else {
                        docImagePreview.setImage(defaultDocImgPrev);
                        docImagePreview.setOnMouseClicked(event2 -> setDocImagePreview());
                    }
                }

                errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                errorLabel.setText("Success!");
            } else {
                errorLabel.setText("Not Found in API");
                errorLabel.setTextFill(javafx.scene.paint.Color.RED);
            }
            scheduler.shutdown();
        });

        bookInfoService.setOnFailed(event -> {
            errorLabel.setText("Error loading data from API");
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);
            docImagePreview.setImage(defaultDocImgPrev);
            docImagePreview.setOnMouseClicked(event2 -> setDocImagePreview());
            scheduler.shutdown();
        });

        isbnField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d{10}|\\d{13}")) {
                bookInfoService.restart();
            } else {
                docImagePreview.setImage(defaultDocImgPrev);
                docImagePreview.setOnMouseClicked(event -> setDocImagePreview());
            }
        });

        addButton.setOnAction(event -> {
            String isbn = isbnField.getText();
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();
            String publishDate = publishDateField.getText();
            String quantityText = quantityField.getText();

            int quantity = 0;
            try {
                quantity = Integer.parseInt(quantityText);
                if (quantity <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                errorLabel.setText("Please enter a valid quantity.");
                return;
            }

            if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()
                    || genre.isEmpty() || publishDate.isEmpty()) {
                errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                errorLabel.setText("Please fill in all fields.");
                return;
            } else {
                for (int i = 0; i < quantity; i++) {
                    Book newBook = new Book();
                    newBook.setID(libraryService.generateID());
                    newBook.setName(title);
                    newBook.setAuthor(author);
                    newBook.setGroup(genre);
                    newBook.setPublishDate(publishDate);
                    newBook.setISBN(isbn);
                    newBook.setIsAvailable(true);
                    if (docImagePreview.getImage() != defaultDocImgPrev && docImagePreview.getImage() != null) {
                        newBook.setImagePreview(docImagePreview.getImage().getUrl());
                        docImagePreview.setImage(defaultDocImgPrev);
                    } else {
                        newBook.setImagePreview("/img/noprev.png");
                    }

                    bookManagement.addDocuments(newBook);
                }

                errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                errorLabel.setText("Successfully added " + quantity + " books to the library");

                isbnField.clear();
                titleField.clear();
                authorField.clear();
                genreField.clear();
                publishDateField.clear();
                quantityField.clear();
            }
        });
    }

    private void createForMagazine() {
        TextField titleField = createTextField("Enter Title",
                startX, startY, fieldWidth);
        TextField publisherField = createTextField("Enter Publisher",
                startX, startY + spacing, fieldWidth);
        TextField genreField = createTextField("Enter Genre",
                startX, startY + spacing * 2, fieldWidth);

        Label errorLabel = createErrorLabel(startX, startY + spacing * 3);

        inputPane.getChildren().addAll(titleField, publisherField, genreField, errorLabel);

        addButton.setOnAction(event -> {
            String title = titleField.getText().trim();
            String publisher = publisherField.getText().trim();
            String genre = genreField.getText().trim();

            if (title.isEmpty() || publisher.isEmpty() || genre.isEmpty()) {
                errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                errorLabel.setText("Please fill in all fields.");
                return;
            }

            Magazine newMagazine = new Magazine();
            newMagazine.setID(libraryService.generateID());
            newMagazine.setName(title);
            newMagazine.setPublisher(publisher);
            newMagazine.setGroup(genre);
            newMagazine.setIsAvailable(true);

            magazineManagement.addDocuments(newMagazine);

            errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            errorLabel.setText("Successfully added to the library.");
        });
    }

    private void createForNewspaper() {
        TextField titleField = createTextField("Enter Title",
                startX, startY, fieldWidth);
        TextField genreField = createTextField("Enter Genre",
                startX, startY + spacing, fieldWidth);
        TextField sourceField = createTextField("Enter Source",
                startX, startY + spacing * 2, fieldWidth);
        TextField regionField = createTextField("Enter Region",
                startX, startY + spacing * 3, fieldWidth);

        Label errorLabel = createErrorLabel(startX, startY + spacing * 4);

        inputPane.getChildren().addAll(titleField, genreField, sourceField, regionField, errorLabel);

        addButton.setOnAction(event -> {
            String title = titleField.getText().trim();
            String genre = genreField.getText().trim();
            String source = sourceField.getText().trim();
            String region = regionField.getText().trim();

            if (title.isEmpty() || genre.isEmpty() || source.isEmpty() || region.isEmpty()) {
                errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                errorLabel.setText("Please fill in all fields.");
                return;
            }

            Newspaper newNewspaper = new Newspaper();
            newNewspaper.setID(libraryService.generateID());
            newNewspaper.setName(title);
            newNewspaper.setGroup(genre);
            newNewspaper.setSource(source);
            newNewspaper.setRegion(region);
            newNewspaper.setIsAvailable(true);

            newsPaperManagament.addDocuments(newNewspaper);

            errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            errorLabel.setText("Successfully added to the library.");
        });
    }

}
