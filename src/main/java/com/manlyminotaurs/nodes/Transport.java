package com.manlyminotaurs.nodes;

import java.util.ArrayList;

public class Transport extends Node {

    private String directionality;
    private ArrayList<String> floors;

    public Transport(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        this.directionality = "Two way";
        this.floors = new ArrayList<>();
        this.floors.add("L2");
        this.floors.add("L1");
        this.floors.add("1");
    }

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

    public String floorsToString(){
        StringBuffer floorBuffer = new StringBuffer();
        int i;
        for (i =0; i < this.floors.size()-1; i++) {
            floorBuffer.append(floors.get(i)+"/");
        }
        floorBuffer.append(floors.get(i));
        return floorBuffer.toString();
    }
}
