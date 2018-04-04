package com.manlyminotaurs.nodes;

public class ScoredNode implements Comparable<ScoredNode> {
    Node node;
    ScoredNode parent;
    double gScore;
    double hScore;
    double fScore;

    public ScoredNode(Node node, ScoredNode parent, double gScore, double hScore, double fScore) {
        this.node = node;
        this.parent = parent;
        this.gScore = gScore;
        this.hScore = hScore;
        this.fScore = fScore;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public ScoredNode getParent() {
        return parent;
    }

    public void setParent(ScoredNode parent) {
        this.parent = parent;
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
     * Compares this ScoredNode to another ScoredNode.
     * @param other The other ScoredNode to be compared
     * @return &lt;0 if other is greater, &gt;0 if this is greater, 0 if they are equal
     */
    @Override
    public int compareTo(ScoredNode other){
        return Double.compare(this.getfScore(), other.getfScore());
    }
}