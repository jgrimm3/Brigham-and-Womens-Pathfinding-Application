package com.manlyminotaurs.nodes;

public class Exit extends Node {

    public Exit(String nodeID, int xcoord, int ycoord, String floor, String building, String nodeType, String longName, String shortName, int status, int yCoord3D, int xCoord3D) {
        super(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName, status, xCoord3D, yCoord3D);
    }

    public boolean isType(String type) {
        if(type == "EXIT") { return true; }
        return false;
    }

    public boolean equals(Node node) {
        return false;
    }

}
