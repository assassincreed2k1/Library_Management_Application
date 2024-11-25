package com.library.controller.personController;

import com.library.model.Person.Member;
import com.library.model.Person.User;
import com.library.model.helpers.DateString;
import com.library.model.helpers.MessageUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UpdateMemberController {
    private Member member;
    private String beforeSceneURL;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField dobTextField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField joinDateTextField;

    @FXML
    private TextField expiryDateTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text notification;

    @FXML
    private Button updateButton;

    @FXML
    private Button backButton;

    
    public void setMember(Member member) {
        this.member = member;
        populateFields();
    }

    public void setBeforeSceneURL(String url) {
        this.beforeSceneURL = url;
    }

    /**
     * fill in all information of text field.
     */
    private void populateFields() {
        nameTextField.setPromptText(member.getName());
        addressTextField.setPromptText(member.getAddress());
        dobTextField.setPromptText(member.getDateOfBirth());
        genderComboBox.setPromptText(member.getGender());
        joinDateTextField.setPromptText(member.getJoinDate());
        expiryDateTextField.setPromptText(member.getExpiryDate());

        genderComboBox.getItems().addAll("Male", "Female");

        setFieldStyle(Color.GRAY);

        enableFieldEdit(nameTextField);
        enableFieldEdit(addressTextField);
        enableFieldEdit(dobTextField);
        enableFieldEdit(joinDateTextField);
        enableFieldEdit(expiryDateTextField);
        enableFieldEdit(genderComboBox);
        enableFieldEdit(passwordField);
    }

    /**
     * set field style.
     * @param color Color color
     */
    private void setFieldStyle(Color color) {
        String colorStyle = "-fx-prompt-text-fill: " + toRGBCode(color) + ";";
        nameTextField.setStyle(colorStyle);
        addressTextField.setStyle(colorStyle);
        dobTextField.setStyle(colorStyle);
        genderComboBox.setStyle(colorStyle);
        joinDateTextField.setStyle(colorStyle);
        expiryDateTextField.setStyle(colorStyle);
        passwordField.setStyle(colorStyle);
    }

    /**
     * enable field edit.
     * @param textField TextField textField
     */
    private void enableFieldEdit(TextField textField) {
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle("-fx-prompt-text-fill: black;");
            } else if (textField.getText().isEmpty()) {
                textField.setStyle("-fx-prompt-text-fill: gray;");
            }
        });
    }

    /**
     * enable field edit.
     * @param comboBox ComboBox<String> comboBox
     */
    private void enableFieldEdit(ComboBox<String> comboBox) {
        comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                comboBox.setStyle("-fx-prompt-text-fill: black;");
            } else if (comboBox.getValue() == null) {
                comboBox.setStyle("-fx-prompt-text-fill: gray;");
            }
        });
    }

    /**
     * enable field edit.
     * @param passwordField PasswordField passwordField
     */
    private void enableFieldEdit(PasswordField passwordField) {
        passwordField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                passwordField.setStyle("-fx-prompt-text-fill: black;");
            } else if (passwordField.getText().isEmpty()) {
                passwordField.setStyle("-fx-prompt-text-fill: gray;");
            }
        });
    }

    /**
     * to RBG Code.
     * @param color Color color
     * @return RGG of color
     */
    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * set action for update button.
     */
    @FXML
    private void onUpdate() {
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String dob = dobTextField.getText();
        String gender = genderComboBox.getValue();
        String joinDate = joinDateTextField.getText();
        String expiryDate = expiryDateTextField.getText();
        String newPassword = passwordField.getText();

        Task<Void> updateTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (!(dob.isEmpty() || DateString.isValidDate(dob))) {
                    throw new Exception("Invalid Date of Birth format.");
                }
                if (!(joinDate.isEmpty() || DateString.isValidDate(joinDate))) {
                    throw new Exception("Invalid Join Date format.");
                }
                if (!(expiryDate.isEmpty() || DateString.isValidDate(expiryDate))) {
                    throw new Exception("Invalid Expiry Date format.");
                }

                if (!newPassword.isEmpty() && newPassword.equals(member.getPassword())) {
                    throw new Exception("New password must be different.");
                }

                if (!User.getId().equals(member.getMembershipId()) && (newPassword != null && !newPassword.isEmpty())) {
                    throw new Exception("You don't have access to change password.");
                }

                member.setName(name.isEmpty() ? member.getName() : name);
                member.setAddress(address.isEmpty() ? member.getAddress() : address);
                member.setDateOfBirth(dob.isEmpty() ? member.getDateOfBirth() : dob);
                member.setGender(gender == null ? member.getGender() : gender);
                member.setJoinDate(joinDate.isEmpty() ? member.getJoinDate() : joinDate);
                member.setExpiryDate(expiryDate.isEmpty() ? member.getExpiryDate() : expiryDate);
                if (!newPassword.isEmpty()) {
                    member.setPassword(newPassword);
                }

                member.updateMember();
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> MessageUtil.showMessage(notification, "Member updated successfully.", "green"));
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> MessageUtil.showMessage(notification, "Update failed: " + getException().getMessage(), "red"));
            }
        };

        MessageUtil.showMessage(notification, "Updating member. Please wait...", "blue");
        new Thread(updateTask).start();
    }

    /**
     * initialize when starting.
     */
    @FXML
    public void initialize() {
        updateButton.setOnAction(event -> onUpdate());
        backButton.setOnAction(event -> onBack());
    }

    /**
     * set action of back button.
     */
    @FXML
    public void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(beforeSceneURL));
            Stage stage = (Stage) updateButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
