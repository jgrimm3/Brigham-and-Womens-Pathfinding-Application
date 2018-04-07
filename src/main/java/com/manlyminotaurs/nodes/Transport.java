package com.manlyminotaurs.nodes;

public class Transport extends Node {


    public Transport(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building, int xCoord3D, int yCoord3D) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building, xCoord3D, yCoord3D);
    }

    public boolean isType(String type) {
        if(type == "TRANS") { return true; }
        return false;
    }
}
