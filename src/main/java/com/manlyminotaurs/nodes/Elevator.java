package com.manlyminotaurs.nodes;

import java.util.ArrayList;

public class Elevator extends Transport {
    public Elevator(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building, ArrayList<Edge> edges) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building, edges);
    }
}
