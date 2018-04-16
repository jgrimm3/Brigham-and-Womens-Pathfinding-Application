package com.manlyminotaurs.log;

import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.users.User;

import java.time.LocalDateTime;

public class Pathfinder {
    private String pathfinderID;
    private String startNodeID;
    private String endNodeID;

    public Pathfinder(String pathfinderID, String startNodeID, String endNodeID) {
        this.pathfinderID = pathfinderID;
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
    }

    public String getPathfinderID() {
        return pathfinderID;
    }

    public String getStartNodeID() {
        return startNodeID;
    }

    public String getEndNodeID() {
        return endNodeID;
    }

    public void setPathfinderID(String pathfinderID) {
        this.pathfinderID = pathfinderID;
    }

    public void setStartNodeID(String startNodeID) {
        this.startNodeID = startNodeID;
    }

    public void setEndNodeID(String endNodeID) {
        this.endNodeID = endNodeID;
    }
}
