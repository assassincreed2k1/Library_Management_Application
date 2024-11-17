package com.library.controller.personController;

import com.library.model.Person.Member;
import com.library.model.helpMethod.PersonIdHandle;
import com.library.service.MemberManagement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
            inforMemberTextArea.setText("Please select a renewal option.");
            return;
        }

        // Lấy thông tin thành viên
        member = (Member) PersonIdHandle.getPerson(membershipId);

        if (member == null) {
            inforMemberTextArea.setText("No member found with ID: " + membershipId);
        } else {
            // Gia hạn thẻ thành viên
            member.renewMembership(selectedRenewal);
            member = member.getInforFromDatabase(member.getMembershipId());
            System.out.println(member.getDetails());

            // Hiển thị thông tin sau khi gia hạn
            inforMemberTextArea.setText("Membership successfully renewed!\n" + member.getDetails());
        }
    }
}
