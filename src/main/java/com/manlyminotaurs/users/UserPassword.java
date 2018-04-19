package com.manlyminotaurs.users;

import java.time.LocalDateTime;

public class UserPassword {
    String userName;
    String password;
    String userID;
    private LocalDateTime deleteTime;

    public UserPassword(String userName, String password, String userID) {
        this.userName = userName;
        this.password = password;
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public LocalDateTime getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(LocalDateTime deleteTime) {
        this.deleteTime = deleteTime;
    }
}
