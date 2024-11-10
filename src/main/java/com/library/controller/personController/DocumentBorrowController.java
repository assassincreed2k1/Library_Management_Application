package com.library.controller.personController;

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

//chưa chỉnh sửa
public class DocumentBorrowController {

    @FXML
    private TextField MemberIDTextField;

    @FXML
    private TextField DocumentIDTextField;

    @FXML
    private ComboBox<String> DocumentTypeComboBox;

    @FXML
    private TextArea inforMemTextArea;

    @FXML
    private TextArea inforDocTextArea;

    @FXML
    private Button confirmButton;

    @FXML
    private Text notification;

    @FXML
    public void initialize() {
        // Thêm các loại tài liệu vào comboBox (ví dụ)
        DocumentTypeComboBox.getItems().addAll("Book", "Magazine", "Thesis", "Newspaper");
    }

    @FXML
    private void handleConfirmAction() {
        String memberID = MemberIDTextField.getText().trim();
        String documentID = DocumentIDTextField.getText().trim();
        String documentType = DocumentTypeComboBox.getValue();

        // Kiểm tra các trường có được điền đầy đủ không
        if (memberID.isEmpty() || documentID.isEmpty() || documentType == null) {
            showNotification("Please fill in all fields.", Color.RED);
            return;
        }

        // Lấy thông tin của thành viên từ memberID (giả sử từ cơ sở dữ liệu)
        String memberInfo = getMemberInfo(memberID);
        if (memberInfo == null) {
            inforMemTextArea.setText("Not found.");
            return;
        } else {
            inforMemTextArea.setText(memberInfo);
        }

        // Lấy thông tin của tài liệu từ documentID và documentType
        String documentInfo = getDocumentInfo(documentID, documentType);
        if (documentInfo == null) {
            inforDocTextArea.setText("Not found.");
            return;
        } else {
            inforDocTextArea.setText(documentInfo);
        }

        showNotification("Borrow request processed successfully.", Color.GREEN);
    }

    private void showNotification(String message, Color color) {
        notification.setText(message);
        notification.setFill(color);

        // Tạo một Timeline để ẩn thông báo sau 2 giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> notification.setText("")));
        timeline.setCycleCount(1); // Chỉ chạy một lần
        timeline.play();
    }

    private String getMemberInfo(String memberID) {
        return "Sample Member Info for ID: " + memberID;
    }

    private String getDocumentInfo(String documentID, String documentType) {
        return "Sample Document Info for ID: " + documentID + ", Type: " + documentType;
    }
}
