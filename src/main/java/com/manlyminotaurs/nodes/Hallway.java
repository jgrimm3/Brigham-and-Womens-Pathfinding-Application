package com.manlyminotaurs.nodes;

public class Hallway extends Node {
    private int popularity;

    public Hallway(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        popularity = 1;
    }

    public Hallway(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building, int popularity) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        this.popularity = popularity;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}
