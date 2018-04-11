package com.manlyminotaurs.messaging;

import com.manlyminotaurs.databases.DataModelI;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Request implements IRequest {
	DataModelI dataModelI = DataModelI.getInstance();
    String requestID;
    String requestType;
    int priority;
    Boolean isComplete;
    Boolean adminConfirm;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String nodeID;
    String messageID;
    String password;

    public Request(String requestID, String requestType, int priority, Boolean isComplete, Boolean adminConfirm, LocalDateTime startTime, LocalDateTime endTime, String password, String nodeID, String messageID) {
        this.requestID = requestID;
        this.requestType = requestType;
        this.priority = priority;
        this.isComplete = isComplete;
        this.adminConfirm = adminConfirm;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return priority == request.priority &&
                Objects.equals(dataModelI, request.dataModelI) &&
                Objects.equals(requestID, request.requestID) &&
                Objects.equals(requestType, request.requestType) &&
                Objects.equals(isComplete, request.isComplete) &&
                Objects.equals(adminConfirm, request.adminConfirm) &&
                Objects.equals(startTime, request.startTime) &&
                Objects.equals(endTime, request.endTime) &&
                Objects.equals(nodeID, request.nodeID) &&
                Objects.equals(messageID, request.messageID) &&
                Objects.equals(password, request.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(dataModelI, requestID, requestType, priority, isComplete, adminConfirm, startTime, endTime, nodeID, messageID, password);
    }
}
