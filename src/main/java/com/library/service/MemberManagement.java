package com.library.service;

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
     * create table for member, if existed -> not create.
     *
     */
    public MemberManagement() {
        super.createList("CREATE TABLE IF NOT EXISTS Member ("
                        + "membershipId INT PRIMARY KEY AUTO_INCREMENT, "
                        + "name VARCHAR(255), "
                        + "address VARCHAR(255), "
                        + "dateOfBirth DATE,  "
                        + "phoneNumber VARCHAR(11),  "
                        + "gender VARCHAR(6),  "
                        + "joinDate DATE,  "
                        + "expiryDate DATE"
                        + ");  ");
    }

    /**
     * add member to database.
     * 
     * @param member Member
     */
    public void addMember(Member member) {
        String sql_statement = "INSERT INTO Member "
                + "(membershipId, name, address, dateOfBirth, phoneNumber, gender, joinDate, expiryDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
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
            PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, member.getMembershipId());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getAddress());
            pstmt.setDate(4, birth);
            pstmt.setString(5, member.getPhoneNumber());
            pstmt.setString(6, member.getGender());
            pstmt.setDate(7, join);
            pstmt.setDate(8, expiry);
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * update member from Member.
     * 
     * @param member Member
     */
    public void updateMember(Member member) {
        String sql_stmt = "UPDATE Member SET "
                        + "name = ?, "
                        + "address = ?, "
                        + "dateOfBirth = ?, "
                        + "phoneNumber = ?, "
                        + "joinDate = ?,  "
                        + "expiryDate = ?,  "
                        + "gender = ? "
                        + "WHERE membershipId = ?"
                        + ";";

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
            pstmt.setString(1,member.getName());
            pstmt.setString(2, member.getAddress());
            pstmt.setDate(3, birth);
            pstmt.setString(4, member.getPhoneNumber());
            pstmt.setDate(5, join);
            pstmt.setDate(6, expiry);
            pstmt.setString(7,member.getGender());
            pstmt.setString(8, member.getMembershipId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * remove Member from database.
     * 
     * @param id String
     */
    public void removeMember(String id) {
        String sql_statement = "DELETE FROM Member WHERE membershipId = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            System.out.println("Data deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * get member information from sql.
     * 
     * @param membershipId membership id string
     * @return Member
     */
    public Member getMemberInfo(String id) {
        String sql_statement = "SELECT * FROM Member WHERE membershipId = ?";
        Member member = null;

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new Member();
                member.setMembershipId(rs.getString("membershipId"));
                member.setName(rs.getString("name"));
                member.setAddress(rs.getString("address"));
                member.setDateOfBirth(rs.getDate("dateOfBirth").toString());
                member.setPhoneNumber(rs.getString("phoneNumber"));
                member.setGender(rs.getString("gender"));
                member.setJoinDate(rs.getDate("joinDate").toString());
                member.setExpiryDate(rs.getDate("expiryDate").toString());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return member;
    }

    // 

}
