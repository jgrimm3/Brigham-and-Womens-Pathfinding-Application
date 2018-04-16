package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Chris on 4/14/2018.
 */

public class CalcDistance {

    public CalcDistance(){}

    /**
     * Returns the distance from one node to its neighbor,
     * if the two nodes are on separate floors or if the two nodes do not have a connecting edge, 0 is returned
     * @param startNode Node to start the calc at
     * @param endNode Node to end the calc at
     * @return the distance from one node to the next, 0 if error
     */
    public static double calcDistance(Node startNode, Node endNode){
        if(startNode.getAdjacentNodes().contains(endNode)){
            double distance =  Math.sqrt(Math.pow((startNode.getXCoord() - endNode.getXCoord()),2) +
                    Math.pow((startNode.getYCoord() - endNode.getYCoord()),2));
            return metersToFeet(distance);
        }
        return 0;
    }

    /**
     * Returns the sum of all distances in the List
     * @param nodeList the list of nodes to calculate with
     * @return the sum of all calls to calcDistance with the list
     */
    public static double calcDistance(List<Node> nodeList){
        double sum = 0;
        Iterator<Node> laggingIterator = nodeList.iterator();
        Iterator<Node> leadingIterator = nodeList.iterator();
        leadingIterator.next();

        while(leadingIterator.hasNext()){
            sum += calcDistance(laggingIterator.next(), leadingIterator.next());
        }

        return metersToFeet(sum);
    }

    /**
     * converts meter distance to feet
     *
     * @param distance the distance in meters to convert
     * @return the input distance in feet units
     */
    private static double metersToFeet(double distance) {
        return distance*3.2808;
    }
}
