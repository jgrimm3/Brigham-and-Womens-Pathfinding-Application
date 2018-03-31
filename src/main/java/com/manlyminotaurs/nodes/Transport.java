package com.manlyminotaurs.nodes;

import java.util.ArrayList;

public abstract class Transport extends Node {
    public String getDirectionality() {
        return directionality;
    }

    public void setDirectionality(String directionality) {
        this.directionality = directionality;
    }

    public ArrayList<String> getFloors() {
        return floors;
    }

    public void setFloors(ArrayList<String> floors) {
        this.floors = floors;
    }

    String directionality;
    ArrayList<String> floors;

    public Transport(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        this.directionality = "";
        floors = new ArrayList<>();
    }

    public Transport(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building,
                     String directionality, ArrayList<String> floors) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        this.directionality = directionality;
        this.floors = floors;
    }
}
