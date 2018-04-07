package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.User;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CsvFileController {
    NodesDBUtil nodesDBUtil = new NodesDBUtil();
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
            return null;
        }
        return list_of_rows;
    } // parseCsvFile() ends

    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    public void updateNodeCSVFile(String csvFileName) {
        Iterator<Node> iterator = DataModelI.getNodeList().iterator();
        System.out.println("Updating node csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName,teamAssigned,status,xCoord3D,yCoord3D\n");
            while (iterator.hasNext()) {
                Node a_node = iterator.next();
                printWriter.printf("%s,%d,%d,%s,%s,%s,%s,%s,Team M,%d\n", a_node.getID(), a_node.getXCoord(), a_node.getYCoord(), a_node.getFloor(), a_node.getBuilding(), a_node.getNodeType(), a_node.getLongName(), a_node.getShortName(), a_node.getStatus());
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
    public void updateEdgeCSVFile(String csvFileName) {
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Iterator<Edge> iterator = nodesEditor.edgeList.iterator();
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
    public void updateRoomCSVFile(String csvFileName) {
        Iterator<Room> iterator = DataModelI.getRoomList().iterator();
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
    public void updateMessageCSVFile(String csvFileName) {
        Iterator<Message> iterator = DataModelI.getMessageList().iterator();
        System.out.println("Updating message csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("messageID,message,isRead,senderID,receiverID\n");
            while (iterator.hasNext()) {
                Message a_message = iterator.next();
                printWriter.printf("%s,%s,%b,%s,%s\n", a_message.getMessageID(), a_message.getMessage(), a_message.getRead(), a_message.getSenderID(), a_message.getReceiverID());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }//updateMessageCSVFile ends

    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    public void updateRequestCSVFile(String csvFileName) {
        Iterator<Request> iterator = DataModelI.getRequestList().iterator();
        System.out.println("Updating request csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("requestID,requestType,priority,isComplete,adminConfirm,nodeID,messageID,password\n");
            while (iterator.hasNext()) {
                Request a_request = iterator.next();
                printWriter.printf("%s,%s,%d,%b,%b,%s,%s,%s\n", a_request.getRequestID(),a_request.getRequestType(),a_request.getPriority(),a_request.getComplete(),a_request.getAdminConfirm(),a_request.getNodeID(),a_request.getMessageID(),a_request.getPassword());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }//updateRequestCSVFile ends

    /**
     * Write formatted String to CSVFile using PrintWriter class
     * @param csvFileName the csv file to be updated
     */
    public void updateUserCSVFile(String csvFileName) {
        Iterator<User> iterator = DataModelI.getUserList().iterator();
        System.out.println("Updating user csv file...");
        try {
            FileWriter fileWriter = new FileWriter(csvFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("userID,firstName,middleInitial,lastName,language\n");
            while (iterator.hasNext()) {
                User a_user = iterator.next();
                printWriter.printf("%s,%s,%s,%s,%s\n", a_user.getUserID(), a_user.getFirstName(),a_user.getMiddleInitial(),a_user.getLastName(),a_user.getLanguage());
            }
            printWriter.close();
            System.out.println("csv file updated");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }//updateUserCSVFile ends
}
