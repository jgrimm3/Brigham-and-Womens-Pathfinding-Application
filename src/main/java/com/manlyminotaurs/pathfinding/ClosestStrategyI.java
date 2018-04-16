package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;
import sun.util.cldr.CLDRLocaleDataMetaInfo;

import java.util.*;

public class ClosestStrategyI extends DistancePathdinder implements IPathFindingStrategy {
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

        return stripNodeWrappers(calcPath(dijStartNode, dijEndNode));
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
    private LinkedList<PathfindingNode> calcPath(ClosestStrategyNode startNode, ClosestStrategyNode endNode) throws PathNotFoundException {
        // keep track of all visited nodes
        Set<Node> visited = new HashSet<>();
        // keep queue
        Set<ClosestStrategyNode> openList = new HashSet<>();
        // the nearest type we are looking for
        String target = endNode.getNode().getNodeType();

        // check for start=end
        if (startNode.getNode().equals(endNode.getNode())) { return getNodeTrail(startNode); }

        // init startNode
        startNode.setDistance(0);
        openList.add(startNode);

        while (openList.size() != 0) {
            // find closest node from starting point based on distance (x,y coords)
            ClosestStrategyNode currNode = findClosestNode(openList);
            // remove from queue
            openList.remove(currNode);
            if (currNode.getNode().getNodeType().equals(target)) { return getNodeTrail(currNode); }
            // loop through children of current node
            for (Node node: currNode.getNode().getAdjacentNodes()) {
                ClosestStrategyNode dijNode = new ClosestStrategyNode(node, currNode);
                // don't revisit an old node
                if (visited.contains(node)) { continue; }
                if (dijNode.getNode().getNodeType().equals(target)) { return getNodeTrail(dijNode); }
                // find and set distance from child to parent
                double distance = distanceBetweenNodes(dijNode, currNode);
                dijNode.setDistance(distance);
                if (!openList.contains(currNode)) {
                    // find distance from child to parent
                    calcMinDistance(dijNode, currNode, distance);
                    // add child to queue
                    openList.add(dijNode);
                }
            }
            // update visited list
            visited.add(currNode.getNode());
        }
        // no paths found
        throw new PathNotFoundException();
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



}
