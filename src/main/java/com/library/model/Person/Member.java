package com.library.model.Person;

import com.library.service.MemberManagement;

public class Member extends Person {
    private int membershipId;  // Changed to int
    private String joinDate;
    private String expiryDate;

    private MemberManagement memManagement = new MemberManagement();

    /**
     * Default constructor for Member.
     */
    public Member() {
        super();
        membershipId = 0;
        joinDate = "";
        expiryDate = "";
    }

    /**
     * Constructor for member who has full information.
     * @param name is String
     * @param address is String
     * @param dateOfBirth is String
     * @param phoneNumber is String
     * @param gender is String 
     * @param membershipId is int
     * @param joinDate is String
     * @param expiryDate is String
     */
    public Member(String name, String address, String dateOfBirth, String phoneNumber, 
                String gender, int membershipId, String joinDate, String expiryDate) {
        super(name, address, gender, dateOfBirth, phoneNumber);
        this.membershipId = membershipId;
        this.joinDate = joinDate;
        this.expiryDate = expiryDate;
    }

    /**
     * Constructor for Member like Person.
     * @param name String name
     * @param address String address
     * @param dateOfBirth String dateOfBirth
     * @param phoneNumber String phoneNumber
     * @param gender String gender
     */
    public Member(String name, String address, String dateOfBirth, String phoneNumber,
                String gender) {
        super(name, address, gender, dateOfBirth, phoneNumber);
    }

    /**
     * Copy constructor of Member class.
     * @param other Member other
     */
    public Member(Member other) {
        super(other.getName(), other.getAddress(), other.getGender(), 
              other.getDateOfBirth(), other.getPhoneNumber());
        
        this.membershipId = other.membershipId;
        this.joinDate = other.joinDate;
        this.expiryDate = other.expiryDate;
    }
    

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    /** 
     * @return String
     */
    @Override
    public String getDetails() {
        return String.format("%sMembership ID: M%09d\nJoin Date: %s\nExpiry Date: %s\n",
                            super.getDetails(), membershipId, joinDate, expiryDate);  // Change %s to %d for membershipId
    }

    public void addMember() {
        memManagement.addMember(this);
    }

    public void deleteMember() {
        memManagement.removeMember(this.membershipId);  // Pass membershipId as int
    }

    public void updateMember() {
        memManagement.updateMember(this);
    }

    public Member getInforFromDatabase(int id) {
        Member memberFromDB = memManagement.getMemberInfo(id);
        
        // If no member is found, return null or display an error message
        if (memberFromDB == null) {
            System.out.println("No member found with ID: " + id);
            return null;
        }
        return memberFromDB;  // Return the retrieved Member object
    }  

    public void renewMembership(String addDate) {
        System.out.println(addDate);
        memManagement.renewCard(this.membershipId, addDate);
        
    }
}
