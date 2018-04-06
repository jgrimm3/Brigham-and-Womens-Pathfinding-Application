package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.ScoredNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class DepthFirstStrategyI implements IPathFindingStrategy {
    public LinkedList<ScoredNode> calcPath(ScoredNode startNode, ScoredNode endNode, PriorityQueue<ScoredNode> openList, HashMap<String, ScoredNode> closedList, ArrayList<Node> nodes, ArrayList<Edge> edges) throws PathNotFoundException {
        return null;
    }
}
