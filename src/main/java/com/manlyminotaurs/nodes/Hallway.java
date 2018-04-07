package com.manlyminotaurs.nodes;

public class Hallway extends Node {

    public Hallway(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building, int xCoord3D, int yCoord3D) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building, xCoord3D, yCoord3D);
    }

	public boolean isType(String type) {
		if(type == "HALL") { return true; }
		return false;
	}
}
