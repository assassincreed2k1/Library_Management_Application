package com.library.controller.personController;

import com.library.model.Person.Librarian;
import com.library.model.Person.User;
import com.library.model.helpers.DateString;
import com.library.model.helpers.MessageUtil;
import com.library.model.helpers.PhoneNumber;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AddLibrarianController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField dobField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField positionField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private Text messageText;
    @FXML
    private Button addButton;

    /**
     * initialize when starting.
     */
    @FXML
    private void initialize() {
        genderComboBox.getItems().addAll("Male", "Female");
        
        if (User.isAdmin()) {
            addButton.setOnAction((event -> addToDataBase()));
        } else {
            MessageUtil.showMessage(messageText, "You don't have access to add Librarian.", "red");
        }
    }

    /**
     * add all information to database.
     */
    private void addToDataBase() {
        String name = nameField.getText();
        String address = addressField.getText();
        String dob = dobField.getText();
        String phone = phoneField.getText();
        String position = positionField.getText();
        String gender = genderComboBox.getValue();

        if (name.isEmpty() || address.isEmpty() || dob.isEmpty() || phone.isEmpty() || position.isEmpty() || gender == null) {
            MessageUtil.showMessage(messageText, "Please fill in all required fields.", "red");
            return;
        }

        Task<Void> addLibrarianTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                if (!DateString.isValidDate(dob)) {
                    throw new Exception("Invalid date of birth format.");
                }
                if (!PhoneNumber.isValidPhoneNumber(phone)) {
                    throw new Exception("Invalid phone number format.");
                }

                Librarian librarian = new Librarian(name, address, gender, dob, phone, position);
                if (!librarian.addLibrarian()) {
                    throw new Exception("Error when adding librarian to database");
                }
                return null;
            }

            @Override
            protected void succeeded() {
                MessageUtil.showMessage(messageText, "Librarian added successfully!", "green");
                clearFields();
            }

            @Override
            protected void failed() {
                MessageUtil.showMessage(messageText, "Failed to add librarian: " + getException().getMessage(), "red");
            }
        };

        MessageUtil.showMessage(messageText, "Adding librarian, please wait...", "blue");

        Thread thread = new Thread(addLibrarianTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * clear fields when finishing.
     */
    private void clearFields() {
        nameField.clear();
        addressField.clear();
        dobField.clear();
        phoneField.clear();
        positionField.clear();
        genderComboBox.setValue(null); 
    }
}
