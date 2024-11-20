package com.library.controller.tools;

import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.service.BookManagement;
import com.library.service.ServiceManager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class RemoveDocumentController {

    @FXML
    private VBox mainBox;

    private final BookManagement bookManagement = ServiceManager.getBookManagement();

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    @FXML
    private TextField idField;

    @FXML
    private Button checkButton;

    @FXML
    private Button removeButton;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        mainBox.setPadding(new Insets(20));
        mainBox.setSpacing(10);
    }

    public void setup(Object selectedDocument) {
        if (selectedDocument instanceof Book) {
            Book selectedBook = (Book) selectedDocument;
            idField.setText(selectedBook.getID());
            setupRemoveBook();
        } else if (selectedDocument instanceof Magazine) {
            Magazine selectedMagazine = (Magazine) selectedDocument;
            idField.setText(selectedMagazine.getID());
            setupRemoveMagazine();
        } else if (selectedDocument instanceof Newspaper) {
            Newspaper selectedNewspaper = (Newspaper) selectedDocument;
            idField.setText(selectedNewspaper.getID());
            setupRemoveNewspaper();
        } else {
            System.out.println("Invalid document type.");
        }
    }

    private void setupRemoveBook() {
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        statusLabel.setWrapText(true);

        checkButton.setOnAction(event -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                statusLabel.setText("Please enter an ID.");
                statusLabel.setStyle("-fx-text-fill: red;");
            } else {
                Book foundBook = bookManagement.getDocument(id);
                if (foundBook != null) {
                    statusLabel.setText("Book found:\nName: " + foundBook.getName()
                            + "\nAuthor: " + foundBook.getAuthor()
                            + "\nGenre: " + foundBook.getGroup()
                            + "\nPublish Date: " + foundBook.getPublishDate()
                            + "\nISBN: " + foundBook.getISBN());
                    statusLabel.setStyle("-fx-text-fill: green;");
                } else {
                    statusLabel.setText("No book found with ID: " + id);
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });

        removeButton.setOnAction(event -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                statusLabel.setText("Please enter an ID to remove.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            alert.setTitle("Confirm Removal");
            alert.setHeaderText("Are you sure you want to remove this book?");
            alert.setContentText("Book ID: " + id);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Book foundBook = bookManagement.getDocument(id);
                        if (foundBook != null) {
                            bookManagement.removeDocument(foundBook);
                            statusLabel.setText("Book with ID " + id + " removed successfully.");
                            statusLabel.setStyle("-fx-text-fill: green;");
                            idField.clear();
                        } else {
                            statusLabel.setText("No book found with ID: " + id);
                            statusLabel.setStyle("-fx-text-fill: red;");
                        }
                    } catch (Exception e) {
                        statusLabel.setText("Failed to remove book.");
                        statusLabel.setStyle("-fx-text-fill: red;");
                        e.printStackTrace();
                    }
                } else {
                    statusLabel.setText("Removal canceled.");
                    statusLabel.setStyle("-fx-text-fill: orange;");
                }
            });
        });
        mainBox.getChildren().clear();
        mainBox.getChildren().addAll(idField, checkButton, removeButton, statusLabel);
    }
    
    private void setupRemoveMagazine() {

        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        statusLabel.setWrapText(true);
    
        checkButton.setOnAction(event -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                statusLabel.setText("Please enter an ID.");
                statusLabel.setStyle("-fx-text-fill: red;");
            } else {
                Magazine foundMagazine = ServiceManager.getMagazineManagement().getDocument(id);
                if (foundMagazine != null) {
                    statusLabel.setText("Magazine found:\nTitle: " + foundMagazine.getName()
                            + "\nGenre: " + foundMagazine.getGroup()
                            + "\nPublisher: " + foundMagazine.getPublisher());
                    statusLabel.setStyle("-fx-text-fill: green;");
                } else {
                    statusLabel.setText("No magazine found with ID: " + id);
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });
    
        removeButton.setOnAction(event -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                statusLabel.setText("Please enter an ID to remove.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            alert.setTitle("Confirm Removal");
            alert.setHeaderText("Are you sure you want to remove this magazine?");
            alert.setContentText("Magazine ID: " + id);
    
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Magazine foundMagazine = ServiceManager.getMagazineManagement().getDocument(id);
                        if (foundMagazine != null) {
                            ServiceManager.getMagazineManagement().removeDocument(foundMagazine);
                            statusLabel.setText("Magazine with ID " + id + " removed successfully.");
                            statusLabel.setStyle("-fx-text-fill: green;");
                            idField.clear();
                        } else {
                            statusLabel.setText("No magazine found with ID: " + id);
                            statusLabel.setStyle("-fx-text-fill: red;");
                        }
                    } catch (Exception e) {
                        statusLabel.setText("Failed to remove magazine.");
                        statusLabel.setStyle("-fx-text-fill: red;");
                        e.printStackTrace();
                    }
                } else {
                    statusLabel.setText("Removal canceled.");
                    statusLabel.setStyle("-fx-text-fill: orange;");
                }
            });
        });
        mainBox.getChildren().clear();
        mainBox.getChildren().addAll(idField, checkButton, removeButton, statusLabel);
    }

    private void setupRemoveNewspaper() {

        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        statusLabel.setWrapText(true);
    
        checkButton.setOnAction(event -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                statusLabel.setText("Please enter an ID.");
                statusLabel.setStyle("-fx-text-fill: red;");
            } else {
                Newspaper foundNewspaper = ServiceManager.getNewsPaperManagament().getDocument(id);
                if (foundNewspaper != null) {
                    statusLabel.setText("Newspaper found:\nName: " + foundNewspaper.getName()
                            + "\nGroup: " + foundNewspaper.getGroup()
                            + "\nSource: " + foundNewspaper.getSource()
                            + "\nRegion: " + foundNewspaper.getRegion());
                    statusLabel.setStyle("-fx-text-fill: green;");
                } else {
                    statusLabel.setText("No newspaper found with ID: " + id);
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });
    
        removeButton.setOnAction(event -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                statusLabel.setText("Please enter an ID to remove.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            alert.setTitle("Confirm Removal");
            alert.setHeaderText("Are you sure you want to remove this newspaper?");
            alert.setContentText("Newspaper ID: " + id);
    
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Newspaper foundNewspaper = ServiceManager.getNewsPaperManagament().getDocument(id);
                        if (foundNewspaper != null) {
                            ServiceManager.getNewsPaperManagament().removeDocument(foundNewspaper);
                            statusLabel.setText("Newspaper with ID " + id + " removed successfully.");
                            statusLabel.setStyle("-fx-text-fill: green;");
                            idField.clear();
                        } else {
                            statusLabel.setText("No newspaper found with ID: " + id);
                            statusLabel.setStyle("-fx-text-fill: red;");
                        }
                    } catch (Exception e) {
                        statusLabel.setText("Failed to remove newspaper.");
                        statusLabel.setStyle("-fx-text-fill: red;");
                        e.printStackTrace();
                    }
                } else {
                    statusLabel.setText("Removal canceled.");
                    statusLabel.setStyle("-fx-text-fill: orange;");
                }
            });
        });
        mainBox.getChildren().clear();
        mainBox.getChildren().addAll(idField, checkButton, removeButton, statusLabel);
    }
}
