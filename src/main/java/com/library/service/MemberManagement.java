
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

/**
 * Manages member-related operations for the library system.
 * 
 * This class provides functionality to add, remove, update, retrieve, and renew
 * membership records for library members. It interacts with the `Member` table
 * in the database.
 */
public class MemberManagement extends LibraryService {

    public static final String ONEMONTH = "1 month";
    public static final String THREEMONTHS = "3 months";
    public static final String SIXMONTHS = "6 months";
    public static final String ONEYEAR = "1 year";

    private final PersonIDManagement memberIdManagement = new PersonIDManagement("MemberID");

    /**
     * Constructs a new {@code MemberManagement} instance and initializes the 
     * `Member` table in the database if it does not already exist.
     */
    public MemberManagement() {
        super.createList("CREATE TABLE IF NOT EXISTS Member ("
                + "membershipId CHAR(7) PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "address VARCHAR(255), "
                + "dateOfBirth DATE, "
                + "phoneNumber VARCHAR(11), "
                + "gender VARCHAR(6), "
                + "joinDate DATE NOT NULL, "
                + "expiryDate DATE, "
                + "password VARCHAR(255) NOT NULL"
                + ");");
    }

    /**
     * Adds a new member to the library system.
     * 
     * @param member the {@code Member} object containing the details of the new member.
     * @return {@code true} if the member was added successfully; {@code false} otherwise.
     */
    public boolean addMember(Member member) {
        if (member == null) {
            System.err.println("Member object is null.");
            return false;
        }

        String newId = String.format("M%06d", memberIdManagement.getID());
        String sql_statement = "INSERT INTO Member "
                + "(membershipId, name, address, dateOfBirth, phoneNumber, gender, joinDate, expiryDate, password) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            pstmt.setString(9, member.getPassword()); 

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

    /**
     * Removes a member from the library system.
     * 
     * @param membershipId the unique membership ID of the member to be removed.
     */
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

    /**
     * Updates the details of an existing member in the library system.
     * 
     * @param member the {@code Member} object containing updated information.
     */
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
                + "expiryDate = ?, "
                + "password = ? " 
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
            pstmt.setString(8, member.getPassword()); // Cập nhật password
            pstmt.setString(9, member.getMembershipId());

            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated + " member(s) updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves information about a specific member.
     * 
     * @param membershipId the unique membership ID of the member.
     * @return a {@code Member} object containing the member's details, or {@code null} if no member is found.
     */
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
                member.setExpiryDate(rs.getString("expiryDate") != null ? rs.getString("expiryDate") : null);
                member.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching member info: " + e.getMessage());
            e.printStackTrace();
        }
        return member;
    }

    /**
     * Renews a library member's card by extending its expiry date.
     * 
     * @param membershipId the unique membership ID of the member.
     * @param addDate the duration to add to the current expiry date, formatted as an interval (e.g., "1 month").
     */
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
