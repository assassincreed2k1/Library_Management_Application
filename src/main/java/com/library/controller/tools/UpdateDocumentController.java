package com.library.controller.tools;

import com.library.model.doc.*;
import com.library.service.ServiceManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class UpdateDocumentController {

    @FXML
    private VBox mainBox;

    @FXML
    private TextField idField;

    @FXML
    private Button checkButton;

    @FXML
    private Button updateButton;

    @FXML
    private Label statusLabel;

    private Document currentDocument;

    @FXML
    public void initialize() {
        mainBox.setPadding(new Insets(20));
        mainBox.setSpacing(10);
        setupUI();
    }

    public void setId(String id) {
        this.idField.setText(id);
        checkButton.fire();
    }

    private void setupUI() {
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        statusLabel.setWrapText(true);

        checkButton.setOnAction(event -> handleCheckAction());
        updateButton.setOnAction(event -> handleUpdateAction());
        updateButton.setDisable(true);
    }

    private void handleCheckAction() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            updateStatus("Please enter an ID.", "red");
            return;
        }

        currentDocument = findDocumentById(id);
        if (currentDocument != null) {
            updateStatus("Document found!", "green");
            showUpdateForm(currentDocument);
        } else {
            updateStatus("No document found with ID: " + id, "red");
            updateButton.setDisable(true);
        }
    }

    private void handleUpdateAction() {
        if (currentDocument == null) {
            updateStatus("No document to update. Please search for a valid document ID.", "red");
            return;
        }

        try {
            updateDocumentDetails(currentDocument);
            if (currentDocument instanceof Book) {
                ServiceManager.getBookManagement().updateDocuments((Book) currentDocument);
            } else if (currentDocument instanceof Magazine) {
                ServiceManager.getMagazineManagement().updateDocuments((Magazine) currentDocument);
            } else if (currentDocument instanceof Newspaper) {
                ServiceManager.getNewsPaperManagement().updateDocuments((Newspaper) currentDocument);
            }
            updateStatus("Document updated successfully!", "green");
        } catch (Exception e) {
            updateStatus("Failed to update document.", "red");
            e.printStackTrace();
        }
    }

    private Document findDocumentById(String id) {
        Document document = ServiceManager.getBookManagement().getDocument(id);
        if (document == null) {
            document = ServiceManager.getMagazineManagement().getDocument(id);
        }
        if (document == null) {
            document = ServiceManager.getNewsPaperManagement().getDocument(id);
        }
        return document;
    }

    private void showUpdateForm(Document document) {
        mainBox.getChildren().clear();

        Label idLabel = new Label("ID: " + document.getID());
        idLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextField[] fields = createUpdateFields(document);

        CheckBox availabilityCheckBox = new CheckBox("Available");
        availabilityCheckBox.setSelected(document.getIsAvailable());

        updateButton.setDisable(false);

        //mainBox.getChildren().add(idLabel);
        for (TextField field : fields) {
            mainBox.getChildren().add(new Label(field.getPromptText())); // 0 2 4 6 8...
            mainBox.getChildren().add(field); //1 3 5 7 9...
        }
        mainBox.getChildren().addAll(availabilityCheckBox, checkButton, updateButton);
    }

    private TextField[] createUpdateFields(Document document) {
        if (document instanceof Book book) {
            return new TextField[] {
                    createTextField("Title", book.getName()),
                    createTextField("Author", book.getAuthor()),
                    createTextField("Genre", book.getGroup()),
                    createTextField("Publish Date", book.getPublishDate()),
                    createTextField("ISBN", book.getISBN())
            };
        } else if (document instanceof Magazine magazine) {
            return new TextField[] {
                    createTextField("Title", magazine.getName()),
                    createTextField("Genre", magazine.getGroup()),
                    createTextField("Publisher", magazine.getPublisher())
            };
        } else if (document instanceof Newspaper newspaper) {
            return new TextField[] {
                    createTextField("Name", newspaper.getName()),
                    createTextField("Group", newspaper.getGroup()),
                    createTextField("Source", newspaper.getSource()),
                    createTextField("Region", newspaper.getRegion())
            };
        }
        return new TextField[] {};
    }

    private void updateDocumentDetails(Document document) {
        if (document instanceof Book book) {
            book.setName(((TextField) mainBox.getChildren().get(1)).getText());
            book.setAuthor(((TextField) mainBox.getChildren().get(3)).getText());
            book.setGroup(((TextField) mainBox.getChildren().get(5)).getText());
            book.setPublishDate(((TextField) mainBox.getChildren().get(7)).getText());
            book.setISBN(((TextField) mainBox.getChildren().get(9)).getText());
        } else if (document instanceof Magazine magazine) {
            magazine.setName(((TextField) mainBox.getChildren().get(1)).getText());
            magazine.setGroup(((TextField) mainBox.getChildren().get(3)).getText());
            magazine.setPublisher(((TextField) mainBox.getChildren().get(5)).getText());
        } else if (document instanceof Newspaper newspaper) {
            newspaper.setName(((TextField) mainBox.getChildren().get(1)).getText());
            newspaper.setGroup(((TextField) mainBox.getChildren().get(3)).getText());
            newspaper.setSource(((TextField) mainBox.getChildren().get(5)).getText());
            newspaper.setRegion(((TextField) mainBox.getChildren().get(7)).getText());
        }
        document.setIsAvailable(((CheckBox) mainBox.getChildren().get(mainBox.getChildren().size() - 3)).isSelected());
    }

    private TextField createTextField(String promptText, String value) {
        TextField textField = new TextField(value);
        textField.setPromptText(promptText);
        return textField;
    }

    private void updateStatus(String message, String color) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: " + color + ";");
    }
}
