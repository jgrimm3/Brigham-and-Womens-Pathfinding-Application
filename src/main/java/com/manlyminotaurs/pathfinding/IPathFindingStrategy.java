package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.util.*;

public interface IPathFindingStrategy {
    LinkedList<Node> find(Node startNode, Node endNode) throws PathNotFoundException;
}
