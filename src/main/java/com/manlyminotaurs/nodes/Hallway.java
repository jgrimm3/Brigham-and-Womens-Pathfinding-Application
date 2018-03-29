package com.manlyminotaurs.nodes;

import java.util.ArrayList;

public class Hallway extends Node {
    public Hallway(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building, ArrayList<Edge> edges) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building, edges);
    }
}
