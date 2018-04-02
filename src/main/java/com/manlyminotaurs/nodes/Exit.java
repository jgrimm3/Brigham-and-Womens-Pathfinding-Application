package com.manlyminotaurs.nodes;

public class Exit extends Node {

    private boolean isFireExit;
    private boolean isArmed;

    public Exit(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        isFireExit = true;
        isArmed = true;
    }

    public Exit(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building, boolean isFireExit, boolean isArmed) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        this.isFireExit = isFireExit;
        this.isArmed = isArmed;
    }

    public boolean isFireExit() {
        return isFireExit;
    }

    public void setFireExit(boolean fireExit) {
        isFireExit = fireExit;
    }

    public boolean isArmed() {
        return isArmed;
    }

    public void setArmed(boolean armed) {
        isArmed = armed;
    }
}
