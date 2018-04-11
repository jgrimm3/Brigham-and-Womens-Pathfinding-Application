package com.manlyminotaurs.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Node implements INode {

    private Location loc;
    private String nodeID;
    private String longName;
    private String shortName;
    private int status;
    private String nodeType;
    private List<Node> adjacentNodes;
    private int popularity;

    public Node(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int status,
                int xCoord3D, int yCoord3D) {
        this.nodeID = nodeID;
        this.loc = loc;
        this.longName = longName;
        this.shortName = shortName;
        this.status = status;
        this.nodeType = nodeType;
        this.loc = new Location(xCoord, yCoord, xCoord3D, yCoord3D, floor, building);
        this.status = status;
        popularity = 0;
        this.adjacentNodes = new ArrayList<>();
    }

    public void setLoc(Location loc) { this.loc = loc; }

    public void setLongName(String longName) { this.longName = longName; }

    public void setShortName(String shortName) { this.shortName = shortName; }

    public void setNodeType(String nodeType) { this.nodeType = nodeType; }

    public Location getLoc() { return loc; }

    public String getLongName(){return longName; }

    public String getShortName() { return shortName; }

    public String getNodeType() { return nodeType; }

    public String getNodeID(){ return this.nodeID; }

    public int getXCoord() { return loc.getxCoord(); }

    public int getYCoord() { return loc.getyCoord(); }

    public int getXCoord3D() { return loc.getxCoord3D(); }

    public int getYCoord3D() { return loc.getyCoord3D(); }

    public String getFloor() { return loc.getFloor(); }

    public String getBuilding() { return loc.getBuilding(); }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

    public void setAdjacentNodes(List<Node> adjacentNodes){
        this.adjacentNodes = adjacentNodes;
    }

    public List<Node> getAdjacentNodes(){
        return this.adjacentNodes;
    }

    public boolean addAdjacentNode(Node node){
        return this.adjacentNodes.add(node);
    }

    public boolean removeAdjacentNode(Node node){
        return this.adjacentNodes.remove(node);
    }

    @Override
    public boolean equals(Object other){
        Node n = (Node) other;
        return this.getNodeID().equals(n.getNodeID());
    }
    @Override
    public String toString(){
        return this.getNodeID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeID);
    }
}
