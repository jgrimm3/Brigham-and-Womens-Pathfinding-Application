package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.ScoredNode;

public class PathfinderUtil {

    /**
     * Takes in two nodes and calculates the slope between them
     *
     * @param sNode1
     * @param sNode2
     * @return The slope between given nodes
     */
    public double calcSlope(ScoredNode sNode1, ScoredNode sNode2){
        double dx = (sNode2.getNode().getXCoord() - sNode1.getNode().getXCoord());
        double dy = (sNode2.getNode().getYCoord() - sNode1.getNode().getYCoord());
        return dy/dx;
    }

    /**
     *
     * @param sNode1 starting node
     * @param sNode2 ending node
     * @return Angle of difference of two given nodes
     */
    public double calcAngle(ScoredNode sNode1, ScoredNode sNode2){
//        double angle2 = Math.atan2(sNode2.getNode().getYCoord(), sNode2.getNode().getXCoord());
//        double angle1 = Math.atan2(sNode1.getNode().getYCoord(), sNode1.getNode().getXCoord());
//        double deg = (angle2-angle1) * 180/Math.PI;

        return deg;
    }


    /**
     *
     * @param sNode1
     * @param sNode2
     * @return String indicating direction of angle
     */
    public String angleToTurn(ScoredNode sNode1, ScoredNode sNode2){
        double angle = calcAngle(sNode1, sNode2);
        System.out.println("atan2 angle: " + angle);
        if(angle <= -5 && angle >= 5) {
            return "Straight";
        }
        else if (angle >= -175 && angle <= 175) {
            return "Backwards";
        } else if (angle > -5 && angle > 5) {
            return "Left";
        } else return "Right";
    }
}
