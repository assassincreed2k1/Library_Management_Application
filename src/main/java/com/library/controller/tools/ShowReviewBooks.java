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

public class ShowReviewBooks {
    private final DocumentTransaction documentTransaction = new DocumentTransaction();
    private final BookManagement bookManagement = new BookManagement();
    private String isbn;

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

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> onBack());
    }

    private void loadBookDetails() {
        if (isbn == null || isbn.isEmpty()) {
            System.err.println("ISBN is not set.");
            return;
        }

        double averageScore = documentTransaction.getAverageScore(isbn);
        scoreLabel.setText(averageScore == 0.0 ? "N/A" : Double.toString(averageScore));

        Book book = bookManagement.getDocumentViaIsbn(isbn);
        documentTextArea.setText(book != null ? book.getDetailsReview() : "Book information not found.");

        ArrayList<String> comments = documentTransaction.getComment(isbn);
        if (comments != null && !comments.isEmpty()) {
            addComments(comments);
        } else {
            addNoCommentMessage();
        }
    }

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

    private void addNoCommentMessage() {
        Label noCommentLabel = new Label("No comments available.");
        noCommentLabel.setStyle("-fx-font-size: 14; -fx-text-fill: gray;");
        commentVBox.getChildren().add(noCommentLabel);
    }

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
