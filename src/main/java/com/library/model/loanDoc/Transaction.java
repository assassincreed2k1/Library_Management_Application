package com.library.model.loanDoc;

public class Transaction {
    private int transactionId;
    private String documentId;
    private String membershipId;
    private String edited_by;
    private String borrowedDate;
    private String dueDate;
    private String returnDate;
    private String status;
    private int score;
    private String comment;

    public Transaction(int transactionid, String documentId, String memberId,
                    String borrowedDate, String dueDate, String returnedDate, String editedBy) {
        this.transactionId = transactionid;
        this.documentId = documentId;
        this.membershipId = memberId;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnDate = returnedDate;
        this.edited_by = editedBy;        
    }

    public Transaction(int transactionId, String documentId, String borrowDate, 
                    String dueDate, String returnDate, int score) {
        this.transactionId = transactionId;
        this.documentId = documentId;
        this.borrowedDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.score = score;                       
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public String getEdited_by() {
        return edited_by;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public void setEdited_by(String edited_by) {
        this.edited_by = edited_by;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    public double getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }
}
