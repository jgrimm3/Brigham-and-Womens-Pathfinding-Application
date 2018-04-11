package com.manlyminotaurs.nodes;

public class Transport extends Node {


    public Transport(String nodeID, int xcoord, int ycoord, String floor, String building, String nodeType, String longName, String shortName, int status, int xCoord3D, int yCoord3D) {
        super(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName, status, xCoord3D, yCoord3D);
    }

    public boolean isType(String type) {
        if(type == "TRANS") { return true; }
        return false;
    }
    public boolean equals(Node node) {
        return false;
    }
}
