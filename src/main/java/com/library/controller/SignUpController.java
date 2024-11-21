package com.library.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordField1; 
    @FXML
    private Button registerButton;
    @FXML
    private Button closeButton;
    @FXML
    private Label errorLabel;

    @FXML
    private void registerButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = passwordField1.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
        } else if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
        } else {
            // Logic to register a new user
            // Call user management service to save new user information
            errorLabel.setText("Registration successful!");
            clearFields();
        }
    }

    @FXML
    private void closeWindow() {
        try {
            switchTo("/fxml/Login/SignIn.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void switchTo(String pagePath) throws IOException {
        Parent libraryPage = FXMLLoader.load(getClass().getResource(pagePath));
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Scene scene = new Scene(libraryPage);
        stage.setScene(scene);
        stage.show();
    }

    // Clear fields after successful registration
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        passwordField1.clear();
        errorLabel.setText("");
    }
}
