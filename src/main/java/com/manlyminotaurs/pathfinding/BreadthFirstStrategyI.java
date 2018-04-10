package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class BreadthFirstStrategyI implements IPathFindingStrategy {

    /*
    TODO:
    - Have BFS find a nearest type of node
    - Flesh out error handling
     */

    /**
     * Wrapper for calcPath function. Creates empty data structures.
     *
     * @param startNode: starting node
     * @param endNode:   ending nodes
     * @return list of nodes traversed
     */
    public LinkedList<Node> find(Node startNode, Node endNode) throws PathNotFoundException {
        BFSNode bfsStartNode = new BFSNode(startNode, null);
        BFSNode bfsEndNode = new BFSNode(endNode, null);

        LinkedList<Node> path = stripNodeWrappers(calcPath(bfsStartNode, bfsEndNode));
        return path;
    }

    /**
     * use breadth first search algorithm to find closest path to end node
     *
     * @param startNode: starting node
     * @param endNode:   destination node
     * @return Linked List of Node
     * @throws PathNotFoundException: for an invalid path
     */

    private LinkedList<Node> calcPat2(BFSNode startNode, BFSNode endNode) throws PathNotFoundException {
        //Initialization.
        Map<Node, Node> nextNodeMap = new HashMap<>();
        Node currentNode = startNode.getNode();

        //Queue
        Queue<Node> q = new LinkedList<>();
        q.add(currentNode);

        ArrayList<Node> visitedNodes = new ArrayList<>();
        visitedNodes.add(currentNode);

        //Search.
        while (!q.isEmpty()) {
            currentNode = q.remove();
            System.out.println(currentNode.getNodeID());
            if (currentNode.equals(endNode.getNode())) {
                break; // construct path
            } else {
                for (Node nextNode : currentNode.getAdjacentNodes()) {
                    for (int i = 0; i < currentNode.getAdjacentNodes().size(); i++) {
                        // check if node is already visited
                        if (!(visitedNodes.get(i).equals(nextNode))) {
                            q.add(nextNode);
                            visitedNodes.add(nextNode);

                            //Look up of next node instead of previous.
                            nextNodeMap.put(currentNode, nextNode);
                        }
                    }
                }
            }
        }


        //If all nodes are explored and the destination node hasn't been found.
        if (!currentNode.equals(endNode.getNode())) {
            throw new PathNotFoundException();
        }

        //Reconstruct path. No need to reverse.
        LinkedList<Node> directions = new LinkedList<>();
        for (Node node = startNode.getNode(); node != null; node = nextNodeMap.get(node)) {
            directions.add(node);
        }

        return directions;
    }

    private LinkedList<BFSNode> calcPath(BFSNode startNode, BFSNode endNode) throws PathNotFoundException {
        Queue<BFSNode> q = new LinkedList<>();
        BFSNode currentNode = startNode;
        q.add(currentNode);
        ArrayList<PathfindingNode> visitedNodes = new ArrayList<>();

        while(!q.isEmpty()) {
            currentNode = q.remove();
            currentNode.setVisitedStatus(true);
            visitedNodes.add(currentNode);
            if (!currentNode.getNode().equals(endNode.getNode())) {
                for (Node node: currentNode.getNode().getAdjacentNodes()) {
                    boolean wasVisited = false;
                    for(PathfindingNode bfs : visitedNodes){
                        if(bfs.getNode().equals(node)){
                            wasVisited = true;
                        }
                    }
                    if(!wasVisited){
                        q.add(new BFSNode(node, currentNode));
                    }else {
                        BFSNode nowVisisted = new BFSNode(node, currentNode);
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
     * Finds all the other nodes connected by edges to given node
     * @param aNode: node
     * @return children
     */
    private ArrayList<BFSNode> expandNode(BFSNode aNode) throws PathNotFoundException{
        ArrayList<BFSNode> children = new ArrayList<>();
        ArrayList<Node> childEdges;

        try {
            childEdges = getEdges(aNode);
        } catch (OrphanNodeException e){
            throw new PathNotFoundException();
        }

        for (Node ne: childEdges){
            if(isValidNode(aNode)){
                BFSNode scoredChild = new BFSNode(ne, aNode);
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

    LinkedList<BFSNode> getNodeTrail(BFSNode aNode){
        LinkedList<BFSNode> nodeTrail = new LinkedList<>();
        while(!(aNode.getParent() == null)){
            nodeTrail.addFirst(aNode);
            aNode = (BFSNode) aNode.getParent();
        }
        nodeTrail.addFirst(aNode); // addFirst ensures correct order
        return nodeTrail;
    }

    /**
     * Takes in a list of PathfindingNodes and strips their metadata, returning a list of the raw Nodes
     * @param toStrip List of nodes to have their metadata stripped
     * @return strippedNodes
     */
    LinkedList<Node> stripNodeWrappers(LinkedList<BFSNode> toStrip){
        LinkedList<Node> strippedNodes = new LinkedList<>();
        for(BFSNode a: toStrip){
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
    boolean isValidNode(BFSNode sNode){
        return sNode.getNode().getStatus() == 1;
    }

}
