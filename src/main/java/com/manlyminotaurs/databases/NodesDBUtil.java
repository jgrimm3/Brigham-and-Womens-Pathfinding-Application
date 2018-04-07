package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.Patient;
import com.manlyminotaurs.users.User;
import javafx.collections.FXCollections;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


//update CSV file from room, exit, hallway, transport nodes.
//finish erd diagram and create request table
class NodesDBUtil {

    // Get a csv file controller
    private CsvFileController csvFileController = new CsvFileController();
    DataModelI dataModelI = DataModelI.getInstance();
    int nodeIDGeneratorCount = 200;
    int elevatorCounter;

    /*---------------------------------------- Create java objects ---------------------------------------------------*/
    /**
     * Creates a list of objects and stores them in the global variable nodeList
     */
    void retrieveNodes() {
        try {
            // Connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");

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
                    } if (nodeType.equals("ELEV")) {
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
                    dataModelI.getNodeList().add(node);
                    System.out.println("Node added to list...");
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding nodes");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    } // retrieveNodes() ends

    // TODO test
    /**
     * Creates a list of objects and stores them in the global variable edgeList
     */
    /*
    void retrieveEdges() {
        try {
            // Connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");

            // Variables
            Edge edge;
            String edgeID;
            String startNode;
            String endNode;
            int status;

            try {
                Statement stmt = connection.createStatement();
                String str = "SELECT * FROM MAP_EDGES";
                ResultSet rset = stmt.executeQuery(str);

                // For every edge, get the information
                while (rset.next()) {
                    edgeID = rset.getString("edgeID");
                    startNode = rset.getString("startNode");
                    endNode = rset.getString("endNode");
                    status = rset.getInt("status");

                    // Add the new edge to the list
                    Node startNodeObject = getNodesFromList(startNode);
                    Node endNodeObject = getNodesFromList(endNode);

                    //
                    if(startNode != null && endNode != null) {
                        edge = new Edge(startNodeObject, endNodeObject, edgeID);
                        edge.setStatus(status);

                        Node sNode = getNodesFromList(startNode);
                        Node eNode = getNodesFromList(startNode);

                        sNode.addEdge(edge);
                        eNode.addEdge(edge);

                        dataModelI.getEdgeList().add(edge);

                        System.out.println("Edge added to corresponding nodes: " + edgeID);
                    }
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding edges");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    } // retrieveEdges() ends
*/
    /*
    /** NOT IN USE
     *  get data from UserAccount table in database and put them into the list of request objects
     */void retrieveUser() {
        try {
            // Connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");

            // Variables
            User userObject;
            String userID;
            String firstName;
            String middleInitial;
            String lastName;
            String language;

            try {
                Statement stmt = connection.createStatement();
                String str = "SELECT * FROM UserAccount";
                ResultSet rset = stmt.executeQuery(str);

                while (rset.next()) {
                    userID = rset.getString("userID");
                    firstName = rset.getString("firstName");
                    middleInitial = rset.getString("middleInitial");
                    lastName = rset.getString("lastName");
                    language = rset.getString("language");

                    // Add the new edge to the list
                    userObject = new Patient(userID,firstName,middleInitial,lastName,language);
                    dataModelI.getUserList().add(userObject);
                    System.out.println("User added to the list: "+userID);
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding users");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    } // retrieveUser() ends


    /*---------------------------------------- Add/edit/delete nodes -------------------------------------------------*/

    /**
     * Adds the java object and the corresponding entry in the database table
     * @param longName long name of the node
     * @param shortName short name of the node
     * @param nodeType node type
     * @param xcoord xcoord
     * @param ycoord ycoord
     *
     */
    Node addNode(String longName, String shortName, String nodeType, int xcoord, int ycoord, String floor, String building, int xCoord3D, int yCoord3D)
    {
        String ID = generateNodeID(nodeType,floor,"A");

        Node node;
        if(nodeType.equals("HALL")) {
            node = new Hallway(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building, xCoord3D, yCoord3D);
        }
        else if(nodeType.equals("ELEV")) {
            node = new Transport(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building, xCoord3D, yCoord3D);
        }
        else if(nodeType.equals("STAI")){
            node = new Transport(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building, xCoord3D, yCoord3D);
        }
        else if(nodeType.equals("EXIT")) {
            node = new Exit(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building, xCoord3D, yCoord3D);
        }
        else {
            node = new Room(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building, xCoord3D, yCoord3D);
        }

        dataModelI.getNodeList().add(node);
        System.out.println("Node added to object list...");
        try {
            // Connect to the database
            System.out.println("Getting connection to database...");
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            String str = "INSERT INTO map_nodes(nodeID,xCoord,yCoord,floor,building,nodeType,longName,shortName) VALUES (?,?,?,?,?,?,?,?)";

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
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            System.out.println("Node added to database");
            csvFileController.updateNodeCSVFile("./MapGNodes.csv");
        } catch (SQLException e)
        {
            System.out.println("Node already in the database");
        }
        return node;
    } // end addNode()

    /**
     *
     * @param node
     */
    boolean modifyNode(Node node){
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");

            try {
                // Connect to the database
                System.out.println("Getting connection to database...");
                connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
                String str = "UPDATE map_nodes SET nodeID = ?,xCoord = ?,yCoord = ?,floor = ?,building = ?,nodeType = ?,longName = ?, shortName =?";

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
                System.out.println("Prepared statement created...");
                statement.executeUpdate();
                System.out.println("Node added to database");
                csvFileController.updateNodeCSVFile("./MapGNodes.csv");
            } catch (SQLException e)
            {
                System.out.println("Node already in the database");
            }
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
            return true;
        }finally{
            return true;
        }
    }

    // TODO test
    // TODO should we remove edges
    /**
     * Removes a node from the list of objects as well as the database
     * @param node
     */
    void removeNode (Node node) {
        //first remove any adjacent edges from this node
        List<Edge> listOfEdges = getEdgesFromNode(node);
        Iterator<Edge> iterator = listOfEdges.iterator();
        while (iterator.hasNext()) {
            Edge a_edge = iterator.next();
            removeEdge(a_edge);
        }
            dataModelI.getNodeList().remove(node);

        // Find the node to remove from the nodeList
        int i = 0;
        while(i < dataModelI.getNodeList().size()){
            if(dataModelI.getNodeList().get(i).getID().equals(node.getID())) {
                // remove the node
                System.out.println("Node removed from object list: "+node.getID());
                dataModelI.getNodeList().remove(i);
            }
            i++;
        }
        try {
            // Get connection to database and delete the node from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM MAP_NODES WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            System.out.println("Node removed from database");
            csvFileController.updateNodeCSVFile("./MapGNodes.csv");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*---------------------------------------- Add/delete/edit edges -------------------------------------------------*/

    // TODO fix add edge

    /**
     * Adds the java object and the corresponding entry in the database table
     * @param startNode
     * @param endNode
     */
    /*
    void addEdge(Node startNode, Node endNode) {
        String edgeID = startNode.getID() + "_" + endNode.getID();
        Edge edge = new Edge(startNode , endNode, edgeID);
        edgeList.add(edge);
        System.out.println("Node added to object list...");
        try {
            // Connect to the database
            System.out.println("Getting connection to database...");
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            String str = "INSERT INTO MAP_EDGES(edgeID, startNode, endNode) VALUES (?,?,?)";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, edge.getEdgeID());
            statement.setString(2, edge.getStartNode().getID());
            statement.setString(3, edge.getEndNode().getID());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            System.out.println("Node added to database");
            csvFileController.updateEdgeCSVFile("./MapGEdges.csv");
        } catch (SQLException e)
        {
            System.out.println("Node already in the database");
        }

    } // end addEdge()*/

    // TODO fix removeEdge
    /**
     * Removes a node from the list of objects as well as the database
     * @param edge is the edge to be removed
     */
    void removeEdge (Edge edge) {
        /*// Find the node to remove from the edgeList
        int i = 0;
        while(i < edgeList.size()){
            if(edgeList.get(i).getEdgeID().equals(edge.getEdgeID())) {
                // remove the node
                System.out.println("edge removed from object list: "+edge.getEdgeID());
                edgeList.remove(i);
            }
            i++;
        }
        try {
            // Get connection to database and delete the edge from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM MAP_EDGES WHERE edgeID = '" + edge.getEdgeID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            csvFileController.updateEdgeCSVFile("./MapGEdges.csv");
            System.out.println("Edge removed from database: " + edge.getEdgeID());
        } catch (SQLException e) {
            e.printStackTrace();
        } */
    } // removeEdge

    /*----------------------------------------- Helper functions -----------------------------------------------------*/

    /**
     * Print the nodeList
     */
    void printNodeList() {
        int i = 0;
        while(i < dataModelI.getNodeList().size()) { System.out.println("Object " + i + ": " + dataModelI.getNodeList().get(i).getLongName()); i++; }
    } // end printNodeList

    // TODO fix
    /**
     * Print the edgeList
     */
    void printEdgeList() {
        /*
        int i = 0;
        while(i < edgeList.size()) {
            System.out.print("Object " + i + ": " + edgeList.get(i).getEdgeID());
            System.out.println("   Start: " + edgeList.get(i).getStartNode().getID());
            i++;
        } */
    } // end printEdgeList

    // TODO fix
    Edge getEdgeFromList(String edgeID){
        /*
        Iterator<Edge> iterator = edgeList.iterator();
        while (iterator.hasNext()) {
            Edge a_edge = iterator.next();
            if (a_edge.getEdgeID().equals(edgeID)) {
                return a_edge;
            }
        } */
        //System.out.println("getNdeFromList: Null-----------Something might break");
        return null;
    }

    List<Node> getAdjacentNodesFromNode(Node node){
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
     * @param node
     * @return
     */
    private List<Edge> getEdgesFromNode(Node node){
        List<Edge> listOfEdges = new ArrayList<Edge>();
        try {
            // Connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");

            Edge edge;
            String edgeID;
            String startNode;
            String endNode;

            try {
                Statement stmt = connection.createStatement();
                String str = "SELECT * FROM MAP_EDGES WHERE STARTNODE = '"+ node.getID() +"'"+ "OR ENDNODE = '" + node.getID() +"'";
                ResultSet rset = stmt.executeQuery(str);

                // For every edge, get the information
                while (rset.next()) {
                    edgeID = rset.getString("edgeID");
                    startNode = rset.getString("startNode");
                    endNode = rset.getString("endNode");

                    // Add the new edge to the list
                    Node startNodeObject = getNodesFromList(startNode);
                    Node endNodeObject = getNodesFromList(endNode);
                    if(startNode != null && endNode != null) {
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
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return listOfEdges;
    }

    /**
     * return the node object that has the matching nodeID with the ID provided in the argument
     * return null if it can't  find any
     * @param nodeID
     * @return
     */
    Node getNodesFromList(String nodeID){
        Iterator<Node> iterator = dataModelI.getNodeList().iterator();
        while (iterator.hasNext()) {
            Node a_node = iterator.next();
            if (a_node.getID().equals(nodeID)) {
                return a_node;
            }
        }
        //System.out.println("getNdeFromList: Null-----------Something might break");
        return null;
    }

    List<String> getBuildingsFromList(List<Node> listOfNodes){
        List<String> buildings = new ArrayList<>();
        Iterator<Node> iterator = listOfNodes.iterator();

        //insert rows
        while (iterator.hasNext()) {
            Node a_node = iterator.next();
            if(buildings.contains(a_node.getBuilding()) == false){
                buildings.add(a_node.getBuilding());
            }
        }
        return buildings;
    }

    List<String> getTypesFromList(String building, List<Node> listOfNodes){
        List<String> types= new ArrayList<>();
        Iterator<Node> iterator = listOfNodes.iterator();
       iterator.next(); // get rid of the header

        //insert rows
        while (iterator.hasNext()) {
            Node a_node = iterator.next();
            if(building.equals(a_node.getBuilding()) && types.contains(a_node.getNodeType()) == false){
                types.add(a_node.getNodeType());
            }
        }
        return types;
    }

    List<Node> getNodesFromList(String building, String type, List<Node> listOfNodes){
        List<Node> selectedNodes = new ArrayList<>();
        Iterator<Node> iterator = listOfNodes.iterator();

        //insert rows
        while (iterator.hasNext()) {
            Node a_node = iterator.next();
            if(building.equals(a_node.getBuilding()) && type.equals(a_node.getNodeType())){
                selectedNodes.add(a_node);
            }
        }
        return selectedNodes;
    }

    public List<Node> getNodesByType(String type, List<Node> listOfNodes){
        List<Node> selectedNodes = new ArrayList<>();
        Iterator<Node> iterator = listOfNodes.iterator();

        //insert rows
        while (iterator.hasNext()) {
            Node a_node = iterator.next();
            if(type.equals(a_node.getNodeType())){
                selectedNodes.add(a_node);
            }
        }
        return selectedNodes;
    }

    public List<Node> getNodesByFloor(String floor, List<Node> listOfNodes){
        List<Node> selectedNodes = new ArrayList<>();
        Iterator<Node> iterator = listOfNodes.iterator();

        //insert rows
        while (iterator.hasNext()) {
            Node a_node = iterator.next();
            if(floor.equals(a_node.getFloor())){
                selectedNodes.add(a_node);
            }
        }

        return selectedNodes;
    }

    public Node getNodesByID(String nodeID, List<Node> listOfNodes){
        Iterator<Node> iterator = listOfNodes.iterator();

        //insert rows
        while (iterator.hasNext()) {
            Node a_node = iterator.next();
            if(nodeID.equals(a_node.getID())){
                return a_node;
            }
        }
        System.out.println("getNodesByID: returned null ");
        return null;
    }

    /**
     * used to generate unique nodeID when adding a new node on the map
     * @param nodeType
     * @param floor
     * @param elevatorLetter
     * @return
     */
    public String generateNodeID(String nodeType, String floor, String elevatorLetter){
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

        if(nodeType.equals("ELEV")){
            if(elevatorLetter == null || elevatorLetter.equals("")){
                System.out.println("elevator exception happened!!!!!");
                return "ERROR";
            }
            else {
                nodeID = nodeID + "00" + elevatorLetters.get(elevatorCounter);
                elevatorCounter++;
            }
        }
        else{
            nodeID += Integer.toString(nodeIDGeneratorCount);
            nodeIDGeneratorCount++;
        }

        if(floor.equals("1")){
            nodeID +="01";
        }
        else if(floor.equals("2")){
            nodeID +="02";
        }
        else if(floor.equals("3")){
            nodeID +="03";
        }
        else{
            nodeID +=floor;
        }
        return nodeID;
    }


} // end NodesDBUtil class