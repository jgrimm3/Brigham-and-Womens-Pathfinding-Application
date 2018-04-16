package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class DistancePathdinder {

    /**
     * Calculates the Euclidean distance between given nodes
     *
     * @param node1: starting node
     * @param node2: ending node
     * @return The distance between nodes
     */

    public double distanceBetweenNodes(PathfindingNode node1, PathfindingNode node2){
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
     * Finds all the other nodes connected by edges to given node
     * @param aNode: node
     * @return children
     */
    public ArrayList<PathfindingNode> expandNode(PathfindingNode aNode) throws PathNotFoundException{
        ArrayList<PathfindingNode> children = new ArrayList<>();
        ArrayList<Node> childEdges;

        try {
            childEdges = getEdges(aNode);
        } catch (OrphanNodeException e){
            throw new PathNotFoundException(e.getMessage());
        }

        for (Node ne: childEdges){
            if(isValidNode(aNode)){
                PathfindingNode scoredChild = new PathfindingNode(ne, aNode) {
                };
                children.add(scoredChild);
            }
        }
        return children;
    }

    /**
     * Get all the edges that a node belongs to, in the form of list of nodes
     *
     * @param pNode: scored node
     * @return list of node's edges, node list
     */
    private ArrayList<Node> getEdges(PathfindingNode pNode) throws OrphanNodeException{
        ArrayList<Node> nodeEdges = new ArrayList<>(pNode.getNode().getAdjacentNodes());
        if(nodeEdges.isEmpty()){ throw new OrphanNodeException("Node " + pNode.getNode().getNodeID() + " has no valid edges"); }
        return nodeEdges;
    }


    /**
     * Takes in a PathfindingNode and follows the path of parents, creating a LinkedList as it goes
     * @param aNode The end of the trail to be generated
     * @return The trail of nodes leading back to the start node
     */

    public LinkedList<PathfindingNode> getNodeTrail(PathfindingNode aNode){
        LinkedList<PathfindingNode> nodeTrail = new LinkedList<>();
        while(!(aNode.getParent() == null)){
            nodeTrail.addFirst(aNode);
            aNode = aNode.getParent();
        }
        nodeTrail.addFirst(aNode); // addFirst ensures correct order
        return nodeTrail;
    }

    /**
     * Takes in a list of PathfindingNodes and strips their metadata, returning a list of the raw Nodes
     * @param toStrip List of nodes to have their metadata stripped
     * @return strippedNodes
     */
    public LinkedList<Node> stripNodeWrappers(LinkedList<PathfindingNode> toStrip) {
        LinkedList<Node> strippedNodes = new LinkedList<>();
        for (PathfindingNode a : toStrip) {
            strippedNodes.add(a.getNode());
        }
        return strippedNodes;
    }
    /**
     * Checks to see if A* is allowed to route through the given node
     * @param sNode: node
     * @return True if allowed to visit node, false if not
     */
    boolean isValidNode(PathfindingNode sNode){
        return sNode.getNode().getStatus() == 1;
    }


}
