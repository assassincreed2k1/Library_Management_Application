package com.library.controller.tools;

import com.library.model.helpers.MessageUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class HistoryTransactionEmployee {
    @FXML
    private AnchorPane taskBar;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private TableView<?> bookTable;

    @FXML
    private TableColumn<?, ?> authorColumn;

    @FXML
    private TableColumn<?, ?> isbnColumn;

    @FXML
    private TableColumn<?, ?> genreColumn;

    @FXML
    private TableColumn<?, ?> publishDateColumn;

    @FXML
    private Button returnButton;

    @FXML
    private TextArea documentTextArea;

    @FXML
    private TextArea memberTextArea;

    // Initialize method to configure the controller
    @FXML
    public void initialize() {
        // Add listeners or set default configurations
        configureTableView();
        configureSearchField();
    }

    private void configureTableView() {
        // Set up columns if necessary (binding data models to columns)
        // Example: authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    }

    private void configureSearchField() {
        returnButton.setOnAction(event -> onReturn());
        searchButton.setOnAction(event -> onSearch());
    }

    @FXML
    private void onSearch() {
        String query = searchTextField.getText().trim();
        if (query.isEmpty()) {
            MessageUtil.showAlert("warning", "Search Warning", "Please enter a query to search.");
            return;
        }

        // Logic to search for transactions, members, or documents
        System.out.println("Search query: " + query);
        // Example: Call a service or database query method to fetch results
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Get selected item and show details in text areas
            Object selectedItem = bookTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                showDocumentDetails(selectedItem);
                showMemberDetails(selectedItem);
            }
        }
    }

    private void showDocumentDetails(Object document) {
        // Populate documentTextArea with details about the selected document
        documentTextArea.setText("Document details go here...");
    }

    private void showMemberDetails(Object member) {
        // Populate memberTextArea with details about the selected member
        memberTextArea.setText("Member details go here...");
    }

    @FXML
    private void onReturn() {
        // Logic to process returning a document
        System.out.println("Return button clicked!");
    }


}
