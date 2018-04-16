package com.manlyminotaurs.log;

import java.time.LocalDateTime;

public class Log {

    private String loginID;
    private String description;
    private LocalDateTime logTime;
    private String userID;
    private String associatedID;
    private String associatedType;

    public Log(String loginID, String description, LocalDateTime logTime, String userID, String associatedID, String associatedType) {
        this.loginID = loginID;
        this.description = description;
        this.logTime = logTime;
        this.userID = userID;
        this.associatedID = associatedID;
        this.associatedType = associatedType;
    }


    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalDateTime logTime) {
        this.logTime = logTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAssociatedID() {
        return associatedID;
    }

    public void setAssociatedID(String associatedID) {
        this.associatedID = associatedID;
    }

    public String getAssociatedType() {
        return associatedType;
    }

    public void setAssociatedType(String associatedType) {
        this.associatedType = associatedType;
    }
}
