package com.library.controller.personController;

import com.library.model.Person.Admin;
import com.library.model.Person.Librarian;
import com.library.model.Person.User;
import com.library.model.helpers.DateString;
import com.library.model.helpers.MessageUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UpdateLibrarianController {
    private Librarian librarian;
    private String beforeSceneURL;
    
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField dobTextField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField positionTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text messageText;

    @FXML
    private Button updateButton;

    @FXML
    private Button backButton;

    // Hàm này được gọi khi khởi tạo để nhận Librarian từ controller trước đó
    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
        populateFields();
    }

    public void setBeforeSceneURL(String url) {
        this.beforeSceneURL = url;
    }

    // Hàm populateFields để điền thông tin vào các trường
    private void populateFields() {
        // Set các trường là placeholder và không cho phép chỉnh sửa ngay
        nameTextField.setPromptText(librarian.getName());
        addressTextField.setPromptText(librarian.getAddress());
        dobTextField.setPromptText(librarian.getDateOfBirth());
        genderComboBox.setPromptText(librarian.getGender());
        phoneTextField.setPromptText(librarian.getPhoneNumber());
        positionTextField.setPromptText(librarian.getPosition());

        // Các lựa chọn giới tính cho ComboBox
        genderComboBox.getItems().addAll("Male", "Female");

        // Thiết lập màu placeholder-style ban đầu
        setFieldStyle(Color.GRAY);

        // Đổi màu khi nhấn vào TextField/ComboBox để chỉnh sửa
        enableFieldEdit(nameTextField);
        enableFieldEdit(addressTextField);
        enableFieldEdit(dobTextField);
        enableFieldEdit(phoneTextField);
        enableFieldEdit(positionTextField);
        enableFieldEdit(passwordField);
        enableFieldEdit(genderComboBox);
    }

    private void setFieldStyle(Color color) {
        String colorStyle = "-fx-prompt-text-fill: " + toRGBCode(color) + ";";
        nameTextField.setStyle(colorStyle);
        addressTextField.setStyle(colorStyle);
        dobTextField.setStyle(colorStyle);
        genderComboBox.setStyle(colorStyle);
        phoneTextField.setStyle(colorStyle);
        positionTextField.setStyle(colorStyle);
        passwordField.setStyle(colorStyle);
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

    private void enableFieldEdit(PasswordField passwordField) {
        passwordField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                passwordField.setStyle("-fx-prompt-text-fill: black;");
            } else if (passwordField.getText().isEmpty()) {
                passwordField.setStyle("-fx-prompt-text-fill: gray;");
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
        String phone = phoneTextField.getText();
        String position = positionTextField.getText();
        String password = passwordField.getText();

        // Tạo một task mới để thực hiện cập nhật
        Task<Void> updateTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Xử lý các ngoại lệ liên quan tới nhập liệu
                if (!(dob.isEmpty() || dob == null) && !DateString.isValidDate(dob)) {
                    throw new Exception("Invalid Date of birth format.");
                }

                if (password.equals(librarian.getPassword())) {
                    throw new Exception("This password is the same as before.");
                }

                if (!User.getId().equals(librarian.getEmployeeId()) && (password != null && !password.isEmpty())) {
                    throw new Exception("You don't have access to change password.");
                }

                librarian.setName(name.isEmpty() ? librarian.getName() : name);
                librarian.setAddress(address.isEmpty() ? librarian.getAddress() : address);
                librarian.setDateOfBirth(dob.isEmpty() ? librarian.getDateOfBirth() : dob);
                librarian.setGender(gender == null ? librarian.getGender() : gender);
                librarian.setPhoneNumber(phone.isEmpty() ? librarian.getPhoneNumber() : phone);
                librarian.setPosition(position.isEmpty() ? librarian.getPosition() : position);
                librarian.setPassword(password.isEmpty() ? librarian.getPassword() : password);

                if (librarian instanceof Admin) { //nếu là Admin thì cập nhật vào bảng Admin, librarian cập nhật bảng librarian
                    Admin admin = (Admin) librarian;
                    //admin.getInforFromDatabase();
                    admin.updateAdmin();
                } else {
                    librarian.updateLibrarian(); 
                }

                return null; 
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    MessageUtil.showMessage(messageText, "Librarian updated successfully.", "green");
                    System.out.println("Librarian updated: " + librarian.getDetails());
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    MessageUtil.showMessage(messageText, "Failed to update librarian. Error: " + getException().getMessage(), "red");
                });
            }
        };

        MessageUtil.showMessage(messageText, "Processing update. Please wait.", "blue");
        new Thread(updateTask).start();
    }

    @FXML
    public void initialize() {
        updateButton.setOnAction(event -> onUpdate());
        backButton.setOnAction(event -> onBack());

        updateButton.setDisable(false);
    }

    @FXML
    public void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(beforeSceneURL));
            Stage stage = (Stage) updateButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
