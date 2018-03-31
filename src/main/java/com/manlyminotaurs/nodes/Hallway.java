package com.manlyminotaurs.nodes;

public class Hallway extends Node {
    int popularity;

    public Hallway(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        popularity = 0;
    }

    public Hallway(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building, int popularity) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        this.popularity = popularity;
    }

}
