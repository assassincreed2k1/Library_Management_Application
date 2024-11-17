package com.library.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import com.library.service.BackgroundService;
import com.library.service.LibrarianManagement;
import com.library.service.ServiceManager;

public class SignInController {
    private BackgroundService executor = ServiceManager.getBackgroundService();

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField; 
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton; 
    @FXML
    private Button closeButton;
    @FXML
    private Label errorLabel;

    private LibrarianManagement libManagement = new LibrarianManagement();

    @FXML
    private void loginButtonClick() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
        } else {
            try {
                int usernameID = Integer.parseInt(username.substring(1));
                if (libManagement.checkLibrarian(usernameID, password)) {
                    errorLabel.setText("Login successful!");
                    switchTo("/fxml/Library/LibraryHome.fxml");
                } else {
                    errorLabel.setText("Invalid username or password.");
                }
            } catch (NumberFormatException e) {
                errorLabel.setText("Username must start with a letter followed by numbers.");
            }
        }
    }

    // @FXML
    // private void loginButtonClick() throws IOException {
    //     String username = usernameField.getText();
    //     String password = passwordField.getText();

    //     if (username.isEmpty() || password.isEmpty()) {
    //         errorLabel.setText("Please enter username and password.");
    //     } else {
    //         switchTo("/fxml/Library/LibraryHome.fxml");
            
    //     }
    // }

    @FXML
    private void registerButtonClick() {
        errorLabel.setText("Register button clicked.");
        try {
            switchTo("/fxml/Login/SignUp.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
    }

    @FXML
    private void closeWindow() {
        this.executor.stopAllThreads();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void switchTo(String pagePath) throws IOException {
        Parent libraryPage = FXMLLoader.load(getClass().getResource(pagePath));
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Scene scene = new Scene(libraryPage);
        stage.setScene(scene);
        stage.show();
    }
}
