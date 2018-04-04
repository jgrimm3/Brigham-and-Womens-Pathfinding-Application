package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.Patient;
import com.manlyminotaurs.users.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.traversal.NodeIterator;

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
    public static List<User> userList = new ArrayList<>();

    public static List<Exit> exitList = new ArrayList<>();
    public static List<Room> roomList = new ArrayList<>();
    public static List<Hallway> hallwayList = new ArrayList<>();
    public static List<Transport> transportList = new ArrayList<>();

    int nodeIDGeneratorCount = 200;
    static TableInitializer initializer = new TableInitializer();
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
        RequestsDBUtil requestsDB = new RequestsDBUtil();
        MessagesDBUtil messagesDBUtil = new MessagesDBUtil();

        initializer.initTables();
        initializer.populateNodeEdgeTables("./nodesDB/MapGNodes.csv","./nodesDB/MapGEdges.csv");
        initializer.populateUserAccountTable("./nodesDB/UserAccountTable.csv");
        initializer.populateMessageTable("./nodesDB/MessageTable.csv");
        initializer.populateRequestTable("./nodesDB/RequestTable.csv");

        nodesEditor.retrieveNodes();
        nodesEditor.retrieveEdges();

        messagesDBUtil.retrieveMessage();
        requestsDB.retrieveRequest();
        nodesEditor.retrieveUser();

        initializer.populateExitTable("./nodesDB/NodeExitTable.csv");
        initializer.populateHallwayTable("./nodesDB/NodeHallwayTable.csv");
        initializer.populateRoomTable();
        initializer.populateTransportTable();

        Node oneNode = new Room("", "", "WHALL00902","", 23, 46,"2", "yolobuil");
        nodesEditor.removeNode(oneNode);
//        ObservableList<Message> list1 = messagesDBUtil.searchMessageByReceiver("1");
//        messagesDBUtil.addMessage("doctor", "hello world", false, "2", "1");
//        ObservableList<Message> list2 = messagesDBUtil.searchMessageByReceiver("1");
//        messagesDBUtil.printMessageList();
//
//
//        ObservableList<Request> list6 = requestsDB.searchRequestByReceiver("2");
//        ObservableList<Request> list3 = requestsDB.searchRequestByReceiver("6");
//        messagesDBUtil.addMessage("second", "This is second", false, "5", "doctor");
//        requestsDB.addRequest("help", 3, "GHALL00201", "hi nurse, can you help me", "nurse");
//        ObservableList<Request> list4 = requestsDB.searchRequestByReceiver("nurse");
//        ObservableList<Request> list5 = requestsDB.searchRequestBySender("user");
//        requestsDB.printRequestList();

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

    public void populateNodeEdgeTables() {
        // Make sure we aren't ruining the database
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure you want to recreate the database from the csv files? (y/n): ");
        String ans = scanner.nextLine();

        // If you're positive...
        if(ans.equals("y")) {
            try {
                // Variables we need to make the tables
                CsvFileController csvFileControl = new CsvFileController();
                List<String[]> list_of_nodes;
                List<String[]> list_of_edges;
                list_of_nodes = csvFileControl.parseCsvFile("./nodesDB/MapGnodes.csv");
                list_of_edges = csvFileControl.parseCsvFile("./nodesDB/MapGedges.csv");

                // Get the database connection
                Connection connection;
                connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
                Statement stmt = connection.createStatement();

                // Print parsed array
                // This portion can be used to send each row to database also.
                String node_id;
                String xcoord;
                String ycoord;
                String floor;
                String building;
                String nodeType;
                String long_name;
                String short_name;
                String team_assigned;
                String status;

                Iterator<String[]> iterator = list_of_nodes.iterator();
                iterator.next(); // get rid of header of csv file

                //insert data for every row
                while (iterator.hasNext()) {
                    String[] node_row = iterator.next();
                    node_id = node_row[0];
                    xcoord = node_row[1];
                    ycoord = node_row[2];
                    floor = node_row[3];
                    building = node_row[4];
                    nodeType = node_row[5];
                    long_name = node_row[6];
                    short_name = node_row[7];
                    team_assigned = node_row[8];
                    status = node_row[9];
                    System.out.println("row is: " + node_id + " " + xcoord + " " + ycoord + " " + floor + " " + building + " " + nodeType + " " + long_name + " " + short_name + " " + team_assigned);

                    // Add to the database table
                    String str = "INSERT INTO map_nodes(nodeID,xCoord,yCoord,floor,building,nodeType,longName,shortName,status) VALUES (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setString(1, node_id);
                    statement.setInt(2, Integer.parseInt(xcoord));
                    statement.setInt(3, Integer.parseInt(ycoord));
                    statement.setString(4, floor);
                    statement.setString(5, building);
                    statement.setString(6, nodeType);
                    statement.setString(7, long_name);
                    statement.setString(8, short_name);
                    statement.setInt(9, Integer.parseInt(status));
                    statement.executeUpdate();
                }// while loop ends

                System.out.println("----------------------------------------------------");
                Iterator<String[]> iterator2 = list_of_edges.iterator();
                iterator2.next(); // get rid of the header

                //insert rows
                while (iterator2.hasNext()) {
                    String[] node_row = iterator2.next();
                    System.out.println("row is: " + node_row[0] + " " + node_row[1] + " " + node_row[2]);

                    String str = "INSERT INTO map_edges(edgeID,startNode, endNode,status) VALUES (?,?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setString(1, node_row[0]);
                    statement.setString(2, node_row[1]);
                    statement.setString(3, node_row[2]);
                    statement.setInt(4,Integer.parseInt(node_row[3]));
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void populateExitTable() {
        int i = 0;
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(i<nodeList.size()) {
            if(nodeList.get(i).getNodeType().equals("EXIT")) {
                try {
                    System.out.println("Found an exit...");
                    Exit exit = (Exit)nodeList.get(i);
                    exitList.add(exit);

                    String str = "INSERT INTO exit(isFireExit,isArmed,nodeID) VALUES (?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setBoolean(1, exit.isFireExit());
                    statement.setBoolean(2, exit.isArmed());
                    statement.setString(3, exit.getID());
                    statement.executeUpdate();
                    System.out.println("Added exit to table...");
                }catch (SQLException se) {
                    //Handle errors for JDBC
                    se.printStackTrace();
                }
            }
            i++;
        }
    }

    public void populateHallwayTable() {
        int i = 0;
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(i<nodeList.size()) {
            if(nodeList.get(i).getNodeType().equals("HALL")) {
                try {
                    System.out.println("Found an hallway...");
                    Hallway hall = (Hallway) nodeList.get(i);
                    hallwayList.add(hall);

                    String str = "INSERT INTO hallway(popularity, nodeID) VALUES (?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setInt(1, hall.getPopularity());
                    statement.setString(2, hall.getID());
                    statement.executeUpdate();
                    System.out.println("Added hall to table...");
                }catch (SQLException se) {
                    //Handle errors for JDBC
                    se.printStackTrace();
                }
            }
            i++;
        }
    }

    public void populateRoomTable() {
        int i = 0;
        String type;
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(i<nodeList.size()) {
            type = nodeList.get(i).getNodeType();
            if(type.equals("DEPT") || type.equals("RETA") || type.equals("LABS") || type.equals("REST") || type.equals("SERV") || type.equals("INFO") || type.equals("CONF")) {
                try {
                    System.out.println("Found an room...");
                    Room room = (Room)nodeList.get(i);
                    roomList.add(room);

                    String str = "INSERT INTO room(specialization, detail, popularity, isOpen, nodeID) VALUES (?,?,?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setString(1, room.getSpecialization());
                    statement.setString(2, room.getDetailedInfo());
                    statement.setInt(3, room.getPopularity());
                    statement.setBoolean(4, room.isOpen());
                    statement.setString(5, room.getID());
                    statement.executeUpdate();
                    System.out.println("Added room to table...");
                }catch (SQLException se) {
                    //Handle errors for JDBC
                    se.printStackTrace();
                }
            }
            i++;
        }
    }

    public void populateTransportTable() {
        int i = 0;
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(i<nodeList.size()) {
            String type = nodeList.get(i).getNodeType();
            if(type.equals("STAI") || type.equals("ELEV")) {
                try {
                    System.out.println("Found an transport...");
                    Transport transport = (Transport)nodeList.get(i);
                    transportList.add(transport);

                    String str = "INSERT INTO transport(directionality, floors, nodeID) VALUES (?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setString(1, transport.getDirectionality());
                    statement.setString(2, "0");
                    statement.setString(3, transport.getID());
                    statement.executeUpdate();
                    System.out.println("Added transport to table...");
                }catch (SQLException se) {
                    //Handle errors for JDBC
                    se.printStackTrace();
                }
            }
            i++;
        }
    }

    /*---------------------------------------- Add/edit/delete nodes -------------------------------------------------*/

    /**
     * Adds the java object and the corresponding entry in the database table
     * @param longName
     * @param shortName
     * @param ID
     * @param nodeType
     * @param xcoord
     * @param ycoord
     * @param floor
     * @param building
     */
    public void addNode(String longName, String shortName, String ID, String nodeType, int xcoord, int ycoord, String floor, String building)
    {
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

    /**
     * used to generate unique nodeID when adding a new node on the map
     * @param nodeType
     * @param floor
     * @param elevatorLetter
     * @return
     */
    String generateNodeID(String TeamLetter, String nodeType, String floor, String elevatorLetter){
        String nodeID = TeamLetter; // change this later
        nodeID += nodeType;

        if(nodeType.equals("ELEV")){
            if(elevatorLetter == null || elevatorLetter.equals("")){
                System.out.println("elevator exception happened!!!!!");
                return "ERROR";
            }
            else {
                nodeID = nodeID + "00" + elevatorLetter;
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


} // end NodesEditor class