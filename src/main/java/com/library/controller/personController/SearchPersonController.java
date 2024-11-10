package com.library.controller.personController;

import com.library.model.Person.Admin;
import com.library.model.Person.Librarian;
import com.library.model.Person.Member;
import com.library.model.Person.Person;
import com.library.model.helpMethod.PersonIdHandle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SearchPersonController {

    @FXML
    private TextField idTextField;

    @FXML
    private Button searchButton;

    @FXML
    private TextArea informationTextField;

    @FXML
    private Button updateButton;

    @FXML
    private Button removeButton;

    @FXML
    private Text notification;

    @FXML
    public void initialize() {
        searchButton.setOnAction(event -> onSearch());
        updateButton.setOnAction(event -> onUpdate());
        removeButton.setOnAction(event -> onRemove());

        // Vô hiệu hóa 2 nút khi khởi động
        updateButton.setDisable(true);
        removeButton.setDisable(true);
    }

    private void onSearch() {
        String personID = idTextField.getText();

        if (personID.isEmpty()) {
            showNotification("Please enter a member ID.", Color.RED);
        } else {
            Person person = PersonIdHandle.getPerson(personID);
            if (person == null) {
                informationTextField.setText("Not Found!");
                updateButton.setDisable(true);
                removeButton.setDisable(true);
            } else {
                informationTextField.setText(person.getDetails());
                updateButton.setDisable(false);
                removeButton.setDisable(false);
            }
        }
    }

    private void onRemove() {
        String personID = idTextField.getText();

        if (personID.isEmpty()) {
            showNotification("Please enter ID.", Color.RED);
        } else {
            Person person = PersonIdHandle.getPerson(personID);
            if (person == null) {
                informationTextField.setText("Not Found!");
            } else if (person instanceof Admin) {
                showNotification("You don't have access to delete admin.", Color.RED);
            } else if (person instanceof Librarian) {
                showNotification("You don't have access to delete librarian", Color.RED);
            } else {
                if (person instanceof Member) {
                    Member member = (Member) person;
                    member.deleteMember();
                }
                showNotification("Person removed successfully.", Color.GREEN);
                informationTextField.clear();
                updateButton.setDisable(true);
                removeButton.setDisable(true);
            }
        }
    }

    private void onUpdate() {
        String personID = idTextField.getText();
        Person person = PersonIdHandle.getPerson(personID);
    
        if (!personID.isEmpty() && person != null) {
            if (person instanceof Member) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DemoPerson/UpdateMember.fxml"));
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    Scene scene = new Scene(loader.load());
        
                    // Lấy controller của UpdateMember và truyền Member vào
                    UpdateMemberController controller = loader.getController();
                    controller.setMember((Member) person);
        
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                showNotification("You don't have access to update", Color.RED);
            }
        } else {
            showNotification("Invalid ID for update.", Color.RED);
        }
    }
    

    private void showNotification(String message, Color color) {
        notification.setText(message);
        notification.setFill(color);

        // Sử dụng Timeline để xóa thông báo sau 2 giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> notification.setText("")));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
