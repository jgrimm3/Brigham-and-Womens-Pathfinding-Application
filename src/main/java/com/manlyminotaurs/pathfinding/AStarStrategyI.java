package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.util.*;

/* TODO:
    - Add floor switch functionality. calcPath(toElev) + calcPath(toDest).
 */

public class AStarStrategyI extends DistancePathfinder implements IPathFindingStrategy {
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

        return stripNodeWrappers(calcPath(AStarStart, AStarEnd));
    }

    /**
     * Main pathfinding function. Finds endNode from startNode using A*.
     *
     * @param startNode: startNode
     * @param endNode: endNode
     * @return: list of scored nodes visited
     */

    private LinkedList<PathfindingNode> calcPath(AStarNode startNode, AStarNode endNode) throws PathNotFoundException {
        /* check for edgeless-node */
//        if (startNode.getNode() == null) throw new PathNotFoundException();
//        if (getEdges(startNode) == null) throw new PathNotFoundException();

        if (startNode.getNode().equals(endNode.getNode())) return getNodeTrail(startNode);
        ArrayList<PathfindingNode> children = expandNode(startNode);

        for (PathfindingNode child: children){
            AStarNode aChild = new AStarNode(child.getNode(), startNode);
            child.setParent(startNode);
            scoreNode(aChild, endNode);
            if(!openList.contains(child) && !closedList.contains(child)) {
                openList.add(aChild);
            }
        }

        closedList.add(startNode);
        if(openList.size() == 0) { throw new PathNotFoundException("Checked all accessible nodes");}
        AStarNode nextNode = openList.poll(); // Equivalent of .pop()

        return calcPath(nextNode, endNode);
    }
    /**
     * Takes in a ScoredNode and updates its g, h, and f scores
     *
     * @param node: start node
     * @param endNode: end node
     * @return: scored node
     */

    public PathfindingNode scoreNode(AStarNode node, AStarNode endNode){
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
        LinkedList<PathfindingNode> nodeTrail = getNodeTrail(aNode);
        double gScore = 0;
        for(PathfindingNode a: nodeTrail){
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

}
