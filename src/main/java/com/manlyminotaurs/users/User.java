package com.manlyminotaurs.users;

import java.util.List;
import java.util.Objects;

public abstract class User {

    String userID;
    String firstName;
    String middleName;
    String lastName;
    List<String> languages;
    String userType;

    public User(String userID, String firstName, String middleName, String lastName, List<String> languages, String userType) {
        this.userID = userID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.languages = languages;
        this.userType = userType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void addLanguage(String language) {
        this.languages.add(language);
    }

    public void setLanguages(List<String> languages){
        this.languages = languages;
    }

    public void removeLanguage(String language){
        this.languages.remove(language);
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isType(String userType) {
        return this.userType.equalsIgnoreCase(userType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(middleName, user.middleName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(languages, user.languages) &&
                Objects.equals(userType, user.userType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, firstName, middleName, lastName, languages, userType);
    }
}
