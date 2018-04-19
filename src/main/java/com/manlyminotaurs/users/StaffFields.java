package com.manlyminotaurs.users;


import java.time.LocalDateTime;

public class StaffFields {
    boolean isWorking;
    boolean isAvailable;
    String languageSpoken;
    String userID;
    private LocalDateTime deleteTime;

    public StaffFields(boolean isWorking, boolean isAvailable, String languageSpoken, String userID) {
        this.isWorking = isWorking;
        this.isAvailable = isAvailable;
        this.languageSpoken = languageSpoken;
        this.userID = userID;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getLanguageSpoken() {
        return languageSpoken;
    }

    public void setLanguageSpoken(String languageSpoken) {
        this.languageSpoken = languageSpoken;
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
