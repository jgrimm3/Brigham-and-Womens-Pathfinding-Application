package com.manlyminotaurs.nodes;

public class Location {
    private int xCoord;
    private int yCoord;
    private int xCoord3D;
    private int yCoord3D;
    private String floor;
    private String building;

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
        if(floor.equals("03")){
            floor = "3";
        }
        else if(floor.equals("02")){
            floor = "2";
        }
        else if(floor.equals("01")){
            floor = "1";
        }
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
