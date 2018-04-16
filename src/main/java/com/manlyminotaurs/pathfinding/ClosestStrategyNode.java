package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.util.LinkedList;
import java.util.List;


public class ClosestStrategyNode extends PathfindingNode implements Comparable<ClosestStrategyNode> {
private double distance;
private List<ClosestStrategyNode> shortestPath = new LinkedList<>();


public ClosestStrategyNode(Node node, ClosestStrategyNode parent) {
        super(node, parent);
        this.distance = Integer.MAX_VALUE;
        }


    public double getDistance() { return distance; }

    public void setDistance(double distance) { this.distance = distance; }

    /**
 * Compares AStarNodes based on f score
 * @param other The other AStarNode to be compared
 * @return &lt;0 if other is greater, &gt;0 if this is greater, 0 if they are equal
 */
@Override
public int compareTo(ClosestStrategyNode other){
        return Double.compare(this.distance, other.distance);
        }

@Override
public boolean equals(Object other){
        ClosestStrategyNode n = (ClosestStrategyNode) other;
        return this.getNode().getNodeID().equals(n.getNode().getNodeID());
        }

    public List<ClosestStrategyNode> getShortestPath() { return shortestPath; }

    public void setShortestPath(List<ClosestStrategyNode> shortestPath) { this.shortestPath = shortestPath; }

    @Override
public int hashCode(){
        return this.getNode().getNodeID().hashCode();
        }
}