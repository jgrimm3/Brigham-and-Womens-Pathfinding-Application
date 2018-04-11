package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

public class AStarNode extends PathfindingNode implements Comparable<AStarNode> {
    private double gScore;
    private double hScore;
    private double fScore;

    public AStarNode(Node node, AStarNode parent) {
        super(node, parent);
        this.gScore = -1;
        this.hScore = -1;
        this.fScore = -1;
    }

    public double getgScore() {
        return gScore;
    }
    public void setgScore(double gScore) {
        this.gScore = gScore;
    }
    public double gethScore() {
        return hScore;
    }
    public void sethScore(double hScore) {
        this.hScore = hScore;
    }
    public double getfScore() {
        return fScore;
    }
    public void setfScore(double fScore) {
        this.fScore = fScore;
    }

    /**
     * Compares AStarNodes based on f score
     * @param other The other AStarNode to be compared
     * @return &lt;0 if other is greater, &gt;0 if this is greater, 0 if they are equal
     */
    @Override
    public int compareTo(AStarNode other){
        return Double.compare(this.getfScore(), other.getfScore());
    }

    @Override
    public boolean equals(Object other){
        AStarNode n = (AStarNode) other;
        return this.getNode().getNodeID().equals(n.getNode().getNodeID());
    }

    @Override
    public int hashCode(){
        return this.getNode().getNodeID().hashCode();
    }
}