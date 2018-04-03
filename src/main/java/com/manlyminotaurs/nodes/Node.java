package com.manlyminotaurs.nodes;

public abstract class Node {

    Location loc;
    String longName;
    String shortName;
    String ID;
    String nodeType;
    int status;

    public Node(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building) {
        this.loc = loc;
        this.longName = longName;
        this.shortName = shortName;
        this.ID = ID;
        this.nodeType = nodeType;
        this.loc = new Location(xcoord, ycoord, floor, building);
        status = 1;
    }

    /**
     * Overrides default equals method so that nodes are compared by their IDs
     *
     * @param o
     * @return True if Node IDs are the same
     */

    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        Node n = (Node) o;
        return n.ID.equals(this.ID);
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

    public String getFloor() { return loc.floor; }

    public String getBuilding() { return loc.building; }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

}
