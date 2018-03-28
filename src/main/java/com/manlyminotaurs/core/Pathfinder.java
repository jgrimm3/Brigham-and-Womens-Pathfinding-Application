package com.manlyminotaurs.core;

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

        LinkedList<Node> currentPath = new LinkedList<>();
        currentPath.add(startNode);

        PriorityQueue<ScoredNode> openList = new PriorityQueue<>();
        HashMap<String, ScoredNode> closedList = new HashMap<>();

        return calcPath(startNode, endNode, openList, closedList, currentPath);
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
    LinkedList<Node> calcPath(Node startNode, Node endNode, PriorityQueue<ScoredNode> openList, HashMap<String, ScoredNode> closedList, LinkedList<Node> currentPath){
        if (startNode == endNode) return currentPath;
        ArrayList<Node> children = expandNode(startNode);
        children.forEach((child)-> {
            openList.add(scoreNode(child));
        });
        ScoredNode nextNode = openList.peek();
        calcPath(nextNode, endNode, openList, closedList, currentPath);
        return null;
    }

    /**
     * Finds all the other nodes connected by edges to given node
     * @param node
     * @return children
     */
    ArrayList<Node> expandNode(Node node){
        ArrayList<Node> children = new ArrayList<>();

        node.edges.forEach(edge -> children.add(edge.otherNode(node)));
        return children;
    }

    /**
     *
     * @param currPath
     * @return
     */
    double calcGScore(LinkedList<Node> currPath){
    return 0;
    }

    /**
     * Calculate the H Score for the current node to the end Node
     * This is done using Euclidean Distance
     * @param currNode
     * @param endNode
     * @return
     */
    double calcHScore(Node currNode, Node endNode){
        return 0;
    }

    /**
     * Calculate the F score for the given Node.
     * This is done by adding the G and H scores for the Node
     * @param currNode The node currently at
     * @param startNode The node at the beginning of the path-finding
     * @param endNode The node at the
     * @return
     */
    double calcFScore(Node currNode, Node startNode, Node endNode) {
    return 0;
    }

    /**
     * Converts a Node into the equivalent ScoredNode
     *
     * @param curNode
     * @return scoredNode
     */

    ScoredNode scoreNode(Node curNode){
        ScoredNode scoredNode = new ScoredNode();
        scoredNode.setgScore(1);
        scoredNode.sethScore(1);
        scoredNode.sethScore(2);

        return scoredNode;
    }
}
