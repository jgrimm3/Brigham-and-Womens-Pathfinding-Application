package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.util.*;

public class ClosestStrategyI implements IPathFindingStrategy {

    /**
     * Wrapper for calcPath function. Creates empty data structures.
     *
     * @param startNode: starting node
     * @param endNode:   ending nodes
     * @return list of nodes traversed
     */
    public LinkedList<Node> find(Node startNode, Node endNode) throws PathNotFoundException {
        ClosestStrategyNode closestStartNode = new ClosestStrategyNode(startNode, null);
        ClosestStrategyNode closestEndNode = new ClosestStrategyNode(endNode, null);

        LinkedList<Node> path = stripNodeWrappers(calcPath(closestStartNode, closestEndNode));
        return path;
    }

    /**
     * uses bfs to find closest node by type
     * @param startNode starting node
     * @param endNode ending node
     * @return
     * @throws PathNotFoundException
     */
    private LinkedList<ClosestStrategyNode> calcPath(ClosestStrategyNode startNode, ClosestStrategyNode endNode) throws PathNotFoundException {
        String target = endNode.getNode().getNodeType();
        Queue<ClosestStrategyNode> q = new LinkedList<>();
        ClosestStrategyNode currentNode = startNode;
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
                        q.add(new ClosestStrategyNode(node, currentNode));
                    }else {
                        ClosestStrategyNode nowVisisted = new ClosestStrategyNode(node, currentNode);
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

    /**
     * Calculates the Euclidean distance between given nodes
     *
     * @param node1: starting node
     * @param node2: ending node
     * @return The distance between nodes
     */

    public double distanceBetweenNodes(ClosestStrategyNode node1, ClosestStrategyNode node2){
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
    private ArrayList<ClosestStrategyNode> expandNode(ClosestStrategyNode aNode) throws PathNotFoundException{
        ArrayList<ClosestStrategyNode> children = new ArrayList<>();
        ArrayList<Node> childEdges;

        try {
            childEdges = getEdges(aNode);
        } catch (OrphanNodeException e){
            throw new PathNotFoundException();
        }

        for (Node ne: childEdges){
            if(isValidNode(aNode)){
                ClosestStrategyNode scoredChild = new ClosestStrategyNode(ne, aNode);
                children.add(scoredChild);
            }
        }
        return children;
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


    /**
     * Checks to see if A* is allowed to route through the given node
     * @param sNode: node
     * @return True if allowed to visit node, false if not
     */
    boolean isValidNode(ClosestStrategyNode sNode){
        return sNode.getNode().getStatus() == 1;
    }

    /**
     * looks through open list to find the node with the shortest distance
     *
     * @param openList: queue of nodes
     * @return the closest ClosestStrategyNode
     */

    private ClosestStrategyNode findClosestNode(Set<ClosestStrategyNode> openList) {
        ClosestStrategyNode closestNode = null;
        Double min = Double.MAX_VALUE;
        // loop through set to find shortest distance
        for (ClosestStrategyNode currNode: openList) {
            Double nodeDistance = currNode.getDistance();
            if (nodeDistance < min) {
                min = nodeDistance;
                closestNode = currNode;
            }
        }
        return closestNode;
    }

    /**
     * adds current minimum distance to a desired nodes distance
     *
     * @param child: node to += distance to
     * @param parent: node to find distance from
     * @param weight: current minimum distance to add to children
     */

    private void calcMinDistance(ClosestStrategyNode child, ClosestStrategyNode parent, double weight) {
        double sourceDistance = parent.getDistance();
        if (sourceDistance + weight < child.getDistance()) {
            child.setDistance(sourceDistance + weight);
            LinkedList<ClosestStrategyNode> shortestPath = new LinkedList<>(child.getShortestPath());
            shortestPath.add(parent);
            child.setShortestPath(shortestPath);
        }
    }
    /**
      *
      * Uses Dijktra's shortest path algorithm to find the shortest distance from a start to a certain node type
      *
      * @param startNode: starting node
      * @param endNode: destination type
      * @return: LinkedList<ClosestStrategyNode>: linked list of nodes to the destination
      * @throws new PathNotFoundException
      */

//    private LinkedList<ClosestStrategyNode> calcPath(ClosestStrategyNode startNode, ClosestStrategyNode endNode) throws PathNotFoundException {
//        // keep track of all visited nodes
//        Set<Node> visited = new HashSet<>();
//        // keep queue
//        Set<ClosestStrategyNode> openList = new HashSet<>();
//        // the nearest type we are looking for
//        String target = endNode.getNode().getNodeType();
//
//        // check for start=end
//        if (startNode.getNode().equals(endNode.getNode())) { return getNodeTrail(startNode); }
//
//        // init startNode
//        startNode.setDistance(0);
//        openList.add(startNode);
//
//        while (openList.size() != 0) {
//            // find closest node from starting point based on distance (x,y coords)
//            ClosestStrategyNode currNode = findClosestNode(openList);
//            // remove from queue
//            openList.remove(currNode);
//            if (currNode.getNode().getNodeType().equals(target)) { return getNodeTrail(currNode); }
//            // loop through children of current node
//            for (Node node: currNode.getNode().getAdjacentNodes()) {
//                ClosestStrategyNode dijNode = new ClosestStrategyNode(node, currNode);
//                // don't revisit an old node
//                if (visited.contains(node)) { continue; }
//                if (dijNode.getNode().getNodeType().equals(target)) { return getNodeTrail(dijNode); }
//                // find and set distance from child to parent
//                double distance = distanceBetweenNodes(dijNode, currNode);
//                dijNode.setDistance(distance);
//                if (!openList.contains(currNode)) {
//                    // find distance from child to parent
//                    calcMinDistance(dijNode, currNode, distance);
//                    // add child to queue
//                    openList.add(dijNode);
//                }
//            }
//            // update visited list
//            visited.add(currNode.getNode());
//        }
//        // no paths found
//        throw new PathNotFoundException();
//    }
}
