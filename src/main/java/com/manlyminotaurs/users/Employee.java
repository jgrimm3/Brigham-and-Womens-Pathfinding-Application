package com.manlyminotaurs.users;

public abstract class Employee extends User{
    public Employee(String userID, String firstName, String middleInitial, String lastName, String language, String userType) {
        super(userID, firstName, middleInitial, lastName, language, userType);
    }
}