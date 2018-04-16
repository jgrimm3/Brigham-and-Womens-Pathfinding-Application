package com.manlyminotaurs.users;

import java.util.List;

public abstract class Employee extends User{
    public Employee(String userID, String firstName, String middleInitial, String lastName, List<String> languages, String userType) {
        super(userID, firstName, middleInitial, lastName, languages, userType);
    }
}