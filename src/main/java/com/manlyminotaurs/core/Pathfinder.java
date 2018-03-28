package main.java.com.manlyminotaurs.core;

import main.java.com.manlyminotaurs.nodes.Node;
import main.java.com.manlyminotaurs.nodes.ScoredNode;

import java.util.ArrayList;
import java.util.LinkedList;

public class Pathfinder {
    ArrayList nodeSet;
    Hospital hospital;

    /**
     *
     * @param startNode
     * @param endNode
     * @return
     */
    LinkedList<Node> find(Node startNode, Node endNode) {
        return null;
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
    LinkedList<Node> calcPath(Node startNode, Node endNode, ArrayList<ScoredNode>openList, ArrayList<ScoredNode> closedList, LinkedList<Node> currentPath){
        return null;
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
}
