package com.library.controller.personController;

import java.time.LocalDate;

import com.library.model.Person.Member;
import com.library.model.Person.User;
import com.library.model.doc.Book;
import com.library.model.doc.Document;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;
import com.library.model.helpers.MessageUtil;
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

    String memberID = null;
    String documentID = null;
    String documentType = null;
    String transactionType = null;

    @FXML
    public void initialize() {
        documentTypeComboBox.getItems().addAll("Book", "Magazine", "Newspaper");
        typeComboBox.getItems().addAll("Borrow", "Return");

        confirmButton.setOnAction(event -> onConfirm());
        checkButton.setOnAction(event -> onCheck());

        confirmButton.setDisable(true); // Vô hiệu hóa nút Confirm trước khi check thông tin
    }

    @FXML
    private void onCheck() {
        memberID = memberIDTextField.getText().trim();
        documentID = documentIDTextField.getText().trim();
        documentType = documentTypeComboBox.getValue();
        transactionType = typeComboBox.getValue();

        // Kiểm tra các trường có được điền đầy đủ không
        if (memberID.isEmpty() || documentID.isEmpty() || documentType == null || transactionType == null) {
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

                if (member.getExpiryDate() == null || LocalDate.parse(member.getExpiryDate()).isBefore(LocalDate.now())) {
                    inforMemTextArea.setText("Card has expired!");
                    throw new Exception("This member needs to be renewed before borrowing");
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
        // String transactionType = typeComboBox.getValue();
    
        // if (transactionType == null) {
        //     MessageUtil.showMessage(notification, "Please select a transaction type.", "red");
        //     return;
        // }
    
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
            }
    
            @Override
            protected void failed() {
                // Gọi khi có lỗi xảy ra
                MessageUtil.showMessage(notification, "Error: " + getException().getMessage(), "red");
            }
        };
    
        // Bắt đầu tác vụ trong một luồng mới
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
