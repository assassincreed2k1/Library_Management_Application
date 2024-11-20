package com.library.service;

import java.time.LocalDate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.library.model.Person.Member;
import com.library.model.helpers.DateString;

public class MemberManagement extends LibraryService {

    public static final String ONEMONTH = "1 month";
    public static final String THREEMONTHS = "3 months";
    public static final String SIXMONTHS = "6 months";
    public static final String ONEYEAR = "1 year";

    private final PersonIDManagement memberIdManagement = new PersonIDManagement("MemberID");

    // Constructor
    public MemberManagement() {
        super.createList("CREATE TABLE IF NOT EXISTS Member ("
                + "membershipId CHAR(7) PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "address VARCHAR(255), "
                + "dateOfBirth DATE, "
                + "phoneNumber VARCHAR(11), "
                + "gender VARCHAR(6), "
                + "joinDate DATE NOT NULL, "
                + "expiryDate DATE"
                + ");");
    }

    public boolean addMember(Member member) {
        if (member == null) {
            System.err.println("Member object is null.");
            return false;
        }

        String newId = String.format("M%06d", memberIdManagement.getID());
        String sql_statement = "INSERT INTO Member "
                + "(membershipId, name, address, dateOfBirth, phoneNumber, gender, joinDate, expiryDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            
            pstmt.setString(1, newId);
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getAddress());
            pstmt.setDate(4, member.getDateOfBirth() != null ? DateString.toSqlDate(member.getDateOfBirth()) : null);
            pstmt.setString(5, member.getPhoneNumber());
            pstmt.setString(6, member.getGender());
            pstmt.setDate(7, member.getJoinDate() != null ? DateString.toSqlDate(member.getJoinDate()) : Date.valueOf(LocalDate.now()));
            pstmt.setDate(8, member.getExpiryDate() != null ? DateString.toSqlDate(member.getExpiryDate()) : null);

            pstmt.executeUpdate();
            memberIdManagement.increaseID();
            System.out.println("Member added successfully with ID: " + newId);
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding member: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void removeMember(String membershipId) {
        if (membershipId == null || membershipId.isEmpty()) {
            System.err.println("Invalid membership ID.");
            return;
        }

        String sql_statement = "DELETE FROM Member WHERE membershipId = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {

            pstmt.setString(1, membershipId);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted + " member(s) deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error removing member: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateMember(Member member) {
        if (member == null || member.getMembershipId() == null) {
            System.err.println("Invalid member object or membership ID.");
            return;
        }

        String sql_stmt = "UPDATE Member SET "
                + "name = ?, "
                + "address = ?, "
                + "dateOfBirth = ?, "
                + "phoneNumber = ?, "
                + "gender = ?, "
                + "joinDate = ?, "
                + "expiryDate = ? "
                + "WHERE membershipId = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {

            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getAddress());
            pstmt.setDate(3, member.getDateOfBirth() != null ? DateString.toSqlDate(member.getDateOfBirth()) : null);
            pstmt.setString(4, member.getPhoneNumber());
            pstmt.setString(5, member.getGender());
            pstmt.setDate(6, member.getJoinDate() != null ? DateString.toSqlDate(member.getJoinDate()) : null);
            pstmt.setDate(7, member.getExpiryDate() != null ? DateString.toSqlDate(member.getExpiryDate()) : null);
            pstmt.setString(8, member.getMembershipId());

            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated + " member(s) updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Member getMemberInfo(String membershipId) {
        if (membershipId == null || membershipId.isEmpty()) {
            System.err.println("Invalid membership ID.");
            return null;
        }

        String sql_statement = "SELECT * FROM Member WHERE membershipId = ?";
        Member member = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {

            pstmt.setString(1, membershipId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new Member();
                member.setMembershipId(rs.getString("membershipId"));
                member.setName(rs.getString("name"));
                member.setAddress(rs.getString("address"));
                member.setDateOfBirth(rs.getDate("dateOfBirth") != null ? rs.getDate("dateOfBirth").toString() : null);
                member.setPhoneNumber(rs.getString("phoneNumber"));
                member.setGender(rs.getString("gender"));
                member.setJoinDate(rs.getDate("joinDate") != null ? rs.getDate("joinDate").toString() : null);
                member.setExpiryDate(rs.getDate("expiryDate") != null ? rs.getDate("expiryDate").toString() : null);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching member info: " + e.getMessage());
            e.printStackTrace();
        }
        return member;
    }

    public void renewCard(String membershipId, String addDate) {
        if (membershipId == null || membershipId.isEmpty() || addDate == null || addDate.isEmpty()) {
            System.err.println("Invalid input for renew card.");
            return;
        }

        String sql_statement = "UPDATE Member "
                + "SET expiryDate = DATE(COALESCE(expiryDate, DATE('now')), ?) "
                + "WHERE membershipId = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {

            pstmt.setString(1, addDate);
            pstmt.setString(2, membershipId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " membership(s) renewed.");
        } catch (SQLException e) {
            System.err.println("Error renewing card: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
