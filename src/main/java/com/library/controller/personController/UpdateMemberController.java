package com.library.controller.personController;

import com.library.model.Person.Member;
import com.library.model.helpers.DateString;
import com.library.model.helpers.MessageUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UpdateMemberController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField dobTextField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField joinDateTextField;

    @FXML
    private TextField expiryDateTextField;

    @FXML
    private Button updateButton;

    @FXML
    private Button backButton;

    @FXML
    private Text notification;

    private Member member;

    // Hàm này được gọi khi khởi tạo để nhận Member từ controller trước đó
    public void setMember(Member member) {
        this.member = member;
        populateFields();
    }

    // Hàm populateFields để điền thông tin vào các trường
    private void populateFields() {
        // Set các trường là placeholder và không cho phép chỉnh sửa ngay
        nameTextField.setPromptText(member.getName());
        addressTextField.setPromptText(member.getAddress());
        dobTextField.setPromptText(member.getDateOfBirth());
        genderComboBox.setPromptText(member.getGender());
        joinDateTextField.setPromptText(member.getJoinDate());
        expiryDateTextField.setPromptText(member.getExpiryDate());

        // Các lựa chọn giới tính cho ComboBox
        genderComboBox.getItems().addAll("Male", "Female");

        // Thiết lập màu placeholder-style ban đầu
        setFieldStyle(Color.GRAY);

        // Đổi màu khi nhấn vào TextField/ComboBox để chỉnh sửa
        enableFieldEdit(nameTextField);
        enableFieldEdit(addressTextField);
        enableFieldEdit(dobTextField);
        enableFieldEdit(joinDateTextField);
        enableFieldEdit(expiryDateTextField);
        enableFieldEdit(genderComboBox);
    }

    private void setFieldStyle(Color color) {
        String colorStyle = "-fx-prompt-text-fill: " + toRGBCode(color) + ";";
        nameTextField.setStyle(colorStyle);
        addressTextField.setStyle(colorStyle);
        dobTextField.setStyle(colorStyle);
        genderComboBox.setStyle(colorStyle);
        joinDateTextField.setStyle(colorStyle);
        expiryDateTextField.setStyle(colorStyle);
    }

    private void enableFieldEdit(TextField textField) {
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle("-fx-prompt-text-fill: black;");
            } else if (textField.getText().isEmpty()) {
                textField.setStyle("-fx-prompt-text-fill: gray;");
            }
        });
    }

    private void enableFieldEdit(ComboBox<String> comboBox) {
        comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                comboBox.setStyle("-fx-prompt-text-fill: black;");
            } else if (comboBox.getValue() == null) {
                comboBox.setStyle("-fx-prompt-text-fill: gray;");
            }
        });
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X", 
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    // Hàm này sẽ được gọi khi nhấn nút Update
    @FXML
    private void onUpdate() {
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String dob = dobTextField.getText();
        String gender = genderComboBox.getValue();
        String join = joinDateTextField.getText();
        String expiry = expiryDateTextField.getText();
        
        // Tạo một task mới để thực hiện cập nhật
        Task<Void> updateTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //xử lý các ngoại lệ liên quan tới nhập liệu
                if (!(dob.isEmpty() || dob == null) && !DateString.isValidDate(dob)) {
                    throw new Exception("Invalid Date of birth format.");
                } else if (!(join.isEmpty()) || join == null && !DateString.isValidDate(join)) {
                    throw new Exception("Invalid Join date format.");
                } else if (!(expiry.isEmpty() || expiry == null) && !DateString.isValidDate(expiry)) {
                    throw new Exception("Invalid Expiry date format.");
                }

                member.setName(name.isEmpty() ? member.getName() : name);
                member.setAddress(address.isEmpty() ? member.getAddress() : address);
                member.setDateOfBirth(dob.isEmpty() ? member.getDateOfBirth() : dob);
                member.setGender(gender == null ? member.getGender() : gender);
                member.setJoinDate(join.isEmpty() ? member.getJoinDate() : join);
                member.setExpiryDate(expiry.isEmpty() ? member.getExpiryDate() : expiry);

                member.updateMember(); //cập nhật member trên cơ sở dữ liệu

                return null; 
            }

            @Override
            protected void succeeded() {
                // Hiển thị thông báo thành công trên luồng chính
                Platform.runLater(() -> {
                    MessageUtil.showMessage(notification, "Member updated successfully.", "green");
                    System.out.println("Member updated: " + member.getDetails());
                });
            }

            @Override
            protected void failed() {
                // Hiển thị thông báo lỗi trên luồng chính
                Platform.runLater(() -> {
                    MessageUtil.showMessage(notification, "Failed to update member. Error: " + getException().getMessage(), "red");
                });
            }
        };

        MessageUtil.showMessage(notification, "Processing update. Please wait.", "blue");
        // Chạy Task trên một luồng riêng
        new Thread(updateTask).start();
    }

    // Hàm initialize được gọi khi controller được load
    @FXML
    public void initialize() {
        // Cấu hình sự kiện cho nút updateButton
        updateButton.setOnAction(event -> onUpdate());
        backButton.setOnAction(event -> onBack());

        // Vô hiệu hóa nút updateButton ban đầu
        updateButton.setDisable(false);
    }

    @FXML
    public void onBack() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DemoPerson/SearchPerson.fxml"));
            Stage stage = (Stage) updateButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
