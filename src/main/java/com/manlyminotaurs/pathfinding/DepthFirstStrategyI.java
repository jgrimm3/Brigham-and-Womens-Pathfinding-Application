package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.*;

public class DepthFirstStrategyI implements IPathFindingStrategy {
    private Stack<DFSNode> openList = new Stack<>();

    /**
     * The Wrapper for calcPath function. Creates empty data structures.
     *
     * @param startNode: starting node
     * @param endNode:   ending nodes
     * @return list of nodes traversed
     */
    public LinkedList<Node> find(Node startNode, Node endNode) throws PathNotFoundException {
        DFSNode dfsStartNode = new DFSNode(startNode, null);
        DFSNode dfsEndNode = new DFSNode(endNode, null);

        ArrayList<DFSNode> queue = new ArrayList<>();
        ArrayList<DFSNode> visisted = new ArrayList<>();


        LinkedList<Node> path = stripNodeWrappers(calcPath(dfsStartNode, dfsEndNode, queue, visisted));
        return path;
    }

    /**
     * recursive depth first search pathfinding algorithm
     *
     * @param currentNode: source
     * @param endNode:     destination
     * @return list to destination through breadth first search
     * @throws PathNotFoundException
     */

    private LinkedList<DFSNode> calcPath(DFSNode currentNode, DFSNode endNode, ArrayList<DFSNode> queue, ArrayList<DFSNode> visited) throws PathNotFoundException {
        visited.add(currentNode);
        if (!currentNode.getNode().equals(endNode.getNode())) {
            for (Node node : currentNode.getNode().getAdjacentNodes()) {
                boolean wasVisited = false;
                for (PathfindingNode dfs : visited) {
                    if (dfs.getNode().equals(node)) {
                        wasVisited = true;
                    }
                }
                if (!wasVisited) {
                    queue.add(0, new DFSNode(node, currentNode));
                } else {
                    DFSNode nowVisisted = new DFSNode(node, currentNode);
                    nowVisisted.setVisitedStatus(true);
                    visited.add(nowVisisted);
                }
            }
        } else {
            return getNodeTrail(currentNode);
        }
        if (queue.size() == 0) {
            throw new PathNotFoundException();
        }
        DFSNode nextNode = queue.remove(0);
        return calcPath(nextNode, endNode, queue, visited);
    }

    /**
     * Takes in a PathfindingNode and follows the path of parents, creating a LinkedList as it goes
     *
     * @param dfsNode The end of the trail to be generated
     * @return The trail of nodes leading back to the start node
     */

    LinkedList<DFSNode> getNodeTrail(DFSNode dfsNode) {
        LinkedList<DFSNode> nodeTrail = new LinkedList<>();
        while (!(dfsNode.getParent() == null)) {
            nodeTrail.addFirst(dfsNode);
            dfsNode = (DFSNode) dfsNode.getParent();
        }
        nodeTrail.addFirst(dfsNode); // addFirst ensures correct order
        return nodeTrail;
    }

    /**
     * Takes in a list of PathfindingNodes and strips their metadata, returning a list of the raw Nodes
     *
     * @param toStrip List of nodes to have their metadata stripped
     * @return strippedNodes
     */
    LinkedList<Node> stripNodeWrappers(LinkedList<DFSNode> toStrip) {
        LinkedList<Node> strippedNodes = new LinkedList<>();
        for (DFSNode dfs : toStrip) {
            strippedNodes.add(dfs.getNode());
        }
        return strippedNodes;
    }
}