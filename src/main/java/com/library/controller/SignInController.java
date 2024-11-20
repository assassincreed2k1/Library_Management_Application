package com.library.controller;

import java.io.IOException;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import com.library.model.Person.Person;
import com.library.model.Person.User;
import com.library.model.helpers.PersonIdHandle;
// import com.library.service.BackgroundService;
// import com.library.service.ServiceManager;

public class SignInController {
    //private BackgroundService executor = ServiceManager.getBackgroundService();

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField; 
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton; 
    // @FXML
    // private Button closeButton;
    @FXML
    private Label errorLabel;

    Person person = null;

    @FXML
    private void loginButtonClick() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }

        Task<Void> loginTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                person = PersonIdHandle.getPerson(username);
                if (person == null) {
                    throw new Exception("User not found");
                }
                if (!person.getPassword().equals(password)) {
                    throw new Exception("Invalid username or password.");
                }
                return null;
            }

            @Override
            protected void succeeded() {
                User.setUser(username);
                try{
                    System.out.println(User.getDetails()); //test user
                    switchTo("/fxml/Library/LibraryHome.fxml");
                } catch (IOException e) {
                    errorLabel.setText("Faild to load the next scene. Error: " + e.getMessage());
                }
            }

            @Override
            protected void failed() {
                errorLabel.setText("Invalid username or password.");
            }
        };
        errorLabel.setText("Loading...");
        new Thread(loginTask).start();
    }

    // @FXML
    // private void loginButtonClick() throws IOException {
    //     String username = usernameField.getText();
    //     String password = passwordField.getText();

    //     if (username.isEmpty() || password.isEmpty()) {
    //         errorLabel.setText("Please enter username and password.");
    //     } else if (username.equals("admin") && password.equals("qwerty")) {
    //         switchTo("/fxml/Library/LibraryHome.fxml");
    //     } else {
    //         errorLabel.setText("Invalid username or password");
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
        }
    }

    // @FXML
    // private void closeWindow() {
    //     this.executor.stopAllThreads();
    //     Stage stage = (Stage) closeButton.getScene().getWindow();
    //     stage.close();
    // }

    private void switchTo(String pagePath) throws IOException {
        Parent libraryPage = FXMLLoader.load(getClass().getResource(pagePath));
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Scene scene = new Scene(libraryPage);
        stage.setScene(scene);
        stage.show();
    }
}
