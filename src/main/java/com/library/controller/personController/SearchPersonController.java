package com.library.controller.personController;

import com.library.model.Person.Admin;
import com.library.model.Person.Librarian;
import com.library.model.Person.Member;
import com.library.model.Person.Person;
import com.library.model.helpMethod.PersonIdHandle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SearchPersonController {

    @FXML
    private TextField idTextField;

    @FXML
    private Button searchButton;

    @FXML
    private TextArea informationTextField;

    @FXML
    public void initialize() {
        // Initialize any necessary data or listeners here if needed
        searchButton.setOnAction(event -> onSearch());
    }

    private void onSearch() {
        // Get the text from the TextField
        String personID = idTextField.getText();

        // You can add the code to search for the member here
        // For example:
        if (personID.isEmpty()) {
            System.out.println("Please enter a member ID.");
        } else {
            Person person = PersonIdHandle.getPerson(personID);
            if (person == null) {
                informationTextField.setText("Not Found!");
            } else{
                if (person instanceof Member) {
                    Member member = (Member) person;
                    informationTextField.setText(member.getDetails());
                } else if (person instanceof Librarian) {
                    Librarian librarian = (Librarian) person;
                    informationTextField.setText(librarian.getDetails());
                } else if (person instanceof Admin) {
                    Admin admin = (Admin) person;
                    informationTextField.setText(admin.getDetails());
                }
            }
        }
    }
}
