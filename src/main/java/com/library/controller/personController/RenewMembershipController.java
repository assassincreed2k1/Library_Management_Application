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

    /**
     * initialize when starting.
     */
    @FXML
    private void initialize() {
        renewComboBox.getItems().addAll(MemberManagement.ONEMONTH, MemberManagement.THREEMONTHS, MemberManagement.SIXMONTHS, MemberManagement.ONEYEAR);

        renewButton.setOnAction(event -> onRenewButtonClicked());
    }

    /**
     * renew card membership.
     */
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
        
        Task<Void> renewalTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                member = (Member) PersonIdHandle.getPerson(membershipId);

                if (member == null) {
                    throw new Exception("No member found with ID: " + membershipId);
                } else {
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
        Thread thread = new Thread(renewalTask);
        thread.setDaemon(true);
        thread.start();
    }
}
