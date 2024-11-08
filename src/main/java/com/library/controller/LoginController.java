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
    private Button closeButton;
    @FXML
    private Label errorLabel;

    private LibrarianManagement libManagament = new LibrarianManagement();

    @FXML
    private void loginButtonClick() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        //tam thoi la chi cho librarian 
        int usernameID = Integer.parseInt(username.substring(1));
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
        } else {
            // doan trong if nay can duoc sua boi nguoi code user management --
            if (!username.isEmpty() && !password.isEmpty() && libManagament.checkLibrarian(usernameID, password)) {
                errorLabel.setText("Login successful!");
                switchToLibrary();
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        }
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

