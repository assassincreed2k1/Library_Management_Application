package com.library.controller.tools;

import com.library.controller.ServiceManager;
import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.service.APIService;
import com.library.service.BookManagement;
import com.library.service.MagazineManagement;
import com.library.service.NewsPaperManagament;
import com.library.service.LibraryService;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

    @FXML
    private Button exitButton;

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

        exitButton.setOnAction(event -> onExitButtonClicked());
    }

    private void onExitButtonClicked() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
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

            Label errorLabel = new Label();
            errorLabel.setLayoutX(startX);
            errorLabel.setLayoutY(startY + spacing * 5);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);

            inputPane.getChildren().addAll(isbnField, titleField,
                    authorField, genreField,
                    publishDateField, errorLabel);

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
                            athString += authorsArray.getJSONObject(i).optString("name", "") + ", ";
                        }
                        if (!athString.isEmpty()) {
                            athString = athString.substring(0, athString.length() - 2);
                        }
                        authorField.setText(athString);
                    }

                    publishDateField.setText(bookData.optString("publish_date", publishDateField.getText()));

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

                if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || genre.isEmpty() || publishDate.isEmpty()) {
                    errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                    errorLabel.setText("Please fill in all fields.");
                } else {
                    Book newBook = new Book();
                    newBook.setID(libraryService.generateID());
                    newBook.setName(title);
                    newBook.setAuthor(author);
                    newBook.setGroup(genre);
                    newBook.setPublishDate(publishDate);
                    newBook.setISBN(isbn);
                    newBook.setIsAvailable(true);

                    bookManagement.addDocuments(newBook);
                    errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                    errorLabel.setText("Successfully added to the library");
                }
            });

        } else if (documentType.equals("Magazine")) {
            TextField titleField = new TextField();
            titleField.setPromptText("Enter Title");
            titleField.setLayoutX(50);
            titleField.setLayoutY(60);

            TextField publisherField = new TextField();
            publisherField.setPromptText("Enter Publisher");
            publisherField.setLayoutX(50);
            publisherField.setLayoutY(100);

            TextField genreField = new TextField();
            genreField.setPromptText("Enter Genre");
            genreField.setLayoutX(50);
            genreField.setLayoutY(140);

            inputPane.getChildren().addAll(titleField, publisherField, genreField);
            
            addButton.setOnAction(event -> {
                Magazine newMgz = new Magazine();
                newMgz.setID(libraryService.generateID());

            });
        } else if (documentType.equals("Newspaper")) {
            TextField titleField = new TextField();
            titleField.setPromptText("Enter Title");
            titleField.setLayoutX(50);
            titleField.setLayoutY(60);

            TextField genreField = new TextField();
            genreField.setPromptText("Enter Genre");
            genreField.setLayoutX(50);
            genreField.setLayoutY(100);

            TextField sourceField = new TextField();
            sourceField.setPromptText("Enter Source");
            sourceField.setLayoutX(50);
            sourceField.setLayoutY(140);

            TextField regionField = new TextField();
            regionField.setPromptText("Enter Region");
            regionField.setLayoutX(50);
            regionField.setLayoutY(180);

            inputPane.getChildren().addAll(titleField, genreField, sourceField, regionField);
            addButton.setOnAction(event -> {

            });
        }

        getDocumentInfoPane.getChildren().add(inputPane);
    }

}
