package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;
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
    public static List<Edge> messageList = new ArrayList<>();
    public static List<Edge> requestList = new ArrayList<>();
    public static List<Edge> userAccountList = new ArrayList<>();
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
        initializer.initTables();
        initializer.populateNodeEdgeTables();
        initializer.populateUserAccountTable();
        initializer.populateMessageTable();
        initializer.populateRequestTable();
        nodesEditor.retrieveNodes();
        nodesEditor.retrieveEdges();
        nodesEditor.populateExitTable();
        nodesEditor.populateHallwayTable();
        nodesEditor.populateRoomTable();
        nodesEditor.populateTransportTable();

      //TODO: Re-enter these functions with correct calls
      //  nodesEditor.updateNodeCSVFile("./nodesDB/TestUpdateNodeFile.csv");
      //  nodesEditor.updateEdgeCSVFile("./nodesDB/TestUpdateEdgeFile.csv");
      //  nodesEditor.updateExitCSVFile("./nodesDB/TestUpdateExitFile.csv");
      //  nodesEditor.updateHallwayCSVFile("./nodesDB/TestUpdateHallwayFile.csv");
      //  nodesEditor.updateRoomCSVFile("./nodesDB/TestUpdateRoomFile.csv");
      //  nodesEditor.updateTransportCSVFile("./nodesDB/TestUpdateTransportFile.csv");
        System.out.println("main function ended");
    }
    /*------------------------------------- Database and csv methods -------------------------------------------------*/

    /**
     * Delete any pre-existing tables and create new tables in the database
     */
    public void initTables(){
       NodesEditor a_database = new NodesEditor();
       // Get the database connection
       Connection connection = null;
       Statement stmt = null;
       try {
       connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
       stmt = connection.createStatement();
           a_database.executeDBScripts("./src/main/resources/DropTables.sql", stmt);
           a_database.executeDBScripts("./src/main/resources/CreateTables.sql", stmt);
       } catch (IOException e) {
           e.printStackTrace();
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           try { stmt.close(); } catch (Exception e) { /* ignored */ }
           try { connection.close(); } catch (Exception e) { /* ignored */ }
       }
   }

    /**
     *
     * @param aSQLScriptFilePath path to the sql file to run
     * @param stmt statement object passed from callee
     * @return true if sql file is executed successfully.
     * @throws IOException
     * @throws SQLException
     */
    public boolean executeDBScripts(String aSQLScriptFilePath, Statement stmt) throws IOException,SQLException {
        boolean isScriptExecuted = false;
        try {
            BufferedReader in = new BufferedReader(new FileReader(aSQLScriptFilePath));
            String str;
            StringBuffer sb;
            sb = new StringBuffer();
            while ((str = in.readLine()) != null) {
                if (str.contains(";")) {
                    sb.append(str.replace(";",""));
                    try {
                        stmt.executeUpdate(sb.toString());
                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }
                    sb.delete(0,sb.length());
                }
                else {
                    sb.append(str + "\n ");
                }
            }

            in.close();
            isScriptExecuted = true;
        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath +". The error is"+ e.getMessage());
        }
        System.out.println("Tables created: "+aSQLScriptFilePath);
        return isScriptExecuted;
    }

    //---------------------------------------CSV File Interface--------------------------------------------
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
                printWriter.printf("%s,%d,%d,%s,%s,%s,%s,%s,Team M,%d\n", a_node.getID(), a_node.getXCoord(), a_node.getYCoord(), a_node.getFloor(), a_node.getBuilding(), a_node.getNodeType(), a_node.getLongName(), a_node.getShortName(),a_node.getStatus());
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
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Iterator<Edge> iterator = edgeList.iterator();
        System.out.println("Updating edge csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("edgeID,startNode,endNode,status\n");
            while (iterator.hasNext()) {
                Edge a_edge = iterator.next();
                printWriter.printf("%s,%s,%s,%d\n", a_edge.getEdgeID(), a_edge.getStartNode().getID(), a_edge.getEndNode().getID(), a_edge.getStatus());
            }
            printWriter.close();
            System.out.println("csv edge file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    public void updateExitCSVFile(String csvFileName) {
        Iterator<Exit> iterator = exitList.iterator();
        System.out.println("Updating exit csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("isFireExit, isArmed, nodeID\n");
            while (iterator.hasNext()) {
                Exit a_node = iterator.next();
                printWriter.printf("%b,%b,%s\n", a_node.isFireExit(), a_node.isArmed(), a_node.getID());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    public void updateHallwayCSVFile(String csvFileName) {
        Iterator<Hallway> iterator = hallwayList.iterator();
        System.out.println("Updating hallway csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("popularity, nodeID\n");
            while (iterator.hasNext()) {
                Hallway a_node = iterator.next();
                printWriter.printf("%d,%s\n", a_node.getPopularity(), a_node.getID());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    public void updateRoomCSVFile(String csvFileName) {
        Iterator<Room> iterator = roomList.iterator();
        System.out.println("Updating room csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("specialization, detail, popularity, isOpen, nodeID\n");
            while (iterator.hasNext()) {
                Room a_node = iterator.next();
                printWriter.printf("%s,%s,%d,%b,%s\n", a_node.getSpecialization(), a_node.getDetailedInfo(), a_node.getPopularity(), a_node.isOpen(), a_node.getID());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    public void updateTransportCSVFile(String csvFileName) {
        Iterator<Transport> iterator = transportList.iterator();
        System.out.println("Updating transport csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("directionality, floors, nodeID\n");
            while (iterator.hasNext()) {
                Transport a_node = iterator.next();
                printWriter.printf("%s,%s,%s\n", a_node.getDirectionality(), a_node.floorsToString(), a_node.getID());
            }
            printWriter.close();
            System.out.println("csv file updated");
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

    /*---------------------------------- Insert values into database ---------------------------------------------------*/

    /**
     * Populate the database tables from the csv files
     */
    public void populateNodeEdgeTables() {

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
                list_of_nodes = a_database.parseCsvFile("./nodesDB/MapGnodes.csv");
                list_of_edges = a_database.parseCsvFile("./nodesDB/MapGedges.csv");

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
  
   public ObservableList<String> getBuildingsFromList(List<Node> listOfNodes){
        ObservableList<String> buildings = FXCollections.observableArrayList();
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
        iterator.next(); // get rid of the header

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
} // end NodesEditor class