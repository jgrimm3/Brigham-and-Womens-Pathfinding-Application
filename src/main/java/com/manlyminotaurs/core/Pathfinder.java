package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.ScoredNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Pathfinder {
    ArrayList nodeSet;
    Hospital hospital;

    /**
     *
     * @param startNode
     * @param endNode
     * @return
     */
    public LinkedList<Node> find(Node startNode, Node endNode) {

        PriorityQueue<ScoredNode> openList = new PriorityQueue<>();
        HashMap<String, ScoredNode> closedList = new HashMap<>();

        ScoredNode scoredStart = new ScoredNode(startNode, null, -1, -1, -1);
        ScoredNode scoredEnd = new ScoredNode(endNode, null, -1, -1, -1);

        LinkedList<ScoredNode> currentPath = new LinkedList<>();
        currentPath.add(scoredStart);

        return stripScores(calcPath(scoredStart, scoredEnd, openList, closedList, currentPath));
    }

    /**
     * Takes in a list of ScoredNodes and strips their scores, returning a list of the raw Nodes
     *
     * @param scoredNodes List of nodes to have their scores stripped
     * @return strippedNodes
     */

    LinkedList<Node> stripScores(LinkedList<ScoredNode> scoredNodes){
        LinkedList<Node> strippedNodes = new LinkedList<>();
        for(ScoredNode scoredNode: scoredNodes){
            strippedNodes.add(scoredNode.getNode());
        }
        return strippedNodes;
    }

    /**
     *
     * @param startNode
     * @param endNode
     * @param openList
     * @param closedList
     * @param currentPath
     * @return
     */
    LinkedList<ScoredNode> calcPath(ScoredNode startNode, ScoredNode endNode, PriorityQueue<ScoredNode> openList, HashMap<String, ScoredNode> closedList, LinkedList<ScoredNode> currentPath){
        if (startNode.getNode() == endNode.getNode()) return currentPath;
        ArrayList<ScoredNode> children = expandNode(startNode);
        children.forEach((child)-> {
            child.setParent(startNode);
            scoreNode(child, currentPath, startNode, endNode);
            openList.add(child);
        });
        ScoredNode nextNode = openList.poll();

        currentPath.add(nextNode);
        return calcPath(nextNode, endNode, openList, closedList, currentPath);
    }

    /**
     * Finds all the other nodes connected by edges to given node
     * @param node
     * @return children
     */
    ArrayList<ScoredNode> expandNode(ScoredNode node){
        ArrayList<ScoredNode> children = new ArrayList<>();
        ArrayList<Edge> edges = node.getNode().getEdges();
        for (Edge edge: edges){
            // Finds the node on the other end of an edge and converts it to a ScoredNode before adding
            children.add(new ScoredNode(edge.otherNode(node.getNode()), node, -1, -1, -1));
        }
        return children;
    }

    /**
     * Takes in a ScoredNode and updates its g, h, and f scores
     *
     * @param node
     * @param curPath
     * @param startNode
     * @param endNode
     * @return
     */

    ScoredNode scoreNode(ScoredNode node, LinkedList<ScoredNode> curPath, ScoredNode startNode, ScoredNode endNode){
        node.setgScore(calcGScore(node));
        node.sethScore(calcHScore(node, endNode));
        node.setfScore(calcFScore(node));
        return node;
    }

    /**
     *
     * @param node
     * @return
     */
    double calcGScore(ScoredNode node){
        return (double) getNodeTrail(node).size();
    }

    /**
     * Calculate the H Score for the current node to the end Node
     * This is done using Euclidean Distance
     * @param curNode
     * @param endNode
     * @return
     */
    double calcHScore(ScoredNode curNode, ScoredNode endNode){
        int curX = curNode.getNode().getXCoord();
        int curY = curNode.getNode().getYCoord();

        int goalX = endNode.getNode().getXCoord();
        int goalY = endNode.getNode().getYCoord();

        double xDist = goalX - curX;
        double yDist = goalY - curY;

        return Math.hypot(xDist, yDist);

    }

    /**
     * Calculate the F score for the given Node.
     * This is done by adding the G and H scores for the Node. Must be called after calcGScore and calcHScore
     * @param node The node to be scored
     * @return
     */
    double calcFScore(ScoredNode node) {
        return node.getgScore() + node.gethScore();
    }

    LinkedList<ScoredNode> getNodeTrail(ScoredNode node){
        LinkedList<ScoredNode> nodeTrail = new LinkedList<>();
        while(!(node.getParent() == null)){
            nodeTrail.add(node);
            node = node.getParent();
        }
        return nodeTrail;
    }

    /**
     * Calculates the scores for the given node
     *
     * @param curNode
     * @return scoredNode
     */

    void scoreNode(Node curNode, LinkedList<Node> curPath, Node startNode, Node endNode){
        curNode.setgScore(calcGScore(curPath));
        curNode.sethScore(calcHScore(curNode, endNode));
        curNode.setfScore(calcFScore(curNode, startNode, endNode));
    }
}
