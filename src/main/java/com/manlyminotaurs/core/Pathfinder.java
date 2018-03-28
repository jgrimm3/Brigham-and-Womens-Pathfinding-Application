package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Node;

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

        PriorityQueue<Node> openList = new PriorityQueue<>();
        HashMap<String, Node> closedList = new HashMap<>();

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
    LinkedList<Node> calcPath(Node startNode, Node endNode, PriorityQueue<Node> openList, HashMap<String, Node> closedList, LinkedList<Node> currentPath){
        if (startNode == endNode) return currentPath;
        ArrayList<Node> children = expandNode(startNode);
        children.forEach((child)-> {
            scoreNode(child, currentPath, startNode, endNode);
            openList.add(child);
        });
        Node nextNode = openList.poll();

        currentPath.add(nextNode);
        return calcPath(nextNode, endNode, openList, closedList, currentPath);
    }

    /**
     * Finds all the other nodes connected by edges to given node
     * @param node
     * @return children
     */
    ArrayList<Node> expandNode(Node node){
        ArrayList<Node> children = new ArrayList<>();

        for (Edge edge: node.edges){
            children.add(edge.otherNode(node));
        }
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
