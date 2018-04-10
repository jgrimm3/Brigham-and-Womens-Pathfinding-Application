package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.messaging.RequestFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class RequestsDBUtil {

    /*------------------------------------------------ Variables -----------------------------------------------------*/
    //public static List<Request> requestList = new ArrayList<>();
    private static int requestIDCounter = 1;

    /*------------------------------------------------ Add/Remove Request -------------------------------------------------------*/
    //TODO addRequest - add a request object instead of all of the attributes
	Request addRequest(Request requestObject, Message message){
        Connection connection = DataModelI.getInstance().getNewConnection();
        MessagesDBUtil messagesDBUtil = new MessagesDBUtil();
        Message mObject= messagesDBUtil.addMessage(message);
        try {
            String str = "INSERT INTO Request(requestID,requestType,priority,isComplete,adminConfirm,nodeID,messageID,password) VALUES (?,?,?,?,?,?,?,?)";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, requestObject.getRequestID());
            statement.setString(2, requestObject.getRequestType());
            statement.setInt(3, requestObject.getPriority());
            statement.setBoolean(4, requestObject.getComplete());
            statement.setBoolean(5, requestObject.getAdminConfirm());
            statement.setString(6, requestObject.getNodeID());
            statement.setString(7, mObject.getMessageID());
            statement.setString(8, requestObject.getRequestType());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            statement.close();
            System.out.println("Request added to database");
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
        return requestObject;
    }

    boolean removeRequest(Request request) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSucessful = true;
        try {
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM REQUEST WHERE requestID = '" + request.getRequestID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            System.out.println("Node removed from database");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
        return isSucessful;
    }

    public boolean modifyRequest(Request newRequest) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        try {
            String str = "UPDATE Request SET requestType= ?,priority = ?,isComplete= ?,adminConfirm= ?,nodeID= ?,messageID= ?,password= ? WHERE requestID = '"+ newRequest.getRequestID()+ "'";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, newRequest.getRequestType());
            statement.setInt(2, newRequest.getPriority());
            statement.setBoolean(3, newRequest.getComplete());
            statement.setBoolean(4, newRequest.getAdminConfirm());
            statement.setString(5, newRequest.getNodeID());
            statement.setString(6, newRequest.getMessageID());
            statement.setString(7, newRequest.getPassword());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            statement.close();
            isSuccess = true;
        } catch (SQLException e)
        {
            System.out.println("Request already in the database");
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
        return isSuccess;
    }

    public String generateRequestID(){
        requestIDCounter++;
        return Integer.toString(requestIDCounter-1);
    }

    /*------------------------------------ Set status Complete/Admin Confirm -------------------------------------------------*/
    void setIsAdminConfim(Request request, boolean newConfirmStatus){
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE Request SET ADMINCONFIRM = '" + newConfirmStatus + "'" + " WHERE requestID = '" + request.getRequestID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
        new CsvFileController().updateRequestCSVFile("./RequestTable.csv");
    }

    void setIsComplete(Request request, boolean newCompleteStatus){
        Connection connection = DataModelI.getInstance().getNewConnection();
        request.setComplete(newCompleteStatus);
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE Request SET ISCOMPLETE = '" + newCompleteStatus + "'" + " WHERE requestID = '" + request.getRequestID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
        new CsvFileController().updateRequestCSVFile("./RequestTable.csv");
    }

    /*------------------------------------------------ Retrieve Request / Search by ID / Print all -------------------------------------------------------*/
    /**
     *  get data from request table in database and put them into the list of request objects
     */
    List<Request> retrieveRequests() {
            // Connection
            Connection connection = DataModelI.getInstance().getNewConnection();

            // Variables
            RequestFactory rFactory = new RequestFactory();
            Request requestObject;
            String requestID;
            String requestType;
            int priority;
            Boolean isComplete;
            Boolean adminConfirm;
            String nodeID;
            String messageID;
            String password;
            List<Request> listOfRequest = new ArrayList<>();
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
                    requestObject = rFactory.genExistingRequest(requestID, requestType, priority, isComplete, adminConfirm, nodeID, messageID, password);
                    listOfRequest.add(requestObject);
                    requestIDCounter++;
                    System.out.println("Request added to the list: "+requestID);
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding Requests");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DataModelI.getInstance().closeConnection(connection);
            }
        return listOfRequest;
    } // retrieveRequests() ends

	Request getRequestByID(String requestID){
        // Connection
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        RequestFactory rFactory = new RequestFactory();
        Request requestObject = null;
        String requestType;
        int priority;
        Boolean isComplete;
        Boolean adminConfirm;
        String nodeID;
        String messageID;
        String password;
        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM Request WHERE requestID = '" + requestID + "'";
            ResultSet rset = stmt.executeQuery(str);

            if (rset.next()) {
                requestID = rset.getString("requestID");
                requestType = rset.getString("requestType");
                priority = rset.getInt("priority");
                isComplete =rset.getBoolean("isComplete");
                adminConfirm = rset.getBoolean("adminConfirm");
                nodeID = rset.getString("nodeID");
                messageID = rset.getString("messageID");
                password = rset.getString("password");
                // Add the new edge to the list
                requestObject = rFactory.genExistingRequest(requestID, requestType, priority, isComplete, adminConfirm, nodeID, messageID, password);


                System.out.println("Request added to the list: "+requestID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding Request");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
        return requestObject;
    }

    /*------------------------------------ Search Request by Receiver/Sender -------------------------------------------------*/

    /**
     * to find requests that has message object which has given receiverID
     * @param receiverID
     * @return
     */
    List<Request> searchRequestsByReceiver(String receiverID){
        List<Request> selectedRequests = new ArrayList<>();
        List<Request> listOfRequests = retrieveRequests();
        List<Message> listOfMessages = DataModelI.getInstance().getMessageByReceiver(receiverID);
        for(Request a_request: listOfRequests){
            for(Message a_message: listOfMessages){
                if(a_request.getMessageID().equals(a_message.getMessageID())){
                    selectedRequests.add(a_request);
                    break;
                }
            }
        }
        return selectedRequests;
    }

    /**
     * To find requests that has message object which has given senderID
     * @param senderID
     * @return
     */
    List<Request> searchRequestsBySender(String senderID){
        List<Request> selectedRequests = new ArrayList<>();
        List<Request> listOfRequests = retrieveRequests();
        List<Message> listOfMessages = DataModelI.getInstance().getMessageBySender(senderID);
        for(Request a_request: listOfRequests){
            for(Message a_message: listOfMessages){
                if(a_request.getMessageID().equals(a_message.getMessageID())){
                    selectedRequests.add(a_request);
                    break;
                }
            }
        }
        return selectedRequests;
    }

}
