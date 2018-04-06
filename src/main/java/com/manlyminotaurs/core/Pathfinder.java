package com.manlyminotaurs.core;

import com.manlyminotaurs.databases.NodesDBUtil;
import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.ScoredNode;

import java.util.*;

/* TODO:
    - Add floor switch functionality. calcPath(toElev) + calcPath(toDest).
 */

public class Pathfinder {
    ArrayList nodeSet;
    Hospital hospital;

    /**
     * Wrapper for calcPath function. Creates empty data structures.
     *
     * @param startNode
     * @param endNode
     * @retur LinkedList<Node>
     */
    public LinkedList<Node> find(Node startNode, Node endNode){

        PriorityQueue<ScoredNode> openList = new PriorityQueue<>();
        HashMap<String, ScoredNode> closedList = new HashMap<>();

        ScoredNode scoredStart = new ScoredNode(startNode, null, -1, -1, -1);
        ScoredNode scoredEnd = new ScoredNode(endNode, null, -1, -1, -1);

        NodesDBUtil ne = new NodesDBUtil();
        ne.retrieveNodes();
        ne.retrieveEdges();
        ArrayList<Node> nodes = new ArrayList<>(ne.getNodeList());
        ArrayList<Edge> edges = new ArrayList<>(ne.getEdgeList());

        LinkedList<Node> path = null;
        try {
            path  = stripScores(calcPath(scoredStart, scoredEnd, openList, closedList, nodes, edges));
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * Main pathfinding function. Finds endNode from startNode using A*.
     *
     * @param startNode
     * @param endNode
     * @param openList
     * @param closedList
     * @return
     */

    public LinkedList<ScoredNode> calcPath(ScoredNode startNode, ScoredNode endNode, PriorityQueue<ScoredNode> openList, HashMap<String, ScoredNode> closedList, ArrayList<Node> nodes, ArrayList<Edge> edges) throws PathNotFoundException {
        /* check for edgeless-node */
        if (startNode.getNode() == null) throw new PathNotFoundException();
        if (getEdges(startNode, edges) == null) throw new PathNotFoundException();

        if (startNode.getNode().equals(endNode.getNode())) return getNodeTrail(startNode);
        ArrayList<ScoredNode> children = expandNode(startNode, edges);

        for (ScoredNode child: children){
            child.setParent(startNode);
            scoreNode(child, endNode);
            if (!alreadySeen(child, openList)) {
                openList.add(child);
            }
        }

        closedList.put(startNode.getNode().getID(), startNode);
        ScoredNode nextNode = openList.poll(); // Equivalent of .pop()

        return calcPath(nextNode, endNode, openList, closedList, nodes, edges);
    }

    /**
     * Finds all the other nodes connected by edges to given node
     * @param sNode
     * @return children
     */
    ArrayList<ScoredNode> expandNode(ScoredNode sNode, ArrayList<Edge> edges){
        ArrayList<ScoredNode> children = new ArrayList<>();
        ArrayList<Edge> childEdges = getEdges(sNode, edges);
        for (Edge edge: childEdges){
            if(isValidNode(sNode)){
                // Finds the node on the other end of an edge and converts it to a ScoredNode before adding
                ScoredNode scoredChild = new ScoredNode(findOtherNode(edge, sNode), sNode, -1, -1, -1);
                children.add(scoredChild);
            }
        }
        return children;
    }

    /**
     * Takes in a ScoredNode and updates its g, h, and f scores
     *
     * @param node
     * @param endNode
     * @return
     */

    ScoredNode scoreNode(ScoredNode node, ScoredNode endNode){
        node.setgScore(calcGScore(node));
        if (node.gethScore() == -1) {
            node.sethScore(calcHScore(node, endNode));
        }
        node.setfScore(calcFScore(node));
        return node;
    }

    /**
     * Calculates the cost of getting to the given node from its earliest parent.
     *
     * @param node
     * @return Total cost
     */
    double calcGScore(ScoredNode node){
        LinkedList<ScoredNode> nodeTrail = getNodeTrail(node);
        double gScore = 0;
        for(ScoredNode n: nodeTrail){
            if(n.getParent() != null) {
                gScore += distanceBetweenNodes(n, n.getParent());
            }
        }
        return gScore;
    }

    /**
     * Calculate the H Score for the current node to the end Node
     * This is done using Euclidean Distance
     * @param curNode
     * @param endNode
     * @return
     */
    double calcHScore(ScoredNode curNode, ScoredNode endNode){
        return distanceBetweenNodes(curNode, endNode);
    }

    /**
     * Calculate the F score for the given Node.
     * This is done by adding the G and H scores for the Node. Must be called after calcGScore and calcHScore
     * @param node The node to be scored
     * @return The score of that node
     */
    double calcFScore(ScoredNode node) {
        return node.getgScore() + node.gethScore();
    }

    /**
     * Takes in a list of ScoredNodes and strips their scores, returning a list of the raw Nodes
     *
     * @param scoredNodes List of nodes to have their scores stripped
     * @return strippedNodes
     */

    LinkedList<Node> stripScores(LinkedList<ScoredNode> scoredNodes) throws PathNotFoundException{
        LinkedList<Node> strippedNodes = new LinkedList<>();
        if (scoredNodes == null) {
            throw new PathNotFoundException();
        }
        for(ScoredNode scoredNode: scoredNodes){
            strippedNodes.add(scoredNode.getNode());
        }
        return strippedNodes;
    }

    /**
     * Takes in a node and follows the path of parents, creating a LinkedList as it goes
     *
     * @param node
     * @return The trail of nodes leading back to the start node
     */

    LinkedList<ScoredNode> getNodeTrail(ScoredNode node){
        if (node.getNode() == null) {
            System.out.println("ERROR: No Edges for this node!");
            return null;
        }
        LinkedList<ScoredNode> nodeTrail = new LinkedList<>();
        while(!(node.getParent() == null)){
            nodeTrail.addFirst(node);
            node = node.getParent();
        }
        nodeTrail.addFirst(node); // Adding start node to trail. addFirst ensures correct order
        return nodeTrail;
    }

    /**
     * Calculates the Euclidean distance between given nodes
     *
     * @param node1
     * @param node2
     * @return The distance between nodes
     */

    double distanceBetweenNodes(ScoredNode node1, ScoredNode node2){
        int x1 = node1.getNode().getXCoord();
        int y1 = node1.getNode().getYCoord();

        int x2 = node2.getNode().getXCoord();
        int y2 = node2.getNode().getYCoord();

        double xDist = x2 - x1;
        double yDist = y2 - y1;

        return Math.hypot(xDist, yDist);
    }

    /**
     * Checks whether a node is already in the openList.
     *
     * @param node
     * @param openList
     * @return True: Node has already been seen and scored and added to openList.
     */

    boolean alreadySeen(ScoredNode node, PriorityQueue openList) {
        return openList.contains(node);
    }

    /**
     * finds other node in an edge
     *
     * @param sNode
     * @param edge
     * @return Other node of edge
     */

    Node findOtherNode(Edge edge, ScoredNode sNode) {
        if (sNode.getNode().equals(edge.getStartNode())) {
            return edge.getEndNode();
        }
        else if (sNode.getNode().equals(edge.getEndNode())) {
            return edge.getStartNode();
        }
        return null;
    }

    /**
     * Get all the edges that a node belongs to
     *
     * @param edges
     * @return list of node's edges
     */
    public ArrayList<Edge> getEdges(ScoredNode sNode, ArrayList<Edge> edges) {
        ArrayList<Edge> nodeEdges = new ArrayList<Edge>();
        for (Edge e: edges) {
            if (sNode.getNode().equals(e.getStartNode()) || sNode.getNode().equals(e.getEndNode())) {
                nodeEdges.add(e);
            }
        }
        return nodeEdges;
    }

    /**
     * Checks to see if A* is allowed to route through the given node
     * @param sNode
     * @return True if allowed to visit node, false if not
     */
    boolean isValidNode(ScoredNode sNode){
        return sNode.getNode().getStatus() == 1;
    };


}
