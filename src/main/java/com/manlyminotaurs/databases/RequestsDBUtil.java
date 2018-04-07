package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class RequestsDBUtil {

    /*------------------------------------------------ Variables -----------------------------------------------------*/
    //public static List<Request> requestList = new ArrayList<>();
    private DataModelI dataModelI = DataModelI.getInstance();
    private static int requestIDCounter = 1;

    /*------------------------------------------------ Add/Remove Request -------------------------------------------------------*/
    //TODO addRequest - add a request object instead of all of the attributes
	Request addRequest(String requestType, int priority,  String nodeID, String message, String senderID){
        MessagesDBUtil messagesDBUtil = new MessagesDBUtil();
        String messageID = messagesDBUtil.generateMessageID();
        Message mObject= messagesDBUtil.addMessage(messageID,message,false,senderID,"admin");
        Request requestObject = new Request(generateRequestID(), requestType, priority, false, false, nodeID, messageID, requestType);
        dataModelI.getRequestList().add(requestObject);

        try {
            // Connect to the database
            System.out.println("Getting connection to database...");
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            String str = "INSERT INTO Request(requestID,requestType,priority,isComplete,adminConfirm,nodeID,messageID,password) VALUES (?,?,?,?,?,?,?,?)";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, requestObject.getRequestID());
            statement.setString(2, requestObject.getRequestType());
            statement.setInt(3, requestObject.getPriority());
            statement.setBoolean(4, requestObject.getComplete());
            statement.setBoolean(5, requestObject.getAdminConfirm());
            statement.setString(6, requestObject.getNodeID());
            statement.setString(7, messageID);
            statement.setString(8, requestType);
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            System.out.println("Request added to database");
        } catch (SQLException e)
        {
            System.out.println("Request already in the database");
            e.printStackTrace();
        }
        new CsvFileController().updateRequestCSVFile("./RequestTable.csv");
        return requestObject;
    }

    boolean removeRequest(Request request) {
        boolean isSucessful = true;
        for (int i = 0; i < dataModelI.getRequestList().size(); i++) {
            if (dataModelI.getRequestList().get(i).getRequestID().equals(request.getRequestID())) {
                // remove the node
                System.out.println("Node removed from object list...");
                dataModelI.getRequestList().remove(i);
                isSucessful = true;
                break;
            }
        }
        try {
            // Get connection to database and delete the node from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM REQUEST WHERE requestID = '" + request.getRequestID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            System.out.println("Node removed from database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSucessful;
    }

    String generateRequestID(){
        requestIDCounter++;
        return Integer.toString(requestIDCounter-1);
    }

    /*------------------------------------ Set status Complete/Admin Confirm -------------------------------------------------*/
    void setIsAdminConfim(Request request, boolean newConfirmStatus){
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE Request SET ADMINCONFIRM = '" + newConfirmStatus + "'" + " WHERE requestID = '" + request.getRequestID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        new CsvFileController().updateRequestCSVFile("./RequestTable.csv");
    }

    void setIsComplete(Request request, boolean newCompleteStatus){
        request.setComplete(newCompleteStatus);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE Request SET ISCOMPLETE = '" + newCompleteStatus + "'" + " WHERE requestID = '" + request.getRequestID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        new CsvFileController().updateRequestCSVFile("./RequestTable.csv");
    }

    /*------------------------------------------------ Retrieve Request / Search by ID / Print all -------------------------------------------------------*/
    /**
     *  get data from request table in database and put them into the list of request objects
     */
    void retrieveRequests() {
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
            String messageID;
            String password;

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
                    messageID = rset.getString("messageID");
                    password = rset.getString("password");
                    // Add the new edge to the list
                    requestObject = new Request(requestID,requestType,priority,isComplete,adminConfirm,nodeID, messageID, password);
                    dataModelI.getRequestList().add(requestObject);
                    requestIDCounter++;
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
    } // retrieveRequests() ends

	Request searchRequestsByID(String requestID){
        Iterator<Request> iterator = dataModelI.getRequestList().iterator();
        while (iterator.hasNext()) {
            Request a_request = iterator.next();
            if (a_request.getRequestID().equals(requestID)) {
                return a_request;
            }
        }
        System.out.println("getMessageFromList: Something might break--------------------");
        return null;
    }

    /**
     * Print the requestList
     */
    void printRequestList() {
        int i = 0;
        while(i < dataModelI.getRequestList().size()) { System.out.println("Object " + i + ": " + dataModelI.getRequestList().get(i).getRequestID()); i++; }
    } // end printNodeList

    /*------------------------------------ Search Request by Receiver/Sender -------------------------------------------------*/
    List<Request> searchRequestByReceiver(String userID){
        MessagesDBUtil messagesDBUtil = new MessagesDBUtil();
        List<Message> listOfMessages = messagesDBUtil.searchMessageByReceiver(userID);
        List<Request> listOfRequests = new ArrayList<>();
        Iterator<Message> iteratorMessage = listOfMessages.iterator();
        Iterator<Request> iteratorRequest = dataModelI.getRequestList().iterator();

        //insert rows
        while (iteratorRequest.hasNext()) {
            Request a_request = iteratorRequest.next();
            iteratorMessage = listOfMessages.iterator();
            while (iteratorMessage.hasNext()) {
                Message a_message = iteratorMessage.next();
                if (a_request.getMessageID().equals(a_message.getMessageID())) {
                    listOfRequests.add(a_request);
                }
            }
        }
        return listOfRequests;
    }

    List<Request> searchRequestBySender(String userID){
        MessagesDBUtil messagesDBUtil = new MessagesDBUtil();
        List<Message> listOfMessages = messagesDBUtil.searchMessageBySender(userID);
        List<Request> listOfRequests = new ArrayList<>();
        Iterator<Message> iteratorMessage = listOfMessages.iterator();
        Iterator<Request> iteratorRequest = dataModelI.getRequestList().iterator();

        //insert rows
        while (iteratorRequest.hasNext()) {
            Request a_request = iteratorRequest.next();
            iteratorMessage = listOfMessages.iterator();
            while (iteratorMessage.hasNext()) {
                Message a_message = iteratorMessage.next();
                if (a_request.getMessageID().equals(a_message.getMessageID())) {
                    listOfRequests.add(a_request);
                }
            }
        }
        return listOfRequests;
    }

}
