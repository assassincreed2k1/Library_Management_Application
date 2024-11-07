package com.library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void registerButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
        } else if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
        } else {
            errorLabel.setText("Registered successfully!");
            // code doan nay nhe Thanh
        }
    }

    @FXML
    private void closeWindow() {
        errorLabel.getScene().getWindow().hide();
    }
}
