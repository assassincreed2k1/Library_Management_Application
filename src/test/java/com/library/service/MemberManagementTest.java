package com.library.service;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.library.model.Person.Member;

public class MemberManagementTest {

    private MemberManagement memberManagement;

    @Before
    public void setUp() {
        memberManagement = new MemberManagement();
    }

    @Test
    public void testAddMember() {
        Member member = new Member();
        member.setMembershipId("M003");
        member.setName("Duong Thi D");
        member.setAddress("125 Xuan Thuy, Cau Giay");
        member.setDateOfBirth("1990-05-15");
        member.setPhoneNumber("0987654321");
        member.setGender("female");
        member.setJoinDate("2024-02-01");
        member.setExpiryDate("2025-02-01");
    
        memberManagement.addMember(member);
    
        Member retrievedMember = memberManagement.getMemberInfo("M003");
        assertNotNull(retrievedMember);
        assertEquals("Duong Thi D", retrievedMember.getName());
        assertEquals("125 Xuan Thuy, Cau Giay", retrievedMember.getAddress());
        assertEquals("1990-05-15", retrievedMember.getDateOfBirth());
        assertEquals("0987654321", retrievedMember.getPhoneNumber());
        assertEquals("female", retrievedMember.getGender());
        assertEquals("2024-02-01", retrievedMember.getJoinDate());
        assertEquals("2025-02-01", retrievedMember.getExpiryDate());
    }
    

    @Test
    public void testUpdateMember() {
        // Giả sử member đã tồn tại
        Member member = new Member();
        member.setMembershipId("M001");
        member.setName("Tran Thi B");
        member.setAddress("456 Xuan Thuy");
        member.setDateOfBirth("1992-02-02");
        member.setPhoneNumber("0987654321");
        member.setGender("female");
        member.setJoinDate("2023-01-01");
        member.setExpiryDate("2025-01-01");

        memberManagement.updateMember(member);

        // Kiểm tra nếu member đã được cập nhật thành công
        Member updatedMember = memberManagement.getMemberInfo("M001");
        assertEquals("Tran Thi B", updatedMember.getName());
        assertEquals("456 Xuan Thuy", updatedMember.getAddress());
    }

    @Test
    public void testRemoveMember() {
        String membershipId = "M001";

        // Thêm member để xóa
        Member member = new Member();
        member.setMembershipId(membershipId);
        memberManagement.addMember(member);

        memberManagement.removeMember(membershipId);

        // Kiểm tra nếu member đã bị xóa thành công
        Member removedMember = memberManagement.getMemberInfo(membershipId);
        assertNull(removedMember.getMembershipId());
    }

    @Test
    public void testGetMemberInfo() {
        String membershipId = "M001";

        // Thêm member để lấy thông tin
        Member member = new Member();
        member.setMembershipId(membershipId);
        member.setName("Tran Thi B");
        memberManagement.addMember(member);

        Member retrievedMember = memberManagement.getMemberInfo(membershipId);
        assertNotNull(retrievedMember);
        assertEquals(membershipId, retrievedMember.getMembershipId());
    }
}
