package com.library.controller.personController;

import com.library.model.Person.Admin;
import com.library.model.Person.Librarian;
import com.library.model.Person.Member;
import com.library.model.Person.Person;
import com.library.model.Person.User;
import com.library.model.helpers.MessageUtil;
import com.library.model.helpers.PersonIdHandle;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    private Text messageText;

    private Person person = null;

    /**
     * initizalize when starting.
     */
    @FXML
    public void initialize() {
        searchButton.setOnAction(event -> onSearch());
        updateButton.setOnAction(event -> onUpdate());
        removeButton.setOnAction(event -> onRemove());

        updateButton.setDisable(true);
        removeButton.setDisable(true);
    }

    /**
     * search person based on id.
     */
    private void onSearch() {
        String personID = idTextField.getText();
    
        if (personID.isEmpty()) {
            MessageUtil.showMessage(messageText, "Please enter a member ID.", "red");
        } else {
            // Tạo một task mới để thực hiện tìm kiếm
            Task<Void> searchTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    person = PersonIdHandle.getPerson(personID);
                    return null;
                }
    
                @Override
                protected void succeeded() {
                    // Cập nhật giao diện người dùng
                    if (person == null) {
                        informationTextField.setText("Not Found!");
                    } else {
                        informationTextField.setText(person.getDetails());
                    }
                    MessageUtil.showMessage(messageText, "Searched successfully", "green");
                    updateButton.setDisable(false);
                    removeButton.setDisable(false);
                }
    
                @Override
                protected void failed() {
                    super.failed();
                    MessageUtil.showMessage(messageText, "An error occurred during search. Error: " + getException().getMessage(), "red");
                    updateButton.setDisable(true);
                    removeButton.setDisable(true);
                }
            };
    
            MessageUtil.showMessage(messageText, "Searching. Please wait.... ", "blue");
            Thread thread = new Thread(searchTask);
            thread.setDaemon(true);
            thread.start();
        }
    }
    
    /**
     * remove person from database.
     */
    private void onRemove() {
        if (person == null) {
            MessageUtil.showMessage(messageText, "Can't find this member.", "red");
            return;
        } 
    
        Task<Void> removeTask = new Task<Void>() {   
            @Override 
            protected Void call() throws Exception {
                // Kiểm tra loại người dùng và xử lý xóa
                if (person instanceof Admin) {
                    throw new Exception("You don't have access to remove Admin.");
                } else if (person instanceof Librarian) {
                    if (User.isAdmin()) {
                        Librarian librarian = (Librarian) person;
                        librarian.deleteLibrarian();
                    } else {
                        throw new Exception("You don't have access to remove Librarian.");
                    }
                } else if (person instanceof Member) {
                    Member member = (Member) person;
                    member.deleteMember();
                } else {
                    throw new Exception("Unknown person type.");
                }
                return null; // Phải trả về null để không có lỗi biên dịch
            }  
    
            @Override
            protected void succeeded() {
                MessageUtil.showMessage(messageText, "Person removed successfully.", "green");
                informationTextField.clear();
                updateButton.setDisable(true);
                removeButton.setDisable(true);
            }
    
            @Override
            protected void failed() {
                MessageUtil.showMessage(messageText, "Fail to remove this member. Error: " + getException().getMessage(), "red");
            }
        };
    
        MessageUtil.showMessage(messageText, "Finding member information. Please wait: ", "blue");
        // Chạy Task trên một luồng riêng
        Thread thread = new Thread(removeTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * update database.
     */
    private void onUpdate() {
        if (person != null) {
            if (person instanceof Member && (!User.isMember() 
                || User.getId().equals(((Member)person).getMembershipId()))) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Person/UpdateMember.fxml"));
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    Scene scene = new Scene(loader.load());
        
                    // Lấy controller của UpdateMember và truyền Member vào
                    UpdateMemberController controller = loader.getController();
                    controller.setMember((Member) person);
                    controller.setBeforeSceneURL("/fxml/Person/SearchPerson.fxml");
        
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (person instanceof Librarian && (User.isAdmin() 
                    || User.getId().equals(((Librarian)person).getEmployeeId()))) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Person/UpdateLibrarian.fxml"));
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    Scene scene = new Scene(loader.load());
        
                    UpdateLibrarianController controller = loader.getController();
                    controller.setLibrarian((Librarian) person);
                    controller.setBeforeSceneURL("/fxml/Person/SearchPerson.fxml");
        
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                MessageUtil.showMessage(messageText, "You don't have access to update", "red");
            }
        } else {
            MessageUtil.showMessage(messageText, "Invalid ID for update.", "red");
        }
    }
}
