package com.library.controller.personController;

import com.library.model.Person.Member;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        // Cập nhật thông tin của member từ các trường đã chỉnh sửa
        member.setName(nameTextField.getText().isEmpty() ? member.getName() : nameTextField.getText());
        member.setAddress(addressTextField.getText().isEmpty() ? member.getAddress() : addressTextField.getText());
        member.setDateOfBirth(dobTextField.getText().isEmpty() ? member.getDateOfBirth() : dobTextField.getText());
        member.setGender(genderComboBox.getValue() == null ? member.getGender() : genderComboBox.getValue());
        member.setJoinDate(joinDateTextField.getText().isEmpty() ? member.getJoinDate() : joinDateTextField.getText());
        member.setExpiryDate(expiryDateTextField.getText().isEmpty() ? member.getExpiryDate() : expiryDateTextField.getText());

        member.updateMember();

        // Hiển thị thông báo thành công
        showNotification("Member updated successfully.", Color.GREEN);

        // Thực hiện các bước cần thiết sau khi cập nhật thông tin
        System.out.println("Member updated: " + member.getDetails());
    }

    private void showNotification(String message, Color color) {
        notification.setText(message);
        notification.setFill(color);

        // Sử dụng Timeline để xóa thông báo sau 2 giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> notification.setText("")));
        timeline.setCycleCount(1);
        timeline.play();
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
