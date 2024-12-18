package com.library.controller.personController;

import java.time.LocalDate;

import com.library.model.Person.Member;
import com.library.model.Person.User;
import com.library.model.doc.Book;
import com.library.model.doc.Document;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.model.helpers.MessageUtil;
import com.library.model.helpers.PDFPrinter;
import com.library.model.helpers.PersonIdHandle;
import com.library.service.CombinedDocument;
import com.library.service.DocumentTransaction;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DocumentBorrowController {
    private Member member = null;
    private Document document = null;
    private DocumentTransaction documentTransaction = new DocumentTransaction();
    private CombinedDocument combinedDocument = new CombinedDocument();

    @FXML
    private TextField memberIDTextField;

    @FXML
    private TextField documentIDTextField;

    @FXML
    private ComboBox<String> documentTypeComboBox;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextArea inforMemTextArea;

    @FXML
    private TextArea inforDocTextArea;

    @FXML
    private Button confirmButton;

    @FXML
    private Text notification;

    @FXML
    private Button checkButton;

    @FXML
    private Button printButton;

    String memberID = null;
    String documentID = null;
    String documentType = null;
    String transactionType = null;

    /**
     * initialize when starting.
     */
    @FXML
    public void initialize() {
        documentTypeComboBox.getItems().addAll("Book", "Magazine", "Newspaper");
        typeComboBox.getItems().addAll("Borrow", "Return");

        confirmButton.setOnAction(event -> onConfirm());
        checkButton.setOnAction(event -> onCheck());
        printButton.setOnAction(event -> onPrint());

        printButton.setDisable(true);
        confirmButton.setDisable(true); 
    }

    /**
     * on checking in borrowing document.
     */
    @FXML
    private void onCheck() {
        memberID = memberIDTextField.getText().trim();
        documentID = documentIDTextField.getText().trim();
        documentType = documentTypeComboBox.getValue();
        transactionType = typeComboBox.getValue();

        if (memberID.isEmpty() || documentID.isEmpty() || documentType == null || transactionType == null) {
            MessageUtil.showMessage(notification, "Please fill in all required fields.", "red");
            return;
        }

        Task<Void> findInforTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                member = (Member) PersonIdHandle.getPerson(memberID);

                if (member == null) {
                    
                    inforMemTextArea.setText("Not found");
                    throw new IllegalArgumentException("Not exists member with id: " + memberID);

                }

                if (member.getExpiryDate() == null || LocalDate.parse(member.getExpiryDate()).isBefore(LocalDate.now())) {
                    inforMemTextArea.setText("Card has expired!");
                    throw new Exception("This member needs to be renewed before borrowing");
                }
                

                Platform.runLater(() -> inforMemTextArea.setText(member.getDetails()));

                combinedDocument.updateCombinedDocument();
                document = combinedDocument.getDocument(documentID); 

                if (document == null) {
                    inforDocTextArea.setText("Not found");
                    throw new IllegalArgumentException("Not exists document with id: " + documentID);
                }

                Platform.runLater(() -> {
                    if (document instanceof Book && "Book".equals(documentType)) {
                        inforDocTextArea.setText(((Book) document).getDetails());
                    } else if (document instanceof Magazine && "Magazine".equals(documentType)) {
                        inforDocTextArea.setText(((Magazine) document).getDetails());
                    } else if (document instanceof Newspaper && "Newspaper".equals(documentType)) {
                        inforDocTextArea.setText(((Newspaper) document).getDetails());
                    } else {
                        inforDocTextArea.setText("Invalid ID. This is for another type.");
                        throw new IllegalArgumentException("Invalid document type."); 
                    }
                });

                return null; 
            }

            @Override
            protected void succeeded() {
                MessageUtil.showMessage(notification, "Finish finding information.", "green");
                confirmButton.setDisable(false);
            } 

            @Override
            protected void failed() {
                MessageUtil.showMessage(notification, "Failed to finding information: " + getException().getMessage(), "red");
            }
        };

        MessageUtil.showMessage(notification, "Finding information of document and member, please wait...", "blue");

        Thread thread = new Thread(findInforTask);
        thread.setDaemon(true); 
        thread.start();
    }

    /**
     * confirm putting to database after checking.
     */
    @FXML
    private void onConfirm() {
        String memberID = memberIDTextField.getText().trim();
        String documentID = documentIDTextField.getText().trim();
        String editedBy = User.getId(); // sau này vào ứng dụng được rồi thì sửa sau
    
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                if ("Borrow".equals(transactionType)) {
                    LocalDate localDate = LocalDate.now();
                    String borrowDate = localDate.toString();
                    String dueDate = localDate.plusDays(30).toString(); //30 is default, can change !
    
                    // Thực hiện mượn document
                    return documentTransaction.borrowDocument(documentID, memberID, 
                                                                editedBy, borrowDate, dueDate);
                } else if ("Return".equals(transactionType)) {
                    // Thực hiện trả document
                    return documentTransaction.returnDocument(documentID, memberID);
                }
                return "Invalid transaction type.";
            }
    
            @Override
            protected void succeeded() {
                // Gọi khi tác vụ thành công
                MessageUtil.showMessage(notification, getValue(), "green");
                printButton.setDisable(false);
            }
    
            @Override
            protected void failed() {
                // Gọi khi có lỗi xảy ra
                MessageUtil.showMessage(notification, "Error: " + getException().getMessage(), "red");
            }
        };
    
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    
    /**
     * print transaction when borrowing.
     */
    private void onPrint() {
        VBox contentBox = new VBox();
        contentBox.setSpacing(10);

        contentBox.getChildren().add(new javafx.scene.control.Label("Information of borrowing "));
        contentBox.getChildren().add(new javafx.scene.control.Label("\nMember information: "));
        contentBox.getChildren().add(new javafx.scene.control.Label(member.getDetails()));
        contentBox.getChildren().add(new javafx.scene.control.Label("\nDocument information: "));
        contentBox.getChildren().add(new javafx.scene.control.Label(document.getDetails()));
        contentBox.getChildren().add(new javafx.scene.control.Label("\nTransaction information: "));
        contentBox.getChildren().add(new javafx.scene.control.Label("Borrowed Date " + LocalDate.now().toString()));
        contentBox.getChildren().add(new javafx.scene.control.Label("Due Date: " + LocalDate.now().plusDays(30).toString()));

        try {
            PDFPrinter.printPDF(contentBox, memberID + "_" + documentID + "_" + LocalDate.now().toString());
            MessageUtil.showMessage(notification, "PDF has been successfully generated", "green");
        } catch (Exception e) {
            MessageUtil.showMessage(notification, "Error during PDF generating", "red");
            e.printStackTrace();
        }
    }
}
