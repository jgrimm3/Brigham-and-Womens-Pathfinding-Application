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
        public int calcAngle(Node node1, Node node2, Node node3) {
            double n1x = node1.getXCoord();
            double n1y = node1.getYCoord();
            double n2x = node2.getXCoord();
            double n2y = node2.getYCoord();
            double n3x = node3.getXCoord();
            double n3y = node3.getYCoord();
            double angle1 = Math.atan2(n1y-n2y, n1x-n2x);
            double angle2 = Math.atan2(n2y-n3y, n2x-n3x);
            double finalAngle = (angle2-angle1)*(180/Math.PI);
            System.out.println("Angle: " + finalAngle + " Degrees");
            return (int) finalAngle;

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

