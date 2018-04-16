package com.manlyminotaurs.users;

import java.util.List;

public class interpreter extends Employee{
    public interpreter(String userID, String firstName, String middleInitial, String lastName, List<String> languages, String userType) {
        super(userID, firstName, middleInitial, lastName, languages, userType);
    }
}
