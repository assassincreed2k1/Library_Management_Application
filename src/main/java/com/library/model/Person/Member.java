package com.library.model.Person;

public class Member extends Person {
    private String membershipId;
    private String joinDate;
    private String expiryDate;

    public Member() {
        super();
        membershipId = "";
        joinDate = "";
        expiryDate = "";
    }

    /**
     * constructor for member who has full information.
     * @param name is String
     * @param address is String
     * @param dateOfBirth is String
     * @param phoneNumber is String
     * @param gender is String 
     * @param membershipId is String
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
        return String.format("%s\nMembership ID: %s\nJoin Date: %s\nExpiry Date: %s\n",
                            super.getDetails(), membershipId, joinDate, expiryDate);
    }
}
