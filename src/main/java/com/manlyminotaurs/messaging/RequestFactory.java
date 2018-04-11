package com.manlyminotaurs.messaging;


import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;

import java.time.LocalDate;

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
    public Request genNewRequest(RequestType requestType, Node nodeAt, String message, String senderID) {
        Message newMsg;
        Request newReq;
        switch (requestType) {
            case MedicalRequest:
                newMsg = new Message(dataModel.getNextMessageID(), message, false, LocalDate.now(), "admin", senderID);
                newReq = new MedicalRequest(dataModel.getNextRequestID(), "MedicalRequest", 5, false, false, nodeAt.getNodeID(), message, "Password123");

                return dataModel.addRequest(newReq, newMsg);

            case JanitorialRequest:
                newMsg = new Message(dataModel.getNextMessageID(), message, false, LocalDate.now(), "admin", senderID);
                newReq = new MedicalRequest(dataModel.getNextRequestID(), "Janitorial", 3, false, false, nodeAt.getNodeID(), message, "Password123");

                return dataModel.addRequest(newReq, newMsg);
            default:
                return null;
        }
    }

    /**
     *  Makes a new request object with all fields populated. This should only be called if the data is coming from the database as the DB is not updated with this call
     * @param requestID The ID of the Request in the DB
     * @param requestType The request type
     * @param priority the priority level of the request
     * @param isComplete represents if the request has been marked complete
     * @param adminConfirm represents if the request has been confirmed by an admin
     * @param timeTaken
     * @param messageID the ID of the message tied to the Request
     * @param password the password used to complete the request
     * @param nodeID the ID of the node that the request is based out of
     * @return a Request object with the fields populated with the parameters
     */
    public Request genExistingRequest(String requestID, String requestType, int priority, Boolean isComplete, Boolean adminConfirm, String timeTaken, String messageID, String password, String nodeID){
        Request newReq;
        if(requestType.equals("MedicalRequest")) {
            newReq = new MedicalRequest(requestID, "MedicalRequest", priority, isComplete, adminConfirm, nodeID, messageID, password);
        }else if(requestType.equals("JanitorialRequest")) {

            newReq = new JanitorialRequest(requestID, "JanitorialRequest", priority, isComplete, adminConfirm, nodeID, messageID, password);
        }else {
            newReq = new MedicalRequest(requestID, requestType, priority, isComplete, adminConfirm, nodeID, messageID, password);
        }
        return newReq;
    }
}
