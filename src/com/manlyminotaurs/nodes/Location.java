package com.manlyminotaurs.nodes;

class Location {

    int xCoord;
    int yCoord;
    int floor;
    String building;

    public Location(int xcoord, int ycoord, int floor, String building) {
        this.xCoord = xcoord;
        this.yCoord = ycoord;
        this.floor = floor;
        this.building = building;
    }
}
