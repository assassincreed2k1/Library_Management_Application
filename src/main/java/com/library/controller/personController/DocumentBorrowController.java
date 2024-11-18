package com.library.controller.personController;

import java.time.LocalDate;

import com.library.model.Person.Member;
import com.library.model.doc.Book;
import com.library.model.doc.Document;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.model.helpers.MessageUtil;
import com.library.model.helpers.PersonIdHandle;
import com.library.service.CombinedDocument;
import com.library.service.DocumentTransaction;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class DocumentBorrowController {
    private Member member = null;
    private Document document = null;
    private DocumentTransaction documentTransaction = new DocumentTransaction();
    private CombinedDocument combinedDocument = new CombinedDocument();

    @FXML
    private TextField MemberIDTextField;

    @FXML
    private TextField DocumentIDTextField;

    @FXML
    private ComboBox<String> DocumentTypeComboBox;

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
    public void initialize() {
        DocumentTypeComboBox.getItems().addAll("Book", "Magazine", "Newspaper");
        typeComboBox.getItems().addAll("Borrow", "Return");

        confirmButton.setOnAction(event -> onConfirm());
        checkButton.setOnAction(event -> onCheck());

        confirmButton.setDisable(true); // Vô hiệu hóa nút Confirm trước khi check thông tin
    }

    @FXML
    private void onCheck() {
        String memberID = MemberIDTextField.getText().trim();
        String documentID = DocumentIDTextField.getText().trim();
        String documentType = DocumentTypeComboBox.getValue();

        // Kiểm tra các trường có được điền đầy đủ không
        if (memberID.isEmpty() || documentID.isEmpty() || documentType == null || typeComboBox == null) {
            MessageUtil.showMessage(notification, "Please fill in all required fields.", "red");
            return;
        }

        // Tạo một Task để thực hiện các công việc nặng
        Task<Void> findInforTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Kiểm tra thông tin của thành viên 
                member = (Member) PersonIdHandle.getPerson(memberID);

                if (member == null) {
                    inforMemTextArea.setText("Not found");
                    throw new IllegalArgumentException("Not exists member with id: " + memberID);

                }

                Platform.runLater(() -> inforMemTextArea.setText(member.getDetails()));

                // Kiểm tra thông tin của tài liệu
                combinedDocument.updateCombinedDocument(); // Cập nhật dữ liệu
                document = combinedDocument.getDocument(documentID); 

                if (document == null) {
                    inforDocTextArea.setText("Not found");
                    throw new IllegalArgumentException("Not exists document with id: " + documentID);
                }

                // Cập nhật thông tin tài liệu vào UI
                Platform.runLater(() -> {
                    if (document instanceof Book && "Book".equals(documentType)) {
                        inforDocTextArea.setText(((Book) document).getDetails());
                    } else if (document instanceof Magazine && "Magazine".equals(documentType)) {
                        inforDocTextArea.setText(((Magazine) document).getDetails());
                    } else if (document instanceof Newspaper && "Newspaper".equals(documentType)) {
                        inforDocTextArea.setText(((Newspaper) document).getDetails());
                    } else {
                        inforDocTextArea.setText("Invalid ID. This is for another type.");
                        throw new IllegalArgumentException("Invalid document type."); // Ném ngoại lệ
                    }
                });

                return null; // Trả về null khi hoàn thành
            }

            @Override
            protected void succeeded() {
                MessageUtil.showMessage(notification, "Finish finding information.", "green");
                confirmButton.setDisable(false);
            } 

            // Nếu gặp lỗi, gọi failed()
            @Override
            protected void failed() {
                MessageUtil.showMessage(notification, "Failed to finding information: " + getException().getMessage(), "red");
            }
        };

        // Hiển thị thông báo "Đang thêm" trong khi chạy công việc trong background
        MessageUtil.showMessage(notification, "Finding information of document and member, please wait...", "blue");

        // Chạy Task trong một thread riêng
        Thread thread = new Thread(findInforTask);
        thread.setDaemon(true); // Đảm bảo thread tự động dừng khi ứng dụng kết thúc
        thread.start();
    }

    @FXML
    private void onConfirm() {
        String transactionType = typeComboBox.getValue();

        if (transactionType == null) {
            MessageUtil.showMessage(notification, "Please select a transaction type.", "red");
            return;
        }

        try {
            String memberID = MemberIDTextField.getText().trim();
            String documentID = DocumentIDTextField.getText().trim();
            int editedBy = 1; // sau này vào ứng dụng được rồi thì sửa sau

            if ("Borrow".equals(transactionType)) {
                LocalDate localDate = LocalDate.now();
                String borrowDate = localDate.toString();
                String dueDate = localDate.plusDays(7).toString(); //7 is default, can change !

                // Thực hiện mượn document
                String message = documentTransaction.borrowDocument(documentID, Integer.parseInt(memberID.substring(1)), 
                                                                        editedBy, borrowDate, dueDate);
                MessageUtil.showMessage(notification, message, "green");
                
            } else if ("Return".equals(transactionType)) {
                // Thực hiện trả document
                String message = documentTransaction.returnDocument(documentID, Integer.parseInt(memberID.substring(1)));
                MessageUtil.showMessage(notification, message, "green");
            }
        } catch (Exception e) {
            MessageUtil.showMessage(notification, "Error: " + e.getMessage(), "red");
        }
    }
}
