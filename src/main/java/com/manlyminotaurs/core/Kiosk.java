package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Node;

public class Kiosk {
    private static Node kioskLocation;
    private static String User;

    public Kiosk(){}

    Kiosk(Node kioskLocation, String user) {
        this.kioskLocation = kioskLocation;
        this.User = user;
    }

    public Node getKioskLocation() {
        return kioskLocation;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String newUser){
        this.User = newUser;
    }
}
