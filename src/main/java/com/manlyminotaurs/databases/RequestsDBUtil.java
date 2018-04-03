package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Request;

import java.util.List;

public class RequestsDBUtil {

    public void addRequest(String RequestType, int priority,  String NodeID, String Message, String senderID){}
    public List<Request> searchRequest(int UserID){ return null; }
    public void setIsConfimed(int requestID, boolean newConfirmedStatus){}
    public void setIsCompleted(int requestID, boolean newCompletedStatus){}

}
