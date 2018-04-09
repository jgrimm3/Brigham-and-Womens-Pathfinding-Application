package com.manlyminotaurs.messaging;

class JanitorialRequest extends Request{

    JanitorialRequest(String requestID, String requestType, int priority, Boolean isComplete, Boolean adminConfirm, String nodeID, String messageID, String password) {
        super(requestID, requestType, priority, isComplete, adminConfirm, nodeID, messageID, password);
    }
    @Override
    public boolean service() {
        this.setComplete(true);
        return true;
    }

    //TODO: Delete the request from the DB
    @Override
    public boolean clear() {
        return false;
    }
}
