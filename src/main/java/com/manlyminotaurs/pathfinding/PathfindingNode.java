package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

public abstract class PathfindingNode {
    private Node node;
    private PathfindingNode parent;
    private boolean visitedStatus;

    public PathfindingNode(Node node, PathfindingNode parent) {
        this.node = node;
        this.parent = parent;
        this.visitedStatus = false;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public PathfindingNode getParent() {
        return parent;
    }

    public void setParent(PathfindingNode parent) {
        this.parent = parent;
    }

    public boolean getVisitedStatus() {
        return visitedStatus;
    }

    public void setVisitedStatus(boolean visitedStatus) {
        this.visitedStatus = visitedStatus;
    }
}
