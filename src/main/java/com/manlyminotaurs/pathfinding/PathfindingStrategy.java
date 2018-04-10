//package com.manlyminotaurs.pathfinding;
//
//import com.manlyminotaurs.nodes.Node;
//
//import java.util.LinkedList;
//
//abstract class PathfindingStrategy {
//
//    /**
//     * Takes in a list of PathfindingNodes and strips their metadata, returning a list of the raw Nodes
//     * @param toStrip List of nodes to have their metadata stripped
//     * @return strippedNodes
//     */
//    LinkedList<Node> stripNodeWrappers(LinkedList<PathfindingNode> toStrip){
//        LinkedList<Node> strippedNodes = new LinkedList<>();
//        for(PathfindingNode p: toStrip){
//            strippedNodes.add(p.getNode());
//        }
//        return strippedNodes;
//    }
//
//    /**
//     * Takes in a PathfindingNode and follows the path of parents, creating a LinkedList as it goes
//     * @param pNode The end of the trail to be generated
//     * @return The trail of nodes leading back to the start node
//     */
//
//    LinkedList<PathfindingNode> getNodeTrail(PathfindingNode pNode){
//        LinkedList<PathfindingNode> nodeTrail = new LinkedList<>();
//        while(!(pNode.getParent() == null)){
//            nodeTrail.addFirst(pNode);
//            pNode = pNode.getParent();
//        }
//        nodeTrail.addFirst(pNode); // addFirst ensures correct order
//        return nodeTrail;
//    }
//
//    /**
//     * Checks whether a PathfindingNode has already been visited
//     *
//     * @param pNode The node to check
//     * @return True: node has already been seen
//     */
//
//    boolean alreadySeen(PathfindingNode pNode) {
//        return pNode.getVisitedStatus();
//    }
//
//}
