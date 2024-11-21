package com.library.controller.personController;

import java.time.LocalDate;

import com.library.model.Person.Member;
import com.library.model.doc.Book;
import com.library.model.doc.Document;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.model.helpMethod.PersonIdHandle;
import com.library.service.CombinedDocument;
import com.library.service.DocumentTransaction;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
        // Thêm các loại tài liệu vào DocumentTypeComboBox
        DocumentTypeComboBox.getItems().addAll("Book", "Magazine", "Newspaper");

        // Thêm các loại giao dịch vào typeComboBox
        typeComboBox.getItems().addAll("Borrow", "Return");

        confirmButton.setOnAction(event -> onConfirm());
        checkButton.setOnAction(event -> onCheck());

        confirmButton.setDisable(true); // Vô hiệu hóa nút Confirm mặc định
    }

    @FXML
    private void onCheck() {
        String memberID = MemberIDTextField.getText().trim();
        String documentID = DocumentIDTextField.getText().trim();
        String documentType = DocumentTypeComboBox.getValue();

        // Kiểm tra các trường có được điền đầy đủ không
        if (memberID.isEmpty() || documentID.isEmpty() || documentType == null) {
            showNotification("Please fill in all fields.", Color.RED);
            return;
        }

        // Lấy thông tin của thành viên
        member = (Member) PersonIdHandle.getPerson(memberID);

        if (member == null) {
            showNotification("Cannot find information for this member.", Color.RED);
            inforMemTextArea.setText("Not found.");
        } else {
            inforMemTextArea.setText(member.getDetails());
        }

        // Lấy thông tin tài liệu
        combinedDocument.updateCombinedDocument(); //update các dữ liệu mới vào bảng tổng hợp này 
        document = combinedDocument.getDocument(documentID); //lấy thông document từ cơ sở dữ liệu vào đây

        if (document == null) {
            inforDocTextArea.setText("Not found.");
            showNotification("Document not found.", Color.RED);
            confirmButton.setDisable(true);
        } else {
            if (document instanceof Book) {
                if (documentType.equals("Book")) {
                    inforDocTextArea.setText(((Book) document).getDetails());
                } else {
                    inforDocTextArea.setText("Invalid ID. This is for another one.");
                }  
            } else if (document instanceof Magazine) {
                if (documentType.equals("Magazine")) {
                    inforDocTextArea.setText(((Magazine) document).getDetails());
                } else {
                    inforDocTextArea.setText("Invalid ID. This is for another one.");
                }
            } else if (document instanceof Newspaper) {
                if (documentType.equals("Newspaper")) {
                    inforDocTextArea.setText(((Newspaper) document).getDetails());
                } else {
                    inforDocTextArea.setText("Invalid ID. This is for another one.");
                }
            }
            confirmButton.setDisable(false); // Kích hoạt nút Confirm nếu tìm thấy tài liệu
        }
    }

    @FXML
    private void onConfirm() {
        String transactionType = typeComboBox.getValue();

        if (transactionType == null) {
            showNotification("Please select a transaction type.", Color.RED);
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
                String notification = documentTransaction.borrowDocument(documentID, Integer.parseInt(memberID.substring(1)), 
                                                                        editedBy, borrowDate, dueDate);
                showNotification(notification, Color.BLACK);
                
            } else if ("Return".equals(transactionType)) {
                // Thực hiện trả document
                String notification = documentTransaction.returnDocument(documentID, Integer.parseInt(memberID.substring(1)));
                showNotification(notification, Color.BLACK);
            }
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Color.RED);
        }
    }

    private void showNotification(String message, Color color) {
        notification.setText(message);
        notification.setFill(color);

        // Tạo một Timeline để ẩn thông báo sau 2 giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> notification.setText("")));
        timeline.setCycleCount(1); // Chỉ chạy một lần
        timeline.play();
    }
}
