package com.library.controller.memberController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import com.library.model.Person.Member;

public class AddMemberController {
    
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField dobField;
    @FXML
    private TextField phoneField;
    @FXML 
    private ComboBox<String> genderComboBox;
    @FXML
    private Button addButton;

    public void initialize() {
        genderComboBox.getItems().addAll("Male", "Female");

        addButton.setOnAction((event -> addToDataBase()));
    }

    private void addToDataBase() {
        String name = nameField.getText();
        String address = addressField.getText();
        String dob = dobField.getText();
        String phone = phoneField.getText();
        String gender = genderComboBox.getValue(); //get value from Combo Box

        Member member = new Member(name, address, dob, phone, gender);
        member.addMember();
    }




}
