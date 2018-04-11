package com.manlyminotaurs.messaging;

import java.time.LocalDate;
import java.util.Objects;

public class Message {

    String messageID;
    String message;
    Boolean isRead;
    String senderID;
    String receiverID;
    LocalDate sentDate;


    public Message(String messageID, String message, Boolean isRead, LocalDate sentDate, String receiverID, String senderID) {
        this.messageID = messageID;
        this.message = message;
        this.isRead = isRead;
        this.sentDate = sentDate;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }


    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDate sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(messageID, message1.messageID) &&
                Objects.equals(message, message1.message) &&
                Objects.equals(isRead, message1.isRead) &&
                Objects.equals(senderID, message1.senderID) &&
                Objects.equals(receiverID, message1.receiverID) &&
                Objects.equals(sentDate, message1.sentDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(messageID, message, isRead, senderID, receiverID, sentDate);
    }
}
