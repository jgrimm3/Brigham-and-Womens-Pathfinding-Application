package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.util.*;

/* TODO:
    - Add floor switch functionality. calcPath(toElev) + calcPath(toDest).
 */

public class AStarStrategyI implements IPathFindingStrategy {
    private PriorityQueue<AStarNode> openList;
    private HashSet<AStarNode> closedList;
    public AStarStrategyI() {
        this.openList = new PriorityQueue<>();
        this.closedList = new HashSet<>();
    }
    /**
     * Wrapper for calcPath function. Creates empty data structures.
     *
     * @param startNode: starting node
     * @param endNode: ending nodes
     * @return list of nodes traversed
     */
    public LinkedList<Node> find(Node startNode, Node endNode) throws PathNotFoundException{
        AStarNode AStarStart = new AStarNode(startNode, null);
        AStarNode AStarEnd = new AStarNode(endNode, null);
        LinkedList<Node> path  = stripNodeWrappers(calcPath(AStarStart, AStarEnd));
        return path;
    }
    /**
     * Main pathfinding function. Finds endNode from startNode using A*.
     *
     * @param startNode: startNode
     * @param endNode: endNode
     * @return: list of scored nodes visited
     */
    public LinkedList<AStarNode> calcPath(AStarNode startNode, AStarNode endNode) throws PathNotFoundException {
        /* check for edgeless-node */
//        if (startNode.getNode() == null) throw new PathNotFoundException();
//        if (getEdges(startNode) == null) throw new PathNotFoundException();
        if (startNode.getNode().equals(endNode.getNode())) return getNodeTrail(startNode);
        ArrayList<AStarNode> children = expandNode(startNode);
        for (AStarNode child: children){
            child.setParent(startNode);
            scoreNode(child, endNode);
            if(!openList.contains(child) && !closedList.contains(child)) {
                openList.add(child);
            }
        }
        closedList.add(startNode);
        if(openList.size() == 0) { throw new PathNotFoundException("Checked all accessible nodes");}
        AStarNode nextNode = openList.poll(); // Equivalent of .pop()
        return calcPath(nextNode, endNode);
    }
    /**
     * Finds all the other nodes connected by edges to given node
     * @param aNode: node
     * @return children
     */
    private ArrayList<AStarNode> expandNode(AStarNode aNode) throws PathNotFoundException{
        ArrayList<AStarNode> children = new ArrayList<>();
        ArrayList<Node> childEdges;
        try {
            childEdges = getEdges(aNode);
        } catch (OrphanNodeException e){
            throw new PathNotFoundException(e.getMessage());
        }
        for (Node ne: childEdges){
            if(isValidNode(aNode)){
                AStarNode scoredChild = new AStarNode(ne, aNode);
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
     * Takes in a ScoredNode and updates its g, h, and f scores
     *
     * @param node: start node
     * @param endNode: end node
     * @return: scored node
     */
    private PathfindingNode scoreNode(AStarNode node, AStarNode endNode){
//        AStarNode anode = (AStarNode) node;
//        endNode = (AStarNode) endNode;
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
     * @param aNode
     * @return Total cost
     */
    private double calcGScore(AStarNode aNode){
        LinkedList<AStarNode> nodeTrail = getNodeTrail(aNode);
        double gScore = 0;
        for(AStarNode a: nodeTrail){
            if(a.getParent() != null) {
                gScore += distanceBetweenNodes(a, (AStarNode) a.getParent());
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
    private double calcHScore(AStarNode curNode, AStarNode endNode){
        return distanceBetweenNodes(curNode, endNode);
    }
    /**
     * Calculate the F score for the given Node.
     * This is done by adding the G and H scores for the Node. Must be called after calcGScore and calcHScore
     * @param node The node to be scored
     * @return The score of that node
     */
    private double calcFScore(AStarNode node) {
        return node.getgScore() + node.gethScore();
    }
    /**
     * Calculates the Euclidean distance between given nodes
     *
     * @param node1: starting node
     * @param node2: ending node
     * @return The distance between nodes
     */
    private double distanceBetweenNodes(AStarNode node1, AStarNode node2){
        int x1 = node1.getNode().getXCoord();
        int y1 = node1.getNode().getYCoord();
        int x2 = node2.getNode().getXCoord();
        int y2 = node2.getNode().getYCoord();
        double xDist = x2 - x1;
        double yDist = y2 - y1;
        return Math.hypot(xDist, yDist);
    }
    /**
     * Checks to see if A* is allowed to route through the given node
     * @param sNode: node
     * @return True if allowed to visit node, false if not
     */
    boolean isValidNode(AStarNode sNode){
        return sNode.getNode().getStatus() == 1;
    }
    /**
     * Takes in a list of PathfindingNodes and strips their metadata, returning a list of the raw Nodes
     * @param toStrip List of nodes to have their metadata stripped
     * @return strippedNodes
     */
    LinkedList<Node> stripNodeWrappers(LinkedList<AStarNode> toStrip){
        LinkedList<Node> strippedNodes = new LinkedList<>();
        for(AStarNode a: toStrip){
            strippedNodes.add(a.getNode());
        }
        return strippedNodes;
    }
    /**
     * Takes in a PathfindingNode and follows the path of parents, creating a LinkedList as it goes
     * @param aNode The end of the trail to be generated
     * @return The trail of nodes leading back to the start node
     */
    LinkedList<AStarNode> getNodeTrail(AStarNode aNode){
        LinkedList<AStarNode> nodeTrail = new LinkedList<>();
        while(!(aNode.getParent() == null)){
            nodeTrail.addFirst(aNode);
            aNode = (AStarNode) aNode.getParent();
        }
        nodeTrail.addFirst(aNode); // addFirst ensures correct order
        return nodeTrail;
    }
    /**
     * Checks whether a PathfindingNode has already been visited
     *
     * @param aNode The node to check
     * @return True: node has already been seen
     */
    boolean alreadySeen(AStarNode aNode) {
        return aNode.getVisitedStatus();
    }

}
