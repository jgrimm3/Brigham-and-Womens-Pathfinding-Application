package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;

import java.sql.*;
import java.util.*;

//   __   __          ___          ___
//  |  \ |__)    |  |  |  | |    |  |  \ /
//  |__/ |__)    \__/  |  | |___ |  |   |
//
//              __   __   ___  __
//     __ |\ | /  \ |  \ |__  /__`
//        | \| \__/ |__/ |___ .__/
//

//update CSV file from room, exit, hallway, transport nodes.
//finish erd diagram and create request table
class NodesDBUtil {

	int nodeIDGeneratorCount = 200;
	int elevatorCounter = 0;

	static void closeConnection(Connection connection) {
		try {
			if(connection != null) {
				connection.commit();
				connection.close();
			}
			else {
				System.out.println("Connection not established");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	List<Node> nodes = new ArrayList<>();

	/*---------------------------------------- Create java objects ---------------------------------------------------*/

	/**
	 * Creates a list of objects and stores them in the global variable nodeList
	 */
	List<Node> retrieveNodes() {
		// Connection
		nodes.clear();

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
		PreparedStatement stmt = null;
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:derby:nodesDB");
			String str = "SELECT * FROM MAP_NODES";
			stmt = connection.prepareStatement(str);
			ResultSet rset = stmt.executeQuery();

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
				node = buildNode(ID,xCoord, yCoord, floor, building, nodeType, longName, shortName, status, xCoord3D, yCoord3D);
				// Add the new node to the list
				nodes.add(node);
			}
			rset.close();
 			addAllEdges();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				closeConnection(connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("hllo");
		return nodes;
	} // retrieveNodes() ends

	 private void addAllEdges() {
		for(Node x: nodes) {
			List<String> nodeIDs = getAdjacentNodes(x);
			for(Node y: nodes) {
				if(nodeIDs.contains(y.getNodeID())) {
					if(!areNeighbors(x,y))
						x.addAdjacentNode(y);
					if(!areNeighbors(y,x))
						y.addAdjacentNode(x);
				}
			}
		}
	}

	private boolean areNeighbors(Node start, Node end) {
		for(Node x: start.getAdjacentNodes()) {
			if(x.getNodeID().equals(end.getNodeID())) {
				return true;
			}
		}
		return false;
	}

	/*---------------------------------------- Add/edit/delete nodes -------------------------------------------------*/

	/**
	 * Adds the java object and the corresponding entry in the database table
     * @param xCoord    xcoord
     * @param yCoord    ycoord
     * @param nodeType  node type
     * @param longName  long name of the node
     * @param shortName short name of the node
     * @param status
     * @param yCoord3D  yCoord3D
     * @param xCoord3D  xCoord3D
     */
	Node addNode(int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int status, int yCoord3D, int xCoord3D) {

		Node aNode = buildNode("", xCoord, yCoord, floor, building, nodeType, longName, shortName, status , xCoord3D, yCoord3D);

		Connection connection;
		connection = DataModelI.getInstance().getNewConnection();
		PreparedStatement statement = null;

		System.out.println("Node added to object list...");
		try {
			// Connect to the database
			System.out.println("Getting connection to database...");
			String str = "INSERT INTO map_nodes(nodeID,xCoord,yCoord,floor,building,nodeType,longName,shortName, status, xCoord3D, yCoord3D) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

			// Create the prepared statement
			statement = connection.prepareStatement(str);
			statement.setString(1, aNode.getNodeID());
			statement.setInt(2, aNode.getXCoord());
			statement.setInt(3, aNode.getYCoord());
			statement.setString(4, aNode.getFloor());
			statement.setString(5, aNode.getBuilding());
			statement.setString(6, aNode.getNodeType());
			statement.setString(7, aNode.getLongName());
			statement.setString(8, aNode.getShortName());
			statement.setInt(9,aNode.getStatus());
			statement.setInt(10, aNode.getXCoord3D());
			statement.setInt(11, aNode.getYCoord3D());
			System.out.println("Prepared statement created...");
			statement.executeUpdate();
			System.out.println("Node added to database");
		} catch (SQLException e) {
			System.out.println("Node already in the database");
		} finally {
			try {
				statement.close();
				closeConnection(connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return aNode;
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
			String str = "UPDATE map_nodes SET xCoord = ?,yCoord = ?,floor = ?,building = ?,nodeType = ?,longName = ?, shortName =?, status = ?, xCoord3D = ?, yCoord3D = ? WHERE nodeID = '" + node.getNodeID() +"'";

			// Create the prepared statement
			statement = connection.prepareStatement(str);
			statement.setInt(1, node.getXCoord());
			statement.setInt(2, node.getYCoord());
			statement.setString(3, node.getFloor());
			statement.setString(4, node.getBuilding());
			statement.setString(5, node.getNodeType());
			statement.setString(6, node.getLongName());
			statement.setString(7, node.getShortName());
            statement.setInt(8, node.getStatus());
			statement.setInt(9, node.getXCoord3D());
			statement.setInt(10, node.getYCoord3D());
			System.out.println("Prepared statement created...");
			statement.executeUpdate();
			System.out.println("Node added to database");
			isSucessful = true;
		} catch (SQLException e) {
			System.out.println("Node already in the database");
		} finally {
			try {
				statement.close();
				closeConnection(connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return isSucessful;
		}
	}

	boolean modifyNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int status, int xCoord3D, int yCoord3D) {
		boolean isSucessful = false;
		Connection connection = DataModelI.getInstance().getNewConnection();
		PreparedStatement statement = null;
		try {
			// Connect to the database
			System.out.println("Getting connection to database...");
			connection = DataModelI.getInstance().getNewConnection();
			String str = "UPDATE map_nodes SET xCoord = ?,yCoord = ?,floor = ?,building = ?,nodeType = ?,longName = ?, shortName =?, status = ?, xCoord3D = ?, yCoord3D = ? WHERE nodeID = '" + nodeID +"'";

			// Create the prepared statement
			statement = connection.prepareStatement(str);
			statement.setInt(1, xCoord);
			statement.setInt(2, yCoord);
			statement.setString(3, floor);
			statement.setString(4, building);
			statement.setString(5, nodeType);
			statement.setString(6, longName);
			statement.setString(7, shortName);
            statement.setInt(8, status);
			statement.setInt(9, xCoord3D);
			statement.setInt(10, yCoord3D);
			System.out.println("Prepared statement created...");
			statement.executeUpdate();
			System.out.println("Node added to database");
			isSucessful = true;
		} catch (SQLException e) {
			System.out.println("Node already in the database");
		} finally {
			try {
				statement.close();
				closeConnection(connection);
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
		nodes.remove(node);
		// Remove from the database
		Connection connection = DataModelI.getInstance().getNewConnection();
		try {
			// Get connection to database and delete the node from the database
			Statement stmt = connection.createStatement();
			String str = "DELETE FROM MAP_NODES WHERE nodeID = '" + node.getNodeID() + "'";
			stmt.executeUpdate(str);
			stmt.close();
			System.out.println("Node removed from database");
			isSucessful = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
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
			closeConnection(connection);
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
	void addEdge(Node startNode, Node endNode) {
		Connection connection = DataModelI.getInstance().getNewConnection();
		startNode.getAdjacentNodes().add(endNode);
		endNode.getAdjacentNodes().add(startNode);
		System.out.println("Node added to adjacent node...");
		try {
			// Connect to the database
			System.out.println("Getting connection to database...");
			String str = "INSERT INTO MAP_EDGES(edgeID, startNode, endNode, status) VALUES (?,?,?,?)";

			// Create the prepared statement
			PreparedStatement statement = connection.prepareStatement(str);
			statement.setString(1, startNode.getNodeID() + "_" + endNode.getNodeID());
			statement.setString(2, startNode.getNodeID());
			statement.setString(3, endNode.getNodeID());
			statement.setInt(4, 1);
			System.out.println("Prepared statement created...");
			statement.executeUpdate();
			statement.close();
			System.out.println("Node added to database");
		} catch (SQLException e) {
			System.out.println("Node already in the database");
		} finally {
			closeConnection(connection);
		}
	} // end addEdge()

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
				edgeSet.add(makeEdge(b_node.getNodeID(), a_node.getNodeID()));
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
			String str = "DELETE FROM MAP_EDGES WHERE edgeID = '" + startNode.getNodeID() + "_" + endNode.getNodeID() + "'";
			stmt.executeUpdate(str);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
		}
	} // removeEdge

	/*----------------------------------------- Helper functions -----------------------------------------------------*/

	List<String> getAdjacentNodes(Node node) {
		List<String> adjacentNodes = new ArrayList<>();
        // Connection
        Connection connection = null;

        // Variables
        String nodeID = "";
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
        try {
                connection = DriverManager.getConnection("jdbc:derby:nodesDB");
                stmt = connection.createStatement();
                String str = "SELECT * FROM MAP_EDGES WHERE startNodeID = '"+ node.getNodeID()+ "' OR endNodeID = '" + node.getNodeID() +"'";
                ResultSet rset = stmt.executeQuery(str);

                while(rset.next()) {
                    String newNodeID;
                    String startNodeID = rset.getString("startNodeID");
                    String endNodeID = rset.getString("endNodeID");

                    if (node.getNodeID().equals(startNodeID)) {
                        newNodeID = endNodeID;
                    } else if(node.getNodeID().equals(startNodeID)){
                        newNodeID = startNodeID;
                    } else
					{
						newNodeID = null;
					}

					adjacentNodes.add(newNodeID);
                }
                rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
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
		Connection connection = DataModelI.getInstance().getNewConnection();
		Edge edge;
		String edgeID;
		String startNodeID;
		String endNodeID;
		Statement stmt = null;

		try {
			stmt = connection.createStatement();
			String str = "SELECT * FROM MAP_EDGES WHERE STARTNODEID = '" + node.getNodeID() + "'" + "OR ENDNODEID = '" + node.getNodeID() + "'";
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				closeConnection(connection);
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
				if(!buildings.contains(building))
					buildings.add(building);
			}
			rset.close();
			stmt.close();
			System.out.println("Done adding buildings");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return buildings;
	}

	List<String> getTypesFromList() {
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
				if(!types.contains(type))
				    types.add(type);
			}
			rset.close();
			stmt.close();
			System.out.println("Done adding types");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return types;
	}

	List<Node> getNodesByBuildingTypeFloor (String building, String type, String floor) {
		List<Node> selectedNodes = new ArrayList<>();
		List<Node> allNodes = retrieveNodes();

		for(Node a_node : allNodes){
			if(a_node.getBuilding().equals(building) && a_node.getNodeType().equals(type) && a_node.getFloor().equals(floor)){
				selectedNodes.add(a_node);
			}
		}

		return selectedNodes;
	}

	List<String> getLongNameByBuildingTypeFloor (String building, String type, String floor) {
		List<String> selectedNames = new ArrayList<>();
		List<Node> allNodes = retrieveNodes();

		for(Node a_node : allNodes){
			if(a_node.getBuilding().equals(building) && a_node.getNodeType().equals(type) && a_node.getFloor().equals(floor)){
				selectedNames.add(a_node.getLongName());
			}
		}
		System.out.println("getLongNameByBuildingTypeFloor ends");
		return selectedNames;
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

	public List<Node> getNodesByBuilding(String building) {
		List<Node> selectedNodes = new ArrayList<>();
		List<Node> allNodes = retrieveNodes();

		for(Node a_node : allNodes){
			if(a_node.getBuilding().equals(building)){
				selectedNodes.add(a_node);
			}
		}
		return selectedNodes;
	}

    public boolean doesNodeExist(String nodeID) {
        List<Node> allNodes = retrieveNodes();
        for(Node a_node : allNodes){
            if(a_node.getNodeID().equals(nodeID)){ return true; }
        }
        return false;
    }


    public Node buildNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int status, int xCoord3D, int yCoord3D){
        Node aNode;

	    if (nodeID.equals("") || nodeID.isEmpty())
	    {nodeID = generateNodeID(nodeType, floor, "A"); }
        switch (nodeType){
            case "HALL":
                aNode = new Hallway(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName, status, yCoord3D, xCoord3D);
            case "ELEV":
                aNode = new Transport(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName, status, xCoord3D, yCoord3D);
            case "STAI":
                aNode = new Transport(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName, status, xCoord3D, yCoord3D);
            case "EXIT":
                aNode = new Exit(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName, status, yCoord3D, xCoord3D);
            default:
                aNode = new Room(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName, status, yCoord3D, xCoord3D);
        }

	    return aNode;
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
		Statement stmt = null;
		try {
            stmt = connection.createStatement();
            //String str = "SELECT * FROM MAP_NODES INNER JOIN MAP_EDGES ON"+ nodeID +" = MAP_EDGES.startNodeID OR "+ nodeID +"= MAP_EDGES.endNodeID";
            String str = "SELECT * FROM MAP_NODES WHERE nodeID = '" + nodeID + "'";
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

                node = buildNode(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName, status, xCoord3D, yCoord3D);
			}
			rset.close();
			System.out.println("Done adding nodes");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				closeConnection(connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return node;
	}

	public Node getNodeByIDFromList(String nodeID, List<Node> nodeList) {
		for(Node x: nodeList) {
			if (x.getNodeID().equals(nodeID)) {
				return x;
			}
		}
		return null;
	}

	Node getNodeByCoords(int xCoord, int yCoord) {
		// Connection
		Connection connection = DataModelI.getInstance().getNewConnection();

		// Variables
		Node node = null;
		String nodeID = "";
		String nodeType = "";
		String longName = "";
		String shortName = "";
		int xCoord3D = 0;
		int yCoord3D = 0;
		String floor = "";
		String building = "";
		int status = 0;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			String str = "SELECT * FROM MAP_NODES WHERE xCoord= " + xCoord + " yCoord = " +  yCoord;
			ResultSet rset = stmt.executeQuery(str);

			// For every node, get the information
			if (rset.next()) {
				nodeID = rset.getString("nodeID");
				nodeType = rset.getString("nodeType");
				floor = rset.getString("floor");
				building = rset.getString("building");
				longName = rset.getString("longName");
				shortName = rset.getString("shortName");
				status = rset.getInt("status");
				xCoord3D = rset.getInt("xCoord3D");
				yCoord3D = rset.getInt("yCoord3D");

				// Create the java objects based on the node type
                buildNode(nodeID,xCoord, yCoord, floor, building, nodeType, longName, shortName, status, xCoord3D, yCoord3D);

				// Add the new node to the list
				//node.setAdjacentNodes(getAdjacentNodes(node));
			}
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataModelI.getInstance().closeConnection();
		}
		return node;
	}

	Node getNodeByLongName(String longName){
		// Connection
		Connection connection = DataModelI.getInstance().getNewConnection();

		// Variables
		Node node = null;
		String nodeID = "";
		int xCoord = 0;
		int yCoord = 0;
		String nodeType = "";
		String shortName = "";
		int xCoord3D = 0;
		int yCoord3D = 0;
		String floor = "";
		String building = "";
		int status = 0;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			String str = "SELECT * FROM MAP_NODES WHERE longName= '" + longName + "'";
			ResultSet rset = stmt.executeQuery(str);

			// For every node, get the information
			if (rset.next()) {
				nodeID = rset.getString("nodeID");
				xCoord = rset.getInt("xCoord");
				yCoord = rset.getInt("yCoord");
				nodeType = rset.getString("nodeType");
				floor = rset.getString("floor");
				building = rset.getString("building");
				shortName = rset.getString("shortName");
				status = rset.getInt("status");
				xCoord3D = rset.getInt("xCoord3D");
				yCoord3D = rset.getInt("yCoord3D");

				// Create the java objects based on the node type
				node = buildNode(nodeID,xCoord, yCoord, floor, building, nodeType, longName, shortName, status, xCoord3D, yCoord3D);
				//node.setAdjacentNodes(getAdjacentNodes(node));
			}
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataModelI.getInstance().closeConnection();
		}
		return node;
	}

	Node getNodeByLongNameFromList(String longName, List<Node> nodeList) {
	    for(Node x : nodeList) {
	        if(x.getLongName().equals(longName)) {
	            return x;
            }
        }
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
                break;
            case "2":
                nodeID += "02";
                break;
            case "3":
                nodeID += "03";
                break;
            default:
                nodeID += floor;
                break;
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