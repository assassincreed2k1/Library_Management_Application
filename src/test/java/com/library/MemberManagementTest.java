package com.library;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.library.model.Person.Member;
import com.library.service.MemberManagement;

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
        member.setName("Alice Smith");
        member.setAddress("125 Main St, Cityville");
        member.setDateOfBirth("1990-05-15");
        member.setPhoneNumber("0987654321");
        member.setGender("female");
        member.setJoinDate("2024-02-01");
        member.setExpiryDate("2025-02-01");
    
        memberManagement.addMember(member);
    
        Member retrievedMember = memberManagement.getMemberInfo("M003");
        assertNotNull(retrievedMember);
        assertEquals("Alice Smith", retrievedMember.getName());
        assertEquals("125 Main St, Cityville", retrievedMember.getAddress());
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
        member.setName("Jane Doe");
        member.setAddress("456 Main St");
        member.setDateOfBirth("1992-02-02");
        member.setPhoneNumber("0987654321");
        member.setGender("female");
        member.setJoinDate("2023-01-01");
        member.setExpiryDate("2025-01-01");

        memberManagement.updateMember(member);

        // Kiểm tra nếu member đã được cập nhật thành công
        Member updatedMember = memberManagement.getMemberInfo("M001");
        assertEquals("Jane Doe", updatedMember.getName());
        assertEquals("456 Main St", updatedMember.getAddress());
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
        member.setName("John Doe");
        memberManagement.addMember(member);

        Member retrievedMember = memberManagement.getMemberInfo(membershipId);
        assertNotNull(retrievedMember);
        assertEquals(membershipId, retrievedMember.getMembershipId());
    }
}
