package com.manlyminotaurs.nodes;

public class Location {

    int xCoord;
    int yCoord;
    String floor;
    String building;

    public Location(int xcoord, int ycoord, String floor, String building) {
        this.xCoord = xcoord;
        this.yCoord = ycoord;
        this.floor = floor;
        this.building = building;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public String getFloor() {
        return floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
