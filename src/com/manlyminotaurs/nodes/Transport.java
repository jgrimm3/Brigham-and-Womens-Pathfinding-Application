package com.manlyminotaurs.nodes;

public abstract class Transport extends Node {
    public Transport(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, int floor, String building) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
    }
}
