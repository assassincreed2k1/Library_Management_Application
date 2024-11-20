package com.library.controller.tools;

import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.service.APIService;
import com.library.service.BookManagement;
import com.library.service.MagazineManagement;
import com.library.service.NewsPaperManagament;
import com.library.service.ServiceManager;
import com.library.service.LibraryService;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
//import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
//import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Dialog;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.concurrent.Task;
import javafx.concurrent.Service;

public class AddDocumentController {

    private BookManagement bookManagement;
    private MagazineManagement magazineManagement;
    private NewsPaperManagament newsPaperManagament;
    private LibraryService libraryService;

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView iconImageView;

    // @FXML
    // private Button exitButton;

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

    private final String defDocImgPrevUrl = "/img/Add.png";

    @FXML
    public void initialize() {
        this.bookManagement = ServiceManager.getBookManagement();
        this.magazineManagement = ServiceManager.getMagazineManagement();
        this.newsPaperManagament = ServiceManager.getNewsPaperManagament();
        this.libraryService = ServiceManager.getLibraryService();

        documentTypeComboBox.getItems().addAll("Book", "Magazine", "Newspaper");

        documentTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            createInputFields(newValue);
        });
    }

    private void setDocImagePreview() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Set Image Preview");
        dialog.setHeaderText("Enter the image URL below:");

        TextField urlField = new TextField();
        urlField.setPromptText("Enter image URL");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setWrapText(true);

        VBox content = new VBox(10, urlField, errorLabel);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return urlField.getText().trim();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(url -> {
            if (url.isEmpty()) {
                errorLabel.setText("URL cannot be empty.");
                docImagePreview.setImage(null);
            } else {
                try {
                    Image image = new Image(url, false);
                    if (!image.isError()) {
                        docImagePreview.setImage(image);
                        errorLabel.setText("");
                    } else {
                        errorLabel.setText("Invalid image URL. Please enter a valid URL.");
                        docImagePreview.setImage(null);
                    }
                } catch (Exception e) {
                    errorLabel.setText("An error occurred while loading the image. Please check the URL.");
                    docImagePreview.setImage(null);
                }
            }
        });
    }

    private void createInputFields(String documentType) {
        getDocumentInfoPane.getChildren().removeIf(node -> node instanceof AnchorPane
                && node.getId() != null
                && node.getId().equals("inputPane"));

        AnchorPane inputPane = new AnchorPane();
        inputPane.setId("inputPane");

        if (documentType.equals("Book")) {
            double anchorWidth = 400;
            double fieldWidth = anchorWidth * 0.8;
            double startX = (anchorWidth - fieldWidth) / 2;
            double startY = 20;
            double spacing = 35;

            TextField isbnField = new TextField();
            isbnField.setPromptText("Enter ISBN");
            isbnField.setLayoutX(startX);
            isbnField.setLayoutY(startY);
            isbnField.setPrefWidth(fieldWidth);

            TextField titleField = new TextField();
            titleField.setPromptText("Enter Title");
            titleField.setLayoutX(startX);
            titleField.setLayoutY(startY + spacing);
            titleField.setPrefWidth(fieldWidth);

            TextField authorField = new TextField();
            authorField.setPromptText("Enter Author");
            authorField.setLayoutX(startX);
            authorField.setLayoutY(startY + spacing * 2);
            authorField.setPrefWidth(fieldWidth);

            TextField genreField = new TextField();
            genreField.setPromptText("Enter Genre");
            genreField.setLayoutX(startX);
            genreField.setLayoutY(startY + spacing * 3);
            genreField.setPrefWidth(fieldWidth);

            TextField publishDateField = new TextField();
            publishDateField.setPromptText("Enter Publish Date");
            publishDateField.setLayoutX(startX);
            publishDateField.setLayoutY(startY + spacing * 4);
            publishDateField.setPrefWidth(fieldWidth);

            TextField quantityField = new TextField();
            quantityField.setPromptText("Enter Quantity");
            quantityField.setLayoutX(startX);
            quantityField.setLayoutY(startY + spacing * 5);
            quantityField.setPrefWidth(fieldWidth);

            Label errorLabel = new Label();
            errorLabel.setLayoutX(startX);
            errorLabel.setLayoutY(startY + spacing * 6);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);

            inputPane.getChildren().addAll(isbnField, titleField,
                    authorField, genreField,
                    publishDateField, quantityField, errorLabel);

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
                            docImagePreview.setImage(null);
                            docImagePreview.setOnMouseClicked(event2 -> setDocImagePreview());
                        }
                    }

                    errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                    errorLabel.setText("Success!");
                } else {
                    errorLabel.setText("Not Found in API");
                    errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                }
            });

            bookInfoService.setOnFailed(event -> {
                errorLabel.setText("Error loading data from API");
                errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                docImagePreview.setImage(null);
                docImagePreview.setOnMouseClicked(event2 -> setDocImagePreview());
            });

            isbnField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() == 10 || newValue.length() == 13) {
                    bookInfoService.restart();
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
                        if (docImagePreview.getImage() != null) {
                            newBook.setImagePreview(docImagePreview.getImage().getUrl());
                            docImagePreview.setImage(null);
                        } else {
                            newBook.setImagePreview("");
                        }

                        bookManagement.addDocuments(newBook);
                    }

                    errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                    errorLabel.setText("Successfully added " + quantity + " books to the library");
                }
            });

        } else if (documentType.equals("Magazine")) {
            double anchorWidth = 400;
            double fieldWidth = anchorWidth * 0.8;
            double startX = (anchorWidth - fieldWidth) / 2;
            double startY = 20;
            double spacing = 35;

            TextField titleField = new TextField();
            titleField.setPromptText("Enter Title");
            titleField.setLayoutX(startX);
            titleField.setLayoutY(startY);
            titleField.setPrefWidth(fieldWidth);

            TextField publisherField = new TextField();
            publisherField.setPromptText("Enter Publisher");
            publisherField.setLayoutX(startX);
            publisherField.setLayoutY(startY + spacing);
            publisherField.setPrefWidth(fieldWidth);

            TextField genreField = new TextField();
            genreField.setPromptText("Enter Genre");
            genreField.setLayoutX(startX);
            genreField.setLayoutY(startY + spacing * 2);
            genreField.setPrefWidth(fieldWidth);

            Label errorLabel = new Label();
            errorLabel.setLayoutX(startX);
            errorLabel.setLayoutY(startY + spacing * 3);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);

            inputPane.getChildren().addAll(titleField, publisherField, genreField, errorLabel);

            addButton.setOnAction(event -> {
                String title = titleField.getText();
                String publisher = publisherField.getText();
                String genre = genreField.getText();

                if (title.isEmpty() || publisher.isEmpty() || genre.isEmpty()) {
                    errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                    errorLabel.setText("Please fill in all fields.");
                } else {
                    Magazine newMgz = new Magazine();
                    newMgz.setID(libraryService.generateID());
                    newMgz.setName(title);
                    newMgz.setPublisher(publisher);
                    newMgz.setGroup(genre);
                    newMgz.setIsAvailable(true);

                    magazineManagement.addDocuments(newMgz);

                    errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                    errorLabel.setText("Successfully added to the library");
                }
            });
        } else if (documentType.equals("Newspaper")) {
            double anchorWidth = 400;
            double fieldWidth = anchorWidth * 0.8;
            double startX = (anchorWidth - fieldWidth) / 2;
            double startY = 20;
            double spacing = 35;

            TextField titleField = new TextField();
            titleField.setPromptText("Enter Title");
            titleField.setLayoutX(startX);
            titleField.setLayoutY(startY);
            titleField.setPrefWidth(fieldWidth);

            TextField genreField = new TextField();
            genreField.setPromptText("Enter Genre");
            genreField.setLayoutX(startX);
            genreField.setLayoutY(startY + spacing);
            genreField.setPrefWidth(fieldWidth);

            TextField sourceField = new TextField();
            sourceField.setPromptText("Enter Source");
            sourceField.setLayoutX(startX);
            sourceField.setLayoutY(startY + spacing * 2);
            sourceField.setPrefWidth(fieldWidth);

            TextField regionField = new TextField();
            regionField.setPromptText("Enter Region");
            regionField.setLayoutX(startX);
            regionField.setLayoutY(startY + spacing * 3);
            regionField.setPrefWidth(fieldWidth);

            Label errorLabel = new Label();
            errorLabel.setLayoutX(startX);
            errorLabel.setLayoutY(startY + spacing * 4);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);

            inputPane.getChildren().addAll(titleField, genreField, sourceField, regionField, errorLabel);

            addButton.setOnAction(event -> {
                String title = titleField.getText();
                String genre = genreField.getText();
                String source = sourceField.getText();
                String region = regionField.getText();

                if (title.isEmpty() || genre.isEmpty() || source.isEmpty() || region.isEmpty()) {
                    errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                    errorLabel.setText("Please fill in all fields.");
                } else {
                    Newspaper newNewspaper = new Newspaper();
                    newNewspaper.setID(libraryService.generateID());
                    newNewspaper.setName(title);
                    newNewspaper.setGroup(genre);
                    newNewspaper.setSource(source);
                    newNewspaper.setRegion(region);
                    newNewspaper.setIsAvailable(true);

                    newsPaperManagament.addDocuments(newNewspaper);

                    errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                    errorLabel.setText("Successfully added to the library");
                }
            });
        }

        getDocumentInfoPane.getChildren().add(inputPane);
    }

}
