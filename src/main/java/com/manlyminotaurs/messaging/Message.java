package com.manlyminotaurs.messaging;

import com.manlyminotaurs.databases.MessagesDBUtil;

public class Message {

    String messageID;
    String message;
    Boolean isRead;
    String senderID;
    String receiverID;
    public Message(String messageID, String message, Boolean isRead, String senderID, String receiverID) {
        this.messageID = messageID;
        this.message = message;
        this.isRead = isRead;
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
}
