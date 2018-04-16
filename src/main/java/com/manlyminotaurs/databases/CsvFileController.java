package com.manlyminotaurs.databases;

import com.manlyminotaurs.log.Log;
import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.StaffFields;
import com.manlyminotaurs.users.User;
import com.manlyminotaurs.users.UserPassword;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//
//  .oPYo. .oPYo. o     o   .oPYo.                o               8 8
//  8    8 8      8     8   8    8                8               8 8
//  8      `Yooo. 8     8   8      .oPYo. odYo.  o8P oPYo. .oPYo. 8 8 .oPYo. oPYo.
//  8          `8 `b   d'   8      8    8 8' `8   8  8  `' 8    8 8 8 8oooo8 8  `'
//  8    8      8  `b d'    8    8 8    8 8   8   8  8     8    8 8 8 8.     8
//  `YooP' `YooP'   `8'     `YooP' `YooP' 8   8   8  8     `YooP' 8 8 `Yooo' 8
//  :.....::.....::::..::::::.....::.....:..::..::..:..:::::.....:....:.....:..::::
//  :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//  :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//

public class CsvFileController {

    /*---------------------------------- Parse CSV File --------------------------------------------------*/
    /**
     * http://www.avajava.com/tutorials/lessons/how-do-i-read-a-string-from-a-file-line-by-line.html
     * https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
     * @param csv_file_name the name of the csv file
     * @return arrayList of columns from the csv
     */
    public List<String[]> parseCsvFile(String csv_file_name) {
        System.out.println("Parsing csv file: "+csv_file_name);
        List<String[]> list_of_rows = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(csv_file_name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // use comma as separator
                String[] node_row = line.split(",");
                list_of_rows.add(node_row);
            }
            fileReader.close();
            System.out.println("csv file parsed");
/*
            InputStream inputStream = getClass().getResourceAsStream(csv_file_name);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // use comma as separator
                String[] node_row = line.split(",");
                list_of_rows.add(node_row);
            }
            inputStream.close();*/
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return list_of_rows;
    } // parseCsvFile() ends


    /*---------------------------------- Update CSV Files --------------------------------------------------*/

    public void updateAllCSVFiles(){
        updateNodeCSVFile("./nodes.csv");
        updateEdgeCSVFile("./edges.csv");
        updateMessageCSVFile("./MessageTable.csv");
        updateRequestCSVFile("./RequestTable.csv");
        updateUserCSVFile("./UserAccountTable.csv");
        updateUserPasswordFile("./UserPasswordTable.csv");
        updateStaffTable("./StaffTable.csv");
        updateLogCSVFile("./LogTable.csv");
    }

    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    private void updateNodeCSVFile(String csvFileName) {
        Iterator iterator = DataModelI.getInstance().getNodeMap().entrySet().iterator();
        System.out.println("Updating node csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName,teamAssigned,status,xCoord3D,yCoord3D\n");
            while (iterator.hasNext()) {
                Map.Entry a_entry = (Map.Entry)iterator.next();
                Node a_node = (Node) a_entry.getValue();
                printWriter.printf("%s,%d,%d,%s,%s,%s,%s,%s,Team M,%d,%d,%d\n", a_node.getNodeID(), a_node.getXCoord(), a_node.getYCoord(), a_node.getFloor(), a_node.getBuilding(), a_node.getNodeType(), a_node.getLongName(), a_node.getShortName(), a_node.getStatus(), a_node.getXCoord3D(), a_node.getYCoord3D());
            }
            printWriter.close();
            System.out.println("csv node file updated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    private void updateEdgeCSVFile(String csvFileName) {
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:nodesDB;create=true");
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Iterator<Edge> iterator = DataModelI.getInstance().getEdgeList().iterator();
        System.out.println("Updating edge csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("edgeID,startNodeID,endNodeID,status\n");
            while (iterator.hasNext()) {
                Edge a_edge = iterator.next();
                printWriter.printf("%s,%s,%s,%d\n", a_edge.getEdgeID(), a_edge.getStartNodeID(), a_edge.getEndNodeID(), a_edge.getStatus());
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
    private void updateRoomCSVFile(String csvFileName) {
        Iterator<Room> iterator = null;//DataModelI.getInstance().retrieveNodes();
        System.out.println("Updating room csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("specialization, detail, popularity, isOpen, nodeID\n");
            while (iterator.hasNext()) {
                Room a_node = iterator.next();
                printWriter.printf("%s,%s,%d,%b,%s\n", a_node.getSpecialization(), a_node.getDetailedInfo(), a_node.getPopularity(), a_node.isOpen(), a_node.getStatus());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    /*---------------------------------- Messages --------------------------------------------------*/
    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    private void updateMessageCSVFile(String csvFileName) {
        Iterator<Message> iterator = DataModelI.getInstance().retrieveMessages().iterator();
        System.out.println("Updating message csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("messageID,message,isRead,sentTime,senderID,receiverID\n");
            while (iterator.hasNext()) {
                Message a_message = iterator.next();
                printWriter.printf("%s,%s,%b,%s,%s,%s\n", a_message.getMessageID(), a_message.getMessage(), a_message.getRead(), a_message.getSentDate().toString(), a_message.getSenderID(), a_message.getReceiverID());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }//updateMessageCSVFile ends


    /*---------------------------------- Requests --------------------------------------------------*/
    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    private void updateRequestCSVFile(String csvFileName) {
        Iterator<Request> iterator = DataModelI.getInstance().retrieveRequests().iterator();
        System.out.println("Updating request csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("requestID,requestType,priority,isComplete,adminConfirm,startTime,endTime,nodeID,messageID,password\n");
            while (iterator.hasNext()) {
                Request a_request = iterator.next();
                printWriter.printf("%s,%s,%d,%b,%b,%s,%s,%s,%s,%s\n", a_request.getRequestID(),a_request.getRequestType(),a_request.getPriority(),a_request.getComplete(),a_request.getAdminConfirm(), a_request.getStartTime().toString().replace("T"," ").replace(".",":"), a_request.getEndTime().toString().replace("T"," ").replace(".",":"),a_request.getNodeID(),a_request.getMessageID(),a_request.getPassword());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }//updateRequestCSVFile ends


    /*---------------------------------- Users --------------------------------------------------*/
    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    private void updateUserCSVFile(String csvFileName) {
        Iterator<User> iterator = DataModelI.getInstance().retrieveUsers().iterator();
        System.out.println("Updating user csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("userID,firstName,middleName,lastName,language, userType\n");
            while (iterator.hasNext()) {
                User a_user = iterator.next();
                printWriter.printf("%s,%s,%s,%s,%s,%s\n", a_user.getUserID(), a_user.getFirstName(),a_user.getMiddleName(),a_user.getLastName(),DataModelI.getInstance().getLanguageString(a_user.getLanguages()),a_user.getUserType());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }//updateUserCSVFile ends

    private void updateStaffTable(String csvFileName) {
        Iterator<StaffFields> iterator = DataModelI.getInstance().retrieveStaffs().iterator();
        System.out.println("Updating user csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("userID,firstName,middleName,lastName,language, userType\n");
            while (iterator.hasNext()) {
                StaffFields staffFields = iterator.next();
                printWriter.printf("%b,%b,%s,%s\n", staffFields.isWorking(), staffFields.isAvailable(),staffFields.getLanguageSpoken(),staffFields.getUserID());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }//updateStaffCSVFile ends

    private void updateUserPasswordFile(String csvFileName) {
        Iterator<UserPassword> iterator = DataModelI.getInstance().retrieveUserPasswords().iterator();
        System.out.println("Updating user csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("userName, password, userID\n");
            while (iterator.hasNext()) {
                UserPassword userPassword = iterator.next();
                printWriter.printf("%s,%s,%s\n", userPassword.getUserName(), userPassword.getPassword(),userPassword.getUserID());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }//updateUserPasswordCSVFile ends


    ///----------------------------------------Log-----------------------------------
    private void updateLogCSVFile(String csvFileName) {
        Iterator<Log> iterator = DataModelI.getInstance().retrieveLogData().iterator();
        System.out.println("Updating log csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("logID,description,logTime,userID,associatedID,associatedType\n");
            while (iterator.hasNext()) {
                Log a_log = iterator.next();
                printWriter.printf("%s,%s,%s,%s,%s,%s\n", a_log.getLogID(), a_log.getDescription(), a_log.getLogTime().toString().replace("T"," ").replace(".",":"), a_log.getUserID(), a_log.getAssociatedID(), a_log.getAssociatedType());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }//updateLogCSVFile ends

}
