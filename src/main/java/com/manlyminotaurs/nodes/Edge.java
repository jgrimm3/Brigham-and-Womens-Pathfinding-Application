package com.manlyminotaurs.nodes;

import java.util.Objects;

public class Edge {
	Node startNode;
	Node endNode;
	String edgeID;
	int status;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Edge edge = (Edge) o;
		return Objects.equals(startNode, edge.startNode) &&
				Objects.equals(endNode, edge.endNode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(startNode, endNode);
	}

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
