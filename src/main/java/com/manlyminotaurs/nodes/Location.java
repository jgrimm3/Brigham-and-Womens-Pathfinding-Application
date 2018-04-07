package com.manlyminotaurs.nodes;

public class Location {
    int xCoord;
    int yCoord;
    int xCoord3D;
    int yCoord3D;
    String floor;
    String building;

    public Location(int xcoord, int ycoord, int xCoord3D, int yCoord3D, String floor, String building) {
        this.xCoord3D = xCoord3D;
        this.yCoord3D = yCoord3D;
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

    public int getxCoord3D() {
        return xCoord3D;
    }

    public void setxCoord3D(int xCoord3D) {
        this.xCoord3D = xCoord3D;
    }

    public int getyCoord3D() {
        return yCoord3D;
    }

    public void setyCoord3D(int yCoord3D) {
        this.yCoord3D = yCoord3D;
    }
}
