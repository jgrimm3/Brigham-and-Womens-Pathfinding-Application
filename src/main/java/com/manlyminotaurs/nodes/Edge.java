package com.manlyminotaurs.nodes;

import java.util.Objects;

public class Edge {

	String startNodeID;
	String endNodeID;
	String edgeID;
	int status;

	public String getStartNodeID() {
		return startNodeID;
	}

	public void setStartNodeID(String startNodeID) {
		this.startNodeID = startNodeID;
	}

	public String getEndNodeID() {
		return endNodeID;
	}

	public void setEndNodeID(String endNodeID) {
		this.endNodeID = endNodeID;
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

	public Edge(String startNodeID, String endNodeID, String edgeID) {
		this.startNodeID = startNodeID;
		this.endNodeID = endNodeID;
		this.edgeID = edgeID;
		this.status = 1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Edge edge = (Edge) o;
		return status == edge.status &&
				Objects.equals(startNodeID, edge.startNodeID) &&
				Objects.equals(endNodeID, edge.endNodeID) &&
				Objects.equals(edgeID, edge.edgeID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(startNodeID, endNodeID, edgeID, status);
	}
}
