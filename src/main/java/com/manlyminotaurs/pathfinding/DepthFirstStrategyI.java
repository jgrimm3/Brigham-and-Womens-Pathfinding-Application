//package com.manlyminotaurs.pathfinding;
//
//import com.manlyminotaurs.nodes.Node;
//
//import java.nio.file.Path;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.PriorityQueue;
//
//public class DepthFirstStrategyI implements IPathFindingStrategy {
//
//    /**
//     * Wrapper for calcPath function. Creates empty data structures.
//     *
//     * @param startNode: starting node
//     * @param endNode:   ending nodes
//     * @return list of nodes traversed
//     */
//    public LinkedList<Node> find(Node startNode, Node endNode) throws PathNotFoundException{
//        DFSNode dfsStartNode = new DFSNode(startNode, null);
//        DFSNode dfsEndNode = new DFSNode(endNode, null);
//
//        LinkedList<Node> path = calcPath(dfsStartNode, dfsEndNode);
//        return path;
//    }
//
//    /**
//     * recursive depth first search pathfinding algorithm
//     *
//     * @param startNode: source
//     * @param endNode: destinatoin
//     * @return list to destination through breadth first search
//     * @throws PathNotFoundException
//     */
//    private LinkedList<Node> calcPath(DFSNode startNode, DFSNode endNode) throws PathNotFoundException {
//        // path to return for a when startNode = endNode
//        LinkedList<Node> path = new LinkedList<>();
//        // path to return
//        LinkedList<Node> newPath = new LinkedList<>();
//        // check for startNode equalling endNode
//        if (startNode.getNode().equals(endNode.getNode())) {
//            path.add(endNode.getNode());
//            return path;
//        }
//        // look through child nodes in starting node
//        for (DFSNode cnode : startNode.getAdjacentNodes()) {
//            if (!path.contains(cnode.getNode())) {
//                // recursive call
//                calcPath(cnode, endNode);
//                if (newPath.size() > 0) { return newPath; }
//            }
//        }
//        if (startNode.getAdjacentNodes().size() == 0) { throw new PathNotFoundException(); }
//        return null;
//    }
//}
