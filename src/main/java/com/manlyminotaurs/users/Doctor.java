package com.manlyminotaurs.users;

import java.util.List;

public class Doctor extends Employee{
    public Doctor(String userID, String firstName, String middleInitial, String lastName, List<String> language, String userType) {
        super(userID, firstName, middleInitial, lastName, language, userType);
    }
}
