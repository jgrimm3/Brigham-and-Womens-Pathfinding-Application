package com.manlyminotaurs.nodes;

public class Edge {
	String startNode;
	String endNode;
	String edgeID;
	int status;

	public Edge(String startNode, String endNode, String edgeID) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.edgeID = edgeID;
		status = 1;
	}

	public String getStartNode() {
		return startNode;
	}

	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}

	public String getEndNode() {
		return endNode;
	}

	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}

	public String getEdgeID() {
		return edgeID;
	}

	public void setEdgeID(String edgeID) {
		this.edgeID = edgeID;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}
