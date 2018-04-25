package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.databases.DataModelI;
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

    /**
     * calls find function with certain type
     * @param startNode node
     * @param endNode node
     * @param strategy strategy to set
     * @return list of nodes
     * @throws PathNotFoundException
     */
    public LinkedList<Node> getPath(Node startNode, Node endNode, IPathFindingStrategy strategy) throws PathNotFoundException{
        setStrategy(strategy);
        LinkedList<Node> tempList = strategy.find(startNode, endNode);
        DataModelI.getInstance().addPath(startNode.getNodeID(),endNode.getNodeID());
        return tempList;
    }
}
