package com.library.controller.personController;

import com.library.model.Person.Member;
import com.library.service.MemberManagement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

    private MemberManagement memberManagement;

    public AddMemberController() {
        memberManagement = new MemberManagement();
    }

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
            showMessage("Please fill in all required fields.", "red");
            return;
        }

        // Nếu đầy đủ thông tin thì mới thêm
        Member member = new Member(name, address, dob, phone, gender);

        try {
            member.addMember(); // Gọi phương thức addMember
            showMessage("Member added successfully!", "green");
            // Xóa thông tin đã nhập nếu thêm thành công
            clearFields();
        } catch (Exception e) {
            // Nếu có lỗi xảy ra, thông báo lỗi
            showMessage("Failed to add member: " + e.getMessage(), "red");
        }
    }

    private void showMessage(String message, String color) {
        messageText.setText(message);
        messageText.setStyle("-fx-fill: " + color + ";");

        // Tạo Timeline để ẩn thông báo sau 2 giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> messageText.setText("")));
        timeline.play();
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
