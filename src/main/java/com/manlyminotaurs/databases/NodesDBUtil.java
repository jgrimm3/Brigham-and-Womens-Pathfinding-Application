package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;

import java.sql.*;
import java.util.*;


//update CSV file from room, exit, hallway, transport nodes.
//finish erd diagram and create request table
class NodesDBUtil {

	int nodeIDGeneratorCount = 200;
	int elevatorCounter = 0;

	/*---------------------------------------- Create java objects ---------------------------------------------------*/

	/**
	 * Creates a list of objects and stores them in the global variable nodeList
	 */
	List<Node> retrieveNodes() {
		// Connection

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
		Statement stmt = null;
		ResultSet rset = null;

		List<Node> listOfNodes = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection("jdbc:derby:/Users/andrew/Documents/Soft Eng Rep/CS3733_TeamM_Iter2/nodesDB;create=true");
			stmt = connection.createStatement();
			String str = "SELECT * FROM MAP_NODES";
			rset = stmt.executeQuery(str);

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
				} else {
					node = new Room(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
				}
				// Add the new node to the list
				node.setStatus(status);
				node.setAdjacentNodes(getAdjacentNodes(node));
                listOfNodes.add(node);
			}
			System.out.println("Done adding nodes");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//DataModelI.getInstance().closeConnection(connection);
			try {
				rset.close();
				stmt.close();
			} catch (SQLException e) {
                e.printStackTrace();
            }
		}
        return listOfNodes;
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
		switch (nodeType){
            case "Hall":
                node = new Hallway(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
            case "ELEV":
                node = new Transport(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
            case "STAI":
                node = new Transport(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
            case "EXIT":
                node = new Exit(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
            default:
                node = new Room(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
            }


		Connection connection;
		connection = DataModelI.getInstance().getNewConnection();
		PreparedStatement statement = null;

		System.out.println("Node added to object list...");
		try {
			// Connect to the database
			System.out.println("Getting connection to database...");
			String str = "INSERT INTO map_nodes(nodeID,xCoord,yCoord,floor,building,nodeType,longName,shortName, xCoord3D, yCoord3D) VALUES (?,?,?,?,?,?,?,?,?,?)";

			// Create the prepared statement
			statement = connection.prepareStatement(str);
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
		} finally {
			DataModelI.getInstance().closeConnection(connection);
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return node;
	} // end addNode()

	/**
	 * @param node
	 */
	boolean modifyNode(Node node) {
		boolean isSucessful = false;
		Connection connection = DataModelI.getInstance().getNewConnection();
		PreparedStatement statement = null;
		try {
			// Connect to the database
			System.out.println("Getting connection to database...");
			connection = DataModelI.getInstance().getNewConnection();
			String str = "UPDATE map_nodes SET xCoord = ?,yCoord = ?,floor = ?,building = ?,nodeType = ?,longName = ?, shortName =?, xCoord3D = ?, yCoord3D = ? WHERE nodeID = '" + node.getID() +"'";

			// Create the prepared statement
			statement = connection.prepareStatement(str);
			statement.setInt(1, node.getXCoord());
			statement.setInt(2, node.getYCoord());
			statement.setString(3, node.getFloor());
			statement.setString(4, node.getBuilding());
			statement.setString(5, node.getNodeType());
			statement.setString(6, node.getLongName());
			statement.setString(7, node.getShortName());
			statement.setInt(8, node.getXCoord3D());
			statement.setInt(9, node.getXCoord3D());
			System.out.println("Prepared statement created...");
			statement.executeUpdate();
			System.out.println("Node added to database");
			isSucessful = true;
		} catch (SQLException e) {
			System.out.println("Node already in the database");
		} finally {
			DataModelI.getInstance().closeConnection(connection);
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
		Connection connection = DataModelI.getInstance().getNewConnection();
		try {
			// Get connection to database and delete the node from the database
			Statement stmt = connection.createStatement();
			String str = "DELETE FROM MAP_NODES WHERE nodeID = '" + node.getID() + "'";
			stmt.executeUpdate(str);
			stmt.close();
			System.out.println("Node removed from database");
			isSucessful = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataModelI.getInstance().closeConnection(connection);
		}
		return isSucessful;
	}

	/**
	 * Removes a node by id
	 * @param nodeID ID of the node to be removed
	 * @return true if successful, false otherwise
	 */
	boolean removeNodeByID(String nodeID) {
		boolean isSucessful = false;

		// Remove from the database
		Connection connection = DataModelI.getInstance().getNewConnection();
		try {
			// Get connection to database and delete the node from the database
			Statement stmt = connection.createStatement();
			String str = "DELETE FROM MAP_NODES WHERE nodeID = '" + nodeID + "'";
			stmt.executeUpdate(str);
			connection.commit();
			stmt.close();
			System.out.println("Node removed from database");
			isSucessful = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataModelI.getInstance().closeConnection(connection);
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
		Connection connection = DataModelI.getInstance().getNewConnection();
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
			statement.close();
			System.out.println("Node added to database");
		} catch (SQLException e) {
			System.out.println("Node already in the database");
		} finally {
			DataModelI.getInstance().closeConnection(connection);
		}
	} // end addAdjacentNode()

	/**
	 * Makes an "edge" between nodes
	 * @param startNodeID
	 * @param endNodeID
	 * @return
	 */
	private Edge makeEdge(String startNodeID, String endNodeID){
		Edge edge = null;
		int caseInt = startNodeID.compareTo(endNodeID);
		if(caseInt < 0){
			String edgeID = startNodeID + "_" + endNodeID;
			edge = new Edge(startNodeID, endNodeID, edgeID);
		}
		else if(caseInt == 0){
			System.out.println("you messed up");
			return null;
		}
		else if(caseInt > 0){
			String edgeID = endNodeID + "_" + startNodeID;
			edge = new Edge(endNodeID, startNodeID, edgeID);
		}
		return edge;
	}

	Set<Edge> getEdgeList(List<Node> listOfNodes){
		Set<Edge> edgeSet = new HashSet<Edge>();
		for(Node a_node : listOfNodes) {
			for(Node b_node : a_node.getAdjacentNodes()) {
				edgeSet.add(makeEdge(b_node.getID(), a_node.getID()));
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
		Connection connection = DataModelI.getInstance().getNewConnection();
		try {
			// Get connection to database and delete the edge from the database
			Statement stmt = connection.createStatement();
			String str = "DELETE FROM MAP_EDGES WHERE edgeID = '" + startNode.getID() + "_" + endNode.getID() + "'";
			stmt.executeUpdate(str);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataModelI.getInstance().closeConnection(connection);
		}
	} // removeEdge

	/*----------------------------------------- Helper functions -----------------------------------------------------*/

	List<Node> getAdjacentNodes(Node node) {
		List<Edge> listOfEdges = getEdgesFromNode(node);
		List<Node> adjacentNodes = new ArrayList<>();
        for ( Edge anEdge:listOfEdges) {
            if (anEdge.getStartNodeID().equals(node.getID())) {
                adjacentNodes.add(getNodeByID(anEdge.getEndNodeID()));
            }
            else{ adjacentNodes.add(getNodeByID(anEdge.getStartNodeID())); }
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
		Connection connection = DataModelI.getInstance().getNewConnection();
		Edge edge;
		String edgeID;
		String startNodeID;
		String endNodeID;
		Statement stmt = null;

		try {
			stmt = connection.createStatement();
			String str = "SELECT * FROM MAP_EDGES WHERE STARTNODEID = '" + node.getID() + "'" + "OR ENDNODEID = '" + node.getID() + "'";
			ResultSet rset = stmt.executeQuery(str);

			// For every edge, get the information
			while (rset.next()) {
				edgeID = rset.getString("edgeID");
				startNodeID = rset.getString("startNodeID");
				endNodeID = rset.getString("endNodeID");

				// Add the new edge to the list
				edge = new Edge(startNodeID, endNodeID, edgeID);
				listOfEdges.add(edge);
				System.out.println("Edge added to the list: " + edgeID);
				}
			rset.close();
			System.out.println("Done adding edges");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataModelI.getInstance().closeConnection(connection);
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOfEdges;
	}

	List<String> getBuildingsFromList() {
		List<String> buildings = new ArrayList<>();
		String building;

		Connection connection = DataModelI.getInstance().getNewConnection();
		try {
			Statement stmt = connection.createStatement();
			String str = "SELECT building FROM MAP_NODES";
			ResultSet rset = stmt.executeQuery(str);

			// For every node, get the information
			while (rset.next()) {
				building = rset.getString("building");
				buildings.add(building);
			}
			rset.close();
			stmt.close();
			System.out.println("Done adding buildings");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataModelI.getInstance().closeConnection(connection);
		}
		return buildings;
	}

	List<String> getTypesFromList(String building) {
		List<String> types = new ArrayList<>();
		String type;

		Connection connection = DataModelI.getInstance().getNewConnection();
		try {
			Statement stmt = connection.createStatement();
			String str = "SELECT nodeType FROM MAP_NODES";
			ResultSet rset = stmt.executeQuery(str);

			// For every node, get the information
			while (rset.next()) {
				type = rset.getString("nodeType");
				types.add(type);
			}
			rset.close();
			stmt.close();
			System.out.println("Done adding types");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataModelI.getInstance().closeConnection(connection);
		}
		return types;
	}

	List<Node> getNodesFromList(String building, String type) {
		List<Node> selectedNodes = new ArrayList<>();
		List<Node> allNodes = retrieveNodes();

		for(Node a_node : allNodes){
			if(a_node.getBuilding().equals(building) && a_node.getNodeType().equals(type)){
				selectedNodes.add(a_node);
			}
		}

		return selectedNodes;
	}

	public List<Node> getNodesByType(String type) {
		List<Node> selectedNodes = new ArrayList<>();
		List<Node> allNodes = retrieveNodes();

		for(Node a_node : allNodes){
			if(a_node.getNodeType().equals(type)){
				selectedNodes.add(a_node);
			}
		}
		return selectedNodes;
	}

	public boolean doesNodeExist(String nodeID) {
		List<Node> allNodes = retrieveNodes();
        boolean isReal = false;
		for(Node a_node : allNodes){
			if(a_node.getID().equals(nodeID)){ isReal = true; }
		}
		return isReal;
	}

	public List<Node> getNodesByFloor(String floor) {
		List<Node> selectedNodes = new ArrayList<>();
		List<Node> allNodes = retrieveNodes();

		for(Node a_node : allNodes){
			if(a_node.getFloor().equals(floor)){
				selectedNodes.add(a_node);
			}
		}
		return selectedNodes;
	}

	/**
	 * return the node object that has the matching nodeID with the ID provided in the argument
	 * return null if it can't  find any
	 * @param nodeID
	 * @return
	 */
	public Node getNodeByID(String nodeID) {
		// Connection
		Connection connection = DataModelI.getInstance().getNewConnection();

		// Variables
		Node node = null;
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
		List<Node> listOfNodes = new ArrayList<>();
		Statement stmt = null;
		try {
            stmt = connection.createStatement();
            String str = "SELECT * FROM MAP_NODES INNER JOIN MAP_EDGES ON MAP_NODES.nodeID = MAP_EDGES.startNodeID OR MAP_NODES.nodeID = MAP_EDGES.endNodeID";
            ResultSet rset = stmt.executeQuery(str);

			// For every node, get the information
			if (rset.next()) {
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
				switch (nodeType) {
                    case "ELEV":
                        node = new Transport(longName, shortName, nodeID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
                        //System.out.println("Elevator created");
                    case "EXIT":
                        node = new Exit(longName, shortName, nodeID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
                        //System.out.println("Exit created");
                    case "HALL":
                        node = new Hallway(longName, shortName, nodeID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
                        //System.out.println("Hall created");
                    case "STAI":
                        node = new Transport(longName, shortName, nodeID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
                        //System.out.println("Stair created");
                    default:
                        node = new Room(longName, shortName, nodeID, nodeType, xCoord, yCoord, floor, building, xCoord3D, yCoord3D);
                }


				// Add the new node to the list
				node.setStatus(status);
				node.setAdjacentNodes(null);
				listOfNodes.add(node);
			}
            node.setAdjacentNodes(listOfNodes);
			rset.close();
			System.out.println("Done adding nodes");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataModelI.getInstance().closeConnection(connection);
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return node;
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
		String nodeID = "X" + nodeType;

		ArrayList<String> elevatorLetters = new ArrayList<>();
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

		switch (floor){
            case "1":
                nodeID += "01";
            case "2":
                nodeID += "02";
            case "3":
                nodeID += "03";
            default:
                nodeID += floor;
        }
		return nodeID;
	}

	boolean hasEdge(Node startNode, Node endNode) {
		boolean isSuccessful = false;
		if(startNode.getAdjacentNodes().contains(endNode)) {
			assert (endNode.getAdjacentNodes().contains(startNode));
			isSuccessful = true;
		} else{assert(!endNode.getAdjacentNodes().contains(startNode));}
		return isSuccessful;
	}

} // end NodesDBUtil class