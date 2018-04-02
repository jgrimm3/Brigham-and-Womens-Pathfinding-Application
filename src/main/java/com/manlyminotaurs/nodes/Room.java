package com.manlyminotaurs.nodes;

public class Room extends Node {
    private String specialization;
    private String detailedInfo;
    private int popularity;
    private boolean isOpen;

    public Room(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        this.specialization = "yolo";
        this.detailedInfo = "lala";
        this.popularity = 1;
        this.isOpen = true;
    }

    public Room(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord,
                String floor, String building, String specialization,String detailedInfo, int popularity, boolean isOpen) {
        super(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        this.specialization = specialization;
        this.detailedInfo = detailedInfo;
        this.popularity = popularity;
        this.isOpen = isOpen;
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
}
