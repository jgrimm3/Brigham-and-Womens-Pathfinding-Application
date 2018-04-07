package com.manlyminotaurs.nodes;

import java.util.List;

public abstract class Node implements INode {

    private Location loc;
    private String longName;
    private String shortName;
    private String ID;
    private String nodeType;
    private List<Node> adjacentNodes;
    private int status;
    private int popularity;

    public Node(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord,
				String floor, String building, int xCoord3D, int yCoord3D) {
        this.loc = loc;
        this.longName = longName;
        this.shortName = shortName;
        this.ID = ID;
        this.nodeType = nodeType;
        this.loc = new Location(xcoord, ycoord, xCoord3D, yCoord3D, floor, building);
        status = 1;
        popularity = 0;
    }

    public void setLoc(Location loc) { this.loc = loc; }

    public void setLongName(String longName) { this.longName = longName; }

    public void setShortName(String shortName) { this.shortName = shortName; }

    public void setID(String ID) { this.ID = ID; }

    public void setNodeType(String nodeType) { this.nodeType = nodeType; }

    public Location getLoc() { return loc; }

    public String getID() { return ID; }

    public String getLongName(){return longName; }

    public String getShortName() { return shortName; }

    public String getNodeType() { return nodeType; }

    public int getXCoord() { return loc.xCoord; }

    public int getYCoord() { return loc.yCoord; }

    public int getXCoord3D() { return loc.xCoord3D; }

    public int getYCoord3D() { return loc.yCoord3D; }

    public String getFloor() { return loc.floor; }

    public String getBuilding() { return loc.building; }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

    public void setAdjacentNodes(List<Node> adjacentNodes){
        this.adjacentNodes = adjacentNodes;
    }

    public List<Node> getAdjacentNodes(List<Node> adjacentNodes){
        return this.adjacentNodes;
    }

    public boolean addAdjacentNode(Node node){
        return this.adjacentNodes.add(node);
    }

    public boolean removeAdjacentNode(Node node){
        return this.adjacentNodes.remove(node);
    }
}
