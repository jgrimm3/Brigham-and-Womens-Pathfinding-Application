package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

class TableInitializer {
    /*------------------------------------------------ Initialize Tables -----------------------------------------------------*/
    /**
     * Delete any pre-existing tables and create new tables in the database
     */
    private void initTables(){
        TableInitializer tableInit = new TableInitializer();
        // Get the database connection
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:nodesDB;create=true");
            stmt = connection.createStatement();
            tableInit.executeDBScripts("/DropTables.sql", stmt);
            tableInit.executeDBScripts("/CreateTables.sql", stmt);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { stmt.close();connection.close(); } catch (Exception e) { /* ignored */ }

        }
    }

    void setupDatabase(){
        System.out.println("Registering Oracle Driver");
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Oracle JDBC Driver Registered Successfully !");


        TableInitializer initializer = new TableInitializer();
        NodesDBUtil nodesDBUtil = new NodesDBUtil();

        initializer.initTables();

        UserSecurity userSecurity = new UserSecurity();
        //initializer.populateAllNodeEdgeTables();
        initializer.populateNodeEdgeTables("./nodes.csv","./edges.csv");
        initializer.populateRoomTable(nodesDBUtil.getNodeList());
        UserDBUtil.setUserIDCounter(initializer.populateUserAccountTable("./UserAccountTable.csv"));
        MessagesDBUtil.setMessageIDCounter(initializer.populateMessageTable("./MessageTable.csv"));
        RequestsDBUtil.setRequestIDCounter(initializer.populateRequestTable("./RequestTable.csv"));
        LogDBUtil.setlogIDCounter(initializer.populateLogTable("./LogTable.csv"));
        initializer.populateStaffTable("./StaffTable.csv");
        initializer.populateUserPasswordTable("./UserPasswordTable.csv");
        nodesDBUtil.updateNodeMap();

        System.out.println("-----------------------------");
        System.out.println("-----------------------------");
        System.out.println("Finished Setting up Database");
        System.out.println("-----------------------------");
        System.out.println("-----------------------------");
        //initializer.populateExitTable("./NodeExitTable.csv");
        //initializer.populateHallwayTable("./NodeHallwayTable.csv");
        ;
        //initializer.populateTransportTable(null);
    }


    /*------------------------------------------------ Populate Tables -----------------------------------------------------*/

    /**
     * Populate the database tables from the csv files
     */
    private void populateAllNodeEdgeTables() {
        String[] listOfCsvFiles = {"W","I","C","D","E","F","G","H","B","A"};
        //MapAnodes.csv
        //MapAedges.csv
        Connection connection = DataModelI.getInstance().getNewConnection();
        CsvFileController csvFileControl = new CsvFileController();
        try {
            for(int i=0; i< listOfCsvFiles.length ;i++) {
                String csvNodeFileName = "./Map"+listOfCsvFiles[i]+"nodes.csv";
                List<String[]> list_of_nodes;
                list_of_nodes = csvFileControl.parseCsvFile(csvNodeFileName);

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
                String status = "1";
                String xCoord3D;
                String yCoord3D;

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
                    xCoord3D = node_row[9];
                    yCoord3D = node_row[10];
                  //  System.out.println("row is: " + node_id + " " + xcoord + " " + ycoord + " " + floor + " " + building + " " + nodeType + " " + long_name + " " + short_name + " " + team_assigned + " " + xCoord3D + " " + yCoord3D);

                    // Add to the database table
                    String str = "INSERT INTO map_nodes(nodeID,xCoord,yCoord,floor,building,nodeType,longName,shortName,status,xCoord3D,yCoord3d) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
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
                    statement.setInt(10, Integer.parseInt(xCoord3D));
                    statement.setInt(11, Integer.parseInt(yCoord3D));
                    statement.executeUpdate();
                }// while loop ends
            }

            for(int i=0; i< listOfCsvFiles.length ;i++) {
                String csvEdgeFileName = "./Map" + listOfCsvFiles[i] + "edges.csv";
                List<String[]> list_of_edges;
                list_of_edges = csvFileControl.parseCsvFile(csvEdgeFileName);
                Iterator<String[]> iterator2 = list_of_edges.iterator();
                iterator2.next(); // get rid of the header

                //insert rows
                while (iterator2.hasNext()) {
                    String[] node_row = iterator2.next();
                   // System.out.println("row is: " + node_row[0] + " " + node_row[1] + " " + node_row[2]);

                    String str = "INSERT INTO map_edges(edgeID, startNodeID, endNodeID, status) VALUES (?,?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setString(1, node_row[0]);
                    statement.setString(2, node_row[1]);
                    statement.setString(3, node_row[2]);
                    statement.setInt(4, 1);
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
    }



    /**
     * Populate the database tables from the csv files
     */
    private void populateNodeEdgeTables(String CsvNodeFileName, String CsvEdgeFileName) {

        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            // Variables we need to make the tables
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> list_of_nodes;
            List<String[]> list_of_edges;
            list_of_nodes = csvFileControl.parseCsvFile(CsvNodeFileName);
            list_of_edges = csvFileControl.parseCsvFile(CsvEdgeFileName);

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
            String xCoord3D;
            String yCoord3D;

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
                xCoord3D = node_row[10];
                yCoord3D = node_row[11];
        //        System.out.println("row is: " + node_id + " " + xcoord + " " + ycoord + " " + floor + " " + building + " " + nodeType + " " + long_name + " " + short_name + " " + team_assigned + " " + xCoord3D + " " + yCoord3D);

                // Add to the database table
                String str = "INSERT INTO map_nodes(nodeID,xCoord,yCoord,floor,building,nodeType,longName,shortName,status,xCoord3D,yCoord3d) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
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
                statement.setInt(10, Integer.parseInt(xCoord3D));
                statement.setInt(11, Integer.parseInt(yCoord3D));
                statement.executeUpdate();
            }// while loop ends

            Iterator<String[]> iterator2 = list_of_edges.iterator();
            iterator2.next(); // get rid of the header

            //insert rows
            while (iterator2.hasNext()) {
                String[] node_row = iterator2.next();
        //        System.out.println("row is: " + node_row[0] + " " + node_row[1] + " " + node_row[2]);

                String str = "INSERT INTO map_edges(edgeID, startNodeID, endNodeID,status) VALUES (?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setString(1, node_row[0]);
                statement.setString(2, node_row[1]);
                statement.setString(3, node_row[2]);
                statement.setInt(4,Integer.parseInt(node_row[3]));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Node and Edges added successfully");
            DataModelI.getInstance().closeConnection();
        }
    }


    private int populateUserAccountTable(String CsvFileName) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        int userIDCounter = 0;
        try {
            // parse UserTable.csv file
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> userAccountList = csvFileControl.parseCsvFile(CsvFileName);
            if(userAccountList == null){
                return 0;
            }

            Statement stmt = connection.createStatement();

            Iterator<String[]> iterator = userAccountList.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                userIDCounter++;
                String[] node_row = iterator.next();
                String str = "INSERT INTO UserAccount(userID,firstName,middleName,lastName,language, userType) VALUES (?,?,?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setString(1, node_row[0]);
                statement.setString(2, node_row[1]);
                statement.setString(3, node_row[2]);
                statement.setString(4, node_row[3]);
                statement.setString(5, node_row[4]);
                statement.setString(6, node_row[5]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return userIDCounter;
    }

    private void populateStaffTable(String CsvFileName) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            // parse UserTable.csv file
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> userAccountList = csvFileControl.parseCsvFile(CsvFileName);

            Statement stmt = connection.createStatement();

            Iterator<String[]> iterator = userAccountList.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                String[] node_row = iterator.next();
                String str = "INSERT INTO Staff(isWorking, isAvailable, languageSpoken, userID) VALUES (?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setBoolean(1, Boolean.valueOf(node_row[0]));
                statement.setBoolean(2, Boolean.valueOf(node_row[1]));
                statement.setString(3, node_row[2]);
                statement.setString(4, node_row[3]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
    }

    private void populateUserPasswordTable(String CsvFileName) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            // parse UserTable.csv file
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> userAccountList = csvFileControl.parseCsvFile(CsvFileName);

            Statement stmt = connection.createStatement();

            Iterator<String[]> iterator = userAccountList.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                String[] node_row = iterator.next();
                String str = "INSERT INTO UserPassword(userName, password, userID) VALUES (?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setString(1, node_row[0]);
                statement.setString(2, node_row[1]);
                statement.setString(3, node_row[2]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
    }

    private int populateMessageTable(String CsvFileName) {
        int messageIDCounter = 0;
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            // parse MessageTable.csv file
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> messageList = csvFileControl.parseCsvFile(CsvFileName);
            if(messageList == null){
                return 0;
            }

            Statement stmt = connection.createStatement();

            Iterator<String[]> iterator = messageList.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                messageIDCounter++;
                String[] node_row = iterator.next();
                String str = "INSERT INTO message(messageID,message,isRead,sentDate,senderID,receiverID) VALUES (?,?,?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setString(1, node_row[0]);
                statement.setString(2, node_row[1]);
                statement.setBoolean(3, Boolean.valueOf(node_row[2]));
                statement.setDate(4,convertStringToDate(node_row[3]));
                statement.setString(5, node_row[4]);
                statement.setString(6, node_row[5]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return messageIDCounter;
    }

    private int populateRequestTable(String CsvFileName) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        int requestIDCounter = 0;
        try {
            // parse UserTable.csv file
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> requestList = csvFileControl.parseCsvFile(CsvFileName);
            if(requestList == null){
                return 0;
            }

            Statement stmt = connection.createStatement();

            Iterator<String[]> iterator = requestList.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                requestIDCounter++;
                String[] node_row = iterator.next();
                String str = "INSERT INTO Request(requestID,requestType,priority,isComplete,adminConfirm,startTime,endTime,nodeID,messageID,PASSWORD) VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setString(1, node_row[0]);
                statement.setString(2, node_row[1]);
                statement.setInt(3, Integer.parseInt(node_row[2]));
                statement.setBoolean(4, Boolean.valueOf(node_row[3]));
                statement.setBoolean(5, Boolean.valueOf(node_row[4]));
                statement.setTimestamp(6, convertStringToTimestamp(node_row[5]));
                statement.setTimestamp(7, convertStringToTimestamp(node_row[6]));
                statement.setString(8, node_row[7]);
                statement.setString(9, node_row[8]);
                statement.setString(10, node_row[9]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return requestIDCounter;
    }

    private void populateRoomTable(List<Node> nodeList) {
        int i = 0;
        String type;
        Statement stmt = null;
        Connection connection = DataModelI.getInstance().getNewConnection();
        while(i < nodeList.size()) {
            type = nodeList.get(i).getNodeType();
            if(type.equals("DEPT") || type.equals("RETA") || type.equals("LABS") || type.equals("REST") || type.equals("SERV") || type.equals("INFO") || type.equals("CONF")) {
                try {
                    System.out.println("Found an room...");
                    Room room = (Room) nodeList.get(i);

                    String str = "INSERT INTO room(specialization, detail, popularity, isOpen, nodeID) VALUES (?,?,?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(str);
                    statement.setString(1, room.getSpecialization());
                    statement.setString(2, room.getDetailedInfo());
                    statement.setInt(3, room.getPopularity());
                    statement.setBoolean(4, room.isOpen());
                    statement.setInt(5, room.getStatus());
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

    private int populateLogTable(String CsvFileName){
        Connection connection = DataModelI.getInstance().getNewConnection();
        int logIDCounter = 0;
        try {
            // parse LogTable.csv file
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> logList = csvFileControl.parseCsvFile(CsvFileName);
            if(logList == null){
                return 0;
            }

            Statement stmt = connection.createStatement();

            Iterator<String[]> iterator = logList.iterator();
            iterator.next(); // get rid of the header

            //insert rows
            while (iterator.hasNext()) {
                logIDCounter++;
                String[] node_row = iterator.next();

                String str = "INSERT INTO LOG(logID,description,logTime,userID,associatedID,associatedType) VALUES (?,?,?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(str);
                statement.setString(1, node_row[0]);
                statement.setString(2, node_row[1]);
                statement.setTimestamp(3, convertStringToTimestamp(node_row[2]));
                statement.setString(4, node_row[3]);
                statement.setString(5, node_row[4]);
                statement.setString(6, node_row[5]);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return logIDCounter;
    }


    /*------------------------------------------------ Execute Database Scripts -----------------------------------------------------*/
    /**
     *
     * @param aSQLScriptFilePath path to the sql file to run
     * @param stmt statement object passed from callee
     * @return true if sql file is executed successfully.
     * @throws IOException
     * @throws SQLException
     */
    private boolean executeDBScripts(String aSQLScriptFilePath, Statement stmt) throws IOException,SQLException {
        boolean isScriptExecuted = false;
        InputStream inputStream = null;
        try {
            System.out.println("executeDBScripts: "+ getClass().getName());
            inputStream = getClass().getResourceAsStream(aSQLScriptFilePath);
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

    public Timestamp convertStringToTimestamp(String timeString) {/*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss");
       // formatter = formatter.withLocale( putAppropriateLocaleHere );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
        LocalDateTime date = LocalDateTime.parse(timeString, formatter);
        return Timestamp.valueOf(date);*/

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:S");
        Date parsedTimeStamp = null;
        try {
            parsedTimeStamp = dateFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Timestamp(parsedTimeStamp.getTime());
    }

    public java.sql.Date convertStringToDate(String timeString) {/*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss");
       // formatter = formatter.withLocale( putAppropriateLocaleHere );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
        LocalDateTime date = LocalDateTime.parse(timeString, formatter);
        return Timestamp.valueOf(date);*/

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf1.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
        return sqlStartDate;
    }
}
