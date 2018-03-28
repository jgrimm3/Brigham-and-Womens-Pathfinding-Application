package com.manlyminotaurs.nodes;

import java.util.ArrayList;

public abstract class Node {

    Location loc;
    String longName;
    String shortName;
    String ID;
    String nodeType;
    public ArrayList<Edge> edges;
    double gScore;
    double hScore;
    double fScore;

    public Node() {
        this.edges = new ArrayList<>();
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
}
