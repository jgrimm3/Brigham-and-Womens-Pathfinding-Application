package com.manlyminotaurs.messaging;


import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;

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
                //TODO: Check ID's are correct

                newMsg = new Message(dataModel.getNextMessageID(), message, false, senderID, "admin");
                newReq = new MedicalRequest(dataModel.getNextRequestID(), "MedicalRequest", 5, false, false, nodeAt.getID(), message, "Password123");

                return dataModel.addRequest(newReq, newMsg);
            break;
            case JanitorialRequest:
                //TODO: Check ID's are correct

                newMsg = new Message(dataModel.getNextMessageID(), message, false, senderID, "admin");
                newReq = new MedicalRequest(dataModel.getNextRequestID(), "Janitorial", 3, false, false, nodeAt.getID(), message, "Password123");

                return dataModel.addRequest(newReq, newMsg);
            break;
            default:
                return null;
            break;
        }
    }

    public Request genExistingRequest(String requestID, String requestType, int priority, Boolean isComplete, Boolean adminConfirm, String nodeID, String messageID, String password){
        Request newReq;
        if(requestType.equals("MedicalRequest")) {
            //TODO: Check ID's are correct

            newReq = new MedicalRequest(requestID, "MedicalRequest", priority, isComplete, adminConfirm, nodeID, messageID, password);
        }else if(requestType.equals("JanitorialRequest")) {
            //TODO: Check ID's are correct

            newReq = new JanitorialRequest(requestID, "JanitorialRequest", priority, isComplete, adminConfirm, nodeID, messageID, password);
        }else {
            newReq = null;
        }
        return newReq;
    }
}
