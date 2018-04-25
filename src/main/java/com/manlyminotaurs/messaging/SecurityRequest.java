package com.manlyminotaurs.messaging;

import java.time.LocalDateTime;

class SecurityRequest extends Request{

    SecurityRequest(String requestID, String requestType, int priority, Boolean isComplete, Boolean adminConfirm, LocalDateTime startTime, LocalDateTime endTime, String nodeID, String messageID, String password) {
        super(requestID, requestType, priority, isComplete, adminConfirm, startTime,endTime, password, nodeID, messageID);
    }

    /**
     * sets service completed to true
     * @return true if successful
     */
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
