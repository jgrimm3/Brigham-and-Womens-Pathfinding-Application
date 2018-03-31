package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class NodesEditor {

    // global nodeList holds all the java objects for the nodes
    public List<Node> nodeList = new ArrayList<>();
    public List<Edge> edgeList = new ArrayList<>();

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
        System.out.println("Creating tables...");
        NodesEditor nodesEditor = new NodesEditor();
        nodesEditor.createTables();
        nodesEditor.retrieveNodes();
        nodesEditor.retrieveEdges();

        nodesEditor.modifyNodeBuilding(nodesEditor.nodeList.get(0), "BuidlingModify");
        nodesEditor.modifyNodeShortName(nodesEditor.nodeList.get(1), "shortnameModify");
        nodesEditor.modifyNodeLongName(nodesEditor.nodeList.get(2), "LongNameModify");
        nodesEditor.modifyNodeType(nodesEditor.nodeList.get(3), "YOLO");

        nodesEditor.modifyEdgeEndNode(nodesEditor.edgeList.get(1), "testingStart");
        nodesEditor.modifyEdgeEndNode(nodesEditor.edgeList.get(2), "testingEnd");

        nodesEditor.updateNodeCSVFile("./nodesDB/TestUpdateNodeFile.csv");
        nodesEditor.updateEdgeCSVFile("./nodesDB/TestUpdateEdgeFile.csv");
        System.out.println("Tables created");
    }
    /*------------------------------------- Database and csv methods -------------------------------------------------*/
    /**
     * Creates the database tables from the csv files
     */
    public void createTables() {

        // Make sure we aren't ruining the database
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure you want to recreate the database from the csv files? (y/n): ");
        String ans = scanner.nextLine();

        // If you're positive...
        if(ans.equals("y")) {
            try {
                // Variables we need to make the tables
                NodesEditor a_database = new NodesEditor();
                List<String[]> list_of_nodes;
                List<String[]> list_of_edges;
                list_of_nodes = a_database.parseCsvFile("./nodesDB/MapBnodes.csv");
                list_of_edges = a_database.parseCsvFile("./nodesDB/MapBedges.csv");

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

                Iterator<String[]> iterator = list_of_nodes.iterator();
                iterator.next(); // get rid of header of csv file

                //delete table
                System.out.println("Deleting table...");
                String delete_sql = "DROP TABLE map_nodes";
                try {
                    stmt.executeUpdate(delete_sql);
                } catch (SQLException se) {
                    //Handle errors for JDBC
                    se.printStackTrace();
                }
                System.out.println("Table deleted successfully...");

                //create table
                System.out.println("Creating table...");
                String create_sql = "CREATE TABLE map_nodes (" +
                        " nodeID             CHAR(10) PRIMARY KEY," +
                        "  xCoord              INTEGER," +
                        "  yCoord              INTEGER," +
                        "  floor               VARCHAR(2)," +
                        "  building            VARCHAR(255)," +
                        "  nodeType           VARCHAR(4)," +
                        "  longName           VARCHAR(255)," +
                        "  shortName          VARCHAR(255)," +
                        "  teamAssigned       VARCHAR(255))";

                stmt.executeUpdate(create_sql);
                System.out.println("Table created successfully...");

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
                    System.out.println("row is: " + node_id + " " + xcoord + " " + ycoord + " " + floor + " " + building + " " + nodeType + " " + long_name + " " + short_name + " " + team_assigned);

                    // Add to the database table
                    String str = "INSERT INTO map_nodes(nodeID,xCoord,yCoord,floor,building,nodeType,longName,shortName,teamAssigned) VALUES (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setString(1, node_id);
                    statement.setInt(2, Integer.parseInt(xcoord));
                    statement.setInt(3, Integer.parseInt(ycoord));
                    statement.setString(4, floor);
                    statement.setString(5, building);
                    statement.setString(6, nodeType);
                    statement.setString(7, long_name);
                    statement.setString(8, short_name);
                    statement.setString(9, team_assigned);
                    statement.executeUpdate();
                }// while loop ends

                System.out.println("----------------------------------------------------");
                Iterator<String[]> iterator2 = list_of_edges.iterator();
                iterator2.next(); // get rid of the header

                //delete table
                System.out.println("Deleting table...");
                delete_sql = "DROP TABLE map_edges";
                try {
                    stmt.executeUpdate(delete_sql);
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                System.out.println("Database table successfully...");

                //create table
                System.out.println("Creating table...");
                create_sql = "CREATE TABLE map_edges (" +
                        "  edgeID              VARCHAR(255)," +
                        "  startNode           VARCHAR(255)," +
                        "  endNode             VARCHAR(255))";

                stmt.executeUpdate(create_sql);
                System.out.println("table created successfully...");

                //insert rows
                while (iterator2.hasNext()) {
                    String[] node_row = iterator2.next();
                    System.out.println("row is: " + node_row[0] + " " + node_row[1] + " " + node_row[2]);

                    String str = "INSERT INTO map_edges(edgeID,startNode, endNode) VALUES (?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setString(1, node_row[0]);
                    statement.setString(2, node_row[1]);
                    statement.setString(3, node_row[2]);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * http://www.avajava.com/tutorials/lessons/how-do-i-read-a-string-from-a-file-line-by-line.html
     * https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
     * @param csv_file_name the name of the csv file
     * @return arrayList of columns from the csv
     */
    public List<String[]> parseCsvFile(String csv_file_name) {
        System.out.println("Parsing csv file");
        List<String[]> list_of_rows = new ArrayList<>();
        try {
            File file = new File(csv_file_name);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // use comma as separator
                String[] node_row = line.split(",");
                list_of_rows.add(node_row);
            }
            fileReader.close();
            System.out.println("csv file parsed");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list_of_rows;
    } // parseCsvFile() ends

    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    public void updateNodeCSVFile(String csvFileName) {
        Iterator<Node> iterator = nodeList.iterator();
        System.out.println("Updating node csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName,teamAssigned\n");
            while (iterator.hasNext()) {
                Node a_node = iterator.next();
                printWriter.printf("%s,%d,%d,%s,%s,%s,%s,%s,Team M\n", a_node.getID(), a_node.getXCoord(), a_node.getYCoord(), a_node.getFloor(), a_node.getBuilding(), a_node.getNodeType(), a_node.getLongName(), a_node.getShortName());
            }
            printWriter.close();
            System.out.println("csv node file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    public void updateEdgeCSVFile(String csvFileName) {
        Iterator<Edge> iterator = edgeList.iterator();
        System.out.println("Updating edge csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("edgeID,startNode,endNode\n");
            while (iterator.hasNext()) {
                Edge a_edge = iterator.next();
                printWriter.printf("%s,%s,%s\n", a_edge.getEdgeID(), a_edge.getStartNode(), a_edge.getEndNode());
            }
            printWriter.close();
            System.out.println("csv edge file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
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
    } // retrieveData() ends

    /**
     * Creates a list of objects and stores them in the global variable nodeList
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

                // For every node, get the information
                while (rset.next()) {
                    edgeID = rset.getString("edgeID");
                    startNode = rset.getString("startNode");
                    endNode = rset.getString("endNode");

                    edge = new Edge(startNode, endNode, edgeID);

                    // Add the new node to the list
                    edgeList.add(edge);
                    System.out.println("Edge added to list...");
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
    } // retrieveData() ends

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
            statement.setString(5, "" + node.getBuilding());
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
            statement.setString(2, edge.getStartNode());
            statement.setString(3, edge.getEndNode());
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
        edge.setStartNode(startNode);
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
        edge.setEndNode(endNode);
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
        while(i < edgeList.size()) { System.out.println("Object " + i + ": " + edgeList.get(i).getEdgeID()); i++; }
    } // end printEdgeList

} // end NodesEditor class