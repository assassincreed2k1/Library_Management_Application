package com.library.Person;

public class Member extends Person {
    private String membershipId;
    private String membershipType;
    private String joinDate;
    private String expiryDate;

    //can not code in this time
    //borrowedBooks: borrowed books list -> ArrayList ?
    //fines : penalty times because of late book returning or ...
    //borrowHistory : list of books that this member borrowed in the past

    /**
     * constructor for member who has full information.
     * @param name is String
     * @param address is String
     * @param dateOfBirth is String
     * @param phoneNumber is String
     * @param email is String
     * @param gender is String 
     * @param membershipId is String
     * @param membershipType is String
     * @param joinDate is String
     * @param expiryDate is String
     */
    public Member(String name, String address, String dateOfBirth, String phoneNumber, String email, 
                  String gender, String membershipId, String membershipType, String joinDate, String expiryDate) {
        super(name, address, gender, dateOfBirth, phoneNumber, email);
        this.membershipId = membershipId;
        this.membershipType = membershipType;
        this.joinDate = joinDate;
        this.expiryDate = expiryDate;
    }

    /**
     * constructor for member who just has email or phone number.
     * @param name is String
     * @param gender is String
     * @param address is String
     * @param dateOfBirth is String
     * @param phoneNum_email is String
     * @param membershipId is String
     * @param membershipType is String
     * @param joinDate is String
     * @param expiryDate is String
     */
    public Member(String name, String gender, String address, String dateOfBirth, String phoneNum_email,
                  String membershipId, String membershipType, String joinDate, String expiryDate) {
        super(name,address, gender, dateOfBirth, phoneNum_email);
        this.membershipId = membershipId;
        this.membershipType = membershipType;
        this.joinDate = joinDate;
        this.expiryDate = expiryDate;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
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
        return String.format("%s\nMembership ID: %s\nMembership Type: %s\nJoin Date: %s\nExpiry Date: %s\n",
                            super.getDetails(), membershipId, membershipType, joinDate, expiryDate);
    }
}
