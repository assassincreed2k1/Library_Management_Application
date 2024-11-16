package com.library.controller.tools;

import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.service.BookManagement;
import com.library.service.MagazineManagement;
import com.library.service.NewsPaperManagament;
import com.library.service.ServiceManager;
import com.library.service.LibraryService;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UpdateDocumentController {

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
    private Button updateButton;

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Library/LibraryHome.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            TextField idField = new TextField();
            idField.setPromptText("Enter ID");
            idField.setLayoutX(startX);
            idField.setLayoutY(startY);
            idField.setPrefWidth(fieldWidth);

            TextField isbnField = new TextField();
            isbnField.setPromptText("Enter ISBN");
            isbnField.setLayoutX(startX);
            isbnField.setLayoutY(startY + spacing);
            isbnField.setPrefWidth(fieldWidth);

            TextField titleField = new TextField();
            titleField.setPromptText("Enter Title");
            titleField.setLayoutX(startX);
            titleField.setLayoutY(startY + spacing * 2);
            titleField.setPrefWidth(fieldWidth);

            TextField authorField = new TextField();
            authorField.setPromptText("Enter Author");
            authorField.setLayoutX(startX);
            authorField.setLayoutY(startY + spacing * 3);
            authorField.setPrefWidth(fieldWidth);

            TextField genreField = new TextField();
            genreField.setPromptText("Enter Genre");
            genreField.setLayoutX(startX);
            genreField.setLayoutY(startY + spacing * 4);
            genreField.setPrefWidth(fieldWidth);

            TextField publishDateField = new TextField();
            publishDateField.setPromptText("Enter Publish Date");
            publishDateField.setLayoutX(startX);
            publishDateField.setLayoutY(startY + spacing * 5);
            publishDateField.setPrefWidth(fieldWidth);

            Label errorLabel = new Label();
            errorLabel.setLayoutX(startX);
            errorLabel.setLayoutY(startY + spacing * 6);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);

            inputPane.getChildren().addAll(idField, isbnField, titleField, authorField,
                    genreField, publishDateField, errorLabel);

            // Fetch existing book details for updating by ID
            Book bookToUpdate = bookManagement.getDocument(idField.getText());

            if (bookToUpdate != null) {
                // Pre-fill fields with existing data
                idField.setText(bookToUpdate.getID());
                isbnField.setText(bookToUpdate.getISBN());
                titleField.setText(bookToUpdate.getName());
                authorField.setText(bookToUpdate.getAuthor());
                genreField.setText(bookToUpdate.getGroup());
                publishDateField.setText(bookToUpdate.getPublishDate());
                docImagePreview.setImage(new Image(bookToUpdate.getImagePreview()));
            }

            updateButton.setOnAction(event -> {
                String id = idField.getText();
                String isbn = isbnField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                String genre = genreField.getText();
                String publishDate = publishDateField.getText();

                if (id.isEmpty() || isbn.isEmpty() || title.isEmpty() || author.isEmpty()
                        || genre.isEmpty() || publishDate.isEmpty()) {
                    errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                    errorLabel.setText("Please fill in all fields.");
                } else {
                    Book updatedBook = new Book();
                    updatedBook.setID(id);
                    updatedBook.setName(title);
                    updatedBook.setAuthor(author);
                    updatedBook.setGroup(genre);
                    updatedBook.setPublishDate(publishDate);
                    updatedBook.setISBN(isbn);
                    updatedBook.setIsAvailable(bookToUpdate.getIsAvailable());
                    updatedBook.setImagePreview(docImagePreview.getImage().getUrl());

                    bookManagement.updateDocuments(updatedBook);
                    errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                    errorLabel.setText("Successfully updated the book in the library.");
                }
            });

        } else if (documentType.equals("Magazine")) {
            double anchorWidth = 400;
            double fieldWidth = anchorWidth * 0.8;
            double startX = (anchorWidth - fieldWidth) / 2;
            double startY = 20;
            double spacing = 35;

            TextField idField = new TextField();
            idField.setPromptText("Enter Magazine ID");
            idField.setLayoutX(startX);
            idField.setLayoutY(startY);
            idField.setPrefWidth(fieldWidth);

            TextField titleField = new TextField();
            titleField.setPromptText("Enter Title");
            titleField.setLayoutX(startX);
            titleField.setLayoutY(startY + spacing);
            titleField.setPrefWidth(fieldWidth);

            TextField publisherField = new TextField();
            publisherField.setPromptText("Enter Publisher");
            publisherField.setLayoutX(startX);
            publisherField.setLayoutY(startY + spacing * 2);
            publisherField.setPrefWidth(fieldWidth);

            TextField genreField = new TextField();
            genreField.setPromptText("Enter Genre");
            genreField.setLayoutX(startX);
            genreField.setLayoutY(startY + spacing * 3);
            genreField.setPrefWidth(fieldWidth);

            Label errorLabel = new Label();
            errorLabel.setLayoutX(startX);
            errorLabel.setLayoutY(startY + spacing * 4);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);

            inputPane.getChildren().addAll(idField, titleField, publisherField, genreField, errorLabel);

            Magazine magazineToUpdate = magazineManagement.getDocument(idField.getText());

            if (magazineToUpdate != null) {
                titleField.setText(magazineToUpdate.getName());
                publisherField.setText(magazineToUpdate.getPublisher());
                genreField.setText(magazineToUpdate.getGroup());
            }

            updateButton.setOnAction(event -> {
                String id = idField.getText();
                String title = titleField.getText();
                String publisher = publisherField.getText();
                String genre = genreField.getText();

                if (id.isEmpty() || title.isEmpty() || publisher.isEmpty() || genre.isEmpty()) {
                    errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                    errorLabel.setText("Please fill in all fields.");
                } else {
                    Magazine updatedMagazine = new Magazine();
                    updatedMagazine.setID(magazineToUpdate.getID());
                    updatedMagazine.setName(title);
                    updatedMagazine.setPublisher(publisher);
                    updatedMagazine.setGroup(genre);
                    updatedMagazine.setIsAvailable(magazineToUpdate.getIsAvailable());

                    magazineManagement.updateDocuments(updatedMagazine);
                    errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                    errorLabel.setText("Successfully updated the magazine in the library.");
                }
            });
        } else if (documentType.equals("Newspaper")) {
            double anchorWidth = 400;
            double fieldWidth = anchorWidth * 0.8;
            double startX = (anchorWidth - fieldWidth) / 2;
            double startY = 20;
            double spacing = 35;

            TextField idField = new TextField();
            idField.setPromptText("Enter ID");
            idField.setLayoutX(startX);
            idField.setLayoutY(startY);
            idField.setPrefWidth(fieldWidth);

            TextField titleField = new TextField();
            titleField.setPromptText("Enter Title");
            titleField.setLayoutX(startX);
            titleField.setLayoutY(startY + spacing);
            titleField.setPrefWidth(fieldWidth);

            TextField publisherField = new TextField();
            publisherField.setPromptText("Enter Publisher");
            publisherField.setLayoutX(startX);
            publisherField.setLayoutY(startY + spacing * 2);
            publisherField.setPrefWidth(fieldWidth);

            Label errorLabel = new Label();
            errorLabel.setLayoutX(startX);
            errorLabel.setLayoutY(startY + spacing * 3);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);

            inputPane.getChildren().addAll(idField, titleField, publisherField, errorLabel);

            Newspaper newspaperToUpdate = newsPaperManagament.getDocument(idField.getText());

            if (newspaperToUpdate != null) {
                idField.setText(newspaperToUpdate.getID()); 
                titleField.setText(newspaperToUpdate.getName()); 
                publisherField.setText(newspaperToUpdate.getSource()); 
            }

            updateButton.setOnAction(event -> {
                String id = idField.getText();
                String title = titleField.getText();
                String publisher = publisherField.getText();

                if (id.isEmpty() || title.isEmpty() || publisher.isEmpty()) {
                    errorLabel.setTextFill(javafx.scene.paint.Color.RED);
                    errorLabel.setText("Please fill in all fields.");
                } else {
                    Newspaper updatedNewspaper = new Newspaper();
                    updatedNewspaper.setID(id); 
                    updatedNewspaper.setName(title);
                    updatedNewspaper.setSource(publisher);
                    updatedNewspaper.setIsAvailable(newspaperToUpdate.getIsAvailable());

                    newsPaperManagament.updateDocuments(updatedNewspaper);
                    errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                    errorLabel.setText("Successfully updated the newspaper in the library.");
                }
            });
        }

        getDocumentInfoPane.getChildren().add(inputPane);
    }
}
