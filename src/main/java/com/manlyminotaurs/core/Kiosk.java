package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Node;

public class Kiosk {
    private static Node kioskLocation;
    private static String User;

    public Kiosk(){}

    Kiosk(Node kioskLocation) {
        this.kioskLocation = kioskLocation;
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
