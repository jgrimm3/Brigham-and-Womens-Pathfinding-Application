package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.util.ArrayList;
import java.util.LinkedList;


public class PathfindingContext {
    private ArrayList nodeSet;
    private PathfinderUtil pu;
    public IPathFindingStrategy strategy;

    // Forced a getPath call to include a strategy to avoid getPath being called without first setting strategy
    private void setStrategy(IPathFindingStrategy strategy) {
        this.strategy = strategy;
    }
    public LinkedList<Node> getPath(Node startNode, Node endNode, IPathFindingStrategy strategy) throws PathNotFoundException{
        setStrategy(strategy);
        return strategy.find(startNode, endNode);
    }
}
