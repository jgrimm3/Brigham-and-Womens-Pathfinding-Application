package com.manlyminotaurs.nodes;

public class Exit extends Node {

    boolean isFireExit;
    boolean isArmed;

    public Exit(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        isFireExit = false;
        isArmed = false;
    }

    public Exit(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building, boolean isFireExit, boolean isArmed) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        this.isFireExit = isFireExit;
        this.isArmed = isArmed;
    }
}
