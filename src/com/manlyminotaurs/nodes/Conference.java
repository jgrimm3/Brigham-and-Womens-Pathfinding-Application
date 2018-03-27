package com.manlyminotaurs.nodes;

import java.util.IdentityHashMap;

public class Conference extends Room {
    public Conference(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, int floor, String building) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
    }
}

