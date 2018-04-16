package com.manlyminotaurs.users;

import java.util.List;

public class Janitor extends Employee{
    public Janitor(String userID, String firstName, String middleInitial, String lastName, List<String> languages, String userType) {
        super(userID, firstName, middleInitial, lastName, languages, userType);
    }
}
