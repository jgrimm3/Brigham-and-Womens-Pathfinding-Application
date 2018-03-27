package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class NodesEditor {

    public List<Node> nodeList = new ArrayList<Node>();

    public static void main(String [] args) {
        NodesEditor nodesEditor = new NodesEditor();
        nodesEditor.createTables();
    }
    /**
     * Creates the database tables from the csv files
     */
    public void createTables() {

        // Make sure we aren't ruining the database
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure you want to recreate the database from the csv files? (y/n): ");
        String ans = scanner.nextLine();

        if(ans.equals("y")) {
            try {
                NodesEditor a_database = new NodesEditor();
                List<String[]> list_of_nodes;
                List<String[]> list_of_edges;
                list_of_nodes = a_database.parseCsvFile("./resources/MapGnodes.csv");
                list_of_edges = a_database.parseCsvFile("./resources/MapGedges.csv");
                Connection connection = null;
                connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
                Statement stmt = connection.createStatement();

                System.out.println("Create tables? (y/n): ");
                //-------------------Print parsed array--------------------------
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
                        "  node_id              VARCHAR(255)," +
                        "  start_node           VARCHAR(255)," +
                        "  end_node             VARCHAR(255))";

                stmt.executeUpdate(create_sql);
                System.out.println("table created successfully...");

                //insert rows
                while (iterator2.hasNext()) {
                    String[] node_row = iterator2.next();
                    System.out.println("row is: " + node_row[0] + " " + node_row[1] + " " + node_row[2]);

                    String str = "INSERT INTO map_edges(node_id,start_node, end_node) VALUES (?,?,?)";
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

        List<String[]> list_of_rows = new ArrayList<String[]>();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list_of_rows;
    } // parseCsvFile() ends

    /**
     * Creates a list of objects and stores them in the global variable nodeList
     */
    public void retrieveData() {
        try {
            // Connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");

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
                }
                rset.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    } // retrieveData() ends

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
            Connection connection = null;
            connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
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

    public void modifyNodeLongName(Node node, String longName){
        node.setLongName(longName);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET longName = '" + longName + "'" + " WHERE nodeID = '" + node.getID() + "'";
            int count = stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    public void modifyNodeShortName(Node node, String shortName){
        node.setShortName(shortName);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET shortName = '" + shortName + "'" + " WHERE nodeID = '" + node.getID() + "'";
            int count = stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    public void modifyNodeType(Node node, String nodeType){
        node.setNodeType(nodeType);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET nodeType = '" + nodeType + "'" + " WHERE nodeID = '" + node.getID() + "'";
            int count = stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

/*  remove because nodeID is primary Key
    public void modifyNodeID(Node node, String ID){
        node.setID(ID);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
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

    public void modifyNodeFloor(Node node, String floor){
        node.getLoc().setFloor(floor);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET floor = '" + floor + "'" + " WHERE nodeID = '" + node.getID() + "'";
            int count = stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    public void modifyNodeXcoord(Node node, int xCoord){
        node.getLoc().setxCoord(xCoord);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET xCoord = " + xCoord + " WHERE nodeID = '" + node.getID() + "'";
            int count = stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    public void modifyNodeYcoord(Node node, int yCoord){
        node.getLoc().setyCoord(yCoord);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET yCoord = " + yCoord + " WHERE nodeID = '" + node.getID() + "'";
            int count = stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    public void modifyNodeBuilding(Node node, String building){
        node.getLoc().setBuilding(building);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE map_nodes SET building = '" + building + "'" + " WHERE nodeID = '" + node.getID() + "'";
            int count = stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    /**
     *
     * @param node
     */
    public void removeNode (Node node) {
        int i = 0;
        System.out.println("We are here");
        while(i < nodeList.size()) {
            System.out.println("Object " + i + ": " + nodeList.get(i).getLongName());
            i++;
        }
        nodeList.remove(node);
        i = 0;
        while(i < nodeList.size()) {
            System.out.println("Object " + i + ": " + nodeList.get(i).getLongName());
            i++;
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./resources/nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM MAP_NODES WHERE nodeID = '" + node.getID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}