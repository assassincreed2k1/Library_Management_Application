package com.library;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label messageLabel;

    @FXML
    private void loginButtonClick() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter username and password.");
        } else {
            // doan trong if nay can duoc sua boi nguoi code user management --
            if (!username.isEmpty() && !password.isEmpty()) {
                messageLabel.setText("Login successful!");
                switchToLibrary();
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        }
    }

    private void switchToLibrary() throws IOException {
        Parent libraryPage = FXMLLoader.load(getClass().getResource("/fxml/Library/Library.fxml"));
        Stage stage = (Stage) usernameField.getScene().getWindow();

        Scene scene = new Scene(libraryPage);
        stage.setScene(scene);
        stage.show();
    }
}

