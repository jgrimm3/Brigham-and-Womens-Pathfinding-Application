package com.manlyminotaurs.messaging;

import com.manlyminotaurs.databases.RequestsDBUtil;

public abstract class Request implements IRequest {
    String requestID;
    String requestType;
    int priority;
    Boolean isComplete;
    Boolean adminConfirm;
    String nodeID;
    String messageID;
    String password;

    public Request(String requestID, String requestType, int priority, Boolean isComplete, Boolean adminConfirm, String nodeID, String messageID, String password) {
        this.requestID = requestID;
        this.requestType = requestType;
        this.priority = priority;
        this.isComplete = isComplete;
        this.adminConfirm = adminConfirm;
        this.nodeID = nodeID;
        this.messageID = messageID;
        this.password = password;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getRequestType() {
        return requestType;
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
        new RequestsDBUtil().setIsAdminConfim(this, adminConfirm);
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
