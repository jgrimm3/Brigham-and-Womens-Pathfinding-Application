package com.manlyminotaurs.users;

public class UserPassword {
    String userName;
    String password;
    String userID;

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
}
