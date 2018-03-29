package com.manlyminotaurs.nodes;

import java.util.ArrayList;

import java.util.ArrayList;

public abstract class Node {

    Location loc;
    String longName;
    String shortName;
    String ID;
    String nodeType;
    ArrayList<Edge> edges;

    public Node(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building) {
        this.loc = loc;
        this.longName = longName;
        this.shortName = shortName;
        this.ID = ID;
        this.nodeType = nodeType;
        this.loc = new Location(xcoord, ycoord, floor, building);
        this.edges = new ArrayList<>();
    }

    public void setLoc(Location loc) { this.loc = loc; }

    public void setLongName(String longName) { this.longName = longName; }

    public void setShortName(String shortName) { this.shortName = shortName; }

    public void setID(String ID) { this.ID = ID; }

    public void setNodeType(String nodeType) { this.nodeType = nodeType; }

    public Location getLoc() { return loc; }

    public String getID() { return ID; }

    public String getLongName(){return longName; }

    public String getShortName() { return shortName; }

    public String getNodeType() { return nodeType; }

    public int getXCoord() { return loc.xCoord; }

    public int getYCoord() { return loc.yCoord; }

    public String getFloor() { return loc.floor; }

    public String getBuilding() { return loc.building; }

}
