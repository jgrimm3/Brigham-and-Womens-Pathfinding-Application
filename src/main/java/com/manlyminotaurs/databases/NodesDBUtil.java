package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.Patient;
import com.manlyminotaurs.users.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;



//update CSV file from room, exit, hallway, transport nodes.
//finish erd diagram and create request table
class NodesDBUtil {

    // Get a csv file controller
    private CsvFileController csvFileController = new CsvFileController();

    // global nodeList holds all the java objects for the nodes
    public static List<Node> nodeList = new ArrayList<>();
    public static List<Edge> edgeList = new ArrayList<>();
    public static List<User> userList = new ArrayList<>();

    public static List<Exit> exitList = new ArrayList<>();
    public static List<Room> roomList = new ArrayList<>();
    public static List<Hallway> hallwayList = new ArrayList<>();
    public static List<Transport> transportList = new ArrayList<>();

    int nodeIDGeneratorCount = 200;

    /*------------------------------------------------ Main ----------------------------------------------------------*/
    public static void main(String [] args) {

        System.out.println("-------- Step 1: Registering Oracle Driver ------");
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver? Did you follow the execution steps. ");
            System.out.println("*****Open the file and read the comments in the beginning of the file****");
            e.printStackTrace();
            return;
        }

        System.out.println("Oracle JDBC Driver Registered Successfully !");

        System.out.println("main function ended");
    }


    /*---------------------------------------- Create java objects ---------------------------------------------------*/
    /**
     * Creates a list of objects and stores them in the global variable nodeList
     */
    public void retrieveNodes() {
        try {
            // Connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");

            // Variables
            Node node = null;
            String ID;
            String nodeType;
            String longName;
            String shortName;
            int xCoord;
            int yCoord;
            String floor;
            String building;
            int status;

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

                    // Create the java objects based on the node type
                    if (nodeType.equals("CONF")) {
                        node = new Conference(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Conf created");
                    } else if (nodeType.equals("DEPT")) {
                        node = new Department(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Dept created");
                    } else if (nodeType.equals("ELEV")) {
                        node = new Elevator(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Elev created");
                    } else if (nodeType.equals("EXIT")) {
                        node = new Exit(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Exit created");
                    } else if (nodeType.equals("HALL")) {
                        node = new Hallway(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Hall created");
                    } else if (nodeType.equals("INFO")) {
                        node = new Infodesk(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Info created");
                    } else if (nodeType.equals("LABS")) {
                        node = new Lab(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Labs created");
                    } else if (nodeType.equals("REST")) {
                        node = new Restroom(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Rest created");
                    } else if (nodeType.equals("RETL")) {
                        node = new Retail(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Retl created");
                    } else if (nodeType.equals("STAI")) {
                        node = new Stairway(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Stai created");
                    } else if (nodeType.equals("SERV")) {
                        node = new Service(longName, shortName, ID, nodeType, xCoord, yCoord, floor, building);
                        //System.out.println("Serv created");
                    }
                    // Add the new node to the list
                    node.setStatus(status);
                    nodeList.add(node);
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

    /**
     * Creates a list of objects and stores them in the global variable edgeList
     */
    public void retrieveEdges() {
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
                    Node startNodeObject = getNodeFromList(startNode);
                    Node endNodeObject = getNodeFromList(endNode);
                    if(startNode != null && endNode != null) {
                        edge = new Edge(startNodeObject, endNodeObject, edgeID);
                        edge.setStatus(status);
                        edgeList.add(edge);
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
    } // retrieveEdges() ends

    /*---------------------------------- Insert values into database ---------------------------------------------------*/


    /**
     *  get data from UserAccount table in database and put them into the list of request objects
     */
    public void retrieveUser() {
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
                    userList.add(userObject);
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
     * @param longName
     * @param shortName
     * @param nodeType
     * @param xcoord
     * @param ycoord
     */
    public void addNode(String longName, String shortName, String nodeType, int xcoord, int ycoord, String floor, String building)
    {
        String ID = generateNodeID(nodeType,floor,"A");

        Node node;
        if(nodeType.equals("HALL")) {
            node = new Hallway(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        }
        else if(nodeType.equals("ELEV")) {
            node = new Elevator(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        }
        else if(nodeType.equals("STAI")){
            node = new Stairway(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        }
        else if(nodeType.equals("EXIT")) {
            node = new Exit(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        }
        else {
            node = new Room(longName, shortName, ID, nodeType, xcoord, ycoord, floor, building);
        }

        nodeList.add(node);
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

    } // end addNode()

    /**
     *
     * @param node
     */
    public void modifyNode(Node node){
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET longName = gg" + " WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    } // end modifyNodeLongName

    /**
     * Removes a node from the list of objects as well as the database
     * @param node
     */
    public void removeNode (Node node) {
        //first remove any adjacent edges from this node
        List<Edge> listOfEdges = getEdgesFromNode(node);
        Iterator<Edge> iterator = listOfEdges.iterator();
        while (iterator.hasNext()) {
            Edge a_edge = iterator.next();
            removeEdge(a_edge);
        }
        // then remove node from room table
        if(node.getNodeType().equals("EXIT")) {
            removeExitNode(node);
        }
        else if(node.getNodeType().equals("HALL")) {
            removeHallwayNode(node);
        }
        else if(node.getNodeType().equals("REST") || node.getNodeType().equals("STAI")) {
            removeTransportNode(node);
        }
        else {
            removeRoomNode(node);
        }
        // Find the node to remove from the nodeList
        int i = 0;
        while(i < nodeList.size()){
            if(nodeList.get(i).getID().equals(node.getID())) {
                // remove the node
                System.out.println("Node removed from object list: "+node.getID());
                nodeList.remove(i);
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
    } // removeNode

    public void removeRoomNode(Node node){
        // Find the node to remove from the edgeList
        int i = 0;
        while(i < roomList.size()){
            if(roomList.get(i).getID().equals(node.getID())) {
                // remove the node
                System.out.println("node removed from object list: "+node.getID());
                roomList.remove(i);
            }
            i++;
        }

        try {
            // Get connection to database and delete the edge from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM ROOM WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            System.out.println("room removed from database: " + node.getID());
            csvFileController.updateNodeCSVFile("./NodeRoomTable.csv");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeHallwayNode(Node node){
        // Find the node to remove from the edgeList
        int i = 0;
        while(i < hallwayList.size()){
            if(hallwayList.get(i).getID().equals(node.getID())) {
                // remove the node
                System.out.println("node removed from object list: "+node.getID());
                hallwayList.remove(i);
            }
            i++;
        }

        try {
            // Get connection to database and delete the edge from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM HALLWAY WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            csvFileController.updateHallwayCSVFile("./NodeHallwayTable.csv");
            System.out.println("hallway removed from database: " + node.getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeExitNode(Node node){
        // Find the node to remove from the edgeList
        int i = 0;
        while(i < exitList.size()){
            if(exitList.get(i).getID().equals(node.getID())) {
                // remove the node
                System.out.println("node removed from object list: "+node.getID());
                exitList.remove(i);
            }
            i++;
        }

        try {
            // Get connection to database and delete the edge from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM EXIT WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            csvFileController.updateExitCSVFile("./NodeExitTable.csv");
            System.out.println("EXIT removed from database: " + node.getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeTransportNode(Node node){
        // Find the node to remove from the edgeList
        int i = 0;
        while(i < transportList.size()){
            if(transportList.get(i).getID().equals(node.getID())) {
                // remove the node
                System.out.println("node removed from object list: "+node.getID());
                transportList.remove(i);
            }
            i++;
        }

        try {
            // Get connection to database and delete the edge from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM TRANSPORT WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            csvFileController.updateTransportCSVFile("./NodeTransportTable.csv");
            System.out.println("Transport removed from database: " + node.getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*---------------------------------------- Add/delete/edit edges -------------------------------------------------*/

    /**
     * Adds the java object and the corresponding entry in the database table
     * @param startNode
     * @param endNode
     */
    public void addEdge(Node startNode, Node endNode) {
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

    } // end addEdge()

    /**
     * Removes a node from the list of objects as well as the database
     * @param edge is the edge to be removed
     */
    public void removeEdge (Edge edge) {
        // Find the node to remove from the edgeList
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
        }
    } // removeEdge

    /*----------------------------------------- Helper functions -----------------------------------------------------*/

    /**
     * Print the nodeList
     */
    public void printNodeList() {
        int i = 0;
        while(i < nodeList.size()) { System.out.println("Object " + i + ": " + nodeList.get(i).getLongName()); i++; }
    } // end printNodeList

    /**
     * Print the edgeList
     */
    public void printEdgeList() {
        int i = 0;
        while(i < edgeList.size()) {
            System.out.print("Object " + i + ": " + edgeList.get(i).getEdgeID());
            System.out.println("   Start: " + edgeList.get(i).getStartNode().getID());
            i++;
        }
    } // end printEdgeList

    public Edge getEdgeFromList(String edgeID){
        Iterator<Edge> iterator = edgeList.iterator();
        while (iterator.hasNext()) {
            Edge a_edge = iterator.next();
            if (a_edge.getEdgeID().equals(edgeID)) {
                return a_edge;
            }
        }
        //System.out.println("getNdeFromList: Null-----------Something might break");
        return null;
    }
    /**
     * find all adjacent edges from the node object using sql query
     * @param node
     * @return
     */
    public List<Edge> getEdgesFromNode(Node node){
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
                    Node startNodeObject = getNodeFromList(startNode);
                    Node endNodeObject = getNodeFromList(endNode);
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
    public Node getNodeFromList(String nodeID){
        Iterator<Node> iterator = nodeList.iterator();
        while (iterator.hasNext()) {
            Node a_node = iterator.next();
            if (a_node.getID().equals(nodeID)) {
                return a_node;
            }
        }
        //System.out.println("getNdeFromList: Null-----------Something might break");
        return null;
    }
  
   public ObservableList<String> getBuildingsFromList(List<Node> listOfNodes){
        ObservableList<String> buildings = FXCollections.observableArrayList();
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

  public ObservableList<String> getTypesFromList(String building, List<Node> listOfNodes){
        ObservableList<String> types= FXCollections.observableArrayList();
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

   public ObservableList<String> getNodeFromList(String building, String type,List<Node> listOfNodes){
        ObservableList<Node> selectedNodes = FXCollections.observableArrayList();
        ObservableList<String> nodeNames = FXCollections.observableArrayList();
        Iterator<Node> iterator = listOfNodes.iterator();

        //insert rows
        while (iterator.hasNext()) {
            Node a_node = iterator.next();
            if(building.equals(a_node.getBuilding()) && type.equals(a_node.getNodeType())){
                selectedNodes.add(a_node);
                nodeNames.add(a_node.getID());
            }
        }
        return nodeNames;
    }

    int elevatorCounter = 0;

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