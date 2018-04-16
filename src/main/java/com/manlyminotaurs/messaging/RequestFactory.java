package com.manlyminotaurs.messaging;


import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RequestFactory {
    DataModelI dataModel = DataModelI.getInstance();

    /**
     *  Adds a new request as per specifications to the Database, this also adds the required message.
     * @param requestType The type of request to be created
     * @param nodeAt The node the request is to be completed at
     * @param message the message to be attached to the request
     * @param senderID the ID of the user sending the request
     * @return the request that was added to the DB or null if adding failed.
     */
    public Request genNewRequest(RequestType requestType, Node nodeAt, String message, String senderID, int priority) {
        Message newMsg;
        Request newReq;
        String messageID;
        switch (requestType) {
            case MedicalRequest:
                messageID = dataModel.getNextMessageID();
                newMsg = new Message(messageID, message, false, LocalDate.now(), "11", senderID);
                newReq = new MedicalRequest(dataModel.getNextRequestID(), "MedicalRequest", priority, false, false, LocalDateTime.now(), LocalDateTime.now(), nodeAt.getNodeID(), messageID, requestType.toString());

                System.out.println("made Message and Request to add to BD");
                return dataModel.addRequest(newReq, newMsg);

            case JanitorialRequest:
                messageID = dataModel.getNextMessageID();
                newMsg = new Message(messageID, message, false, LocalDate.now(), "11", senderID);
                newReq = new JanitorialRequest(dataModel.getNextRequestID(), "JanitorialRequest", priority, false, false, LocalDateTime.now(), LocalDateTime.now(), nodeAt.getNodeID(), messageID, requestType.toString());

                return dataModel.addRequest(newReq, newMsg);
            default:
                return null;
        }
    }

    /**
     *  Makes a currently existing request object with all fields populated. This should only be called if the data is coming from the database as the DB is not updated with this call
     * @param requestID The ID of the Request in the DB
     * @param requestType The request type
     * @param priority the priority level of the request
     * @param isComplete represents if the request has been marked complete
     * @param adminConfirm represents if the request has been confirmed by an admin
     * @param startTime
     * @param endTime
     * @param nodeID the ID of the node that the request is based out of
     * @param messageID the ID of the message tied to the Request
     * @param password the password used to complete the request
     * @return a Request object with the fields populated with the parameters
     */
    public Request genExistingRequest(String requestID, String requestType, int priority, Boolean isComplete, Boolean adminConfirm, LocalDateTime startTime, LocalDateTime endTime, String nodeID, String messageID, String password){
        Request newReq;
        if(requestType.equals("MedicalRequest")) {
            newReq = new MedicalRequest(requestID, "MedicalRequest", priority, isComplete, adminConfirm, startTime, endTime, nodeID, messageID, password);
        }else if(requestType.equals("JanitorialRequest")) {

            newReq = new JanitorialRequest(requestID, "JanitorialRequest", priority, isComplete, adminConfirm, startTime, endTime, nodeID, messageID, password);
        }else {
            newReq = new MedicalRequest(requestID, requestType, priority, isComplete, adminConfirm, startTime, endTime, nodeID, messageID, password);
        }
        return newReq;
    }
}
