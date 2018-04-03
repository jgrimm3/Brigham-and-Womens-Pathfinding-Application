package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.ScoredNode;

public class PathfinderUtil {

    /**
     * calculates angle from 3 ScoredNodes
     *
     * @param sNode1: Scored Node 1
     * @param sNode2: Scored Node 2
     * @param sNode3: Scored Node 3
     * @return angle of incidence between 3 nodes
     */
        public double calcAngle(ScoredNode sNode1, ScoredNode sNode2, ScoredNode sNode3) {
            double n1x = sNode1.getNode().getXCoord();
            double n1y = sNode1.getNode().getYCoord();
            double n2x = sNode2.getNode().getXCoord();
            double n2y = sNode2.getNode().getYCoord();
            double n3x = sNode3.getNode().getXCoord();
            double n3y = sNode3.getNode().getYCoord();
            double opposite = Math.hypot(n3x-n1x, n3y-n1y);
            double adjacent = Math.hypot(n3x-n2x, n3y-n2y);
            double angle = Math.atan(opposite/adjacent)*(180/Math.PI);
            System.out.println("Angle: " + angle);
            return angle;
        }

    /**
     * associates direction to corresponding angle
     *
     * @param sNode1: Scored Node 1
     * @param sNode2: Scored Node 2
     * @param sNode3: Scored Node 3
     * @return List of Strings containing directions
     */
        /* TODO: Find which angles correspond to which direction, 45 degrees is left?
           TODO: angleToText should take in 'LinkedList<Node> result = pf.find(node1, node1)' and traverse through
           TODO: Have this return a list of strings containing directions and
         */

        public String angleToText(ScoredNode sNode1, ScoredNode sNode2, ScoredNode sNode3) {
            double angle = calcAngle(sNode1, sNode2, sNode3);
            if (angle >= -5 && angle <= 5) { return "Straight"; }
            else return "not straight";
        }
}

