package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

class TableInitializer {
    NodesDBUtil nodesDBUtil = new NodesDBUtil();
    /**
     * Delete any pre-existing tables and create new tables in the database
     */
    public void initTables(){
        TableInitializer tableInit = new TableInitializer();
        // Get the database connection
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();

            tableInit.executeDBScripts("./DropTables.sql", stmt);
            //tableInit.executeDBScripts("./src/main/resources/DropTables.sql", stmt);
            tableInit.executeDBScripts("./CreateTables.sql", stmt);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void setupDatabase(){
        // System.out.println("yolo is: " + getClass().getResource("./src/main/resources/DropTables.sql").toString());
        TableInitializer initializer = new TableInitializer();
        RequestsDBUtil requestsDB = new RequestsDBUtil();
        MessagesDBUtil messagesDBUtil = new MessagesDBUtil();
        NodesDBUtil nodesDBUtil = new NodesDBUtil();

        initializer.initTables();
        initializer.populateNodeEdgeTables("./MapGNodesEdited.csv","./MapGEdges.csv");
        initializer.populateUserAccountTable("./UserAccountTable.csv");
        initializer.populateMessageTable("./MessageTable.csv");
        initializer.populateRequestTable("./RequestTable.csv");

        nodesDBUtil.retrieveNodes();
        nodesDBUtil.retrieveEdges();

        initializer.populateExitTable("./NodeExitTable.csv");
        initializer.populateHallwayTable("./NodeHallwayTable.csv");
        initializer.populateRoomTable();
        initializer.populateTransportTable();

        messagesDBUtil.retrieveMessage();
        requestsDB.retrieveRequest();
        nodesDBUtil.retrieveUser();
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
        InputStream inputStream = null;
        try {
            System.out.println("executeDBScripts: "+ getClass().getName());
            inputStream = getClass().getClassLoader().getResourceAsStream(aSQLScriptFilePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            StringBuffer sb;
            sb = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
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

            bufferedReader.close();
            isScriptExecuted = true;
        } catch (Exception e) {
            System.err.println("Failed to Execute " + aSQLScriptFilePath +". The error is "+ e.getMessage());
            if(inputStream == null){
                System.err.println("in is null");
            }
        }
        return isScriptExecuted;
    }

    /**
     * Populate the database tables from the csv files
     */
    public void populateNodeEdgeTables(String CsvNodeFileName, String CsvEdgeFileName) {

        // Make sure we aren't ruining the database
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Are you sure you want to recreate the database from the csv files? (y/n): ");
//        String ans = scanner.nextLine();
        String ans = "y";
        // If you're positive...
        if(ans.equals("y")) {
            try {
                // Variables we need to make the tables
                CsvFileController csvFileControl = new CsvFileController();
                List<String[]> list_of_nodes;
                List<String[]> list_of_edges;
                list_of_nodes = csvFileControl.parseCsvFile(CsvNodeFileName);
                list_of_edges = csvFileControl.parseCsvFile(CsvEdgeFileName);

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

    /**
     * populates the exit table in database either from csvFIle or nodeLists
     * @param csvFileName
     */
    public void populateExitTable(String csvFileName){
        CsvFileController csvFileControl = new CsvFileController();
        List<String[]> listOfExit = csvFileControl.parseCsvFile(csvFileName);
        if(listOfExit != null){
            populateExitTableFromCsv(listOfExit);
        }
        else{
            populateExitTableFromList();
        }
    }

    /**
     * Use listOfExit node objects parsed from the csv file to populate Exit table
     * @param listOfExit
     */
    public void populateExitTableFromCsv(List<String[]> listOfExit) {
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();

            Iterator<String[]> iterator = listOfExit.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                String[] node_row = iterator.next();
                System.out.println("row is: " + node_row[0] + " " + node_row[1] + " " + node_row[2]);

                String str = "INSERT INTO Exit(isFireExit,isArmed, nodeID) VALUES (?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setBoolean(1, Boolean.valueOf(node_row[0]));
                statement.setBoolean(2, Boolean.valueOf(node_row[1]));
                statement.setString(3, node_row[2]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * insert values into Exit table by identifying nodes having nodeType = EXIT in the nodeList
     */
    public void populateExitTableFromList() {
        Statement stmt = null;
        Connection connection = null;
        try {
            // Variables we need to make the tables
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();

            for(int i = 0; i< nodesDBUtil.nodeList.size(); i++) {
                if(nodesDBUtil.nodeList.get(i).getNodeType().equals("EXIT")) {
                    System.out.println("Found an exit...");
                    Exit exit = (Exit) nodesDBUtil.nodeList.get(i);
                    nodesDBUtil.exitList.add(exit);

                    String str = "INSERT INTO exit(isFireExit,isArmed,nodeID) VALUES (?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setBoolean(1, exit.isFireExit());
                    statement.setBoolean(2, exit.isArmed());
                    statement.setString(3, exit.getID());
                    statement.executeUpdate();
                    System.out.println("Added exit to table...");
                }
            }
        }
        catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }//populateExitTableFromList

//--------------------------------------------------------------------------------------
    /**
     * populates the hallway table in database either from csvFIle or nodeLists
     * @param csvFileName
     */
    public void populateHallwayTable(String csvFileName){
        CsvFileController csvFileControl = new CsvFileController();
        List<String[]> listOfNodes = csvFileControl.parseCsvFile(csvFileName);
        if(listOfNodes != null){
            System.out.println("populateHallwayTableFromCsv");
            populateHallwayTableFromCsv(listOfNodes);
        }
        else{
            System.out.println("populateHallwayTableFromList");
            populateHallwayTableFromList();
        }
    }

    /**
     * Use list of nodes parsed from the csv file to populate Hallway table
     * @param listOfNodes
     */
    public void populateHallwayTableFromCsv(List<String[]> listOfNodes) {
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();

            Iterator<String[]> iterator = listOfNodes.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                String[] node_row = iterator.next();
                System.out.println("row is: " + node_row[0] + " " + node_row[1]);

                String str = "INSERT INTO Hallway(popularity, nodeID) VALUES (?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setInt(1, Integer.parseInt(node_row[0]));
                statement.setString(2, node_row[1]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * insert values into Exit table by identifying nodes having nodeType = EXIT in the nodeList
     */
    public void populateHallwayTableFromList() {
        Statement stmt = null;
        Connection connection = null;
        try {
            // Variables we need to make the tables
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();

            for(int i = 0; i<NodesDBUtil.nodeList.size(); i++) {
                if(NodesDBUtil.nodeList.get(i).getNodeType().equals("HALL")) {
                    try {
                        System.out.println("Found an hallway...");
                        Hallway hall = (Hallway) nodesDBUtil.nodeList.get(i);
                        nodesDBUtil.hallwayList.add(hall);

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
            }
        }
        catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }//populateHallwayTableFromList

    public void populateMessageTable(String CsvFileName) {
        try {
            // parse MessageTable.csv file
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> messageList = csvFileControl.parseCsvFile(CsvFileName);

            // Get the database connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();

            Iterator<String[]> iterator = messageList.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                String[] node_row = iterator.next();
                String str = "INSERT INTO message(messageID,message,isRead,senderID,receiverID) VALUES (?,?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setString(1, node_row[0]);
                statement.setString(2, node_row[1]);
                statement.setBoolean(3, Boolean.valueOf(node_row[2]));
                statement.setString(4, node_row[3]);
                statement.setString(5, node_row[4]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateUserAccountTable(String CsvFileName) {
        try {
            // parse UserTable.csv file
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> userAccountList = csvFileControl.parseCsvFile(CsvFileName);

            // Get the database connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();

            Iterator<String[]> iterator = userAccountList.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                String[] node_row = iterator.next();
                String str = "INSERT INTO UserAccount(userID,firstName,middleInitial,lastName,language) VALUES (?,?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setString(1, node_row[0]);
                statement.setString(2, node_row[1]);
                statement.setString(3, node_row[2]);
                statement.setString(4, node_row[3]);
                statement.setString(5, node_row[4]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateRequestTable(String CsvFileName) {
        try {
            // parse UserTable.csv file
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> userAccountList = csvFileControl.parseCsvFile(CsvFileName);

            // Get the database connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();

            Iterator<String[]> iterator = userAccountList.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                String[] node_row = iterator.next();
                String str = "INSERT INTO Request(requestID,requestType,priority,isComplete,adminConfirm,nodeID,messageID,PASSWORD) VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setString(1, node_row[0]);
                statement.setString(2, node_row[1]);
                statement.setInt(3, Integer.parseInt(node_row[2]));
                statement.setBoolean(4, Boolean.valueOf(node_row[3]));
                statement.setBoolean(5, Boolean.valueOf(node_row[4]));
                statement.setString(6, node_row[5]);
                statement.setString(7, node_row[6]);
                statement.setString(8, node_row[7]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                list_of_nodes = csvFileControl.parseCsvFile("./MapGnodes.csv");
                list_of_edges = csvFileControl.parseCsvFile("./MapGedges.csv");

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

    public void populateExitTable(List<Node> nodeList) {
        int i = 0;
        Statement stmt = null;
        Connection connection = null;
        List<Exit> exitList = new ArrayList<>();
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

    public void populateHallwayTable(List<Node> nodeList) {
        int i = 0;
        Statement stmt = null;
        Connection connection = null;
        List<Hallway> hallwayList = new ArrayList<>();
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
        while(i < nodesDBUtil.nodeList.size()) {
            type = nodesDBUtil.nodeList.get(i).getNodeType();
            if(type.equals("DEPT") || type.equals("RETA") || type.equals("LABS") || type.equals("REST") || type.equals("SERV") || type.equals("INFO") || type.equals("CONF")) {
                try {
                    System.out.println("Found an room...");
                    Room room = (Room) nodesDBUtil.nodeList.get(i);
                    nodesDBUtil.roomList.add(room);

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
        while(i < nodesDBUtil.nodeList.size()) {
            String type = nodesDBUtil.nodeList.get(i).getNodeType();
            if(type.equals("STAI") || type.equals("ELEV")) {
                try {
                    System.out.println("Found an transport...");
                    Transport transport = (Transport) nodesDBUtil.nodeList.get(i);
                    nodesDBUtil.transportList.add(transport);

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
    }// populateTransportTable Ends

}
