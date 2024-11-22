package com.library.controller.personController;

import com.library.model.Person.Member;
import com.library.model.helpers.MessageUtil;
import com.library.model.helpers.PersonIdHandle;
import com.library.service.MemberManagement;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RenewMembershipController {

    private Member member;

    @FXML
    private TextField membershipIdTextField;

    @FXML
    private ComboBox<String> renewComboBox;

    @FXML
    private Button renewButton;

    @FXML
    private TextArea inforMemberTextArea;

    @FXML
    private Text messageText;

    @FXML
    private void initialize() {
        // Thêm các tùy chọn gia hạn vào ComboBox
        renewComboBox.getItems().addAll(MemberManagement.ONEMONTH, MemberManagement.THREEMONTHS, MemberManagement.SIXMONTHS, MemberManagement.ONEYEAR);

        // Gán sự kiện cho nút Renew
        renewButton.setOnAction(event -> onRenewButtonClicked());
    }

    @FXML
    private void onRenewButtonClicked() {
        String membershipId = membershipIdTextField.getText();
        String selectedRenewal = renewComboBox.getValue();

        if (membershipId == null || membershipId.isEmpty()) {
            inforMemberTextArea.setText("Please enter a valid Membership ID.");
            return;
        }

        if (selectedRenewal == null) {
            inforMemberTextArea.setText("Please select a renew time option.");
            return;
        }
        
        // Tạo Task để gia hạn thành viên
        Task<Void> renewalTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Lấy thông tin thành viên
                member = (Member) PersonIdHandle.getPerson(membershipId);

                if (member == null) {
                    throw new Exception("No member found with ID: " + membershipId);
                } else {
                    // Gia hạn thẻ thành viên
                    member.renewMembership(selectedRenewal);
                    member = member.getInforFromDatabase(member.getMembershipId());
                    if (member.getExpiryDate() == null) {
                        throw new Exception("Fail to update expiry date.");
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                MessageUtil.showMessage(messageText, "Membership successfully renewed!", "green");
                member = member.getInforFromDatabase(membershipId);
                inforMemberTextArea.setText(member.getDetails());
            }

            @Override
            protected void failed() {
                MessageUtil.showMessage(messageText, "Failing to renew card. Error: " + getException().getMessage(), "red");
            }
        };

        MessageUtil.showMessage(messageText, "Finding member information. Please wait: ", "blue");
        // Chạy Task trên một luồng riêng
        Thread thread = new Thread(renewalTask);
        thread.setDaemon(true);
        thread.start();
    }
}
