package com.library.controller.tools;

import com.library.model.helpers.MessageUtil;
import com.library.service.DocumentTransaction;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ReviewBookController {
    String membershipId;
    String documentId;
    DocumentTransaction documentTransaction = new DocumentTransaction();

    @FXML
    private HBox starRatingBox;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Button submitCommentButton;

    @FXML
    private VBox commentsBox;

    @FXML
    private Text messageText;

    private int selectedRating = 0;

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * Initialize the controller after the root element has been processed.
     */
    @FXML
    public void initialize() {
        setupStarRating();

        submitCommentButton.setOnAction(event -> handleSubmitReview());
    }

    /**
     * Setup star rating logic.
     */
    private void setupStarRating() {
        for (int i = 0; i < starRatingBox.getChildren().size(); i++) {
            Label star = (Label) starRatingBox.getChildren().get(i);
            int starIndex = i + 1;

            // Highlight stars on hover
            star.setOnMouseEntered(event -> highlightStars(starIndex));
            star.setOnMouseExited(event -> highlightStars(selectedRating));

            // Set rating on click
            star.setOnMouseClicked(event -> {
                selectedRating = starIndex;
                highlightStars(selectedRating);
            });
        }
    }

    /**
     * Highlight stars up to the given count.
     * @param count Number of stars to highlight.
     */
    private void highlightStars(int count) {
        for (int i = 0; i < starRatingBox.getChildren().size(); i++) {
            Label star = (Label) starRatingBox.getChildren().get(i);
            if (i < count) {
                star.setText("★");
            } else {
                star.setText("☆");
            }
        }
    }

    /**
     * Handle the submission of a review.
     */
    private void handleSubmitReview() {
        String comment = commentTextArea.getText().trim();

        if (selectedRating == 0) {
            MessageUtil.showMessage(messageText, "Please select a rating.", "red");
            return;
        }

        if (comment.isEmpty()) {
            MessageUtil.showMessage(messageText, "Please enter a comment.", "red");
            return;
        }

        // // Add the new review to the comments section
        // Label newComment = new Label("Rating: " + selectedRating + " stars\n" + comment);
        // newComment.getStyleClass().add("review-comment");
        // commentsBox.getChildren().add(newComment);
        Task<Void> handleComment = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                documentTransaction.reviewDocument(documentId, membershipId, selectedRating, comment);
                return null;
            }

            @Override
            protected void succeeded() {
                MessageUtil.showMessage(messageText, "Your comment submited successfully", "green");
                commentTextArea.clear();
                selectedRating = 0;
                highlightStars(0);
            }

            @Override
            protected void failed() {
                MessageUtil.showMessage(messageText, "Your comment submmited failed", "red");
            }
        };

        MessageUtil.showMessage(messageText, "In progress submitting your comment", "blue");
        Thread thread = new Thread(handleComment);
        thread.setDaemon(true);
        thread.start();
    }
}
