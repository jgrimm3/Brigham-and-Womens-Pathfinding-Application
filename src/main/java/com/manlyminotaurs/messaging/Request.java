package com.manlyminotaurs.messaging;

public class Request {

    String requestID;
    String requestType;
    int priority;
    Boolean isComplete;
    Boolean adminConfirm;
    String nodeID;
    String messageID;
    String password;

    public Request(String requestID, String requestType, int priority, Boolean isComplete, Boolean adminConfirm, String nodeID, String messageID) {
        this.requestID = requestID;
        this.requestType = requestType;
        this.priority = priority;
        this.isComplete = isComplete;
        this.adminConfirm = adminConfirm;
        this.nodeID = nodeID;
        this.messageID = messageID;
        this.password = "default_password";
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public Boolean getAdminConfirm() {
        return adminConfirm;
    }

    public void setAdminConfirm(Boolean adminConfirm) {
        this.adminConfirm = adminConfirm;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
