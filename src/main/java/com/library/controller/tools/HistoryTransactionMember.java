package com.library.controller.tools;

import com.library.model.Person.User;
import com.library.model.doc.Document;
import com.library.model.helpers.MessageUtil;
import com.library.model.loanDoc.Transaction;
import com.library.service.CombinedDocument;
import com.library.service.LibraryService;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code HistoryTransactionMember} class manages the user's transaction history in the library system.
 * It allows users to view their borrowing history, search for specific transactions, and submit reviews.
 */
public class HistoryTransactionMember {
    private CombinedDocument combinedDocument = new CombinedDocument();
    private Document document = null;

    @FXML
    private TableView<Transaction> bookTable;
    @FXML
    private TableColumn<Transaction, Integer> idTransactionColumn;
    @FXML
    private TableColumn<Transaction, String> documentIdColumn;
    @FXML
    private TableColumn<Transaction, String> borrowDateColumn;
    @FXML
    private TableColumn<Transaction, String> returnDateColumn;
    @FXML
    private TableColumn<Transaction, String> dueDateColumn;
    @FXML
    private TableColumn<Transaction, Integer> scoreColumn;

    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;

    @FXML
    private TextArea documentTextArea;

    @FXML
    private Button reviewButton;

    /**
     * Initializes the controller and sets up the table and event listeners.
     */
    @FXML
    private void initialize() {
        configureTableView();
        loadAllTransactions();

        searchButton.setOnAction(event -> onSearch());
        bookTable.setOnMouseClicked(event -> handleTableClick(event));
        reviewButton.setOnAction(event -> onReview());
    }

    /**
     * Configures the table view by associating table columns with properties from the {@link Transaction} class.
     */
    private void configureTableView() {
        idTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        documentIdColumn.setCellValueFactory(new PropertyValueFactory<>("documentId"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
    }

    /**
     * Loads all transactions for the current user from the database and populates the table.
     */
    private void loadAllTransactions() {
        Task<Void> loadTransactionsTask = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                ObservableList<Transaction> transactions = fetchTransactions();
                bookTable.setItems(transactions);
                return null;
            }

            @Override
            protected void failed() {
                MessageUtil.showAlert("error", "Database Error", "Failed to load transactions.");
                System.out.println(getException().getMessage());
            }
        };

        Thread thread = new Thread(loadTransactionsTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Fetches all transactions for the current user from the database.
     *
     * @return an observable list of {@link Transaction} objects.
     * @throws SQLException if there is an error accessing the database.
     */
    private ObservableList<Transaction> fetchTransactions() throws SQLException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String query = """
                SELECT idTransaction, document_id, borrowDate, dueDate, returnDate, score
                FROM bookTransaction
                WHERE membershipId = ?;
                """;

        try (Connection connection = LibraryService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, User.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(new Transaction(
                            resultSet.getInt("idTransaction"),
                            resultSet.getString("document_id"),
                            resultSet.getString("borrowDate"),
                            resultSet.getString("dueDate"),
                            resultSet.getString("returnDate"),
                            resultSet.getInt("score")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
            throw e; 
        }
        return transactions;
    }

    /**
     * Handles the search functionality for the transaction history.
     */
    @FXML
    private void onSearch() {
        String query = searchTextField.getText().trim();
        if (query.isEmpty()) {
            MessageUtil.showAlert("warning", "Search Warning", "Please enter a query to search.");
            return;
        }

        Task<Void> searchTask = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                if (query.equalsIgnoreCase("@Show all")) {
                    bookTable.setItems(fetchTransactions());
                } else if (query.equalsIgnoreCase("@Not reviewed")) {
                    bookTable.setItems(fetchNotReviewed());
                } else {
                    bookTable.setItems(searchTransactions(query));
                }
                return null;
            }

            @Override
            protected void failed() {
                MessageUtil.showAlert("error", "Database Error", "Search failed!");
                System.out.println(getException().getMessage());
            }
        };

        Thread thread = new Thread(searchTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Fetches transactions matching the search query.
     *
     * @param query the search query.
     * @return an observable list of matching {@link Transaction} objects.
     * @throws SQLException if there is an error accessing the database.
     */
    private ObservableList<Transaction> searchTransactions(String query) throws SQLException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String sql = """
                SELECT idTransaction, document_id, borrowDate, dueDate, returnDate, score
                FROM bookTransaction
                WHERE membershipId = ? AND (idTransaction LIKE ? OR document_id LIKE ?);
                """;

        try (Connection connection = LibraryService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, User.getId());
            preparedStatement.setString(2, "%" + query + "%");
            preparedStatement.setString(3, "%" + query + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(new Transaction(
                            resultSet.getInt("idTransaction"),
                            resultSet.getString("document_id"),
                            resultSet.getString("borrowDate"),
                            resultSet.getString("dueDate"),
                            resultSet.getString("returnDate"),
                            resultSet.getInt("score")
                    ));
                }
            }
        }
        return transactions;
    }

    /**
     * Handles a double-click event on the table, displaying detailed information about the selected document.
     *
     * @param event the mouse event triggered by the user.
     */
    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Transaction selectedTransaction = bookTable.getSelectionModel().getSelectedItem();
            if (selectedTransaction == null) return;

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    combinedDocument.updateCombinedDocument();
                    document = combinedDocument.getDocument(selectedTransaction.getDocumentId());

                    if (document == null) {
                        Platform.runLater(() -> documentTextArea.setText("Document not found!"));
                    } else {
                        Platform.runLater(() -> documentTextArea.setText(document.getDetails()));
                    }
                    return null;
                }
            };

            new Thread(task).start();
        }
    }

    /**
     * Navigates the user to the review submission screen for the selected document.
     */
    @FXML
    private void onReview() {
        if (document == null) {
            MessageUtil.showAlert("error", "Can't review because of error. Please check!", "Error when reviewing");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Library/Tools/ReviewBorrowedBooks.fxml"));
            Parent root = loader.load();
            ReviewBookController controller = loader.getController();
            controller.setDocumentId(document.getID());
            controller.setMembershipId(User.getId());

            Stage stage = (Stage) reviewButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            loadAllTransactions();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading MainScene.fxml. Ensure the file path is correct.");
        }
    }

    /**
     * Fetches all transactions that have not been reviewed by the user.
     *
     * @return an observable list of unreviewed {@link Transaction} objects.
     * @throws SQLException if there is an error accessing the database.
     */
    private ObservableList<Transaction> fetchNotReviewed() throws SQLException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String query = """
                SELECT idTransaction, document_id, borrowDate, dueDate, returnDate, score
                FROM bookTransaction
                WHERE membershipId = ? AND score IS NULL;
                """;

        try (Connection connection = LibraryService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, User.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(new Transaction(
                            resultSet.getInt("idTransaction"),
                            resultSet.getString("document_id"),
                            resultSet.getString("borrowDate"),
                            resultSet.getString("dueDate"),
                            resultSet.getString("returnDate"),
                            resultSet.getInt("score")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
            throw e; 
        }
        return transactions;
    }
}
