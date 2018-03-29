package com.manlyminotaurs.nodes;

public class Edge {
    protected Node startNode;
    protected Node endNode;
    protected String id;
    protected int status;

    public Edge(Node startNode, Node endNode, String id, int status) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.id = id;
        this.status = status;
    }

    /**
     * Takes in one node in an edge and returns the other node in that edge
     *
     * @param node
     * @return otherNode
     */

    public Node otherNode(Node node){
        if (node == this.startNode) {
            return this.endNode;
        }
        else if (node == this.endNode){
            return this.startNode;
        }
        return null;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
