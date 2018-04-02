package com.manlyminotaurs.nodes;

public class Edge {
	Node startNode;
	Node endNode;
	String edgeID;
	int status;

	public Edge(Node startNode, Node endNode, String edgeID) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.edgeID = edgeID;
		status = 1;
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

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        if (!(o instanceof Edge)) {
            return false;
        }
        Edge e = (Edge) o;
        return e.edgeID.equals(this.edgeID);
    }

}
