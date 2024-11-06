package com.library.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import com.library.service.LibrarianManagement;;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Button closeButton;
    @FXML
    private Label errorLabel;

    private LibrarianManagement libManagament = new LibrarianManagement();

    @FXML
    private void loginButtonClick() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
        } else {
            if (libManagament.checkLibrarian(username, password)) {
                errorLabel.setText("Login successful!");
                switchToLibrary();
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        }
    }

    @FXML
    private void registerButtonClick() throws IOException {
        Parent registerPage = FXMLLoader.load(getClass().getResource("/fxml/Login/Register.fxml"));
        Stage stage = (Stage) usernameField.getScene().getWindow();

        Scene scene = new Scene(registerPage);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void switchToLibrary() throws IOException {
        Parent libraryPage = FXMLLoader.load(getClass().getResource("/fxml/Library/LibraryHome.fxml"));
        Stage stage = (Stage) usernameField.getScene().getWindow();

        Scene scene = new Scene(libraryPage);
        stage.setScene(scene);
        stage.show();
    }
}

