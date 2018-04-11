package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.users.User;

public class KioskInfo {
    public static Node getMyLocation() {
        return myLocation;
    }

    public static void setMyLocation(Node myLocation) {
        KioskInfo.myLocation = myLocation;
    }

    public static String getCurrentUserID() {
        return currentUserID;
    }

    public static void setCurrentUserID(String currentUserID) {
        KioskInfo.currentUserID = currentUserID;
    }

    public static Node myLocation;
    public static String currentUserID;
}
