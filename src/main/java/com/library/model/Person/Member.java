package com.library.model.Person;

import com.library.model.helpers.PersonIdHandle;
import com.library.service.MemberManagement;

public class Member extends Person {
    private String membershipId;  // Changed to int
    private String joinDate;
    private String expiryDate;

    private MemberManagement memManagement = new MemberManagement();

    /**
     * Default constructor for Member.
     */
    public Member() {
        super();
        membershipId = "";
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
                String gender, String membershipId, String joinDate, String expiryDate) {
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
    

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
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
        return String.format("%sMembership ID: %s\nJoin Date: %s\nExpiry Date: %s\n",
                            super.getDetails(), membershipId, joinDate, expiryDate);  // Change %s to %d for membershipId
    }

    public boolean addMember() {
        return memManagement.addMember(this);
    }

    public void deleteMember() {
        memManagement.removeMember(this.membershipId);  // Pass membershipId as int
    }

    public void updateMember() {
        memManagement.updateMember(this);
    }

    /**
     * get information of member from database.
     * 
     * @param id String id 
     * @return Member 
     */
    public Member getInforFromDatabase(String id) {
        Member memberFromDB = memManagement.getMemberInfo(id);
        
        // If no member is found, return null or display an error message
        if (memberFromDB == null) {
            System.out.println("No member found with ID: " + id);
            return null;
        }
        return memberFromDB;  // Return the retrieved Member object
    }  

    /**
     * renew membership card.
     * @param addDate String date
     */
    public void renewMembership(String addDate) {
        System.out.println(addDate);
        memManagement.renewCard(this.membershipId, addDate);
        
    }

    /**
     * get last name of name.
     * @return String last name
     */
    public String getLastName() {
        Person person = PersonIdHandle.getPerson(this.membershipId);
        String fullName = person.getName();

        String[] nameParts = fullName.split("\\s+");

        return nameParts[nameParts.length - 1]; 
    }
}
