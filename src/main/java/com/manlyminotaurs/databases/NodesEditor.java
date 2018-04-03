package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.Patient;
import com.manlyminotaurs.users.User;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;



//update CSV file from room, exit, hallway, transport nodes.
//finish erd diagram and create request table
public class NodesEditor {

    // global nodeList holds all the java objects for the nodes
    public static List<Node> nodeList = new ArrayList<>();
    public static List<Edge> edgeList = new ArrayList<>();
    public static List<Message> messageList = new ArrayList<>();
    public static List<Request> requestList = new ArrayList<>();
    public static List<User> userList = new ArrayList<>();


    public static List<Exit> exitList = new ArrayList<>();
    public static List<Room> roomList = new ArrayList<>();
    public static List<Hallway> hallwayList = new ArrayList<>();
    public static List<Transport> transportList = new ArrayList<>();
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

        // run to create the database table
        NodesEditor nodesEditor = new NodesEditor();
        TableInitializer initializer = new TableInitializer();
        CsvFileController csvFileControl = new CsvFileController();

        initializer.initTables();
        initializer.populateNodeEdgeTables("./nodesDB/MapGNodes.csv","./nodesDB/MapGEdges.csv");
        initializer.populateUserAccountTable("./nodesDB/UserAccountTable.csv");
        initializer.populateMessageTable("./nodesDB/MessageTable.csv");
        initializer.populateRequestTable("./nodesDB/RequestTable.csv");

        nodesEditor.retrieveNodes();
        nodesEditor.retrieveEdges();
        nodesEditor.retrieveMessage();
        nodesEditor.retrieveRequest();
        nodesEditor.retrieveUser();

        initializer.populateExitTable("./nodesDB/NodeExitTable.csv");
        initializer.populateHallwayTable("./nodesDB/NodeHallwayTable.csv");
        initializer.populateRoomTable();
        initializer.populateTransportTable();


//        csvFileControl.updateRequestCSVFile("./nodesDB/RequestTable.csv");
//        csvFileControl.updateUserCSVFile("./nodesDB/UserAccountTable2.csv");
//        csvFileControl.updateMessageCSVFile("./nodesDB/MessageTable.csv");
//        csvFileControl.updateNodeCSVFile("./nodesDB/MapGNodes2.csv");
//        csvFileControl.updateEdgeCSVFile("./nodesDB/MapGEdges2.csv");
//        csvFileControl.updateExitCSVFile("./nodesDB/NodeExitTable.csv");
//        csvFileControl.updateHallwayCSVFile("./nodesDB/NodeHallwayTable.csv");
//        csvFileControl.updateRoomCSVFile("./nodesDB/NodeRoomTable.csv");
//        csvFileControl.updateTransportCSVFile("./nodesDB/NodeTransportTable.csv");
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

            try {
                Statement stmt = connection.createStatement();
                String str = "SELECT * FROM MAP_EDGES";
                ResultSet rset = stmt.executeQuery(str);

                // For every edge, get the information
                while (rset.next()) {
                    edgeID = rset.getString("edgeID");
                    startNode = rset.getString("startNode");
                    endNode = rset.getString("endNode");

                    // Add the new edge to the list
                    Node startNodeObject = getNodeFromList(startNode);
                    Node endNodeObject = getNodeFromList(endNode);
                    edge = new Edge(startNodeObject, endNodeObject, edgeID);
                    edgeList.add(edge);
                    System.out.println("Edge added to the list: "+edgeID);
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

    /**
     * Creates a list of objects and stores them in the global variable messageList
//     */
    public void retrieveMessage() {
        try {
            // Connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");

            // Variables
            Message messageObject;
            Node node;
            String messageID;
            String message;
            Boolean isRead;
            String senderID;
            String receiverID;

            try {
                Statement stmt = connection.createStatement();
                String str = "SELECT * FROM Message";
                ResultSet rset = stmt.executeQuery(str);

                while (rset.next()) {
                    messageID = rset.getString("messageID");
                    message = rset.getString("message");
                    isRead = rset.getBoolean("isRead");
                    senderID =rset.getString("senderID");
                    receiverID = rset.getString("receiverID");

                    // Add the new edge to the list
                    messageObject = new Message(messageID,message,isRead,senderID,receiverID);
                    messageList.add(messageObject);
                    System.out.println("Message added to the list: "+messageID);
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding Messages");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    } // retrieveMessage() ends

    /**
     *  get data from request table in database and put them into the list of request objects
     */
    public void retrieveRequest() {
        try {
            // Connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");

            // Variables
            Request requestObject;
            String requestID;
            String requestType;
            int priority;
            Boolean isComplete;
            Boolean adminConfirm;
            String nodeID;
            String employeeID;
            String messageID;

            try {
                Statement stmt = connection.createStatement();
                String str = "SELECT * FROM Request";
                ResultSet rset = stmt.executeQuery(str);

                while (rset.next()) {
                    requestID = rset.getString("requestID");
                    requestType = rset.getString("requestType");
                    priority = rset.getInt("priority");
                    isComplete =rset.getBoolean("isComplete");
                    adminConfirm = rset.getBoolean("adminConfirm");
                    nodeID = rset.getString("nodeID");
                    employeeID = rset.getString("employeeID");
                    messageID = rset.getString("messageID");

                    // Add the new edge to the list
                    requestObject = new Request(requestID,requestType,priority,isComplete,adminConfirm,nodeID,employeeID,messageID);
                    requestList.add(requestObject);
                    System.out.println("Request added to the list: "+requestID);
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding Requests");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    } // retrieveRequest() ends

    /**
     *  get data from request table in database and put them into the list of request objects
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
     * @param node the node to be added to the database
     */
    public void addNode(Node node)
    {
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
        } catch (SQLException e)
        {
            System.out.println("Node already in the database");
        }

    } // end addNode()

    /**
     * Modifies building attribute of a node
     * @param node node to be modified
     * @param longName new longName
     */
    public void modifyNodeLongName(Node node, String longName){
        node.setLongName(longName);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET longName = '" + longName + "'" + " WHERE nodeID = '" + node.getID() + "'";
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
     * Modifies building attribute of a node
     * @param node node to be modified
     * @param shortName new shortName
     */
    public void modifyNodeShortName(Node node, String shortName){
        node.setShortName(shortName);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET shortName = '" + shortName + "'" + " WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    } // end modifyNodeShortName

    /**
     * Modifies building attribute of a node
     * @param node node to be modified
     * @param nodeType new nodeType
     */
    public void modifyNodeType(Node node, String nodeType){
        node.setNodeType(nodeType);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET nodeType = '" + nodeType + "'" + " WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    } // end modifyNodeType

/* not in use because nodeID is primary Key
    public void modifyNodeID(Node node, String ID){
        node.setID(ID);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET nodeID = '" + ID + "'" + " WHERE nodeID = '" + node.getID() + "'";
            int count = stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }*/

    /**
     * Modifies building attribute of a node
     * @param node node to be modified
     * @param floor new floor
     */
    public void modifyNodeFloor(Node node, String floor){
        node.getLoc().setFloor(floor);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET floor = '" + floor + "'" + " WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    } // end modifyNodeFloor

    /**
     * Modifies building attribute of a node
     * @param node node to be modified
     * @param xCoord new xCoord
     */
    public void modifyNodeXcoord(Node node, int xCoord){
        node.getLoc().setxCoord(xCoord);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET xCoord = " + xCoord + " WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    } // end modifyNodeXcoord

    /**
     * Modifies building attribute of a node
     * @param node node to be modified
     * @param yCoord new yCoord
     */
    public void modifyNodeYcoord(Node node, int yCoord){
        node.getLoc().setyCoord(yCoord);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET yCoord = " + yCoord + " WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    } // end modifyNodeYcoord

    /**
     * Modifies building attribute of a node
     * @param node node to be modified
     * @param building new building
     */
    public void modifyNodeBuilding(Node node, String building){
        node.getLoc().setBuilding(building);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET building = '" + building + "'" + " WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    } // end modifyNodeBuilding

    /**
     * Removes a node from the list of objects as well as the database
     * @param node is the node to be removed
     */
    public void removeNode (Node node) {
        // remove the node from the nodeList
        nodeList.remove(node);
        System.out.println("Node removed from object list...");
        try {
            // Get connection to database and delete the node from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM MAP_NODES WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            System.out.println("Node removed from database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // removeNode

    /**
     * Removes a node from the list of objects as well as the database
     * @param nodeID is the nodeID of the node to be removed
     */
    public void removeNode (String nodeID) {
        // Find the node to remove from the nodeList
        int i = 0;
        while(i < nodeList.size()){
            if(nodeList.get(i).getID().equals(nodeID)) {
                // remove the node
                System.out.println("Node removed from object list...");
                nodeList.remove(i);
            }
            i++;
        }
        try {
            // Get connection to database and delete the node from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM MAP_NODES WHERE nodeID = '" + nodeID + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            System.out.println("Node removed from database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // removeNode

    /*---------------------------------------- Add/delete/edit edges -------------------------------------------------*/
    /**
     * Adds the java object and the corresponding entry in the database table
     * @param edge the node to be added to the database
     */
    public void addEdge(Edge edge)
    {
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
        } catch (SQLException e)
        {
            System.out.println("Node already in the database");
        }

    } // end addEdge()

    /**
     * Modifies building attribute of a node
     * @param edge edge to be modified
     * @param startNode new startNode
     */
    public void modifyEdgeStartNode(Edge edge, String startNode){
        Node startNodeObject = getNodeFromList(startNode);
        edge.setStartNode(startNodeObject);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE MAP_EDGES SET startNode = '" + startNode + "'" + " WHERE edgeID = '" + edge.getEdgeID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    } // end modifyEdgeStartNode

    /**
     * Modifies building attribute of a node
     * @param edge edge to be modified
     * @param endNode new endNode
     */
    public void modifyEdgeEndNode(Edge edge, String endNode){
        Node endNodeObject = getNodeFromList(endNode);
        edge.setEndNode(endNodeObject);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE MAP_EDGES SET endNode = '" + endNode + "'" + " WHERE edgeID = '" + edge.getEdgeID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    } // end modifyEdgeEndNode

    /**
     * Removes a node from the list of objects as well as the database
     * @param edge is the edge to be removed
     */
    public void removeEdge (Edge edge) {
        // remove the node from the nodeList
        edgeList.remove(edge);
        System.out.println("Edge removed from object list...");
        try {
            // Get connection to database and delete the edge from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM MAP_EDGES WHERE edgeID = '" + edge.getEdgeID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            System.out.println("Edge removed from database");
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
        System.out.println("getNOdeFromList: Null-----------Something might break");
        return null;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(List<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    List<String> getBuildingsFromList(List<Node> listOfNodes){
        List<String> buildings = new ArrayList<String>();
        Iterator<Node> iterator = listOfNodes.iterator();
        iterator.next(); // get rid of the header

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
        List<String> types= new ArrayList<String>();
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

    List<Node> getNodeFromList(String building, String type,List<Node> listOfNodes){
        List<Node> selectedNodes = new ArrayList<Node>();
        Iterator<Node> iterator = listOfNodes.iterator();
        iterator.next(); // get rid of the header

        //insert rows
        while (iterator.hasNext()) {
            Node a_node = iterator.next();
            if(building.equals(a_node.getBuilding()) && type.equals(a_node.getNodeType())){
                selectedNodes.add(a_node);
            }
        }
        return selectedNodes;
    }
} // end NodesEditor class