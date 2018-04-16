package com.manlyminotaurs.log;

import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.users.User;

import java.time.LocalDateTime;

public class Pathfinder {
    private String pathfinderID;
    private LocalDateTime pathfinderTime;
    private String startNodeID;
    private String endNodeID;
    private String userID;

    public Pathfinder(String pathfinderID, LocalDateTime pathfinderTime, String startNodeID, String endNodeID, String userID) {
        this.pathfinderID = pathfinderID;
        this.pathfinderTime = pathfinderTime;
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.userID = userID;
    }

    public String getPathfinderID() {
        return pathfinderID;
    }

    public LocalDateTime getPathfinderTime() {
        return pathfinderTime;
    }

    public String getStartNodeID() {
        return startNodeID;
    }

    public String getEndNodeID() {
        return endNodeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setPathfinderID(String pathfinderID) {
        this.pathfinderID = pathfinderID;
    }

    public void setPathfinderTime(LocalDateTime pathfinderTime) {
        this.pathfinderTime = pathfinderTime;
    }

    public void setStartNodeID(String startNodeID) {
        this.startNodeID = startNodeID;
    }

    public void setEndNodeID(String endNodeID) {
        this.endNodeID = endNodeID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


}
