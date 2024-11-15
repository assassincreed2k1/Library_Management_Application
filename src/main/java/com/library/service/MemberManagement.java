package com.library.service;

import java.time.LocalDate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.library.model.Person.Member;
import com.library.model.helpMethod.DateString;

public class MemberManagement extends LibraryService {
    private PersonIDManagement memberIdManagement = new PersonIDManagement("MemberID");

    /**
     * Constructor for MemberManagement class. Initializes the Member table in the database if it doesn't already exist.
     */
    public MemberManagement() {
        super.createList("CREATE TABLE IF NOT EXISTS Member ("
                + "membershipId INTEGER PRIMARY KEY, "
                + "name VARCHAR(255), "
                + "address VARCHAR(255), "
                + "dateOfBirth DATE, "
                + "phoneNumber VARCHAR(11), "
                + "gender VARCHAR(6), "
                + "joinDate DATE, "
                + "expiryDate DATE"
                + ");");
    }

    /**
     * Adds a new member to the database.
     * @param member The Member object containing member's details to be added.
     */
    public void addMember(Member member) {
        int newId = memberIdManagement.getID(); // Retrieve new ID from PersonIDManagement
        String sql_statement = "INSERT INTO Member "
                + "(membershipId, name, address, dateOfBirth, phoneNumber, gender, joinDate, expiryDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Date birth = member.getDateOfBirth() != null ? DateString.toSqlDate(member.getDateOfBirth()) : null;
        Date join = member.getJoinDate() != null ? DateString.toSqlDate(member.getJoinDate()) : Date.valueOf(LocalDate.now());
        Date expiry = member.getExpiryDate() != null ? DateString.toSqlDate(member.getExpiryDate()) : null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, newId);
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getAddress());
            pstmt.setDate(4, birth);
            pstmt.setString(5, member.getPhoneNumber());
            pstmt.setString(6, member.getGender());
            pstmt.setDate(7, join);
            pstmt.setDate(8, expiry);
            pstmt.executeUpdate();

            memberIdManagement.increaseID();
            System.out.println("Member added successfully with ID: " + newId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the details of an existing member in the database.
     * @param member The Member object containing updated details.
     */
    public void updateMember(Member member) {
        String sql_stmt = "UPDATE Member SET "
                + "name = ?, "
                + "address = ?, "
                + "dateOfBirth = ?, "
                + "phoneNumber = ?, "
                + "gender = ?, "
                + "joinDate = ?, "
                + "expiryDate = ? "
                + "WHERE membershipId = ?";

        Date birth = member.getDateOfBirth() != null ? DateString.toSqlDate(member.getDateOfBirth()) : null;
        Date join = member.getJoinDate() != null ? DateString.toSqlDate(member.getJoinDate()) : null;
        Date expiry = member.getExpiryDate() != null ? DateString.toSqlDate(member.getExpiryDate()) : null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getAddress());
            pstmt.setDate(3, birth);
            pstmt.setString(4, member.getPhoneNumber());
            pstmt.setString(5, member.getGender());
            pstmt.setDate(6, join);
            pstmt.setDate(7, expiry);
            pstmt.setInt(8, member.getMembershipId());
            pstmt.executeUpdate();
            System.out.println("Member updated successfully");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Removes a member from the database by membership ID.
     * @param membershipId The ID of the member to be removed.
     */
    public void removeMember(int membershipId) {
        String sql_statement = "DELETE FROM Member WHERE membershipId = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, membershipId);
            pstmt.executeUpdate();
            System.out.println("Member deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves member information based on membership ID from the database.
     * @param membershipId The ID of the member whose information is to be retrieved.
     * @return A Member object containing the member's information, or null if not found.
     */
    public Member getMemberInfo(int membershipId) {
        String sql_statement = "SELECT * FROM Member WHERE membershipId = ?";
        Member member = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, membershipId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new Member();
                member.setMembershipId(rs.getInt("membershipId"));
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
        }
        return member;
    }
}
