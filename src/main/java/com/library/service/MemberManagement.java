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

    /**
     * Create table for member, if existed -> not create.
     */
    public MemberManagement() {
        super.createList("CREATE TABLE IF NOT EXISTS Member ("
                + "membershipId INTEGER PRIMARY KEY AUTOINCREMENT, "
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
     * Add member to database.
     *
     * @param member Member
     */
    public void addMember(Member member) {
        String sql_statement = "INSERT INTO Member "
                + "(name, address, dateOfBirth, phoneNumber, gender, joinDate, expiryDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";  // Remove membershipId here
    
        Date birth = null;
        Date join = null;
        Date expiry = null;
    
        if (member.getDateOfBirth() != null) {
            birth = DateString.toSqlDate(member.getDateOfBirth());
        }
        if (member.getJoinDate() != null) {
            join = DateString.toSqlDate(member.getJoinDate());
        } else {
            LocalDate now = LocalDate.now();
            join = Date.valueOf(now);
        }
        if (member.getExpiryDate() != null) {
            expiry = DateString.toSqlDate(member.getExpiryDate());
        }
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            // Do not set membershipId here, let SQLite handle it
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getAddress());
            pstmt.setDate(3, birth);
            pstmt.setString(4, member.getPhoneNumber());
            pstmt.setString(5, member.getGender());
            pstmt.setDate(6, join);
            pstmt.setDate(7, expiry);
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    

    /**
     * Update member from Member.
     *
     * @param member Member
     */
    public void updateMember(Member member) {
        String sql_stmt = "UPDATE Member SET "
                + "name = ?, "
                + "address = ?, "
                + "dateOfBirth = ?, "
                + "phoneNumber = ?, "
                + "joinDate = ?, "
                + "expiryDate = ?, "
                + "gender = ? "
                + "WHERE membershipId = ?";

        Date birth = null;
        Date join = null;
        Date expiry = null;

        if (member.getDateOfBirth() != null) {
            birth = DateString.toSqlDate(member.getDateOfBirth());
        }
        if (member.getJoinDate() != null) {
            join = DateString.toSqlDate(member.getJoinDate());
        }
        if (member.getExpiryDate() != null) {
            expiry = DateString.toSqlDate(member.getExpiryDate());
        }

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_stmt)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getAddress());
            pstmt.setDate(3, birth);
            pstmt.setString(4, member.getPhoneNumber());
            pstmt.setDate(5, join);
            pstmt.setDate(6, expiry);
            pstmt.setString(7, member.getGender());
            pstmt.setInt(8, member.getMembershipId()); // Set membershipId as int
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Remove Member from database.
     *
     * @param id int
     */
    public void removeMember(int membershipId) {
        String sql_statement = "DELETE FROM Member WHERE membershipId = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, membershipId); // Set membershipId as int
            pstmt.executeUpdate();
            System.out.println("Data deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get member information from SQL.
     *
     * @param membershipId membership id (int)
     * @return Member object with information from the database, or null if not found.
     */
    public Member getMemberInfo(int membershipId) {
        String sql_statement = "SELECT * FROM Member WHERE membershipId = ?";
        Member member = null;

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            
            pstmt.setInt(1, membershipId); // Set membershipId as int
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new Member();
                member.setMembershipId(rs.getInt("membershipId"));
                member.setName(rs.getString("name"));
                member.setAddress(rs.getString("address"));
                
                Date birthDate = rs.getDate("dateOfBirth");
                member.setDateOfBirth(birthDate != null ? birthDate.toString() : null);

                member.setPhoneNumber(rs.getString("phoneNumber"));
                member.setGender(rs.getString("gender"));

                Date joinDate = rs.getDate("joinDate");
                member.setJoinDate(joinDate != null ? joinDate.toString() : null);

                Date expiryDate = rs.getDate("expiryDate");
                member.setExpiryDate(expiryDate != null ? expiryDate.toString() : null);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching member info: " + e.getMessage());
        }
        return member;
    }

}
