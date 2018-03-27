package com.manlyminotaurs.nodes;

public abstract class Node {

    Location loc;
    String longName;
    String shortName;
    String ID;
    String nodeType;

    public Node(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, int floor, String building) {
        this.loc = loc;
        this.longName = longName;
        this.shortName = shortName;
        this.ID = ID;
        this.nodeType = nodeType;
        this.loc = new Location(xcoord, ycoord, floor, building);
    }

    public Location getLoc() {
        return loc;
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getNodeType() {
        return nodeType;
    }
}
