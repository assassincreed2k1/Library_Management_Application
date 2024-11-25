package com.library.controller.tools;

import com.library.model.doc.Document;
import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;

import com.library.service.ServiceManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

/**
 * Controller class for handling the removal of library documents.
 */
public class RemoveDocumentController {

    @FXML
    private VBox mainBox;

    @FXML
    private TextField idField;

    @FXML
    private Button checkButton;

    @FXML
    private Button removeButton;

    @FXML
    private Label statusLabel;

    private Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize() {
        mainBox.setPadding(new Insets(20));
        mainBox.setSpacing(10);
        setupUI();
    }

    /**
     * Sets the document ID in the input field and triggers the check action.
     *
     * @param id The document ID to set.
     */
    public void setId(String id) {
        this.idField.setText(id);
        checkButton.fire();
    }

    /**
     * Configures the UI elements, including event listeners for buttons.
     */
    private void setupUI() {
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        statusLabel.setWrapText(true);

        checkButton.setOnAction(event -> handleCheckAction());
        removeButton.setOnAction(event -> handleRemoveAction());
    }

    /**
     * Handles the action of checking if a document exists by its ID.
     */
    private void handleCheckAction() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            updateStatus("Please enter an ID.", "red");
            return;
        }

        Document document = findDocumentById(id);
        if (document != null) {
            updateStatus("Document found:\n" + documentDetails(document), "green");
        } else {
            updateStatus("No document found with ID: " + id, "red");
        }
    }

    /**
     * Handles the action of removing a document by its ID.
     */
    private void handleRemoveAction() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            updateStatus("Please enter an ID to remove.", "red");
            return;
        }

        alert.setTitle("Confirm Removal");
        alert.setHeaderText("Are you sure you want to remove this document?");
        alert.setContentText("Document ID: " + id);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Document document = findDocumentById(id);
                if (document != null) {
                    removeDocument(document);
                    updateStatus("Document with ID " + id + " removed successfully.", "green");
                    idField.clear();
                } else {
                    updateStatus("No document found with ID: " + id, "red");
                }
            } else {
                updateStatus("Removal canceled.", "orange");
            }
        });
    }

    /**
     * Finds a document by its ID from the library's collections.
     *
     * @param id The document ID to search for.
     * @return The Document object if found, or null if not found.
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
     * Removes a document from the library based on its type.
     *
     * @param document The Document object to remove.
     */
    private void removeDocument(Document document) {
        try {
            if (document instanceof Book) {
                ServiceManager.getBookManagement().removeDocument((Book) document);
            } else if (document instanceof Magazine) {
                ServiceManager.getMagazineManagement().removeDocument((Magazine) document);
            } else if (document instanceof Newspaper) {
                ServiceManager.getNewsPaperManagement().removeDocument((Newspaper) document);
            }
        } catch (Exception e) {
            updateStatus("Failed to remove document.", "red");
            e.printStackTrace();
        }
    }

    /**
     * Generates a detailed string description of a document.
     *
     * @param document The Document object to describe.
     * @return A string representation of the document details.
     */
    private String documentDetails(Document document) {
        if (document instanceof Book book) {
            return "Name: " + book.getName() +
                    "\nAuthor: " + book.getAuthor() +
                    "\nGenre: " + book.getGroup() +
                    "\nPublish Date: " + book.getPublishDate() +
                    "\nISBN: " + book.getISBN();
        } else if (document instanceof Magazine magazine) {
            return "Title: " + magazine.getName() +
                    "\nGenre: " + magazine.getGroup() +
                    "\nPublisher: " + magazine.getPublisher();
        } else if (document instanceof Newspaper newspaper) {
            return "Name: " + newspaper.getName() +
                    "\nGroup: " + newspaper.getGroup() +
                    "\nSource: " + newspaper.getSource() +
                    "\nRegion: " + newspaper.getRegion();
        }
        return "Unknown document type.";
    }

    /**
     * Updates the status label with a message and color.
     *
     * @param message The status message to display.
     * @param color   The color of the text (e.g., "red", "green", "orange").
     */
    private void updateStatus(String message, String color) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: " + color + ";");
    }
}
