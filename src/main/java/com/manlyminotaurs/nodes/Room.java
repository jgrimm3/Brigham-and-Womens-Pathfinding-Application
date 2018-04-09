package com.manlyminotaurs.nodes;

public class Room extends Node {
    private String specialization;
    private String detailedInfo;
    private int popularity;
    private boolean isOpen;

    public Room(String nodeID, int xcoord, int ycoord, String floor, String building, String nodeType, String longName, String shortName, int status, int yCoord3D, int xCoord3D) {
        super(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName, status, xCoord3D, yCoord3D);
        this.specialization = "yolo";
        this.detailedInfo = "lala";
        this.popularity = 1;
        this.isOpen = true;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDetailedInfo() {
        return detailedInfo;
    }

    public void setDetailedInfo(String detailedInfo) {
        this.detailedInfo = detailedInfo;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isType(String type) {
        if(type == "ROOM") { return true; }
        return false;
    }

}
