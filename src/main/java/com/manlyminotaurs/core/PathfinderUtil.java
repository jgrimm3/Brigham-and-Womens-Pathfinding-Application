package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.ScoredNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

public class PathfinderUtil {

    /**
     * calculates angle from 3 Nodes
     *
     * @param node1: Node 1
     * @param node2: Node 2
     * @param node3: Node 3
     * @return angle between node 1 and node 3
     */
        public double calcAngle(Node node1, Node node2, Node node3) {
            double n1x = node1.getXCoord();
            double n1y = node1.getYCoord();
            double n2x = node2.getXCoord();
            double n2y = node2.getYCoord();
            double n3x = node3.getXCoord();
            double n3y = node3.getYCoord();
            double sideA = Math.hypot(n3x-n2x, n3y-n2y);
            double sideB = Math.hypot(n3x-n1x, n3y-n1y);
            double sideC = Math.hypot(n2x-n1x, n2y-n1y);
            double angleEq = ((sideA*sideA)+(sideC*sideC)-(sideB*sideB))/(2*sideA*sideC);
            double angle = Math.acos(angleEq)*(180/Math.PI);
            System.out.println("Angle: " + (int)angle + " Degrees");
            return angle;
        }

    /**
     * associates direction to corresponding angle for each node in list
     *
     * @param path: List of Paths
     * @return List of Strings containing directions
     */
        /* TODO: Find which angles correspond to which direction, 45 degrees is left?
           TODO: angleToText should take in 'LinkedList<ScoredNode>' and traverse through
           TODO: Have this return a list of strings containing direction and node-long-name
         */

        public ArrayList<String> angleToText(LinkedList<Node> path) {
            ArrayList<String> tbt = new ArrayList<>();
            if (path.size() <= 2) { tbt.add("Straight"); return tbt; }
                for (int i = 0; i < path.size() - 2; i++) {
                System.out.println("Intersection: " + (i+1));
                double angle = calcAngle(path.get(i), path.get((i+1)), path.get((i+2)));
                if (angle >= 160 && angle <= 180) { tbt.add("Straight"); }
                else tbt.add("Not Straight");
            }
            return tbt;
        }

}

