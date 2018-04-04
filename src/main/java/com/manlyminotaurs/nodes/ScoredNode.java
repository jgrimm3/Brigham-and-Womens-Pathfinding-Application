package com.manlyminotaurs.nodes;

public abstract class ScoredNode implements Comparable<ScoredNode> {
    double gScore;
    double hScore;
    double fScore;

    public ScoredNode(double gScore, double hScore, double fScore) {
        this.gScore = gScore;
        this.hScore = hScore;
        this.fScore = fScore;
    }

    /**
     * Compares this ScoredNode to another ScoredNode.
     * @param other The other ScoredNode to be compared
     * @return &lt;0 if other is greater, &gt;0 if this is greater, 0 if they are equal
     */
    @Override
    public int compareTo(ScoredNode other){
        return 1;
    }
}
