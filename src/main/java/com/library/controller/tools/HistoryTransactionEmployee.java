package com.library.controller.tools;

import com.library.model.Person.Member;
import com.library.model.doc.Document;
import com.library.model.helpers.MessageUtil;
import com.library.model.loanDoc.Transaction;
import com.library.service.CombinedDocument;
import com.library.service.DocumentTransaction;
import com.library.service.LibraryService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.*;

/**
 * Controller class to manage the transaction history of library employees.
 * Handles actions such as viewing transactions, searching, and processing document returns.
 */
public class HistoryTransactionEmployee {

    private CombinedDocument combinedDocument = new CombinedDocument();
    private Member member = null;
    private Document document = null;
    private DocumentTransaction documentTransaction = new DocumentTransaction();

    @FXML
    private TableView<Transaction> bookTable;
    @FXML
    private TableColumn<Transaction, Integer> idTransactionColumn;
    @FXML
    private TableColumn<Transaction, String> documentIdColumn;
    @FXML
    private TableColumn<Transaction, String> memberIdColumn;
    @FXML
    private TableColumn<Transaction, String> borrowedDateColumn;
    @FXML
    private TableColumn<Transaction, String> returnDateColumn;
    @FXML
    private TableColumn<Transaction, String> dueDateColumn;
    @FXML
    private TableColumn<Transaction, String> editedByColumn;

    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;

    @FXML
    private TextArea documentTextArea;
    @FXML
    private TextArea memberTextArea;
    @FXML
    private Button returnButton;

    /**
     * Initializes the controller, sets up event listeners, and loads initial data.
     */
    @FXML
    private void initialize() {
        configureTableView();
        loadAllTransactions();

        searchButton.setOnAction(event -> onSearch());
        bookTable.setOnMouseClicked(event -> handleTableClick(event));
        returnButton.setOnAction(event -> onReturn());
    }

    /**
     * Configures the columns of the TableView for displaying transaction data.
     */
    private void configureTableView() {
        idTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        documentIdColumn.setCellValueFactory(new PropertyValueFactory<>("documentId"));
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        borrowedDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        editedByColumn.setCellValueFactory(new PropertyValueFactory<>("edited_by"));
    }

    /**
     * Loads all transaction records from the database and populates the TableView.
     */
    private void loadAllTransactions() {
        Task<Void> loadTransactionsTask = new Task<Void>() {
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
     * Fetches all transactions from the database.
     *
     * @return an observable list of transactions.
     * @throws SQLException if a database access error occurs.
     */
    private ObservableList<Transaction> fetchTransactions() throws SQLException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String query = """
                select idTransaction, document_id, membershipId, borrowDate, dueDate, returnDate, edited_by 
                from bookTransaction;
                """;

        try (Connection connection = LibraryService.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("idTransaction"),
                        resultSet.getString("document_id"),
                        resultSet.getString("membershipId"),
                        resultSet.getString("borrowDate"),
                        resultSet.getString("dueDate"),
                        resultSet.getString("returnDate"),
                        resultSet.getString("edited_by")
                ));
            }
        }
        return transactions;
    }

    /**
     * Handles the search functionality for transactions.
     * Fetches and displays transactions that match the search query.
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
                } else {
                    bookTable.setItems(searchTransactions(query));
                }
                return null;
            }

            @Override
            protected void failed() {
                MessageUtil.showAlert("error", "Database error", "Search failed!");
                System.out.println(getException().getMessage());
            }
        };

        Thread thread = new Thread(searchTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Searches for transactions based on a query.
     *
     * @param query the search query.
     * @return an observable list of matching transactions.
     * @throws SQLException if a database access error occurs.
     */
    private ObservableList<Transaction> searchTransactions(String query) throws SQLException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String sql = """
                select idTransaction, document_id, membershipId, borrowDate, dueDate, returnDate, edited_by 
                from bookTransaction
                where idTransaction like ? or document_id like ? or membershipId like ?;
                """;

        try (Connection connection = LibraryService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + query + "%");
            preparedStatement.setString(2, "%" + query + "%");
            preparedStatement.setString(3, "%" + query + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(new Transaction(
                            resultSet.getInt("idTransaction"),
                            resultSet.getString("document_id"),
                            resultSet.getString("membershipId"),
                            resultSet.getString("borrowDate"),
                            resultSet.getString("dueDate"),
                            resultSet.getString("returnDate"),
                            resultSet.getString("edited_by")
                    ));
                }
            }
        }
        return transactions;
    }

    /**
     * Handles the double-click event on the TableView, populating document and member details.
     *
     * @param event the mouse event.
     */
    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Transaction selectedTransaction = bookTable.getSelectionModel().getSelectedItem();

            if (selectedTransaction == null) {
                return;
            }

            member = new Member();
            member = member.getInforFromDatabase(selectedTransaction.getMembershipId());
            if (member == null) {
                memberTextArea.setText("Not found!");
                return;
            }

            combinedDocument.updateCombinedDocument();
            document = combinedDocument.getDocument(selectedTransaction.getDocumentId());
            if (document == null) {
                documentTextArea.setText("Not found!");
                return;
            }

            documentTextArea.setText(member.getDetails());
            memberTextArea.setText(document.getDetails());
        }
    }

    /**
     * Handles the return of a document, updating the database and refreshing the TableView.
     */
    @FXML
    private void onReturn() {
        if (member == null || document == null) {
            MessageUtil.showAlert("warning", "Invalid query", "Can't find this member or document in database!");
            return;
        }

        Task<Void> returnTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                documentTransaction.returnDocument(document.getID(), member.getMembershipId());
                loadAllTransactions();
                return null;
            }

            @Override
            protected void succeeded() {
                MessageUtil.showAlert("information", "Return Successful", "The document has been successfully returned.");
            }

            @Override
            protected void failed() {
                MessageUtil.showAlert("error", "Return Failed", "An error occurred while returning the document: " + getException().getMessage());
            }
        };

        Thread thread = new Thread(returnTask);
        thread.setDaemon(true);
        thread.start();
    }
}
