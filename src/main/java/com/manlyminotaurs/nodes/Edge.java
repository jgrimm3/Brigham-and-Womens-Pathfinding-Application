package com.manlyminotaurs.nodes;

public class Edge {
	Node startNode;
	Node endNode;
	String edgeID;
	int status;

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public String getEdgeID() {
		return edgeID;
	}

	public void setEdgeID(String edgeID) {
		this.edgeID = edgeID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Edge(Node startNode, Node endNode, String edgeID) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.edgeID = edgeID;
	}

	public boolean isType(String type) {
		if(type == "EDGE") { return true; }
		return false;
	}
}
