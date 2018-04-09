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
    public Request getRequest(RequestType requestType, Node nodeAt, String message, String senderID) {
        switch (requestType) {
            case MedicalRequest:
                //TODO: Check ID's are correct

                Message newMsg = new Message(dataModel.getNextMessageID(), message, false, senderID, "admin");
                Request newReq = new MedicalRequest(dataModel.getNextRequestID(), "MedicalRequest", 5, false, false, nodeAt.getID(), message, "Password123");

                if(dataModel.addMessage(newMsg) && dataModel.addRequest(newReq)) {
                    return newReq;
                }
                return null;

                break;
            case JanitorialRequest:
                //TODO: Check ID's are correct

                Message newMsg = new Message(dataModel.getNextMessageID(), message, false, senderID, "admin");
                Request newReq = new MedicalRequest(dataModel.getNextRequestID(), "Janitorial", 3, false, false, nodeAt.getID(), message, "Password123");

                if(dataModel.addMessage(newMsg) && dataModel.addRequest(newReq)) {
                    return newReq;
                }                break;
            default:
                break;
        }
    }
}
