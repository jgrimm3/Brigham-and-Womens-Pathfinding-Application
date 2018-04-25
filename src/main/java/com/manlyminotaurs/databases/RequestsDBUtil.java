package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.messaging.RequestFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//   __   __          ___          ___
//  |  \ |__)    |  |  |  | |    |  |  \ /
//  |__/ |__)    \__/  |  | |___ |  |   |
//
//         __   ___  __        ___  __  ___  __
//     __ |__) |__  /  \ |  | |__  /__`  |  /__`
//        |  \ |___ \__X \__/ |___ .__/  |  .__/
//

class RequestsDBUtil {

    /*------------------------------------------------ Variables -----------------------------------------------------*/
    //public static List<Request> requestList = new ArrayList<>();
    private static int requestIDCounter = 0;

    public static void setRequestIDCounter(int requestIDCounter) {
        RequestsDBUtil.requestIDCounter = requestIDCounter;
    }

    public String generateRequestID(){
        requestIDCounter++;
        return Integer.toString(requestIDCounter);
    }

    /*------------------------------------------------ Add/Remove Request -------------------------------------------------------*/
    //TODO addRequest - add a request object instead of all of the attributes

    /**
     * adds request to db
     * @param requestObject request
     * @param message message
     * @return request created
     */
	Request addRequest(Request requestObject, Message message){
     //   Connection connection = DataModelI.getInstance().getNewConnection();
        Connection connection = null;
        if(message == null){
            System.out.println("Critical Error in adding message in AddRequest function");
            return null;
        }

        String aMessageID = DataModelI.getInstance().addMessage(message.getMessageID(),message.getMessage(),message.getRead(),message.getSentDate(),message.getSenderID(),message.getReceiverID());
        message.setMessageID(aMessageID);
        requestObject.setMessageID(message.getMessageID());

        try {
            connection = DriverManager.getConnection("jdbc:derby:nodesDB;create=true");
            String str = "INSERT INTO Request(requestID,requestType,priority,isComplete,adminConfirm,startTime,endTime,nodeID,messageID,password) VALUES (?,?,?,?,?,?,?,?,?,?)";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, requestObject.getRequestID());
            statement.setString(2, requestObject.getRequestType());
            statement.setInt(3, requestObject.getPriority());
            statement.setBoolean(4, requestObject.getComplete());
            statement.setBoolean(5, requestObject.getAdminConfirm());
            statement.setTimestamp(6, Timestamp.valueOf(requestObject.getStartTime()));
            statement.setTimestamp(7, Timestamp.valueOf(requestObject.getEndTime()));
            statement.setString(8, requestObject.getNodeID());
            statement.setString(9, requestObject.getMessageID());
            statement.setString(10, requestObject.getRequestType());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            statement.close();
            System.out.println("Request added to database");
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return requestObject;
    }


    /**
     * adds request based on request object
     * @param requestObject req
     * @return added request
     */
    Request addRequest(Request requestObject){
        //   Connection connection = DataModelI.getInstance().getNewConnection();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:nodesDB;create=true");
            String str = "INSERT INTO Request(requestID,requestType,priority,isComplete,adminConfirm,startTime,endTime,nodeID,messageID,password) VALUES (?,?,?,?,?,?,?,?,?,?)";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, requestObject.getRequestID());
            statement.setString(2, requestObject.getRequestType());
            statement.setInt(3, requestObject.getPriority());
            statement.setBoolean(4, requestObject.getComplete());
            statement.setBoolean(5, requestObject.getAdminConfirm());
            statement.setTimestamp(6, Timestamp.valueOf(requestObject.getStartTime()));
            statement.setTimestamp(7, Timestamp.valueOf(requestObject.getEndTime()));
            statement.setString(8, requestObject.getNodeID());
            statement.setString(9, requestObject.getMessageID());
            statement.setString(10, requestObject.getRequestType());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            statement.close();
            System.out.println("Request added to database");
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return requestObject;
    }

    /**
     * removes request linked to ID from db
     * @param requestID of request
     * @return true if success
     */

    boolean removeRequest(String requestID){
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        try {
            String str = "UPDATE Request SET deleteTime = ? WHERE requestID = '"+ requestID+ "'";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            statement.close();
            isSuccess = true;
        } catch (SQLException e)
        {
            System.out.println("Request already in the database");
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return isSuccess;
    }

    /**
     * reverts deleted request
     * @param requestID id of request
     * @return true if success
     */
    boolean restoreRequest(String requestID){
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        try {
            String str = "UPDATE Request SET deleteTime = NULL WHERE requestID = ?";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, requestID);
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            statement.close();
            isSuccess = true;
        } catch (SQLException e)
        {
            System.out.println("Request already in the database");
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return isSuccess;
    }

    /**
     * PERMANENTLY removes a request
     * @param request
     * @return true if success
     */
    boolean permanentlyRemoveRequest(Request request) {
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
            DataModelI.getInstance().closeConnection();
        }
        DataModelI.getInstance().removeMessage(request.getMessageID());

        return isSucessful;
    }

    /**
     * modifies a request in db
     * @param newRequest to modify
     * @return true if success
     */
    public boolean modifyRequest(Request newRequest) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        try {
            String str = "UPDATE Request SET requestType= ?,priority = ?,isComplete= ?,adminConfirm= ?,startTime =?, endTime = ?,nodeID= ?,messageID= ?,password= ? WHERE requestID = '"+ newRequest.getRequestID()+ "'";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, newRequest.getRequestType());
            statement.setInt(2, newRequest.getPriority());
            statement.setBoolean(3, newRequest.getComplete());
            statement.setBoolean(4, newRequest.getAdminConfirm());
            statement.setTimestamp(5, Timestamp.valueOf(newRequest.getStartTime()));
            statement.setTimestamp(6, Timestamp.valueOf(newRequest.getEndTime()));
            statement.setString(7, newRequest.getNodeID());
            statement.setString(8, newRequest.getMessageID());
            statement.setString(9, newRequest.getPassword());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            statement.close();
            isSuccess = true;
        } catch (SQLException e)
        {
            System.out.println("Request already in the database");
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return isSuccess;
    }

    /*------------------------------------ Set status Complete/Admin Confirm -------------------------------------------------*/

    /**
     * sets status of admin
     *
     * @param request
     * @param newConfirmStatus
     */
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
            DataModelI.getInstance().closeConnection();
        }
    }

    /**
     * set if complete to true
     * @param request
     * @param newCompleteStatus
     */
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
            DataModelI.getInstance().closeConnection();
        }
    }

    /*------------------------------------------------ Retrieve Request / Search by ID / Print all -------------------------------------------------------*/
    /**
     *  get data from request table in database and put them into the list of request objects
     */
    List<Request> retrieveRequests(boolean allEntriesExist) {
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
            Timestamp startTime;
            Timestamp endTime;
            String nodeID;
            String messageID;
            String password;
            LocalDateTime   deleteTime = null;
            List<Request> listOfRequest = new ArrayList<>();
            try {
                Statement stmt = connection.createStatement();
                String str;
                if(allEntriesExist) {
                    str = "SELECT * FROM Request";
                }
                else{
                    str = "SELECT * FROM Request WHERE deleteTime IS NULL";
                }
                ResultSet rset = stmt.executeQuery(str);

                while (rset.next()) {
                    requestID = rset.getString("requestID");
                    requestType = rset.getString("requestType");
                    priority = rset.getInt("priority");
                    isComplete =rset.getBoolean("isComplete");
                    adminConfirm = rset.getBoolean("adminConfirm");
                    startTime = rset.getTimestamp("startTime");
                    endTime = rset.getTimestamp("endTime");
                    nodeID = rset.getString("nodeID");
                    messageID = rset.getString("messageID");
                    password = rset.getString("password");
                    if(rset.getTimestamp("deleteTime") != null) {
                        deleteTime = rset.getTimestamp("deleteTime").toLocalDateTime();
                    } else{
                        deleteTime = null;
                    }
                    // Add the new edge to the list
                    requestObject = rFactory.genExistingRequest(requestID, requestType, priority, isComplete, adminConfirm, startTime.toLocalDateTime(), endTime.toLocalDateTime(),nodeID, messageID, password);
                    requestObject.setDeleteTime(deleteTime);

                    listOfRequest.add(requestObject);
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding Requests");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DataModelI.getInstance().closeConnection();
            }
        return listOfRequest;
    } // retrieveRequests() ends

    /**
     * gets request from database with matching ID
     * @param requestID of req
     * @return request
     */
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
        Timestamp startTime;
        Timestamp endTime;
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
                startTime = rset.getTimestamp("startTime");
                endTime = rset.getTimestamp("endTime");
                nodeID = rset.getString("nodeID");
                messageID = rset.getString("messageID");
                password = rset.getString("password");
                // Add the new edge to the list
                requestObject = rFactory.genExistingRequest(requestID, requestType, priority, isComplete, adminConfirm, startTime.toLocalDateTime(), endTime.toLocalDateTime(), nodeID, messageID, password);


                System.out.println("Request added to the list: " + requestID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding Request");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
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
        List<Request> listOfRequests = retrieveRequests(false);
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
        List<Request> listOfRequests = retrieveRequests(false);
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
