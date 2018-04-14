package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.util.*;

public class ClosestStrategyI implements IPathFindingStrategy {
    /*
    TODO:
    - need to Have BFS find a nearest type of node
    - Flesh out error handling
     */

    /**
     * Wrapper for calcPath function. Creates empty data structures.
     *
     * @param startNode: starting node
     * @param endNode:   ending nodes
     * @return list of nodes traversed
     */
    public LinkedList<Node> find(Node startNode, Node endNode) throws PathNotFoundException {
        ClosestStrategyNode dijStartNode = new ClosestStrategyNode(startNode, null);
        ClosestStrategyNode dijEndNode = new ClosestStrategyNode(endNode, null);

        LinkedList<Node> path = stripNodeWrappers(calcPath(dijStartNode, dijEndNode));
        return path;
    }
/*
    /**
     * use breadth first search algorithm to find closest path to end node
     *
     * @param startNode: starting node
     * @param endNode:   destination node
     * @return Linked List of Node
     * @throws PathNotFoundException: for an invalid path
     *


    private LinkedList<BFSNode> calcPath2(BFSNode startNode, BFSNode endNode) throws PathNotFoundException {
        String target = endNode.getNode().getNodeType();
        Queue<BFSNode> q = new LinkedList<>();
        BFSNode currentNode = startNode;
        q.add(currentNode);
        ArrayList<PathfindingNode> visitedNodes = new ArrayList<>();

        while(!q.isEmpty()) {
            currentNode = q.remove();
            currentNode.setVisitedStatus(true);
            visitedNodes.add(currentNode);
            if (!currentNode.getNode().getNodeType().equals(target)) {
                for (Node node: currentNode.getNode().getAdjacentNodes()) {
                    boolean wasVisited = false;
                    for(PathfindingNode bfs : visitedNodes){
                        if(bfs.getNode().equals(node)){
                            wasVisited = true;
                        }
                    }
                    if(!wasVisited){
                        q.add(new BFSNode(node, currentNode));
                    }else {
                        BFSNode nowVisisted = new BFSNode(node, currentNode);
                        nowVisisted.setVisitedStatus(true);
                        visitedNodes.add(nowVisisted);
                    }
                }
            }else{
                return getNodeTrail(currentNode);
            }
        }
        throw new PathNotFoundException();
    }
    */

    private LinkedList<ClosestStrategyNode> calcPath(ClosestStrategyNode startNode, ClosestStrategyNode endNode) throws PathNotFoundException {
        String target = endNode.getNode().getNodeType();
        HashMap<Double, ClosestStrategyNode> openList = new HashMap<>();

        // return path if startNode equals endNode
        if (startNode.getNode().equals(endNode.getNode())) { return getNodeTrail(startNode); }

        // set distance of starting node to 0
        startNode.setDistance(0);

        // populate open list of startingNode's children
        for (Node node: startNode.getNode().getAdjacentNodes()) {
            // initialize dijNodes with visited=false, dist=INF
            ClosestStrategyNode dijNode = new ClosestStrategyNode(node, startNode);
            // check for match
            if (dijNode.getNode().getNodeID().equals(target)) { return getNodeTrail(dijNode); }
            // set visited=true and distance from parent
            dijNode.setDistance(distanceBetweenNodes(startNode, dijNode));
            //dijNode.setVisited(true);
            // add to open list
            openList.put(dijNode.getDistance(), dijNode);
        }

        // find shortest path for all vertices, check if equals endNode
        for (int i=0; i<openList.size(); i++) {
            // get closest child
            double min = minDistance(openList);
            ClosestStrategyNode closestNode = openList.get(min);
            // set visited to true
            closestNode.setVisited(true);
            // check for match
            if (closestNode.getNode().getNodeID().equals(target)) { return getNodeTrail(closestNode); }
            // update distances of all children of this node
            for (Node node: closestNode.getNode().getAdjacentNodes()) {
                ClosestStrategyNode newNode = new ClosestStrategyNode(node, closestNode);
                // updates child distances by adding minimum distance
                if (!openList.containsValue(newNode) && (startNode.getDistance() + min) <  newNode.getDistance()) {
                    newNode.setDistance(newNode.getDistance() + min);
                }
                // check for match
                if (newNode.getNode().getNodeID().equals(target)) { return getNodeTrail(newNode); }
            }
        }
        // no path available
        throw new PathNotFoundException();
    }

    /**
     * Looks through a node's children and returns shortest child
     *
     * @param openList
     * @return shortest distance in openList
     */
    private double minDistance(HashMap<Double, ClosestStrategyNode> openList) {
        // find shortest node in openList
        double min = 0;
        for (ClosestStrategyNode currDijNode: openList.values()) {
            if (!currDijNode.getVisitedStatus() && currDijNode.getDistance() <= min)
            {
                min = currDijNode.getDistance();
            }
        }
        return min;
    }


    /**
     * Calculates the Euclidean distance between given nodes
     *
     * @param node1: starting node
     * @param node2: ending node
     * @return The distance between nodes
     */

    private double distanceBetweenNodes(ClosestStrategyNode node1, ClosestStrategyNode node2){
        int x = 0;
        int x1 = node1.getNode().getXCoord();
        int y1 = node1.getNode().getYCoord();

        int x2 = node2.getNode().getXCoord();
        int y2 = node2.getNode().getYCoord();

        double xDist = x2 - x1;
        double yDist = y2 - y1;

        return Math.hypot(xDist, yDist);
    }

    /**
     * Takes in a PathfindingNode and follows the path of parents, creating a LinkedList as it goes
     * @param aNode The end of the trail to be generated
     * @return The trail of nodes leading back to the start node
     */

    LinkedList<ClosestStrategyNode> getNodeTrail(ClosestStrategyNode aNode){
        LinkedList<ClosestStrategyNode> nodeTrail = new LinkedList<>();
        while(!(aNode.getParent() == null)){
            nodeTrail.addFirst(aNode);
            aNode = (ClosestStrategyNode) aNode.getParent();
        }
        nodeTrail.addFirst(aNode); // addFirst ensures correct order
        return nodeTrail;
    }

    /**
     * Takes in a list of PathfindingNodes and strips their metadata, returning a list of the raw Nodes
     * @param toStrip List of nodes to have their metadata stripped
     * @return strippedNodes
     */
    LinkedList<Node> stripNodeWrappers(LinkedList<ClosestStrategyNode> toStrip){
        LinkedList<Node> strippedNodes = new LinkedList<>();
        for(ClosestStrategyNode a: toStrip){
            strippedNodes.add(a.getNode());
        }
        return strippedNodes;
    }
    /**
     * Get all the edges that a node belongs to, in the form of list of nodes
     *
     * @param pNode: scored node
     * @return list of node's edges, node list
     */
    private ArrayList<Node> getEdges(PathfindingNode pNode) throws OrphanNodeException{
        ArrayList<Node> nodeEdges = new ArrayList<>(pNode.getNode().getAdjacentNodes());
        if(nodeEdges.isEmpty()){ throw new OrphanNodeException("Node has no valid edges"); }
        return nodeEdges;
    }
}
