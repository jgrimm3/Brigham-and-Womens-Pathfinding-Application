package com.manlyminotaurs.nodes;

import java.util.ArrayList;

public abstract class Transport extends Node {
    public Transport(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
    }
}
