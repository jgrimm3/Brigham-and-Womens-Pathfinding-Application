//package com.manlyminotaurs.pathfinding;
//
//import com.manlyminotaurs.nodes.Node;
//
//import java.util.*;
//
//public class BreadthFirstStrategyI implements IPathFindingStrategy {
//
//
//    /*
//    TODO:
//    - Have BFS find a nearest type of node
//    - Flesh out error handling
//     */
//
//    /**
//     * Wrapper for calcPath function. Creates empty data structures.
//     *
//     * @param startNode: starting node
//     * @param endNode: ending nodes
//     * @return list of nodes traversed
//     */
//    public LinkedList<Node> find(Node startNode, Node endNode) throws PathNotFoundException{
//        BFSNode bfsStartNode = new BFSNode(startNode, null);
//        BFSNode bfsEndNode = new BFSNode(endNode, null);
//
//        LinkedList<Node> path  = calcPath(bfsStartNode, bfsEndNode);
//        return path;
//    }
//
//    /**
//     * use breadth first search algorithm to find closest path to end node
//     *
//     * @param startNode: starting node
//     * @param endNode:   destination node
//     * @return Linked List of Node
//     * @throws PathNotFoundException: for an invalid path
//     */
//    public LinkedList<Node> calcPath(BFSNode startNode, BFSNode endNode) throws PathNotFoundException {
//        // path to return
//        LinkedList<Node> path = new LinkedList<Node>();
//        // make a list of visited nodes
//        LinkedList<BFSNode> closedList = new LinkedList<>();
//        // make a list of nodes to visit
//        LinkedList<BFSNode> openList = new LinkedList<>();
//
//        // add start to open list to initiate calcPath
//        openList.add(startNode);
//        startNode.setParent(null);
//
//        while (!openList.isEmpty()) {
//            // update queue
//            BFSNode currNode = openList.removeFirst();
//            // create path if found end node
//            if (currNode.getNode().equals(endNode.getNode())) {
//                // construct a path for this node
//                while (endNode.getParent() != null) {
//                    path.addFirst(endNode.getNode());
//                    endNode = (BFSNode) endNode.getParent();
//                }
//                return path;
//            }
//            else {
//                closedList.add(currNode);
//                // use iterator to look through adjacent nodes
//                Iterator i = currNode.getNode().getAdjacentNodes().iterator();
//                while (i.hasNext()) {
//                    BFSNode neighborNode = (BFSNode) i.next();
//                    if (!closedList.contains(neighborNode) &&
//                            !openList.contains(neighborNode))
//                    {
//                        neighborNode = currNode;
//                        openList.add(neighborNode);
//                    }
//                }
//            }
//        }
//
//       if (startNode.getAdjacentNodes().size() == 0) { throw new PathNotFoundException(); }
//       return null;
//    }
//}
