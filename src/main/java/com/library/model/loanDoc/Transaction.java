package com.library.model.loanDoc;

/**
 * Represents a transaction related to borrowing or returning a document in the library.
 */
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

    /**
     * Constructor to create a transaction with detailed borrowing and editing information.
     * 
     * @param transactionid The unique ID of the transaction.
     * @param documentId The ID of the document being borrowed.
     * @param memberId The ID of the member borrowing the document.
     * @param borrowedDate The date the document was borrowed.
     * @param dueDate The due date for returning the document.
     * @param returnedDate The date the document was returned.
     * @param editedBy The ID of the person who edited this transaction.
     */
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

    /**
     * Constructor to create a transaction with borrowing details and a score.
     * 
     * @param transactionId The unique ID of the transaction.
     * @param documentId The ID of the document being borrowed.
     * @param borrowDate The date the document was borrowed.
     * @param dueDate The due date for returning the document.
     * @param returnDate The date the document was returned.
     * @param score The score or rating given for this transaction.
     */
    public Transaction(int transactionId, String documentId, String borrowDate, 
                       String dueDate, String returnDate, int score) {
        this.transactionId = transactionId;
        this.documentId = documentId;
        this.borrowedDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.score = score;
    }

    /**
     * Gets the transaction ID.
     * 
     * @return The transaction ID.
     */
    public int getTransactionId() {
        return transactionId;
    }

    /**
     * Gets the document ID associated with this transaction.
     * 
     * @return The document ID.
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * Gets the membership ID of the member involved in this transaction.
     * 
     * @return The membership ID.
     */
    public String getMembershipId() {
        return membershipId;
    }

    /**
     * Gets the ID of the person who edited this transaction.
     * 
     * @return The editor's ID.
     */
    public String getEdited_by() {
        return edited_by;
    }

    /**
     * Gets the borrowed date of this transaction.
     * 
     * @return The borrowed date.
     */
    public String getBorrowedDate() {
        return borrowedDate;
    }

    /**
     * Gets the due date for returning the document.
     * 
     * @return The due date.
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Sets the transaction ID.
     * 
     * @param transactionId The transaction ID to set.
     */
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Sets the document ID.
     * 
     * @param documentId The document ID to set.
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * Sets the membership ID.
     * 
     * @param membershipId The membership ID to set.
     */
    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    /**
     * Sets the editor's ID.
     * 
     * @param edited_by The editor's ID to set.
     */
    public void setEdited_by(String edited_by) {
        this.edited_by = edited_by;
    }

    /**
     * Sets the borrowed date.
     * 
     * @param borrowedDate The borrowed date to set.
     */
    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    /**
     * Sets the due date for returning the document.
     * 
     * @param dueDate The due date to set.
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Sets the return date for the document.
     * 
     * @param returnDate The return date to set.
     */
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Sets the status of the transaction.
     * 
     * @param status The status to set (e.g., "Completed", "Pending").
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the score or rating for this transaction.
     * 
     * @param score The score to set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Sets the comment or feedback for this transaction.
     * 
     * @param comment The comment to set.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the return date for the document.
     * 
     * @return The return date.
     */
    public String getReturnDate() {
        return returnDate;
    }

    /**
     * Gets the status of the transaction.
     * 
     * @return The transaction status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the score or rating for this transaction.
     * 
     * @return The score.
     */
    public double getScore() {
        return score;
    }

    /**
     * Gets the comment or feedback for this transaction.
     * 
     * @return The comment.
     */
    public String getComment() {
        return comment;
    }
}
