package com.library.controller.tools;

import java.io.IOException;
import java.util.ArrayList;

import com.library.model.doc.Book;
import com.library.service.DocumentTransaction;
import com.library.service.BookManagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller class for displaying reviews and ratings of a specific book.
 */
public class ShowReviewBooks {

    private final DocumentTransaction documentTransaction = new DocumentTransaction();
    private final BookManagement bookManagement = new BookManagement();
    private String isbn;

    /**
     * Sets the ISBN of the book whose reviews and ratings are to be displayed.
     *
     * @param isbn the ISBN of the book
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
        loadBookDetails();
    }

    @FXML
    private VBox commentVBox;

    @FXML
    private Label scoreLabel;

    @FXML
    private TextArea documentTextArea;

    @FXML
    private Button backButton;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        backButton.setOnAction(event -> onBack());
    }

    /**
     * Loads the book details, including its average score and user comments.
     */
    private void loadBookDetails() {
        if (isbn == null || isbn.isEmpty()) {
            System.err.println("ISBN is not set.");
            return;
        }

        // Retrieve and display the average score of the book
        double averageScore = documentTransaction.getAverageScore(isbn);
        scoreLabel.setText(averageScore == 0.0 ? "N/A" : Double.toString(averageScore));

        // Retrieve and display the book's details
        Book book = bookManagement.getDocumentViaIsbn(isbn);
        documentTextArea.setText(book != null ? book.getDetailsReview() : "Book information not found.");

        // Retrieve and display user comments
        ArrayList<String> comments = documentTransaction.getComment(isbn);
        if (comments != null && !comments.isEmpty()) {
            addComments(comments);
        } else {
            addNoCommentMessage();
        }
    }

    /**
     * Adds user comments to the comment box.
     *
     * @param comments a list of comments to display
     */
    private void addComments(ArrayList<String> comments) {
        for (String commentText : comments) {
            TextArea comment = new TextArea();
            comment.setText(commentText);
            comment.setWrapText(true);
            comment.setPrefHeight(100);
            comment.setEditable(false);
            comment.setStyle("-fx-font-size: 14; -fx-background-color: #f4f4f4; -fx-padding: 10;");

            commentVBox.getChildren().add(comment);
        }
    }

    /**
     * Displays a message indicating that there are no available comments.
     */
    private void addNoCommentMessage() {
        Label noCommentLabel = new Label("No comments available.");
        noCommentLabel.setStyle("-fx-font-size: 14; -fx-text-fill: gray;");
        commentVBox.getChildren().add(noCommentLabel);
    }

    /**
     * Handles the back button action, navigating to the book management screen.
     */
    @FXML
    private void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Documents/Books.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Books.fxml. Ensure the file path is correct.");
        }
    }
}
