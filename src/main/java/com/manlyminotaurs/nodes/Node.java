package com.manlyminotaurs.nodes;

//import com.manlyminotaurs.databases.NodesEditor;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Node implements INode {

    int xCoord;
    int yCoord;
    String floor;
    String building;
    String longName;
    String shortName;
    String ID;
    String nodeType;
    ArrayList<Node> edgeNodes = new ArrayList<Node>();
    int status;

    public Node(String longName, String shortName, String ID, String nodeType, int xCoord, int yCoord, String floor, String building, ArrayList<Node> edgeNodes) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floor = floor;
        this.building = building;
        this.longName = longName;
        this.shortName = shortName;
        this.ID = ID;
        this.nodeType = nodeType;
        this.edgeNodes = edgeNodes;
        status = 1;
    }

    public int getxCoord() { return xCoord; }

    public void setxCoord(int xCoord) { this.xCoord = xCoord; }

    public int getyCoord() { return yCoord; }

    public void setyCoord(int yCoord) { this.yCoord = yCoord; }

    public String getFloor() { return floor; }

    public void setFloor(String floor) { this.floor = floor; }

    public String getBuilding() { return building; }

    public void setBuilding(String building) { this.building = building; }

    public void setLongName(String longName) { this.longName = longName; }

    public void setShortName(String shortName) { this.shortName = shortName; }

    public void setID(String ID) { this.ID = ID; }

    public void setNodeType(String nodeType) { this.nodeType = nodeType; }

    public String getID() { return ID; }

    public String getLongName(){return longName; }

    public String getShortName() { return shortName; }

    public String getNodeType() { return nodeType; }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

    public boolean isType(String type) { return false; }
}
