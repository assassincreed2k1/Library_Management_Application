package com.library.controller.tools;

import com.library.model.doc.*;
import com.library.service.ServiceManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

/**
 * Controller for updating document details.
 * This class provides functionality to search for a document by ID,
 * display its details, and allow modifications to be saved.
 */
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

    /**
     * Initializes the controller after its root element has been completely
     * processed.
     * Sets up the UI elements and their event handlers.
     */
    @FXML
    public void initialize() {
        mainBox.setPadding(new Insets(20));
        mainBox.setSpacing(10);
        setupUI();
    }

    /**
     * Sets the ID of the document to be searched for and automatically triggers the
     * search action.
     *
     * @param id the ID of the document to search for.
     */
    public void setId(String id) {
        this.idField.setText(id);
        checkButton.fire();
    }

    /**
     * Sets up the UI elements such as buttons and their respective event handlers.
     * Disables the update button initially.
     */
    private void setupUI() {
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        statusLabel.setWrapText(true);

        checkButton.setOnAction(event -> handleCheckAction());
        updateButton.setOnAction(event -> handleUpdateAction());
        updateButton.setDisable(true);
    }

    /**
     * Handles the action triggered by the "Check" button.
     * Searches for the document by ID, updates the status label, and displays the
     * document details if found.
     */
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

    /**
     * Handles the action triggered by the "Update" button.
     * Updates the document details and saves the changes via the service manager.
     */
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

    /**
     * Finds a document by its ID by searching through books, magazines, and
     * newspapers.
     *
     * @param id the ID of the document to search for.
     * @return the document if found, otherwise null.
     */
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

    /**
     * Displays the details of the given document in the update form.
     * The form is dynamically populated based on the document type.
     *
     * @param document the document whose details are to be displayed.
     */
    private void showUpdateForm(Document document) {
        mainBox.getChildren().clear();

        Label idLabel = new Label("ID: " + document.getID());
        idLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextField[] fields = createUpdateFields(document);

        CheckBox availabilityCheckBox = new CheckBox("Available");
        availabilityCheckBox.setSelected(document.getIsAvailable());

        updateButton.setDisable(false);

        for (TextField field : fields) {
            mainBox.getChildren().add(new Label(field.getPromptText()));
            mainBox.getChildren().add(field);
        }
        mainBox.getChildren().addAll(availabilityCheckBox, checkButton, updateButton);
    }

    /**
     * Creates an array of text fields for updating the document details based on
     * its type.
     *
     * @param document the document for which the fields are created.
     * @return an array of text fields for updating the document details.
     */
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

    /**
     * Updates the details of the given document based on the input values from the
     * update form.
     *
     * @param document the document whose details are to be updated.
     */
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

    /**
     * Creates a styled text field with a given prompt text and initial value.
     *
     * @param promptText the prompt text to display in the field.
     * @param value      the initial value to populate the field with.
     * @return the created text field.
     */
    private TextField createTextField(String promptText, String value) {
        TextField textField = new TextField(value);
        textField.setPromptText(promptText);
        return textField;
    }

    /**
     * Updates the status label with a given message and text color.
     *
     * @param message the message to display in the status label.
     * @param color   the color of the text in the status label.
     */
    private void updateStatus(String message, String color) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: " + color + ";");
    }
}
