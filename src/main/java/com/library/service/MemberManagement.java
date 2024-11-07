package com.library.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        } else {
            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
            LocalDate now = zonedDateTime.toLocalDate();
            join = Date.valueOf(now);
        }
        if (member.getExpiryDate() != null) {
            expiry = DateString.toSqlDate(member.getExpiryDate());
        }

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, member.getMembershipId()); // Changed to setInt for membershipId as int
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
    public void removeMember(int id) {
        String sql_statement = "DELETE FROM Member WHERE membershipId = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, id); // Set membershipId as int
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
     * @return Member
     */
    public Member getMemberInfo(int id) {
        String sql_statement = "SELECT * FROM Member WHERE membershipId = ?";
        Member member = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql_statement)) {
            pstmt.setInt(1, id); // Set membershipId as int
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new Member();
                member.setMembershipId(rs.getInt("membershipId")); // Get membershipId as int
                member.setName(rs.getString("name"));
                member.setAddress(rs.getString("address"));

                // Handle if birth date is null
                if (rs.getDate("dateOfBirth") == null) {
                    member.setDateOfBirth(null);
                } else {
                    member.setDateOfBirth(rs.getDate("dateOfBirth").toString());
                }

                member.setPhoneNumber(rs.getString("phoneNumber"));
                member.setGender(rs.getString("gender"));

                // Handle if join date is null
                if (rs.getDate("joinDate") == null) {
                    member.setJoinDate(null);
                } else {
                    member.setJoinDate(rs.getDate("joinDate").toString());
                }

                // Handle if expiry date is null
                if (rs.getDate("expiryDate") == null) {
                    member.setExpiryDate(null);
                } else {
                    member.setExpiryDate(rs.getDate("expiryDate").toString());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return member;
    }
}
