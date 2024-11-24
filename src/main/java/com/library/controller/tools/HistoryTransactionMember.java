package com.library.controller.tools;

import com.library.model.doc.Document;
import com.library.model.helpers.MessageUtil;
import com.library.model.loanDoc.Transaction;
import com.library.service.CombinedDocument;
import com.library.service.LibraryService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.*;

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

    @FXML
    private void initialize() {
        configureTableView();
        loadAllTransactions();

        searchButton.setOnAction(event -> onSearch());
        bookTable.setOnMouseClicked(event -> handleTableClick(event));
        reviewButton.setOnAction(event -> onReview());
    }

    private void configureTableView() {
        idTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        documentIdColumn.setCellValueFactory(new PropertyValueFactory<>("documentId"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
    }

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

    private ObservableList<Transaction> fetchTransactions() throws SQLException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String query = """
                select idTransaction, document_id, borrowDate, dueDate, returnDate, score
                from bookTransaction;
                """;

        try (Connection connection = LibraryService.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

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
        return transactions;
    }

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

    private ObservableList<Transaction> searchTransactions(String query) throws SQLException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String sql = """
                select idTransaction, document_id, borrowDate, dueDate, returnDate, score
                from bookTransaction
                where idTransaction like ? or document_id like ?;
                """;

        try (Connection connection = LibraryService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + query + "%");
            preparedStatement.setString(2, "%" + query + "%");

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

    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Transaction selectedTransaction = bookTable.getSelectionModel().getSelectedItem();
            if (selectedTransaction == null) return;

            combinedDocument.updateCombinedDocument();
            document = combinedDocument.getDocument(selectedTransaction.getDocumentId());
            if (document == null) {
                documentTextArea.setText("Document not found!");
                return;
            }

            documentTextArea.setText(document.getDetails());
        }
    }

    @FXML
    private void onReview() {
        Transaction selectedTransaction = bookTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            MessageUtil.showAlert("warning", "Review Error", "Please select a transaction to review.");
            return;
        }

        MessageUtil.showAlert("information", "Review Details", "Reviewing transaction: " + selectedTransaction);
    }
}
