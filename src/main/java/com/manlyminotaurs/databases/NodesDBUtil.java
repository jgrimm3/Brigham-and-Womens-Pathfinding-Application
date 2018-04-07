package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.Patient;
import com.manlyminotaurs.users.User;
import javafx.collections.FXCollections;

import java.sql.*;
import java.util.*;


//update CSV file from room, exit, hallway, transport nodes.
//finish erd diagram and create request table
class NodesDBUtil {

	// Get a csv file controller
	DataModelI dataModelI = DataModelI.getInstance();
	int nodeIDGeneratorCount = 200;
	int elevatorCounter;

	/*---------------------------------------- Create java objects ---------------------------------------------------*/

	/**
	 * Creates a list of objects and stores them in the global variable nodeList
	 */
	List<Node> retrieveNodes() {
		// Connection
		Connection connection = dataModelI.getNewConnection();

		// Variables
		Node node = null;
		String ID = "";
		String nodeType = "";
		String longName = "";
		String shortName = "";
		int xCoord = 0;
		int yCoord = 0;
		int xCoord3D = 0;
		int yCoord3D = 0;
		String floor = "";
		String building = "";
		int status = 0;
		List<Node> listOfNodes = null;
		try {
			Statement stmt = connection.createStatement();
			String str = "SELECT * FROM MAP_NODES";
			ResultSet rset = stmt.executeQuery(str);

			// For every node, get the information
			while (rset.next()) {
				ID = rset.getString("nodeID");
				nodeType = rset.getString("nodeType");
				floor = rset.getString("floor");
				building = rset.getString("building");
				xCoord = rset.getInt("xCoord");
				yCoord = rset.getInt("yCoord");
				longName = rset.getString("longName");
				shortName = rset.getString("shortName");
				status = rset.getInt("status");
				xCoord3D = rset.getInt("xCoord3D");
				yCoord3D = rset.getInt("yCoord3D");

				// Create the java objects based on the node type
			}
			if (nodeType.equals("ELEV")) {
				node = new Transport(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
				//System.out.println("Elev created");
			} else if (nodeType.equals("EXIT")) {
				node = new Exit(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
				//System.out.println("Exit created");
			} else if (nodeType.equals("HALL")) {
				node = new Hallway(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
				//System.out.println("Hall created");
			} else if (nodeType.equals("STAI")) {
				node = new Transport(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
				//System.out.println("Stai created");
				// Add the new node to the list
				node.setStatus(status);
				node.setAdjacentNodes(getAdjacentNodesFromNode(node));
				listOfNodes.add(node);
				System.out.println("Node added to list...");
			}
			rset.close();
			stmt.close();
			System.out.println("Done adding nodes");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataModelI.closeConnection(connection);
			return listOfNodes;
		}
	} // retrieveNodes() ends




	/*---------------------------------------- Add/edit/delete nodes -------------------------------------------------*/

	/**
	 * Adds the java object and the corresponding entry in the database table
	 *
	 * @param longName  long name of the node
	 * @param shortName short name of the node
	 * @param nodeType  node type
	 * @param xCoord    xcoord
	 * @param yCoord    ycoord
	 * @param xCoord3D  xCoord3D
	 * @param yCoord3D  yCoord3D
	 */
	Node addNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int xCoord3D, int yCoord3D) {
		String ID = generateNodeID(nodeType, floor, "A");

		Node node;
		if (nodeType.equals("HALL")) {
			node = new Hallway(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
		} else if (nodeType.equals("ELEV")) {
			node = new Transport(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
		} else if (nodeType.equals("STAI")) {
			node = new Transport(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
		} else if (nodeType.equals("EXIT")) {
			node = new Exit(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
		} else {
			node = new Room(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
		}

		System.out.println("Node added to object list...");
		try {
			// Connect to the database
			System.out.println("Getting connection to database...");
			Connection connection;
			connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
			String str = "INSERT INTO map_nodes(nodeID,xCoord,yCoord,floor,building,nodeType,longName,shortName, xCoord3D, yCoord3D) VALUES (?,?,?,?,?,?,?,?,?,?)";

			// Create the prepared statement
			PreparedStatement statement = connection.prepareStatement(str);
			statement.setString(1, node.getID());
			statement.setInt(2, node.getXCoord());
			statement.setInt(3, node.getYCoord());
			statement.setString(4, node.getFloor());
			statement.setString(5, node.getBuilding());
			statement.setString(6, node.getNodeType());
			statement.setString(7, node.getLongName());
			statement.setString(8, node.getShortName());
			statement.setInt(9, node.getXCoord3D());
			statement.setInt(10, node.getXCoord3D());
			System.out.println("Prepared statement created...");
			statement.executeUpdate();
			System.out.println("Node added to database");
		} catch (SQLException e) {
			System.out.println("Node already in the database");
		}
		return node;
	} // end addNode()

	/**
	 * @param node
	 */
	boolean modifyNode(Node node) {
		boolean isSucessful = false;
		Connection connection = dataModelI.getNewConnection();
		try {
			// Connect to the database
			System.out.println("Getting connection to database...");
			connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
			String str = "UPDATE map_nodes SET nodeID = ?,xCoord = ?,yCoord = ?,floor = ?,building = ?,nodeType = ?,longName = ?, shortName =?, xCoord3D = ?, yCoord3D = ?";

			// Create the prepared statement
			PreparedStatement statement = connection.prepareStatement(str);
			statement.setString(1, node.getID());
			statement.setInt(2, node.getXCoord());
			statement.setInt(3, node.getYCoord());
			statement.setString(4, node.getFloor());
			statement.setString(5, node.getBuilding());
			statement.setString(6, node.getNodeType());
			statement.setString(7, node.getLongName());
			statement.setString(8, node.getShortName());
			statement.setInt(9, node.getXCoord3D());
			statement.setInt(10, node.getXCoord3D());
			System.out.println("Prepared statement created...");
			statement.executeUpdate();
			System.out.println("Node added to database");
			isSucessful = true;
		} catch (SQLException e) {
			System.out.println("Node already in the database");
		} finally {
			dataModelI.closeConnection(connection);
			return isSucessful;
		}
	}


	/**
	 * Removes a node from the list of objects as well as the database
	 *
	 * @param node
	 */
	boolean removeNode(Node node) {
		boolean isSucessful = false;

		// Remove from the database
		Connection connection = dataModelI.getNewConnection();
		try {
			// Get connection to database and delete the node from the database
			Statement stmt = connection.createStatement();
			String str = "DELETE FROM MAP_NODES WHERE nodeID = '" + node.getID() + "'";
			stmt.executeUpdate(str);
			stmt.close();
			connection.close();
			System.out.println("Node removed from database");
			isSucessful = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataModelI.closeConnection(connection);
		}
		return isSucessful;
	}

	/*---------------------------------------- Add/delete/edit "edges" -------------------------------------------------*/

	/**
	 * Adds the java object and the corresponding entry in the database table
	 *
	 * @param startNode the start node
	 * @param endNode   the end node
	 */
	void addAdjacentNode(Node startNode, Node endNode) {
		Connection connection = dataModelI.getNewConnection();
		startNode.getAdjacentNodes().add(endNode);
		endNode.getAdjacentNodes().add(startNode);
		System.out.println("Node added to adjacent node...");
		try {
			// Connect to the database
			System.out.println("Getting connection to database...");
			String str = "INSERT INTO MAP_EDGES(edgeID, startNode, endNode) VALUES (?,?,?)";

			// Create the prepared statement
			PreparedStatement statement = connection.prepareStatement(str);
			statement.setString(1, startNode.getID() + "_" + endNode.getID());
			statement.setString(2, startNode.getID());
			statement.setString(3, endNode.getID());
			System.out.println("Prepared statement created...");
			statement.executeUpdate();
			System.out.println("Node added to database");
		} catch (SQLException e) {
			System.out.println("Node already in the database");
		} finally {
			dataModelI.closeConnection(connection);
		}

	} // end addAdjacentNode()

	private Edge makeEdge(Node startNode, Node endNode){
		Edge edge = null;
		int caseInt = startNode.getID().compareTo(endNode.getID());
		if(caseInt < 0){
			String edgeID = startNode.getID() + "_" + endNode.getID();
			edge = new Edge(startNode, endNode, edgeID);
		}
		else if(caseInt == 0){
			System.out.println("you fucked up");
			return null;
		}
		else if(caseInt > 0){
			String edgeID = endNode.getID() + "_" + startNode.getID();
			edge = new Edge(endNode, startNode, edgeID);
		}
		return edge;
	}

	Set<Edge> getEdgeList(List<Node> listOfNodes){
		Set<Edge> edgeSet = new HashSet<Edge>();
		for(Node a_node : listOfNodes) {
			for(Node b_node : a_node.getAdjacentNodes()) {
				edgeSet.add(makeEdge(b_node, a_node));
			}
		}
		return edgeSet;
	}

	/**
	 * Removes the connection between nodes
	 *
	 * @param startNode start node
	 * @param endNode   end node
	 */
	void removeEdge(Node startNode, Node endNode) {
		// Find the node to remove from the edgeList
		startNode.getAdjacentNodes().remove(endNode);
		endNode.getAdjacentNodes().remove(startNode);
		Connection connection = dataModelI.getNewConnection();
		try {
			// Get connection to database and delete the edge from the database
			Statement stmt = connection.createStatement();
			String str = "DELETE FROM MAP_EDGES WHERE edgeID = '" + startNode.getID() + "_" + endNode.getID() + "'";
			stmt.executeUpdate(str);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataModelI.closeConnection(connection);
		}
	} // removeEdge

	/*----------------------------------------- Helper functions -----------------------------------------------------*/

	List<Node> getAdjacentNodesFromNode(Node node) {
		List<Edge> listOfEdges = getEdgesFromNode(node);
		List<Node> adjacentNodes = new ArrayList<>();
		Iterator<Edge> iterator = listOfEdges.iterator();
		while (iterator.hasNext()) {
			Edge a_edge = iterator.next();
			if (a_edge.getStartNode().getID() != node.getID()) {
				adjacentNodes.add(a_edge.getEndNode());
			} else {
				adjacentNodes.add(a_edge.getStartNode());
			}
		}
		return adjacentNodes;
	}


	/**
	 * find all adjacent edges from the node object using sql query
	 *
	 * @param node
	 * @return
	 */
	private List<Edge> getEdgesFromNode(Node node) {
		List<Edge> listOfEdges = new ArrayList<Edge>();
		Connection connection = dataModelI.getNewConnection();
		Edge edge;
		String edgeID;
		String startNodeID;
		String endNodeID;

		try {
			Statement stmt = connection.createStatement();
			String str = "SELECT * FROM MAP_EDGES WHERE STARTNODE = '" + node.getID() + "'" + "OR ENDNODE = '" + node.getID() + "'";
			ResultSet rset = stmt.executeQuery(str);

			// For every edge, get the information
			while (rset.next()) {
				edgeID = rset.getString("edgeID");
				startNodeID = rset.getString("startNode");
				endNodeID = rset.getString("endNode");

				// Add the new edge to the list
				Node startNodeObject = getNodesFromList(startNodeID);
				Node endNodeObject = getNodesFromList(endNodeID);
				if (startNodeID != null && endNodeID != null) {
					edge = new Edge(startNodeObject, endNodeObject, edgeID);
					listOfEdges.add(edge);
					System.out.println("Edge added to the list: " + edgeID);
				}
			}
			rset.close();
			stmt.close();
			System.out.println("Done adding edges");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataModelI.closeConnection(connection);
		}
		return listOfEdges;
	}

	List<String> getBuildingsFromList() {
		List<String> buildings = new ArrayList<>();

		return buildings;
	}

	/**
	 * return the node object that has the matching nodeID with the ID provided in the argument
	 * return null if it can't  find any
	 *
	 * @param nodeID
	 * @return
	 */
	Node getNodesFromList(String nodeID) {
		return null;
	}

	List<String> getTypesFromList(String building) {
		List<String> types = new ArrayList<>();

		return types;
	}

	List<Node> getNodesFromList(String building, String type) {
		List<Node> selectedNodes = new ArrayList<>();

		return null;
	}

	public List<Node> getNodesByType(String type) {
		List<Node> selectedNodes = new ArrayList<>();

		return selectedNodes;
	}

	public List<Node> getNodesByFloor(String floor) {
		List<Node> selectedNodes = new ArrayList<>();

		return selectedNodes;
	}

	public Node getNodesByID(String nodeID) {
		System.out.println("getNodesByID: returned null ");
		return null;
	}

	/**
	 * used to generate unique nodeID when adding a new node on the map
	 *
	 * @param nodeType
	 * @param floor
	 * @param elevatorLetter
	 * @return
	 */
	public String generateNodeID(String nodeType, String floor, String elevatorLetter) {
		String nodeID = "X";
		nodeID += nodeType;

		ArrayList<String> elevatorLetters = new ArrayList<String>();
		elevatorLetters.add("A");
		elevatorLetters.add("B");
		elevatorLetters.add("C");
		elevatorLetters.add("D");
		elevatorLetters.add("E");
		elevatorLetters.add("F");
		elevatorLetters.add("G");
		elevatorLetters.add("H");

		if (nodeType.equals("ELEV")) {
			if (elevatorLetter == null || elevatorLetter.equals("")) {
				System.out.println("elevator exception happened!!!!!");
				return "ERROR";
			} else {
				nodeID = nodeID + "00" + elevatorLetters.get(elevatorCounter);
				elevatorCounter++;
			}
		} else {
			nodeID += Integer.toString(nodeIDGeneratorCount);
			nodeIDGeneratorCount++;
		}

		if (floor.equals("1")) {
			nodeID += "01";
		} else if (floor.equals("2")) {
			nodeID += "02";
		} else if (floor.equals("3")) {
			nodeID += "03";
		} else {
			nodeID += floor;
		}
		return nodeID;
	}


} // end NodesDBUtil class