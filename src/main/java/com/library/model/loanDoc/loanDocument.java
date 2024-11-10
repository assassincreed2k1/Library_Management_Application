package com.library.model.loanDoc;

public class loanDocument {
    private int loanID;
    private int membershipID;
    private int documentID;
    private String docType;
    private String borrowDate;
    private String dueDate;

    /**
     * constructor default.
     */
    public loanDocument() {
        loanID = 0;
        membershipID = 0;
        documentID = 0;
        docType = "";
        borrowDate = "";
        dueDate = "";
    }

    /**
     * constructor with all attributes.
     * @param loanID int loan id
     * @param memberID int membership id
     * @param documentID int document id 
     * @param docType String document type
     * @param borrowDate String borrowed date
     * @param dueDate String due Date
     */
    public loanDocument(int loanID, int memberID, int documentID, String docType, String borrowDate, String dueDate) {
        this.loanID = loanID;
        this.membershipID = memberID;
        this.documentID = documentID;
        this.docType = docType;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    // Getters
    public int getLoanID() {
        return loanID;
    }

    public int getMembershipID() {
        return membershipID;
    }

    public int getDocumentID() {
        return documentID;
    }

    public String getDocType() {
        return docType;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    // Setters
    public void setLoanID(int loanID) {
        this.loanID = loanID;
    }

    public void setMembershipID(int membershipID) {
        this.membershipID = membershipID;
    }

    public void setDocumentID(int documentID) {
        this.documentID = documentID;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    //chưa cập nhật được phương thức vì chưa có getDetails của document 
    /**
     * get information of loan activity with books.
     * @return String
     */
    public String getDetailsBook() {
        return String.format("Loan ID: %d\nMembership ID: M%09d\nBook information: %s\n"
                            +"Borrowed Date: %s\nDue Date: %s\n",
                            this.loanID, this.membershipID, this.documentID);
    }
}
