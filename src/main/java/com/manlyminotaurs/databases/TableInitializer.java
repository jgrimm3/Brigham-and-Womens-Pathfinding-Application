package com.manlyminotaurs.databases;

import com.manlyminotaurs.nodes.*;

import java.io.*;
import java.sql.*;
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
        Connection connection = DataModelI.getInstance().getNewConnection();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            tableInit.executeDBScripts("./DropTables.sql", stmt);
            tableInit.executeDBScripts("./CreateTables.sql", stmt);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            DataModelI.getInstance().closeConnection(connection);
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
        initializer.populateNodeEdgeTables("MapGNodes.csv","MapGEdges.csv");
        initializer.populateUserAccountTable("UserAccountTable.csv");
        initializer.populateMessageTable("MessageTable.csv");
        initializer.populateRequestTable("RequestTable.csv");

        //initializer.populateExitTable("./NodeExitTable.csv");
        //initializer.populateHallwayTable("./NodeHallwayTable.csv");
//        initializer.populateRoomTable(nodesDBUtil.retrieveNodes());
        //initializer.populateTransportTable(null);
    }


    /*------------------------------------------------ Populate Tables -----------------------------------------------------*/
    /**
     * Populate the database tables from the csv files
     */
    private void populateNodeEdgeTables(String CsvNodeFileName, String CsvEdgeFileName) {

        // Make sure we aren't ruining the database
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Are you sure you want to recreate the database from the csv files? (y/n): ");
//        String ans = scanner.nextLine();
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
                System.out.println("row is: " + node_id + " " + xcoord + " " + ycoord + " " + floor + " " + building + " " + nodeType + " " + long_name + " " + short_name + " " + team_assigned + " " + xCoord3D + " " + yCoord3D);

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

            System.out.println("----------------------------------------------------");
            Iterator<String[]> iterator2 = list_of_edges.iterator();
            iterator2.next(); // get rid of the header

            //insert rows
            while (iterator2.hasNext()) {
                String[] node_row = iterator2.next();
                System.out.println("row is: " + node_row[0] + " " + node_row[1] + " " + node_row[2]);

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
            DataModelI.getInstance().closeConnection(connection);
        }
    }

    private void populateMessageTable(String CsvFileName) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            // parse MessageTable.csv file
            CsvFileController csvFileControl = new CsvFileController();
            List<String[]> messageList = csvFileControl.parseCsvFile(CsvFileName);

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
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
    }

    private void populateUserAccountTable(String CsvFileName) {
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
            DataModelI.getInstance().closeConnection(connection);
        }
    }

    private void populateRequestTable(String CsvFileName) {
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
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
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

}
