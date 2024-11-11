package com.library.controller.tools;

import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.service.APIService;
import com.library.service.BookManagement;
import com.library.service.MagazineManagement;
import com.library.service.NewsPaperManagament;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddDocumentController {

    private BookManagement bookManagement = new BookManagement();
    private MagazineManagement magazineManagement = new MagazineManagement();
    private NewsPaperManagament newsPaperManagament = new NewsPaperManagament();

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
    public void initialize() {
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
        getDocumentInfoPane.getChildren().removeIf(
                node -> node instanceof AnchorPane && node.getId() != null && node.getId().equals("inputPane"));

        AnchorPane inputPane = new AnchorPane();
        inputPane.setId("inputPane");
        inputPane.setLayoutY(120);
        inputPane.setPrefHeight(200);
        inputPane.setPrefWidth(600);

        if (documentType.equals("Book")) {
            TextField isbnField = new TextField();
            isbnField.setPromptText("Enter ISBN");
            isbnField.setLayoutX(50);
            isbnField.setLayoutY(20);

            TextField titleField = new TextField();
            titleField.setPromptText("Enter Title");
            titleField.setLayoutX(50);
            titleField.setLayoutY(60);

            TextField authorField = new TextField();
            authorField.setPromptText("Enter Author");
            authorField.setLayoutX(50);
            authorField.setLayoutY(100);

            TextField genreField = new TextField();
            genreField.setPromptText("Enter Genre");
            genreField.setLayoutX(50);
            genreField.setLayoutY(140);

            TextField publishDateField = new TextField();
            publishDateField.setPromptText("Enter Publish Date");
            publishDateField.setLayoutX(50);
            publishDateField.setLayoutY(180);

            Label errorLabel = new Label();
            errorLabel.setLayoutX(50);
            errorLabel.setLayoutY(220);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);

            inputPane.getChildren().addAll(isbnField, titleField,
                    authorField, genreField,
                    publishDateField, errorLabel);

            isbnField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() == 10 || newValue.length() == 13) {
                    JSONObject getAPIBook = APIService.getBookInfoByISBN(newValue);

                    if (getAPIBook != null && getAPIBook.has("ISBN:" + newValue)) {
                        JSONObject bookData = getAPIBook.getJSONObject("ISBN:" + newValue);
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
                    }
                }
            });

            addButton.setOnAction(event -> {
                String isbn = isbnField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                String genre = genreField.getText();
                String publishDate = publishDateField.getText();

                if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || genre.isEmpty() || publishDate.isEmpty()) {
                    errorLabel.setText("Please fill in all fields.");
                } else {
                    Book newBook = new Book();
                    newBook.setID(BookManagement.generateID());
                    newBook.setName(title);
                    newBook.setAuthor(author);
                    newBook.setGroup(genre);
                    newBook.setPublishDate(publishDate);

                    bookManagement.addDocuments(newBook);
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
