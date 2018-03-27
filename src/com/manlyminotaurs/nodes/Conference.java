package com.manlyminotaurs.nodes;

import java.util.IdentityHashMap;

public class Conference extends Room {

    public Conference(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, int floor, String building) {
        this.loc = loc;
        this.longName = longName;
        this.shortName = shortName;
        this.ID = ID;
        this.nodeType = nodeType;
        this.loc = new Location(xcoord, ycoord, floor, building);
    }
}
