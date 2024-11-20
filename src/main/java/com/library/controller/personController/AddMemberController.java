package com.library.controller.personController;

import com.library.model.Person.Member;
import com.library.model.helpers.DateString;
import com.library.model.helpers.MessageUtil;
import com.library.model.helpers.PhoneNumber;

// import com.library.service.MemberManagement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
// import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AddMemberController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField dobField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private Text messageText;
    @FXML
    private Button addButton;

    // private MemberManagement memberManagement;

    // public AddMemberController() {
    //     memberManagement = new MemberManagement();
    // }

    @FXML
    private void initialize() {
        // Cài đặt giá trị mặc định cho ComboBox Gender
        genderComboBox.getItems().addAll("Male", "Female");

        addButton.setOnAction((event -> addToDataBase()));
    }

    private void addToDataBase() {
        // Lấy giá trị từ các trường nhập liệu
        String name = nameField.getText();
        String address = addressField.getText();
        String dob = dobField.getText();
        String phone = phoneField.getText();
        String gender = genderComboBox.getValue();

        // Kiểm tra các trường bắt buộc có trống không
        if (name.isEmpty() || address.isEmpty() || dob.isEmpty() || phone.isEmpty() || gender == null) {
            MessageUtil.showMessage(messageText, "Please fill in all required fields.", "red");
            return;
        }

        //Tạo task để xử lý đa luồng thêm member, xử lý ngoại lệ - done 
        Task<Void> addMemberTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (!DateString.isValidDate(dob)) {
                    throw new Exception("Invalid date of birth format.");
                }
                if (!PhoneNumber.isValidPhoneNumber(phone)) {
                    throw new Exception("Invalid phone number format.");
                }

                Member member = new Member(name, address, dob, phone, gender);
                if (!member.addMember()) {
                    throw new Exception("Error when adding member to database.");
                }
                return null;
            }

            @Override
            protected void succeeded() {
                MessageUtil.showMessage(messageText, "Member added successfully!", "green");
                clearFields();
            }

            @Override
            protected void failed() {
                MessageUtil.showMessage(messageText, "Failed to add member: " + getException().getMessage(), "red");
            }
        };

        // Hiển thị thông báo cho người dùng việc đang thêm 
        MessageUtil.showMessage(messageText, "Adding member, please wait...", "blue");

        //Chạy task trong một Thread riêng
        Thread thread = new Thread(addMemberTask);
        thread.setDaemon(true); // Đảm bảo thread tự động dừng khi ứng dụng kết thúc
        thread.start();
    }

    private void clearFields() {
        // Xóa thông tin đã nhập trong các trường
        nameField.clear();
        addressField.clear();
        dobField.clear();
        phoneField.clear();
        genderComboBox.setValue(null); // Xóa lựa chọn trong ComboBox
    }
}
